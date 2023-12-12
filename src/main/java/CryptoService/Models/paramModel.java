package CryptoService.Models;

public class paramModel {

    private int param;
    private int index;
    private String Algor;

    public paramModel() {
    }

    public paramModel(int index, int param) {
        this.param = param;
        this.index = index;
    }

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAlgor() {
        return Algor;
    }

    public void setAlgor(String algor) {
        Algor = algor;
    }
}
