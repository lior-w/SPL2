package bgu.spl.mics;

import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Model;
import sun.misc.Lock;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private final ConcurrentHashMap<MicroService, Queue<Message>> microService_messageQueue;
	private final ConcurrentHashMap<Event, Future> events_unResolvedFuture;
	private final ConcurrentHashMap<Class<? extends Event>, Queue<MicroService>> eventType_microServiceQueue;
	private final ConcurrentHashMap<Class<? extends Broadcast>, List<MicroService>> broadcastType_microServiceList;
	private final Object subscribeEventLock;
	private final Object subscribeBroadcastLock;

	private static class MessageBudHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl() {
		microService_messageQueue = new ConcurrentHashMap<MicroService, Queue<Message>>();
		events_unResolvedFuture = new ConcurrentHashMap<Event, Future>();
		eventType_microServiceQueue = new ConcurrentHashMap<Class<? extends Event>, Queue<MicroService>>();
		broadcastType_microServiceList = new ConcurrentHashMap<Class<? extends Broadcast>, List<MicroService>>();
		subscribeEventLock = new Object();
		subscribeBroadcastLock = new Object();
	}

	public static MessageBusImpl getInstance() {
		return MessageBudHolder.instance;
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (subscribeEventLock) {
			eventType_microServiceQueue.computeIfAbsent(type, k -> new ConcurrentLinkedQueue<MicroService>());
			eventType_microServiceQueue.get(type).add(m);
			subscribeEventLock.notifyAll();
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (subscribeBroadcastLock) {
			broadcastType_microServiceList.computeIfAbsent(type, k -> new LinkedList<MicroService>());
			broadcastType_microServiceList.get(type).add(m);
			subscribeBroadcastLock.notifyAll();
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		synchronized (events_unResolvedFuture.get(e)) {
			events_unResolvedFuture.get(e).resolve(result);
			events_unResolvedFuture.get(e).notifyAll();
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		for (MicroService m : broadcastType_microServiceList.get(b.getClass())) {
			synchronized (broadcastType_microServiceList.get(b.getClass())) {
				m.sendBroadcast(b);
				broadcastType_microServiceList.get(b.getClass()).notifyAll();
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		synchronized (eventType_microServiceQueue.get(e.getClass())) {
			MicroService m = eventType_microServiceQueue.get(e.getClass()).poll(); // gets the head of the micro service queue that subscribed to the event
			Future<T> f = new Future<T>();
			events_unResolvedFuture.put(e, f); // puts the event and an unresolved future in the events-unresolved future hash map
			eventType_microServiceQueue.get(e.getClass()).add(m); // puts the micro service back in the tail of the queue
			microService_messageQueue.get(m).add(e); // puts the event in the tail of the message queue of the micro service
			eventType_microServiceQueue.get(e.getClass()).notifyAll();
			return f;//check when f get complete
		}
	}

	@Override
	public void register(MicroService m) {
		microService_messageQueue.computeIfAbsent(m, k -> new ConcurrentLinkedQueue<Message>());
	}

	@Override
	public void unregister(MicroService m) {
		microService_messageQueue.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		synchronized (microService_messageQueue.get(m)) { //check sync
			while (microService_messageQueue.get(m).isEmpty())
				microService_messageQueue.get(m).wait();
			Message message = microService_messageQueue.get(m).poll();
			microService_messageQueue.get(m).notifyAll();
			return message;
		}
	}

	

}
