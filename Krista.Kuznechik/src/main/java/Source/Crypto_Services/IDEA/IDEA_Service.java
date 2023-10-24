package Source.Crypto_Services.IDEA;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class IDEA_Service {

    public final List<Integer> RoundKeys = new LinkedList<>();
    public final List<Integer> ReversKeys = new LinkedList<>();
    private String Key = "00010002000300040005000600070008";

    public String Make_Cipher_Text(String text, int mode) {
        List<Integer> thisRoundKeys = mode == 1 ? RoundKeys : ReversKeys;
        int[] KeyBytes = Get_ByteRow_From_String(text, 8);
        int[] D1 = new int[2];
        int[] D2 = new int[2];
        int[] D3 = new int[2];
        int[] D4 = new int[2];
        System.arraycopy(KeyBytes, 0, D1, 0, 2);
        System.arraycopy(KeyBytes, 2, D2, 0, 2);
        System.arraycopy(KeyBytes, 4, D3, 0, 2);
        System.arraycopy(KeyBytes, 6, D4, 0, 2);
        int x0 = arrayToInt(D1);
        int x1 = arrayToInt(D2);
        int x2 = arrayToInt(D3);
        int x3 = arrayToInt(D4);
        int p = 0;
        for (int round = 0; round < 8; round++) {
            int y0 = mul(x0, thisRoundKeys.get(p++));
            int y1 = add(x1, thisRoundKeys.get(p++));
            int y2 = add(x2, thisRoundKeys.get(p++));
            int y3 = mul(x3, thisRoundKeys.get(p++));

            int t0 = mul(y0 ^ y2, thisRoundKeys.get(p++));
            int t1 = add(y1 ^ y3, t0);
            int t2 = mul(t1, thisRoundKeys.get(p++));
            int t3 = add(t0, t2);

            x0 = y0 ^ t2;
            x1 = y2 ^ t2;
            x2 = y1 ^ t3;
            x3 = y3 ^ t3;
        }

        int r0 = mul(x0, thisRoundKeys.get(p++));
        int r1 = add(x2, thisRoundKeys.get(p++));
        int r2 = add(x1, thisRoundKeys.get(p++));
        int r3 = mul(x3, thisRoundKeys.get(p++));

        int[] data = new int[8];
        data[0] = (byte) (r0 >> 8);
        data[1] = (byte) r0;
        data[2] = (byte) (r1 >> 8);
        data[3] = (byte) r1;
        data[4] = (byte) (r2 >> 8);
        data[5] = (byte) r2;
        data[6] = (byte) (r3 >> 8);
        data[7] = (byte) r3;

        String result = Get_hex_string(data);
        return result;
    }

    private int mul(int a, int b) {
        long r = (long) a * b;
        if (r != 0) {
            return (int) (r % 0x10001) & 0xFFFF;
        } else {
            return (1 - a - b) & 0xFFFF;
        }
    }

    private static int add(int a, int b) {
        return (a + b) & 0xFFFF;
    }

    private int arrayToInt(int[] arr) {
        String bits = Get_Bit_View(arr);
        int result = Integer.parseInt(bits, 2);

        return result;
    }

    public void Generate_Keys() {
        int[] KeyBytes = Get_ByteRow_From_String(Key, 16);
        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < 16; i++) {
                int[] key_i = new int[2];
                System.arraycopy(KeyBytes, i, key_i, 0, 2);
                i++;
                RoundKeys.add(arrayToInt(key_i));
            }

            KeyBytes = Get_Idea_Left_Shift(KeyBytes);
        }

        RoundKeys.subList(52, 56).clear();
        Generate_revers_keys();
    }

    private void Generate_revers_keys() {
        for (int i = 0; i < 9; i++) {
            int subkey1 = Extend_Alg_Evclida(RoundKeys.get((8 - i) * 6 + 0), 65537)[1];
            int index2 = i == 0 || i == 8 ? ((8 - i) * 6) + 1 : ((8 - i) * 6) + 2;
            int index3 = i == 0 || i == 8 ? ((8 - i) * 6) + 2 : ((8 - i) * 6) + 1;
            int subkey2 = 65536 - RoundKeys.get(index2);
            int subkey3 = 65536 - RoundKeys.get(index3);
            int subkey4 = Extend_Alg_Evclida(RoundKeys.get((8 - i) * 6 + 3), 65537)[1];
            ReversKeys.add(subkey1 < 0 ? subkey1 + 65537 : subkey1);
            ReversKeys.add(subkey2);
            ReversKeys.add(subkey3);
            ReversKeys.add(subkey4 < 0 ? subkey4 + 65537 : subkey4);
            if (i < 8) {
                ReversKeys.add(RoundKeys.get((8 - i - 1) * 6 + 4));
                ReversKeys.add(RoundKeys.get((8 - i - 1) * 6 + 5));
            }
        }
    }

    private int[] Extend_Alg_Evclida(int a, int b) {
        int res[] = new int[3];
        if (b == 0) {
            res[0] = a;
            res[1] = 1;
            res[2] = 0;
            return res;
        }

        res = Extend_Alg_Evclida(b, a % b);
        int coeff = res[2];
        res[2] = res[1] - (a / b) * res[2];
        res[1] = coeff;

        return res;
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

    public String Get_hex_string(int[] byteRow) {
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
}
