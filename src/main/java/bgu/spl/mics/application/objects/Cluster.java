package bgu.spl.mics.application.objects;

import bgu.spl.mics.Event;
import bgu.spl.mics.MessageBusImpl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

	private static Cluster instance = null;
	private Collection<GPU> GPUS;
	private Collection<CPU> CPUS;
	private Statistics stats;

	private Cluster() {
		GPUS = new ArrayList<GPU>();
		CPUS = new ArrayList<CPU>();
		stats = new Statistics();
	}

	public synchronized static Cluster getInstance() {
		if (instance == null)
			instance = new Cluster();
		return instance;
	}

	protected void addGPU(GPU g) {
		GPUS.add(g);
	}

	protected void addCPU(CPU c) {
		CPUS.add(c);
	}

	protected void sendEvent(Event e) {}

}
