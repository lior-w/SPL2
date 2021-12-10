package bgu.spl.mics.application.objects;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class Model {

    enum ModelStatus {
        PreTrained, Training, Trained, Tested
    }

    enum Result {
        None, Good, Bad
    }

    private String name;
    private Data data;
    private Student student;
    private ModelStatus status;
    private Result results;

    public Model(String name, Data data, Student student, ModelStatus status, Result results) {
        this.name = name;
        this.data = data;
        this.student = student;
        this.status = status;
        this.results = results;
    }

    public Model(String name, Data data, Student student, ModelStatus status) {
        this.name = name;
        this.data = data;
        this.student = student;
        this.status = status;
        this.results = Result.None;
    }
}
