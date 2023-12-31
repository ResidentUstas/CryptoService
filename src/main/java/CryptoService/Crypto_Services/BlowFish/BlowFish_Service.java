package CryptoService.Crypto_Services.BlowFish;

import CryptoService.Services.ConvertService;
import CryptoService.Services.IOService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.ArrayUtils;

public class BlowFish_Service {
    private int sbox0[] = BF_Initials.sbox0;
    private int sbox1[] = BF_Initials.sbox1;
    private int sbox2[] = BF_Initials.sbox2;
    private int sbox3[] = BF_Initials.sbox3;
    private int P_array[] = BF_Initials.parray;
    private int[] cellResult = new int[]{0, 0};
    private int[] Key;

    private int Rounds;
    private final ConvertService convertService = new ConvertService();

    public BlowFish_Service(int rounds, int[] key) {
        this.Rounds = rounds;
        this.Key = key;
    }

    public void Setup() throws DecoderException {
        if (P_array.length == 0) {
            P_extension();
            cipher_initials();
        }
    }

    public String Get_Cipher_Text(String OpenBlock) throws DecoderException {
        int[] OpenBytes = convertService.Get_ByteRow_From_String(OpenBlock, 8);
        ArrayUtils.reverse(OpenBytes);
        int Left = 0;
        int Right = 0;

        for (int j = 0, k = 0; j < 4; j++, k++) {
            Left = (Left << 8) | OpenBytes[k % OpenBytes.length];
        }

        for (int j = 0, k = 4; j < 4; j++, k++) {
            Right = (Right << 8) | OpenBytes[k % OpenBytes.length];
        }

        int[] cipher_bytes = Feistel_cipher_net(Left, Right);

        String Left_str = Integer.toHexString(cipher_bytes[0]);
        String Right_str = Integer.toHexString(cipher_bytes[1]);

        while (Left_str.length() < 8) {
            Left_str = "0" + Left_str;
        }

        while (Right_str.length() < 8) {
            Right_str = "0" + Right_str;
        }
        String result = Left_str + Right_str;
        return result;
    }

    public String Get_Open_Text(String OpenBlock) throws DecoderException {
        int[] OpenBytes = convertService.Get_ByteRow_From_String(OpenBlock, 8);
        ArrayUtils.reverse(OpenBytes);
        int Left = 0;
        int Right = 0;

        for (int j = 0, k = 0; j < 4; j++, k++) {
            Left = (Left << 8) | OpenBytes[k % OpenBytes.length];
        }

        for (int j = 0, k = 4; j < 4; j++, k++) {
            Right = (Right << 8) | OpenBytes[k % OpenBytes.length];
        }

        int[] cipher_bytes = Feistel_decipher_net(Left, Right);

        String Left_str = Integer.toHexString(cipher_bytes[0]);
        String Right_str = Integer.toHexString(cipher_bytes[1]);

        while (Left_str.length() < 8) {
            Left_str = "0" + Left_str;
        }

        while (Right_str.length() < 8) {
            Right_str = "0" + Right_str;
        }

        return Left_str + Right_str;
    }

    private void P_extension() throws DecoderException {
        int[] key = Key;
        int long_key = 0;
        for (int i = 0, k = 0; i < 18; i++) {
            for (int j = 0; j < 4; j++, k++) {
                long_key = (long_key << 8) | key[k % key.length];
            }
            P_array[i] ^= long_key;
            long_key = 0;
        }
    }

    private void cipher_initials() {
        for (int i = 0; i < 18; i++) {
            cellResult = Feistel_cipher_net(cellResult[0], cellResult[1]);
            P_array[i] = cellResult[0];
            P_array[++i] = cellResult[1];
        }

        cipher_sbox(sbox0);
        cipher_sbox(sbox1);
        cipher_sbox(sbox2);
        cipher_sbox(sbox3);
    }

    private void cipher_sbox(int[] sbox) {
        for (int j = 0; j < 256; j++) {
            cellResult = Feistel_cipher_net(cellResult[0], cellResult[1]);
            sbox[j] = cellResult[0];
            sbox[++j] = cellResult[1];
        }
    }

    private int[] Feistel_cipher_net(int Left, int Right) {
        for (int i = 0; i < Rounds; i++) {
            Left ^= P_array[i];
            Right ^= F_function(Left);

            int swap = Right;
            Right = Left;
            Left = swap;
        }

        int swap = Right;
        Right = Left;
        Left = swap;

        Left ^= P_array[17];
        Right ^= P_array[16];

        return new int[]{Left, Right};
    }

    private int[] Feistel_decipher_net(int Left, int Right) {
        for (int i = 17; i > 1; --i) {
            Left ^= P_array[i];
            Right ^= F_function(Left);

            int swap = Right;
            Right = Left;
            Left = swap;
        }

        int swap = Right;
        Right = Left;
        Left = swap;

        Left ^= P_array[0];
        if (Rounds > 1)
            Right ^= P_array[1];

        return new int[]{Left, Right};
    }

    public int F_function(int block) {
        return ((sbox0[(block >> 24) & 0xFF] + sbox1[(block >> 16) & 0xFF]) ^
                sbox2[(block >> 8) & 0xFF]) + sbox3[(block) & 0xFF];
    }
}
