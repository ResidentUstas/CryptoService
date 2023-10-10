package Source.Crypto_Services.Kuznechik;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Cipher {
    private final int[] LinearTransformRow = new int[]{1, 148, 32, 133, 16, 194, 192, 1, 251, 1, 192, 194, 16, 133, 32, 148};
    private final char[] HEX_Alphabet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private final List<int[]> RoundKeys = new LinkedList<>();

    private List<Integer> Galua_Field_Mutable_Table = new ArrayList<>();
    private final List<int[]> Constants_Ci = new ArrayList<>();
    private final List<String> Constants_Ci_hex = new ArrayList<>();
    private final int[] Current_Const_Ci = new int[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private final Kuznechik_service cryptoService = new Kuznechik_service();

    public void Generate_Constants_Ci() {
        int dec_result = 0;
        Galua_Field_Mutable_Table = cryptoService.Get_Mute_Table();
        for (int i = 1; i < 33; i++) {
            Current_Const_Ci[0] = i;
            for (int l = 0; l < 16; l++) {
                for (int j = 0; j < 16; j++) {
                    dec_result = dec_result ^ Galua_Mute(LinearTransformRow[j], Current_Const_Ci[j]);
                }

                Kuznechik_service.Left_Shift(Current_Const_Ci);
                Current_Const_Ci[15] = dec_result;
                dec_result = 0;
            }

            Write_Const_Ci(Current_Const_Ci);
            String hex_ci = "";
            for(int ij = 15; ij >= 0; ij--){
                hex_ci+= Integer.toHexString(Current_Const_Ci[ij]);
            }
            for (int il = 0; il < 16; il++){
                Current_Const_Ci[il] = 0;
            }
        }

        int t = 0;
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

    private void Write_Const_Ci(int[] byteArray){
        int[] const_ci = new int[16];

        for (int i = 0; i < 16; i++){
            const_ci[i] = byteArray[i];
        }

        Constants_Ci.add(const_ci);
        String hex_ci = "";
        for(int i = 15; i >= 0; i--){
            hex_ci+= Integer.toHexString(byteArray[i]);
        }
        Constants_Ci_hex.add(hex_ci);
    }

}
