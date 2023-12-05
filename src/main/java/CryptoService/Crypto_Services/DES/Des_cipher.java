package CryptoService.Crypto_Services.DES;

import CryptoService.Services.IOService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Des_cipher {
    Des_Service des_service = new Des_Service();
    public String Get_Cipher_Text(String OpenText) throws IOException, DecoderException {
        des_service.KeyExtension();
        String OpenTextHex = IOService.ReadBytesFromString(OpenText);

        String result = "";
        IOService.WriteStringToFile(result, "D:\\Block_Algorithms\\Block_Ciphers\\cipher\\DES\\cipher_result.txt");
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
            String cipher_result = des_service.Get_Cipher_Text(openBlock);
            result += cipher_result;
        }

        IOService.WriteStringToFile(result, "D:\\Block_Algorithms\\Block_Ciphers\\cipher\\DES\\cipher_result.txt");
        return result;
    }

    public String Get_Open_Text(String CipherText) throws IOException, DecoderException {
        des_service.KeyExtension();

        String result = "";

        IOService.WriteStringToFile(result, "D:\\Block_Algorithms\\Block_Ciphers\\decipher\\DES\\decipher_result.txt");
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
            String openBlockHex = des_service.Get_Open_Text(cipherBlock);
            //Представим шестнадцатиричный результат в десятичных байтах
            byte[] openBytes = Hex.decodeHex(openBlockHex.toCharArray());

            IOService.WriteFile(openBytes, "D:\\Block_Algorithms\\Block_Ciphers\\decipher\\DES\\decipher_result.txt");

            String openBlockSTR = new String(openBytes, StandardCharsets.UTF_8);
            result += openBlockSTR;
        }

        return result;
    }
}
