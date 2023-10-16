package Source.Crypto_Services.IDEA;

import java.util.LinkedList;
import java.util.List;

public class IDEA_Service {

    public final List<int[]> RoundKeys = new LinkedList<>();
    private String Key = "00010002000300040005000600070008";

    public void Generate_Keys() {
        int[] KeyBytes = Get_ByteRow_From_String(Key, 16);
        for (int i = 0; i < 7; i++) {
            String Key_i = Key;
            for (int j = 0; j < 8; j++) {
                String subKey = Key_i.substring(0, 4);
                Key_i = Key_i.substring(4,Key_i.length());
                int[] rowBytes = Get_ByteRow_From_String(subKey, 2);
                RoundKeys.add(rowBytes);
            }

            for (int l = 0; l < 25; l++){
               KeyBytes = Left_Shift(KeyBytes);
            }
        }
    }

    public static int[] Left_Shift(int[] byteRow) {
        int value0 = byteRow[0];
        for (int i = 0; i < byteRow.length - 1; i++) {
            byteRow[i] = byteRow[i + 1];
        }

        byteRow[15] = value0;
        return byteRow;
    }

    public int[] Get_ByteRow_From_String(String subKey, int length) {
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
}
