package CryptoService.Crypto_Services.RC5;
import CryptoService.Services.IOService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;

public class RC5cipher {

    private String p_sixteen = "B7E1";
    private String q_sixteen = "9E37";
    private int[] Key;
    private String p_thirtyTwo = "B7E15163";
    private String q_thirtyTwo = "9E3779B9";

    private String p_sixtyFour = "B7E151628AED2A6B";
    private String q_sixtyFour = "9E3779B97F4A7C15";
    private int Rounds;
    private int WordLength;
    private int Word;
    private int KeyBits;
    private String[] pq_choice = new String[2];
    private  RC5_Service rc5_service = new RC5_Service();
    public RC5cipher(int rounds, int wordLength, int keyBits, int word, int[] key){
        this.Rounds = rounds;
        this.WordLength = wordLength;
        this.KeyBits = keyBits;
        this.Word = word;
        this.pq_choice = Get_PQ_Constants(this.Word);
        this.rc5_service = new RC5_Service(WordLength, Rounds, KeyBits, pq_choice[0], pq_choice[1], key);
    }

    public String Get_Cipher_Text(String OpenText, int sys) throws IOException {
        //Получаем шестнадцатиричное представление текста
        String OpenTextHex = sys == 1 ? IOService.ReadBytesFromString(OpenText) : OpenText;

        String result = "";
        IOService.WriteStringToFile(result, "D:\\Diplom\\CryptoService\\Block_Ciphers\\decipher\\RC-5\\rc5_decipher_result.txt");
        while (OpenTextHex.length() > 0) {
            String openBlock = OpenTextHex.substring(0, (WordLength / 8) * 4);
            OpenTextHex = OpenTextHex.substring((WordLength / 8) * 4, OpenTextHex.length());

            //Дополним последний неполный блок
            if (OpenTextHex.length() < (WordLength / 8) * 4 && OpenTextHex.length() > 0) {
                while (OpenTextHex.length() < 16) {
                    OpenTextHex += "0";
                }
            }

            //Зашифруем полученный блок
            String cipher_result = rc5_service.Get_Cipher_Text(openBlock);
            result += cipher_result;
        }

        IOService.WriteStringToFile(result, "D:\\Diplom\\CryptoService\\Block_Ciphers\\cipher\\RC-5\\rc5_cipher_result.txt");
        return result;
    }

    public String Get_Open_Text(String OpenTextHex) throws IOException, DecoderException {
        String result = "";
        String openBlockHex = "";
        IOService.WriteStringToFile(result, "D:\\Diplom\\CryptoService\\Block_Ciphers\\decipher\\RC-5\\rc5_decipher_result.txt");
        while (OpenTextHex.length() > 0) {
            String openBlock = OpenTextHex.substring(0, (WordLength / 8) * 4);
            OpenTextHex = OpenTextHex.substring((WordLength / 8) * 4, OpenTextHex.length());

            openBlockHex += rc5_service.Get_Open_Text(openBlock);
        }
        //Представим шестнадцатиричный результат в десятичных байтах
        byte[] openBytes = Hex.decodeHex(openBlockHex.toCharArray());
        IOService.WriteFile(openBytes, "D:\\Diplom\\CryptoService\\Block_Ciphers\\decipher\\RC-5\\rc5_decipher_result.txt");
        result = IOService.ReadBytes("D:\\Diplom\\CryptoService\\Block_Ciphers\\decipher\\RC-5\\rc5_decipher_result.txt");
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
