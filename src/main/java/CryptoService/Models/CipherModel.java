package CryptoService.Models;

public class CipherModel {

    private String Cipher;

    private int Mode;

    private int BlockSize;

    private int NumRounds;

    private int KeySize;

    public CipherModel() {

    }

    public CipherModel(String cipher, int mode) {
        this.Cipher = cipher;
        this.Mode = mode;
    }

    public CipherModel(String cipher, int mode, int blockSize, int numRounds, int keySize) {
        Cipher = cipher;
        Mode = mode;
        BlockSize = blockSize;
        NumRounds = numRounds;
        KeySize = keySize;
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

    public int getBlockSize() {
        return BlockSize;
    }

    public void setBlockSize(int blockSize) {
        BlockSize = blockSize;
    }

    public int getNumRounds() {
        return NumRounds;
    }

    public void setNumRounds(int numRounds) {
        NumRounds = numRounds;
    }

    public int getKeySize() {
        return KeySize;
    }

    public void setKeySize(int keySize) {
        KeySize = keySize;
    }
}
