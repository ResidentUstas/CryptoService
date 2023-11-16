package CryptoService.Crypto_Services.IDEA;

import CryptoService.Services.IOService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class IDEAcipher {

    private final IDEA_Service ideaService = new IDEA_Service();

    public String Get_Cipher_Text(String OpenText) throws IOException {
        ideaService.Generate_Keys();

        //Получаем шестнадцатиричное представление текста
        String OpenTextHex = IOService.ReadBytesFromString(OpenText);
        //String OpenTextHex = "0000000100020003";

        String result = "";
        while (OpenTextHex.length() > 0) {
            String openBlock = OpenTextHex.substring(0, 16);
            OpenTextHex = OpenTextHex.substring(16, OpenTextHex.length());

            //Дополним последний неполный блок
            if (OpenTextHex.length() < 16 && OpenTextHex.length() > 0) {
                while (OpenTextHex.length() < 16) {
                    OpenTextHex += "0";
                }
            }

            //Зашифруем полученный блок
            String cipher_result = ideaService.Make_Cipher_Text(openBlock, 1);
            result += cipher_result;
        }

        IOService.WriteStringToFile(result, "D:\\CipherIdeaRes.txt");
        return result;
    }

    public String Get_Open_Text(String CipherText) throws IOException, DecoderException {
        ideaService.Generate_Keys();

        String result = "";
        while (CipherText.length() > 0) {
            String cipherBlock = CipherText.substring(0, 16);
            CipherText = CipherText.substring(16, CipherText.length());

            //Дополним последний неполный блок
            if (CipherText.length() < 16 && CipherText.length() > 0) {
                while (CipherText.length() < 16) {
                    CipherText += "0";
                }
            }

            //расшифруем полученный блок
            String openBlockHex = ideaService.Make_Cipher_Text(cipherBlock, 0);
            //Представим шестнадцатиричный результат в десятичных байтах
            byte[] openBytes = Hex.decodeHex(openBlockHex.toCharArray());

            IOService.WriteFile(openBytes, "D:\\decipherIdea1.txt");

            String openBlockSTR = new String(openBytes, StandardCharsets.UTF_8);
            result += openBlockSTR;
        }

        return result;
    }
}