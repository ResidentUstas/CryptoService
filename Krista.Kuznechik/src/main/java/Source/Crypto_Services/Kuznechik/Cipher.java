package Source.Crypto_Services.Kuznechik;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Cipher {
    private int[] LinearTransformRow = new int[] { 1, 148, 32, 133, 16, 194, 192, 1, 251, 1, 192, 194, 16, 133, 32, 148 };
    private char[] HEX_Alphabet = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    private List<int[]> RoundKeys = new LinkedList<>();
    private List<int[]> Constants_Ci = new ArrayList<>();
    private int[] Current_Const_Ci = new int[] { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    private Kuznechik_service cryptoService = new Kuznechik_service();
    public void Generate_Constants_Ci(){
        int dec_result = 0;
        for (int i = 1; i < 33; i++){
            for (int j = 0; j < 16; j++){
                 dec_result += LinearTransformRow[j] * Current_Const_Ci[j];
            }

            int[] Const_Ci = new int[16];
            Const_Ci = cryptoService.Left_Shift(Current_Const_Ci);
            Const_Ci[15] = dec_result;
            dec_result = 0;
            Constants_Ci.add(Const_Ci);
        }

        int t = 0;
    }


}
