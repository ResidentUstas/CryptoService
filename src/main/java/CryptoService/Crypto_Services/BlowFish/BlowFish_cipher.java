package CryptoService.Crypto_Services.BlowFish;

import CryptoService.Services.IOService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;

public class BlowFish_cipher {
    BlowFish_Service blowFishService;

    public BlowFish_cipher(int rounds, int[] key){
         this.blowFishService = new BlowFish_Service(rounds, key);
    }

    public String Get_Cipher_Text(String OpenText, int sys) throws IOException, DecoderException {
        blowFishService.Setup();

        //Получаем шестнадцатиричное представление текста
        String OpenTextHex = sys == 1 ? IOService.ReadBytesFromString(OpenText) : OpenText;

        String result = "";
        IOService.WriteStringToFile(result, "D:\\Diplom\\CryptoService\\Block_Ciphers\\cipher\\BlowFish\\blowfish_cipher_result.txt");
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
            String cipher_result = blowFishService.Get_Cipher_Text(openBlock);
            result += cipher_result;
        }

        IOService.WriteStringToFile(result, "D:\\Diplom\\CryptoService\\Block_Ciphers\\cipher\\BlowFish\\blowfish_cipher_result.txt");
        return result;
    }

    public String Get_Open_Text(String CipherText) throws IOException, DecoderException {
        blowFishService.Setup();

        String result = "";
        String openBlockHex = "";
        IOService.WriteStringToFile(result, "D:\\Diplom\\CryptoService\\Block_Ciphers\\decipher\\BlowFish\\blowfish_decipher_result.txt");
        while (CipherText.length() > 0) {
            String cipherBlock = CipherText.substring(0, 16);
            CipherText = CipherText.substring(16, CipherText.length());

            //расшифруем полученный блок
            openBlockHex += blowFishService.Get_Open_Text(cipherBlock);
        }
        //Представим шестнадцатиричный результат в десятичных байтах
        byte[] openBytes = Hex.decodeHex(openBlockHex.toCharArray());
        IOService.WriteFile(openBytes, "D:\\Diplom\\CryptoService\\Block_Ciphers\\decipher\\BlowFish\\blowfish_decipher_result.txt");
        result = IOService.ReadBytes("D:\\Diplom\\CryptoService\\Block_Ciphers\\decipher\\BlowFish\\blowfish_decipher_result.txt");
        return result;
    }
}
