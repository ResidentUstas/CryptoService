package CryptoService.Crypto_Services.BlowFish;

import CryptoService.Services.ConvertService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class BlowFish_Service {
    private long sbox0[] = BF_Initials.sbox0;
    private long sbox1[] = BF_Initials.sbox1;
    private long sbox2[] = BF_Initials.sbox2;
    private long sbox3[] = BF_Initials.sbox3;
    private long P_array[] = BF_Initials.parray;
    private long[] cellResult = new long[]{0, 0};
    private String Key = "3d04182a5bb8d979973d6ba37ac18435cd";

    private final ConvertService convertService = new ConvertService();

    private void P_extension() throws DecoderException {
        byte[] key = Hex.decodeHex(Key);
        long long_key = 0;
        for (int i = 0, k = 0; i < 18; i++) {
            for (long j = 0; j < 4; j++, k++) {
                long_key = (long_key << 8) | key[k % key.length];
            }
            P_array[i] ^= long_key;
            long_key = 0;
        }
    }

    private void cipher_initials() {
        for (int i = 0; i < 18; i++) {
            cellResult = Cipher_cell(cellResult[0], cellResult[1]);
            P_array[i] = cellResult[0];
            P_array[++i] = cellResult[1];
        }

        cipher_sbox(sbox0);
        cipher_sbox(sbox1);
        cipher_sbox(sbox2);
        cipher_sbox(sbox3);
    }

    private void cipher_sbox(long[] sbox){
        for (int j = 0; j < 256; j++) {
            cellResult = Cipher_cell(cellResult[0], cellResult[1]);
            sbox[j] = cellResult[0];
            sbox[++j] = cellResult[1];
        }
    }

    private long[] Cipher_cell(long Left, long Right) {
        for (int i = 0; i < 16; i++) {
            Left ^= P_array[i];
            Right ^= F_function(Left);
        }

        long swap = Right;
        Right = Left;
        Left = swap;

        Left ^= P_array[17];
        Right ^= P_array[16];

        return new long[]{Left, Right};
    }

    public long F_function(long block) {
        return ((sbox0[(int) ((block >> 24) & 0xFF)] + sbox1[(int) ((block >> 16) & 0xFF)]) ^
                sbox2[(int) ((block >> 8) & 0xFF)]) + sbox3[(int) ((block) & 0xFF)];
    }
}
