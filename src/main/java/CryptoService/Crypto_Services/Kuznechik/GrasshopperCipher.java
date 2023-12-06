package CryptoService.Crypto_Services.Kuznechik;

import CryptoService.Services.IOService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GrasshopperCipher {
    private final Kuznechik_service cryptoService = new Kuznechik_service();

    //Шифрует
    public String Get_Cipher_Text(String OpenText) throws IOException {
        //генерируем итерационные константы
        cryptoService.Generate_Constants_Ci();

        //генерируем раундовые ключи
        cryptoService.Generate_Round_Keys();

        //Получаем шестнадцатиричное представление текста
        String OpenTextHex = IOService.ReadBytesFromString(OpenText);

        String result = "";
        IOService.WriteStringToFile(result, "D:\\Block_Algorithms\\Block_Ciphers\\cipher\\Grasshopper\\grasshopper_cipher_result.txt");
        while (OpenTextHex.length() > 0) {
            String openBlock = OpenTextHex.substring(0, 32);
            OpenTextHex = OpenTextHex.substring(32, OpenTextHex.length());

            //Дополним последний неполный блок
            if (OpenTextHex.length() < 32 && OpenTextHex.length() > 0) {
                while (OpenTextHex.length() < 32) {
                    OpenTextHex += "0";
                }
            }

            //Зашифруем полученный блок
            String cipher_result = cryptoService.Make_Cipher_Text(openBlock);
            result += cipher_result;
        }

        IOService.WriteStringToFile(result, "D:\\Block_Algorithms\\Block_Ciphers\\cipher\\Grasshopper\\grasshopper_cipher_result.txt");
        return result;
    }

    // Дешифрует
    public String Get_Open_Text(String CipherText) throws DecoderException, IOException {
        //генерируем итерационные константы
        cryptoService.Generate_Constants_Ci();

        //генерируем раундовые ключи
        cryptoService.Generate_Round_Keys();

        String result = "";
        IOService.WriteStringToFile(result, "D:\\Block_Algorithms\\Block_Ciphers\\decipher\\Grasshopper\\grasshopper_decipher_result.txt");
        while (CipherText.length() > 0) {
            String cipherBlock = CipherText.substring(0, 32);
            CipherText = CipherText.substring(32, CipherText.length());

            //Дополним последний неполный блок
            if (CipherText.length() < 32 && CipherText.length() > 0) {
                while (CipherText.length() < 32) {
                    CipherText += "0";
                }
            }

            //Расшифруем полученный блок
            String openBlockHex = cryptoService.Make_Open_Text(cipherBlock);

            //Представим шестнадцатиричный результат в десятичных байтах
            byte[] openBytes = Hex.decodeHex(openBlockHex.toCharArray());

            IOService.WriteFile(openBytes, "D:\\Block_Algorithms\\Block_Ciphers\\decipher\\Grasshopper\\grasshopper_decipher_result.txt");

            String openBlockSTR = new String(openBytes, StandardCharsets.UTF_8);
            result += openBlockSTR;
        }

        return result;
    }
}
