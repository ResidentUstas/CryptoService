package CryptoService.Crypto_Services.RC5;
import CryptoService.Services.IOService;
import org.apache.commons.codec.DecoderException;
import java.io.IOException;

public class RC5cipher {

    private String p_sixteen = "B7E1";
    private String q_sixteen = "9E37";

    private String p_thirtyTwo = "B7E15163";
    private String q_thirtyTwo = "9E3779B9";

    private String p_sixtyFour = "B7E151628AED2A6B";
    private String q_sixtyFour = "9E3779B97F4A7C15";

    public String Get_Cipher_Text(String OpenText, int Rounds, int WordLength, int KeyBits, int word) throws IOException {
        String[] pq_choice = Get_PQ_Constants(word);
        RC5_Service rc5_service = new RC5_Service(WordLength, Rounds, KeyBits, pq_choice[0], pq_choice[1]);

        //Получаем шестнадцатиричное представление текста
        //String OpenTextHex = IOService.ReadBytesFromString(OpenText);
        String OpenTextHex = "0000000100020003";

        String result = "";
        while (OpenTextHex.length() > 0) {
            String openBlock = OpenTextHex.substring(0, (WordLength / 8) * 2);
            OpenTextHex = OpenTextHex.substring((WordLength / 8) * 2, OpenTextHex.length());

            //Дополним последний неполный блок
            if (OpenTextHex.length() < (WordLength / 8) * 2 && OpenTextHex.length() > 0) {
                while (OpenTextHex.length() < 16) {
                    OpenTextHex += "0";
                }
            }

            //Зашифруем полученный блок
            String cipher_result = rc5_service.Get_Cipher_Text(openBlock);
            result += cipher_result;
        }

        IOService.WriteStringToFile(result, "D:\\RC5_cipher_Res.txt");
        return result;
    }

    public String Get_Open_Text(String CipherText, int Rounds, int WordLength, int KeyBits, int word) throws IOException, DecoderException {
        String[] pq_choice = Get_PQ_Constants(word);
        RC5_Service rc5_service = new RC5_Service(WordLength, Rounds, KeyBits, pq_choice[0], pq_choice[1]);
        String result = rc5_service.Get_Open_Text(CipherText);
        return result;
    }

    private String[] Get_PQ_Constants(int word){
        String[] result = new String[2];
        switch(word){
            case 16:
                result[0] = p_sixteen;
                result[1] = q_sixteen;
                break;
            case 32:
                result[0] = p_thirtyTwo;
                result[1] = q_thirtyTwo;
                break;
            case 64:
                result[0] = p_sixtyFour;
                result[1] = q_sixtyFour;
                break;
        }

        return result;
    }
}
