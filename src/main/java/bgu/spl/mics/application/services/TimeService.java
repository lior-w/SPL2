package bgu.spl.mics.application.services;

import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.MicroService;

import java.util.concurrent.TimeUnit;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{
	private long timeUnits;
	private static class TimeServiceHolder {
		private static TimeService instance = new TimeService();
	}


	private TimeService() {
		timeUnits = 1;
		super("TimeService");
	}

	public static TimeService getInstance() {
		return TimeServiceHolder.instance;
	}

	@Override
	protected void initialize() {
		this.wait(TimeUnit.toMillis(timeUnits));
	}

}
