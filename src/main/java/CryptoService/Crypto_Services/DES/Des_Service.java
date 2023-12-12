package CryptoService.Crypto_Services.DES;

import CryptoService.Services.ConvertService;
import CryptoService.Services.IOService;
import org.apache.commons.lang3.ArrayUtils;

public class Des_Service {
    private int[] Key;
    private int[] PC1 = Des_Initials.PC1;
    private int[] PC2 = Des_Initials.PC2;
    private int[] IPb = Des_Initials.IP_back;
    private int[] IP = Des_Initials.IP;
    private int[] PBox_Straight = Des_Initials.pbox_straight;
    private int[][] sbox1 = Des_Initials.sbox1;
    private int[][] sbox2 = Des_Initials.sbox2;
    private int[][] sbox3 = Des_Initials.sbox3;
    private int[][] sbox4 = Des_Initials.sbox4;
    private int[][] sbox5 = Des_Initials.sbox5;
    private int[][] sbox6 = Des_Initials.sbox6;
    private int[][] sbox7 = Des_Initials.sbox7;
    private int[][] sbox8 = Des_Initials.sbox8;
    private int[][][] sbox_collection = new int[][][]{sbox1, sbox2, sbox3, sbox4, sbox5, sbox6, sbox7, sbox8};
    private int[] P_Extention = Des_Initials.P_Extention;
    private int Rounds;
    private String[] RoundKeysStr = new String[16];
    private int[] Key_shifts = Des_Initials.KEY_SHIFTS;
    private final ConvertService convertService = new ConvertService();

    public Des_Service(int rounds, int[] key){
        this.Rounds = rounds;
        this.Key = key;
    }

    public void KeyExtension() {
        if (RoundKeysStr[0] != null) {
            return;
        }

        String key_bits = convertService.Get_Bit_View(Key);
        key_bits = Permutation(PC1, key_bits);
        String Ci = key_bits.substring(0, 28);
        String Di = key_bits.substring(28, key_bits.length());
        for (int i = 0; i < Key_shifts.length; i++) {
            Ci = Left_Shift(Ci, Key_shifts[i]);
            Di = Left_Shift(Di, Key_shifts[i]);
            String Round_key = Permutation(PC2, Ci + Di);
            RoundKeysStr[i] = Round_key;
        }
    }

    public String Get_Cipher_Text(String Block) {
        int[] bytes = convertService.Get_ByteRow_From_String(Block, 8);
        ArrayUtils.reverse(bytes);
        Block = convertService.Get_Bit_View(bytes);
        Block = Permutation(IP, Block);
        Block = Feistel_net(Block, 1); // 1-режим шифрования
        Block = Permutation(IPb, Block);
        Long result_L = Long.parseLong(Block.substring(0, 32), 2);
        Long result_R = Long.parseLong(Block.substring(32, Block.length()), 2);
        String L_str = Long.toHexString(result_L);
        String R_str = Long.toHexString(result_R);
        while (L_str.length() < 8) {
            L_str = "0" + L_str;
        }

        while (R_str.length() < 8) {
            R_str = "0" + R_str;
        }

        Block = L_str + R_str;
        return Block;
    }

    public String Get_Open_Text(String Block) {
        int[] bytes = convertService.Get_ByteRow_From_String(Block, 8);
        ArrayUtils.reverse(bytes);
        Block = convertService.Get_Bit_View(bytes);
        Block = Permutation(IP, Block);
        Block = Feistel_net(Block, 0); // 0-режим расшифрования
        Block = Permutation(IPb, Block);
        Long result_L = Long.parseLong(Block.substring(0, 32), 2);
        Long result_R = Long.parseLong(Block.substring(32, Block.length()), 2);
        String L_str = Long.toHexString(result_L);
        String R_str = Long.toHexString(result_R);
        while (L_str.length() < 8) {
            L_str = "0" + L_str;
        }

        while (R_str.length() < 8) {
            R_str = "0" + R_str;
        }

        Block = L_str + R_str;
        return Block;
    }

    private String Feistel_net(String OpenBlock, int mode) {
        String L = OpenBlock.substring(0, 32);
        String R = OpenBlock.substring(32, OpenBlock.length());
        String L0 = R;
        for (int i = 0; i < Rounds; i++) {
            R = Des_Function(R, mode == 1 ? i : Rounds - i - 1);
            R = XOR(R, L);
            L = L0;
            L0 = R;
        }

        return R + L;
    }

    private String view(String L) {
        Long x = Long.parseLong(L, 2);
        String y = Long.toHexString(x);
        return y;
    }

    private String Des_Function(String R, int round) {
        String R_four = "";
        R = Permutation(P_Extention, R);
        R = XOR(R, RoundKeysStr[round]);
        for (int j = 0; j < 8; j++) {
            String R_six = R.substring(j * 6, j * 6 + 6);
            R_four += Sbox_display(R_six, sbox_collection[j]);
        }

        R_four = Permutation(PBox_Straight, R_four);

        return R_four;
    }

    private String Sbox_display(String R_six, int[][] sbox) {
        String binary1 = String.valueOf(R_six.charAt(0)) + String.valueOf(R_six.charAt(5));
        String binary2 = String.valueOf(R_six.charAt(1)) + String.valueOf(R_six.charAt(2))
                + String.valueOf(R_six.charAt(3)) + String.valueOf(R_six.charAt(4));
        int i = Integer.parseInt(binary1, 2);
        int j = Integer.parseInt(binary2, 2);

        String result = Integer.toString(sbox[i][j], 2);
        while (result.length() < 4) {
            result = "0" + result;
        }
        return result;
    }

    private String XOR(String a, String b) {
        String result = "";
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(i)) {
                result += "0";
            } else {
                result += "1";
            }
        }

        return result;
    }

    private String Permutation(int[] permutation, String val) {
        String result = "";
        for (int i = 0; i < permutation.length; i++) {
            result += val.charAt(permutation[i] - 1);
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
