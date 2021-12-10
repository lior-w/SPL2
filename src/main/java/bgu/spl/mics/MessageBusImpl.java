package bgu.spl.mics;

import bgu.spl.mics.application.messages.TrainModelEvent;

import java.util.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static MessageBusImpl instance = null;
	private HashMap<MicroService, Queue<Message>> queues;
	private HashMap<Event, Future> futures;
	private HashMap<Class<? extends Event>, Queue<MicroService>> events;
	private HashMap<Class<? extends Broadcast>, List<MicroService>> broadcasts;

	private MessageBusImpl() {
		queues = new HashMap<MicroService, Queue<Message>>();
		futures = new HashMap<Event, Future>();
		events = new HashMap<Class<? extends Event>, Queue<MicroService>>();
		broadcasts = new HashMap<Class<? extends Broadcast>, List<MicroService>>();
	}

	public synchronized static MessageBusImpl getInstance() {
		if (instance == null)
			instance = new MessageBusImpl();
		return instance;
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		events.computeIfAbsent(type, k -> new LinkedList<MicroService>());
		events.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		broadcasts.computeIfAbsent(type, k -> new LinkedList<MicroService>());
		broadcasts.get(type).add(m);
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		for (MicroService m : broadcasts.get(b.getClass()))
			m.sendBroadcast(b);
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		MicroService m = events.get(e.getClass()).poll();
		events.get(e.getClass()).add(m);
		queues.get(m).add(e);

	}

	@Override
	public void register(MicroService m) {
		Queue<Message> mq = new LinkedList<Message>();
		queues.put(m, mq);
	}

	@Override
	public void unregister(MicroService m) {
		queues.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if (queues.get(m).isEmpty())
			m.wait();
		Message message = queues.get(m).poll();
		//Future f = m.sendEvent(message);
		//futures.put(message, f)
		//m gets message and execute it
		//m.run();
		//wait for next message
		return null;
	}

	

}
