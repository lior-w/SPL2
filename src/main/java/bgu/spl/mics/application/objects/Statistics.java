package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.Collection;

public class Statistics {

    private Collection<String> modelNames;
    private int dataBatchesUsed;
    private int CPU_timeUnitsUsed;
    private int GPU_timeUnitsUsed;

    public Statistics() {
        modelNames = new ArrayList<String>();
        dataBatchesUsed = 0;
        CPU_timeUnitsUsed = 0;
        GPU_timeUnitsUsed = 0;
    }

    protected void addModelName(String name) {
        modelNames.add(name);
    }

    protected void increaseDataBatchesUsed() {
        dataBatchesUsed++;
    }

    protected void increaseCPU_timeUnitsUsed() {
        CPU_timeUnitsUsed++;
    }

    protected void increaseGPU_timeUnitsUsed() {
        GPU_timeUnitsUsed++;
    }
}
