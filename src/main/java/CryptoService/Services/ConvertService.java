package CryptoService.Services;

import org.apache.commons.lang3.ArrayUtils;
import java.math.BigInteger;
public class ConvertService {

    public static int[] Get_ByteRow_From_String(String subKey, int length) {
        int start_index = 0;
        int end_index = 2;
        int i = length - 1;
        int[] roundKeyRow = new int[length];
        while (end_index <= length * 2) {
            String str_byte = subKey.substring(start_index, end_index);
            int parsedResult = (int) Long.parseLong(str_byte, 16);
            roundKeyRow[i] = parsedResult;
            start_index += 2;
            end_index += 2;
            i--;
        }

        return roundKeyRow;
    }

    public static String Get_hex_string(int[] byteRow) {
        String hex_ci = "";
        ArrayUtils.reverse(byteRow);
        for (int i = byteRow.length - 1; i >= 0; i--) {
            if(byteRow[i] < 0){
                byteRow[i] += 256;
            }

            String line = Integer.toHexString(byteRow[i]);
            if (line.length() < 2) {
                line = "0" + line;
            }
            hex_ci += line;
        }

        return hex_ci;
    }

    public static int[] Get_Bytes_From_Bits(String byteRow) {
        int[] result = new int[16];
        int index = 0;
        while (byteRow.length() > 0) {
            String byte0 = byteRow.substring(0, 8);
            byteRow = byteRow.substring(8, byteRow.length());
            int byte1 = Integer.parseInt(byte0, 2);
            result[index] = byte1;
            index++;
        }

        return result;
    }

    public static int numeric_shift(int n, int k, boolean isLeftShift)
    {
        if (isLeftShift) {
            return (n << k) | (n >> (Integer.SIZE - k));
        }

        return (n >> k) | (n << (Integer.SIZE - k));
    }

    public static String Get_Bit_View(int[] byteRow) {
        String result = "";
        for (int b : byteRow) {
            String binStr = Integer.toBinaryString(b & 0xFF);
            while (binStr.length() < 8) {
                binStr = "0" + binStr;
            }

            result += binStr;
        }

        return result;
    }
}
