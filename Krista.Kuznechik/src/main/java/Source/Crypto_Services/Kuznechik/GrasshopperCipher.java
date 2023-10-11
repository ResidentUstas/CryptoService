package Source.Crypto_Services.Kuznechik;

public class GrasshopperCipher {
    private final Kuznechik_service cryptoService = new Kuznechik_service();

    public String Get_Cipher_Text() {
        //генерируем итерационные константы
        cryptoService.Generate_Constants_Ci();

        //генерируем раундовые ключи
        cryptoService.Generate_Round_Keys();

        //шифруем открытый блок
        return cryptoService.Make_Cipher_Text();
    }

    public String Get_Open_Text(){
        return cryptoService.Make_Open_Text();
    }
}
