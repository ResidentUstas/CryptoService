package CryptoService.Crypto_Services.Kuznechik;

import CryptoService.Services.ConvertService;
import CryptoService.Services.IOService;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Kuznechik_service {

    private final int[] LinearTransformRow = new int[] {1, 148, 32, 133, 16, 194, 192, 1, 251, 1, 192, 194, 16, 133, 32, 148};
    public final List<int[]> RoundKeys = new LinkedList<>();
    private int[] Key;
    private List<Integer> Galua_Field_Mutable_Table = new ArrayList<>();
    private final List<int[]> Constants_Ci = new ArrayList<>();
    private int[] Current_Const_Ci = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private ConvertService convertService = new ConvertService();
    private int Rounds;

    public Kuznechik_service(int rounds, int[] key){
        this.Rounds = rounds;
        this.Key = key;
    }

    public Kuznechik_service(int rounds){
        this.Rounds = rounds;
    }

    private int Not_Linear_Transform_Table[] = new int[]{
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

    public static int[] Left_Shift(int[] byteRow) {
        int value0 = byteRow[0];
        for (int i = 0; i < byteRow.length - 1; i++) {
            byteRow[i] = byteRow[i + 1];
        }

        byteRow[15] = value0;
        return byteRow;
    }

    public static int[] Right_Shift(int[] byteRow) {
        int value0 = byteRow[byteRow.length - 1];
        for (int i = byteRow.length - 1; i > 0; i--) {
            byteRow[i] = byteRow[i - 1];
        }

        byteRow[0] = value0;
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

    public String Make_Cipher_Text(String OpenText){
        //Получаем массив байт из строки
        int[] Open_text_bytes = convertService.Get_ByteRow_From_String(OpenText, 16);
        ArrayUtils.reverse(Open_text_bytes);
        int[] period_result = new int[16];
        int[] Key_i = new int[16];
        SwapMass(RoundKeys.get(0), Key_i);

        //Проходим сеть фейстеля
        for (int i = 0; i < Rounds; i++) {
            SwapMass(Open_text_bytes, period_result);

            //Получаем результат одной ячейки фейстеля
            Open_text_bytes = Feistel_Cell(Open_text_bytes, RoundKeys, i);
            SwapMass(period_result, Key_i);
        }

        int[] Cipher_Byte_result = XOR(Open_text_bytes, RoundKeys.get(9));
        String Cipher_result = convertService.Get_hex_string(Cipher_Byte_result);

        return Cipher_result;
    }

    public String Make_Open_Text(String CipherText){
        //Получаем массив байт из строки
        int[] Cipher_text_bytes = convertService.Get_ByteRow_From_String(CipherText, 16);
        ArrayUtils.reverse(Cipher_text_bytes);
        int[] period_result = new int[16];
        int[] Key_i = new int[16];
        SwapMass(RoundKeys.get(9), Key_i);
        Cipher_text_bytes = XOR(Key_i, Cipher_text_bytes);

        //Проходим сеть фейстеля
        for (int i = Rounds; i > 0; i--) {
            SwapMass(Cipher_text_bytes, period_result);

            //Получаем результат одной ячейки фейстеля
            Cipher_text_bytes = Feistel_Cell_Reverse(Cipher_text_bytes, RoundKeys, i - 1);
            SwapMass(period_result, Key_i);
        }

        String Cipher_result = convertService.Get_hex_string(Cipher_text_bytes);

        return Cipher_result;
    }

    public int[] XOR(int[] Row_a, int[] Row_b) {
        int[] Xor_Result = new int[16];
        for (int i = 0; i < 16; i++) {
            Xor_Result[i] = Row_a[i] ^ Row_b[i];
        }

        return Xor_Result;
    }

    public void SwapMass(int[] mas_from, int[] mas_to){
        for(int i = 0; i < mas_to.length; i++){
            mas_to[i] = mas_from[i];
        }
    }

    public int[] Get_Zero_Array() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    // генерация рауновых ключей
    public void Generate_Round_Keys() {
        int[] K1 = Arrays.copyOfRange(Key, 16, 32);
        int[] K2 = Arrays.copyOfRange(Key, 0, 16);
        int[] K_period = new int[16];

        Write_Round_Row(K1, RoundKeys);
        Write_Round_Row(K2, RoundKeys);
        for (int i = 0; i < 4; i++) {
            for (int l = 0; l < 8; l++) {
                SwapMass(K1, K_period);
                K1 = Feistel_Cell(K1, Constants_Ci, (8 * i) + l);
                K1 = XOR(K2, K1);
                SwapMass(K_period, K2);
            }

            Write_Round_Row(K1, RoundKeys);
            Write_Round_Row(K2, RoundKeys);
        }
    }

    //Генерация итерационных констант
    public void Generate_Constants_Ci() {
        Galua_Field_Mutable_Table = Get_Mute_Table();
        for (int i = 1; i < 33; i++) {
            Current_Const_Ci[0] = i;
            Linear_Transform(Current_Const_Ci);
            Write_Round_Row(Current_Const_Ci, Constants_Ci);
            Current_Const_Ci = Get_Zero_Array();
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

    private void Write_Round_Row(int[] byteArray, List<int[]> RoundRow) {
        int[] round_row = new int[16];

        for (int i = 0; i < 16; i++) {
            round_row[i] = byteArray[i];
        }

        RoundRow.add(round_row);
    }

    public int[] Feistel_Cell(int[] Key_a, List<int[]> byteArray, int round_num) {

        int[] keyRow = XOR(Key_a, byteArray.get(round_num));

        keyRow = Make_NL_Transform(keyRow);

        Linear_Transform(keyRow);

        return keyRow;
    }

    public int[] Feistel_Cell_Reverse(int[] Key_a, List<int[]> byteArray, int round_num) {

        int[] keyRow = Linear_Transform_Revers(Key_a);

        keyRow = Make_NL_Transform_Reverse(keyRow);

        keyRow = XOR(keyRow, byteArray.get(round_num));

        return keyRow;
    }

    private int[] Linear_Transform(int[] byteArray) {
        int dec_result = 0;
        for (int l = 0; l < 16; l++) {
            for (int j = 0; j < 16; j++) {
                dec_result = dec_result ^ Galua_Mute(LinearTransformRow[j], byteArray[j]);
            }

            Left_Shift(byteArray);
            byteArray[15] = dec_result;
            dec_result = 0;
        }

        return byteArray;
    }

    private int[] Linear_Transform_Revers(int[] byteArray) {
        int dec_result = 0;
        for (int l = 0; l < 16; l++) {
            for (int j = 0; j < 16; j++) {
                dec_result = dec_result ^ Galua_Mute(LinearTransformRow[15 - j], byteArray[j]);
            }

            Right_Shift(byteArray);
            byteArray[0] = dec_result;
            dec_result = 0;
        }

        return byteArray;
    }

    private int[] Make_NL_Transform(int[] byteRow) {
        int[] NL_Row = new int[16];
        for (int i = 0; i < 16; i++) {
            NL_Row[i] = Not_Linear_Transform_Table[byteRow[i]];
        }

        return NL_Row;
    }

    private int[] Make_NL_Transform_Reverse(int[] byteRow) {
        int[] NL_Row = new int[16];
        for (int i = 0; i < 16; i++) {
            NL_Row[i] = ArrayUtils.indexOf(Not_Linear_Transform_Table, byteRow[i]);
        }

        return NL_Row;
    }
}
