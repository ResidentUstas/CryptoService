package Source.Crypto_Services.Kuznechik;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GrasshopperCipher {
    private final Kuznechik_service cryptoService = new Kuznechik_service();

    //Шифрует
    public String Get_Cipher_Text() {
        //генерируем итерационные константы
        cryptoService.Generate_Constants_Ci();

        //генерируем раундовые ключи
        cryptoService.Generate_Round_Keys();

        //шифруем открытый блок
        return cryptoService.Make_Cipher_Text();
    }

    // Дешифрует
    public String Get_Open_Text() throws DecoderException {
        String CipherText = ReadFile();
        String result = "";
        while (CipherText.length() > 0) {
            String cipherBlock = CipherText.substring(0, 32);
            CipherText = CipherText.substring(32, CipherText.length());
            String openBlockHex = cryptoService.Make_Open_Text(cipherBlock);

            byte[] openBytes = Hex.decodeHex(openBlockHex.toCharArray());
            WriteFile(openBytes);
            String openBlockSTR = new String(openBytes, StandardCharsets.UTF_8);
            result += openBlockSTR;
        }

        return result;
    }

    public void WriteFile(byte[] buffer){
        try(FileOutputStream fos=new FileOutputStream("D:\\notes23.txt", true))
        {
            fos.write(buffer, 0, buffer.length);
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    public String ReadFile() {
        String Result = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("D:\\Kuznec\\cipher.txt"));
            String line = reader.readLine();
            while (line != null) {
                Result = line;
                line = reader.readLine();
            }
            reader.close();

            return Result;
        } catch (IOException e) {
            e.printStackTrace();
            return "0";
        }
    }
}
