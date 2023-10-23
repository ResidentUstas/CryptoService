package Source.Crypto_Services.IDEA;

public class IDEAcipher {

    private final IDEA_Service ideaService = new IDEA_Service();

    public String Get_Cipher_Text(){
        ideaService.Generate_Keys();
        ideaService.Make_Cipher_Text();
        return "hello_world";
    }
}
