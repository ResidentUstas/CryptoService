package Source.Models;

public class OperModel {

    private int operation;

    private String name;

    public OperModel() {
    }

    public OperModel(int operation, String name) {
        this.operation = operation;
        this.name = name;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
