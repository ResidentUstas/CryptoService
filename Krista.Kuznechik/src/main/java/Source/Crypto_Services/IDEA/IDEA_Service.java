package Source.Crypto_Services.IDEA;

import java.util.LinkedList;
import java.util.List;

public class IDEA_Service {

    public final List<int[]> RoundKeys = new LinkedList<>();
    private String Key = "00010002000300040005000600070008";

    public void Generate_Keys() {
        int[] KeyBytes = Get_ByteRow_From_String(Key, 16);
        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < 16; i++) {
                int[] key_i = new int[2];
                System.arraycopy(KeyBytes, i, key_i, 0, 2);
                i++;
                RoundKeys.add(key_i);
            }

            KeyBytes = Get_Idea_Left_Shift(KeyBytes);
        }

        RoundKeys.subList(51, 55).clear();
    }

    private int[] Get_Idea_Left_Shift(int[] Key) {
        for (int l = 0; l < 3; l++) {
            Key = Left_Shift(Key);
        }

        String key_bit = Get_Bit_View(Key);
        key_bit = Left_Shift_String(key_bit);

        return Get_Bytes_From_Bits(key_bit);
    }

    private int[] Get_Bytes_From_Bits(String byteRow) {
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

    private static int[] Left_Shift(int[] byteRow) {
        int value0 = byteRow[0];
        for (int i = 0; i < byteRow.length - 1; i++) {
            byteRow[i] = byteRow[i + 1];
        }

        byteRow[15] = value0;
        return byteRow;
    }

    private String Get_Bit_View(int[] byteRow) {
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

    public String Left_Shift_String(String byteRow) {
        String value0 = byteRow.substring(0, 1);
        byteRow = byteRow.substring(1, byteRow.length());
        byteRow = byteRow + value0;

        return byteRow;
    }

    public int[] Get_ByteRow_From_String(String subKey, int length) {
        int start_index = 0;
        int end_index = 2;
        int i = 0;
        int[] roundKeyRow = new int[length];
        while (end_index <= length * 2) {
            String str_byte = subKey.substring(start_index, end_index);
            int parsedResult = (int) Long.parseLong(str_byte, 16);
            roundKeyRow[i] = parsedResult;
            start_index += 2;
            end_index += 2;
            i++;
        }

        return roundKeyRow;
    }
}
