package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {

    private int cores;
    private Collection<DataBatch> data;
    private Cluster cluster;

    public CPU(int cores, Collection<DataBatch> data) {
        this.cores = cores;
        this.data = data;
        this.cluster = Cluster.getInstance();
    }

    public CPU(int cores) {
        this.cores = cores;
        this.data = new ArrayList<DataBatch>();
        this.cluster = Cluster.getInstance();
    }


}
