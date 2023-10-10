package Source.Crypto_Services.Kuznechik;

import org.thymeleaf.util.ArrayUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Cipher {
    private final int[] LinearTransformRow = new int[]{1, 148, 32, 133, 16, 194, 192, 1, 251, 1, 192, 194, 16, 133, 32, 148};
    private final char[] HEX_Alphabet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private final List<int[]> RoundKeys = new LinkedList<>();
    private String Key = "8899aabbccddeeff0011223344556677fedcba98765432100123456789abcdef";
    private List<Integer> Galua_Field_Mutable_Table = new ArrayList<>();
    private final List<int[]> Constants_Ci = new ArrayList<>();
    private final List<String> Constants_Ci_hex = new ArrayList<>();
    private final List<String> Round_Keys_hex = new ArrayList<>();
    private int[] Current_Const_Ci = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


    public int Not_Linear_Transform_Table[] = new int[]{
            252, 238, 221, 17, 207, 110, 49, 22, 251, 196, 250, 218, 35, 197, 4, 77,
            233, 119, 240, 219, 147, 46, 153, 186, 23, 54, 241, 187, 20, 205, 95, 193,
            249, 24, 101, 90, 226, 92, 239, 33, 129, 28, 60, 66, 139, 1, 142, 79, 5,
            132, 2, 174, 227, 106, 143, 160, 6, 11, 237, 152, 127, 212, 211, 31, 235,
            52, 44, 81, 234, 200, 72, 171, 242, 42, 104, 162, 253, 58, 206, 204, 181,
            112, 14, 86, 8, 12, 118, 18, 191, 114, 19, 71, 156, 183, 93, 135, 21, 161,
            150, 41, 16, 123, 154, 199, 243, 145, 120, 111, 157, 158, 178, 177, 50, 117,
            25, 61, 255, 53, 138, 126, 109, 84, 198, 128, 195, 189, 13, 87, 223, 245,
            36, 169, 62, 168, 67, 201, 215, 121, 214, 246, 124, 34, 185, 3, 224, 15,
            236, 222, 122, 148, 176, 188, 220, 232, 40, 80, 78, 51, 10, 74, 167, 151,
            96, 115, 30, 0, 98, 68, 26, 184, 56, 130, 100, 159, 38, 65, 173, 69, 70,
            146, 39, 94, 85, 47, 140, 163, 165, 125, 105, 213, 149, 59, 7, 88, 179, 64,
            134, 172, 29, 247, 48, 55, 107, 228, 136, 217, 231, 137, 225, 27, 131, 73,
            76, 63, 248, 254, 141, 83, 170, 144, 202, 216, 133, 97, 32, 113, 103, 164,
            45, 43, 9, 91, 203, 155, 37, 208, 190, 229, 108, 82, 89, 166, 116, 210, 230,
            244, 180, 192, 209, 102, 175, 194, 57, 75, 99, 182};
    private final Kuznechik_service cryptoService = new Kuznechik_service();

    public void Generate_Round_Keys() {
        String Key_str = Key.substring(0, 64);
        int[] Key = cryptoService.Get_ByteRow_From_String(Key_str, 32);
        int[] K1 = Arrays.copyOfRange(Key, 16, 32);
        int[] K2 = Arrays.copyOfRange(Key, 0, 16);
        int[] K_period = new int[16];
        String K1_str = Get_hex_string(K1);
        String K2_str = Get_hex_string(K2);
        Write_Round_Row(K1, RoundKeys, Round_Keys_hex);
        Write_Round_Row(K2, RoundKeys, Round_Keys_hex);
        for (int i = 0; i < 4; i++) {
            for (int l = 0; l < 8; l++) {
                cryptoService.SwapMass(K1, K_period);
                K1 = Feistel_Cell(K1, K2, Constants_Ci, (8 * i) + l);
                cryptoService.SwapMass(K_period, K2);
                K1_str = Get_hex_string(K1);
                K2_str = Get_hex_string(K2);
            }

            Write_Round_Row(K1, RoundKeys, Round_Keys_hex);
            Write_Round_Row(K2, RoundKeys, Round_Keys_hex);
        }
    }

    //Генерация итерационных констант
    public void Generate_Constants_Ci() {
        Galua_Field_Mutable_Table = cryptoService.Get_Mute_Table();
        for (int i = 1; i < 33; i++) {
            Current_Const_Ci[0] = i;
            Linear_Transform();
            Write_Round_Row(Current_Const_Ci, Constants_Ci, Constants_Ci_hex);
            Current_Const_Ci = cryptoService.Get_Zero_Array();
        }

        Generate_Round_Keys();
    }

    //Умножение в поле Галуа
    private int Galua_Mute(int a, int b) {
        int index_a = Galua_Field_Mutable_Table.indexOf(a);
        int index_b = Galua_Field_Mutable_Table.indexOf(b);

        if (a == 0 || b == 0) {
            return 0;
        }

        int index_ab = index_a + index_b;
        index_ab = index_ab > 255 ? index_ab - 255 : index_ab;

        return Galua_Field_Mutable_Table.get(index_ab);
    }

    private void Write_Round_Row(int[] byteArray, List<int[]> RoundRow, List<String> RoundHex) {
        int[] round_row = new int[16];

        for (int i = 0; i < 16; i++) {
            round_row[i] = byteArray[i];
        }

        RoundRow.add(round_row);
        String hex_row = "";
        for (int i = 15; i >= 0; i--) {
            hex_row += Integer.toHexString(byteArray[i]);
        }
        RoundHex.add(hex_row);
    }

    private int[] Feistel_Cell(int[] Key_a, int[] Key_b, List<int[]> byteArray, int round_num) {
        String ks1 = Get_hex_string(Key_a);
        String ks2 = Get_hex_string(Key_b);
        int[] keyRow = cryptoService.XOR(Key_a, byteArray.get(round_num));
        String s1 = Get_hex_string(keyRow);

        Current_Const_Ci = Make_NL_Transform(keyRow);
        String s2 = Get_hex_string(Current_Const_Ci);

        Linear_Transform();
        String s3 = Get_hex_string(Current_Const_Ci);

        keyRow = cryptoService.XOR(Key_b, Current_Const_Ci);
        String s4 = Get_hex_string(keyRow);

        return keyRow;
    }

    private void Linear_Transform() {
        int dec_result = 0;
        for (int l = 0; l < 16; l++) {
            for (int j = 0; j < 16; j++) {
                dec_result = dec_result ^ Galua_Mute(LinearTransformRow[j], Current_Const_Ci[j]);
            }

            Kuznechik_service.Left_Shift(Current_Const_Ci);
            Current_Const_Ci[15] = dec_result;
            dec_result = 0;
        }
    }

    private int[] Make_NL_Transform(int[] byteRow) {
        int[] NL_Row = new int[16];
        for (int i = 0; i < 16; i++) {
            NL_Row[i] = Not_Linear_Transform_Table[byteRow[i]];
        }

        return NL_Row;
    }

    private String Get_hex_string(int[] byteRow) {
        String hex_ci = "";
        for (int i = byteRow.length - 1; i >= 0; i--) {
            hex_ci += Integer.toHexString(byteRow[i]);
        }

        return hex_ci;
    }
}
