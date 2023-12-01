package CryptoService.Crypto_Services.DES;

import CryptoService.Services.ConvertService;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;

public class Des_Service {
    private String Key = "AABB09182736CCDD";
    private int[] PC1 = Des_Initials.PC1;
    private int[] Key_shifts = Des_Initials.KEY_SHIFTS;
    private final ConvertService convertService = new ConvertService();

    public void KeyExtension() {
        int[] keyBytes = convertService.Get_ByteRow_From_String(Key, 8);
        ArrayUtils.reverse(keyBytes);
        String key_bits = convertService.Get_Bit_View(keyBytes);
        key_bits = Permutation(PC1, key_bits);
        String Ci = key_bits.substring(0, 28);
        String Di = key_bits.substring(18, key_bits.length());
        for (int i = 0; i < Key_shifts.length; i++) {

        }
    }

    private String Permutation(int[] permutation, String val) {
        String result = "";
        for (int i = 0; i < permutation.length; i++) {
            result += val.charAt(permutation[i]);
        }

        return result;
    }

    private String Left_Shift(String val, int num) {
        for (int i = 0; i < num; i++) {
            String swap = val.substring(0, 1);
            val = val.substring(1, val.length());
            val += swap;
        }

        return val;
    }
}
