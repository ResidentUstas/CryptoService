package Source.Models;

import java.util.ArrayList;
import java.util.List;

public class CipherModel {

    private String Cipher;

    private int Mode;

    public CipherModel() {

    }

    public CipherModel(String cipher, int mode) {
        this.Cipher = cipher;
        this.Mode = mode;
    }

    public String getCipher() {
        return Cipher;
    }

    public void setCipher(String cipher) {
        Cipher = cipher;
    }

    public int getMode() {
        return Mode;
    }

    public void setMode(int mode) {
        Mode = mode;
    }
}
