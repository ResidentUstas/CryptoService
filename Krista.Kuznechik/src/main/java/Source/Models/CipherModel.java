package Source.Models;

public class CipherModel {
     private String Cipher;

     public CipherModel(){

     }

     public CipherModel(String cipher){
         this.Cipher = cipher;
     }

    public String getCipher() {
        return Cipher;
    }

    public void setCipher(String cipher) {
        Cipher = cipher;
    }
}
