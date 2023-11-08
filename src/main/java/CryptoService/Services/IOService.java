package CryptoService.Services;

import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IOService {

    public static void WriteFile(byte[] buffer, String path) {
        try (FileOutputStream fos = new FileOutputStream(path, true)) {
            fos.write(buffer, 0, buffer.length);
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public static void WriteStringToFile(String text, String path) {
        try (FileWriter writer = new FileWriter(path, true)) {

            writer.write(text);

            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public static String ReadFile(String path) {
        String Result = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
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

    public static String ReadBytes(String pathStr) throws IOException {
        Path path = Paths.get(pathStr);

        byte[] data = Files.readAllBytes(path);
        String opentTextHex = Hex.encodeHexString(data);

        String openBlockSTR = new String(data, StandardCharsets.UTF_8);

        return opentTextHex;
    }

    public static String ReadBytesFromString(String text) throws IOException {

        byte[] data = text.getBytes();
        String opentTextHex = Hex.encodeHexString(data);

        String openBlockSTR = new String(data, StandardCharsets.UTF_8);

        return opentTextHex;
    }
}
