package CryptoService.Services;

import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class IOService {

    public static void WriteFile(byte[] buffer, String path) {
        List<Byte> buff = new ArrayList<>();
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] != 0) {
                buff.add(buffer[i]);
            }
        }

        byte[] result = new byte[buff.size()];
        for (int i = 0; i < buff.size(); i++) {
            result[i] = buff.get(i);
        }

        try (FileOutputStream fos = new FileOutputStream(path, true)) {
            fos.write(result, 0, result.length);
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public static void WriteStringToFile(String text, String path) {
        try (FileWriter writer = new FileWriter(path, false)) {

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

    public static String ReadFileStraight(InputStream in) {
        try {
            byte[] inStr = in.readAllBytes();
            String s = new String(inStr);
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return "0";
        }
    }

    public static String ReadBytes(String pathStr) throws IOException {
        Path path = Paths.get(pathStr);

        byte[] data = Files.readAllBytes(path);
        String openBlockSTR = new String(data, StandardCharsets.UTF_8);

        return openBlockSTR;
    }

    public static String ReadBytesFromString(String text) throws IOException {

        byte[] data = text.getBytes();
        String opentTextHex = Hex.encodeHexString(data);

        String openBlockSTR = new String(data, StandardCharsets.UTF_8);

        return opentTextHex;
    }

    public static String stringToBinary(String s) {
        return s
                .chars()
                .collect(StringBuilder::new,
                        (sb, c) -> sb.append(Integer.toBinaryString(c)).append(' '),
                        StringBuilder::append)
                .toString();
    }

    public static String hexToBinary(String hex) {
        return new BigInteger(hex, 16).toString(2);
    }

    public static int FindHammingDistance(String a, String b) {
        int h_distance = 0;
        for (int i = 0; i < a.length(); i++) {
            if(a.charAt(i) != b.charAt(i)){
                h_distance++;
            }
        }

        return h_distance;
    }
}
