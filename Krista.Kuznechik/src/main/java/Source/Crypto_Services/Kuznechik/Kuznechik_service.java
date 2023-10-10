package Source.Crypto_Services.Kuznechik;

import java.util.ArrayList;
import java.util.List;

public class Kuznechik_service {
    public static int[] Left_Shift(int[] byteRow) {
        int value0 = byteRow[0];
        for (int i = 0; i < byteRow.length - 1; i++) {
            byteRow[i] = byteRow[i + 1];
        }
        byteRow[15] = value0;
        return byteRow;
    }

    ////Заполняет таблицу степеней двойки для поля $GF(2^8)
    public List<Integer> Get_Mute_Table() {
        List<Integer> mute_tabl = new ArrayList<>();
        mute_tabl.add(1);
        for (int i = 1; i < 256; i++) {
            mute_tabl.add(mute_tabl.get(i - 1) * 2);
            if (mute_tabl.get(i) > 255) {
                int modul = mute_tabl.get(i) ^ 195;
                modul -= 256;
                mute_tabl.set(i, modul);
            }
        }

        return mute_tabl;
    }

    public int[] XOR(int[] Row_a, int[] Row_b) {
        int[] Xor_Result = new int[16];
        for (int i = 0; i < 16; i++) {
            Xor_Result[i] = Row_a[i] ^ Row_b[i];
        }

        return Xor_Result;
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

    public void SwapMass(int[] mas_from, int[] mas_to){
        for(int i = 0; i < mas_to.length; i++){
            mas_to[i] = mas_from[i];
        }
    }

    public int[] Get_Zero_Array() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }
}
