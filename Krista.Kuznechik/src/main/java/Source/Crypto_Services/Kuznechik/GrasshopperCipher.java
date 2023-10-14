package Source.Crypto_Services.Kuznechik;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GrasshopperCipher {
    private final Kuznechik_service cryptoService = new Kuznechik_service();

    //Шифрует
    public String Get_Cipher_Text() throws IOException {
        //генерируем итерационные константы
        cryptoService.Generate_Constants_Ci();

        //генерируем раундовые ключи
        cryptoService.Generate_Round_Keys();

        String OpenText = ReadBytes();
        String result = "";
        while (OpenText.length() > 32) {
            String openBlock = OpenText.substring(0, 32);
            OpenText = OpenText.substring(32, OpenText.length());

            String cipher_result = cryptoService.Make_Cipher_Text(openBlock);
            result += cipher_result;
        }

        WriteStringToFile(result);
        return result;
    }

    // Дешифрует
    public String Get_Open_Text(String CipherText) throws DecoderException, IOException {
        //генерируем итерационные константы
        cryptoService.Generate_Constants_Ci();

        //генерируем раундовые ключи
        cryptoService.Generate_Round_Keys();
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
        try(FileOutputStream fos=new FileOutputStream("D:\\decipher1.txt", true))
        {
            fos.write(buffer, 0, buffer.length);
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    public void WriteStringToFile(String text){
        try(FileWriter writer = new FileWriter("D:\\CipherRes.txt", true))
        {

            writer.write(text);

            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    public String ReadFile() {
        String Result = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("D:\\CipherRes.txt"));
            String line = reader.readLine();
            while (line != null) {
                Result += line;
                line = reader.readLine();
            }
            reader.close();

            return Result;
        } catch (IOException e) {
            e.printStackTrace();
            return "0";
        }
    }

    public  String ReadBytes() throws IOException {
        Path path = Paths.get("D:\\Diplom\\open.txt");

        byte[] data = Files.readAllBytes(path);
        String opentTextHex = Hex.encodeHexString(data);

        String openBlockSTR = new String(data, StandardCharsets.UTF_8);

        return opentTextHex;
    }
}
