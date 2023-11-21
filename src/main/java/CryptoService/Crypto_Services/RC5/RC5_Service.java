package CryptoService.Crypto_Services.RC5;

import CryptoService.Services.ConvertService;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RC5_Service {
    //private static String Key = "cca86c95d579197eec83352b066e933c";
    private static String Key = "915F4619BE41B2516355A50110A9CE91";
    public static final List<BigInteger> RoundKeysWords = new LinkedList<>();
    public static final List<String> RoundKeysWordsStr = new LinkedList<>();
    public static final List<BigInteger> WideKeysTable = new LinkedList<>();
    private static ConvertService convertService = new ConvertService();

    private BigInteger t = new BigInteger("B7E151628AED2A6B", 16);
    private static BigInteger P_const;
    private static BigInteger Q_const;
    private static BigInteger Module;
    private static int W;
    private static int R;
    private static int b;
    private static int c;

    RC5_Service(int w, int r, int b, String p_const, String q_const) {
        this.W = w;
        this.R = r;
        this.b = b;
        this.P_const = new BigInteger(p_const, 16);
        this.Q_const = new BigInteger(q_const, 16);
        this.Module = new BigInteger(String.valueOf(Math.round(Math.pow(2, W))));
    }

    public static String Get_Cipher_Text(String OpenBlock) {
        Split_Key_to_Words();
        Get_Wide_Keys();
        Mixing();
        return Make_Cipher(OpenBlock);
    }

    public static String Get_Open_Text(String CipherBlock) {
        Split_Key_to_Words();
        Get_Wide_Keys();
        Mixing();
        return Make_Open(CipherBlock);
    }

    private static String Make_Open(String CipherBlock) {
        CipherBlock = "5c102d309a1b1c63";
        String A_str = CipherBlock.substring(0, (W / 8) * 2);
        String B_str = CipherBlock.substring((W / 8) * 2, CipherBlock.length());
        BigInteger A = new BigInteger(A_str, 16);
        BigInteger B = new BigInteger(B_str, 16);

        for (int i = R; i > 0; i--) {
            B = B.subtract(WideKeysTable.get((2 * i) + 1)).mod(Module);
            B = Right_Shift_String(B, A);
            B = B.xor(A);

            A = A.subtract(WideKeysTable.get(2 * i)).mod(Module);
            A = Right_Shift_String(A, B);
            A = A.xor(B);
        }

        B = B.subtract(WideKeysTable.get(1)).mod(Module);
        A = A.subtract(WideKeysTable.get(0)).mod(Module);

        String result = A.toString(16) + B.toString(16);
        return result;
    }

    private static String Make_Cipher(String OpenBlock) {
        OpenBlock = "eedba5216d8f4b15";
        String A_str = OpenBlock.substring(0, (W / 8) * 2);
        String B_str = OpenBlock.substring((W / 8) * 2, OpenBlock.length());
        BigInteger A = new BigInteger(A_str, 16);
        BigInteger B = new BigInteger(B_str, 16);

        A = (A.add(WideKeysTable.get(0))).mod(Module);
        B = (B.add(WideKeysTable.get(1))).mod(Module);

        for (int i = 1; i <= R; i++) {
            A = A.xor(B);
            A = Left_Shift_String(A, B);
            A = (A.add(WideKeysTable.get(2 * i))).mod(Module);

            B = B.xor(A);
            B = Left_Shift_String(B, A);
            B = (B.add(WideKeysTable.get((2 * i) + 1))).mod(Module);
        }

        String result = A.toString(16) + B.toString(16);
        return result;
    }

    private static void Mixing() {
        int N = 3 * c > 3 * 2 * (R + 1) ? 3 * c : 3 * 2 * (R + 1);
        BigInteger G = new BigInteger("0");
        BigInteger H = new BigInteger("0");
        int i = 0;
        int j = 0;

        for (int index = 0; index < N; index++) {
            G = ((WideKeysTable.get(i).add(G)).add(H)).mod(Module);
            G = Left_Shift_String(G, new BigInteger("3"));

            H = ((RoundKeysWords.get(j).add(G)).add(H)).mod(Module);
            H = Left_Shift_String(H, (G.add(H)).mod(Module));

            WideKeysTable.set(i, G);
            RoundKeysWords.set(j, H);

            i = (i + 1) % (2 * (R + 1));
            j = (j + 1) % c;
        }
    }

    public static BigInteger Left_Shift_String(BigInteger byteRowBG, BigInteger shiftBg) {
        int shift = shiftBg.mod(new BigInteger(String.valueOf(W))).intValue();
        String byteRow = Get_String_View(byteRowBG.toByteArray());
        for (int i = 0; i < shift; i++) {
            String value0 = byteRow.substring(0, 1);
            byteRow = byteRow.substring(1, byteRow.length());
            byteRow = byteRow + value0;
        }

        return new BigInteger(byteRow, 2);
    }

    public static BigInteger Right_Shift_String(BigInteger byteRowBG, BigInteger shiftBg) {
        int shift = shiftBg.mod(new BigInteger(String.valueOf(W))).intValue();
        shift = Integer.parseInt("" + shift);
        String byteRow = Get_String_View(byteRowBG.toByteArray());
        for (int i = 0; i < shift; i++) {
            String value0 = byteRow.substring(byteRow.length() - 1, byteRow.length());
            byteRow = byteRow.substring(0, byteRow.length() - 1);
            byteRow = value0 + byteRow;
        }

        return new BigInteger(byteRow, 2);
    }

    private static String Get_String_View(byte[] arr) {
        int[] mas = new int[arr.length];
        for (int i = 0; i < mas.length; i++) {
            mas[i] = arr[i];
        }

        return convertService.Get_Bit_View(mas);
    }

    private static void Get_Wide_Keys() {
        int index = 2 * (R + 1) - 1;
        WideKeysTable.add(P_const);
        for (int i = 0; i < index; i++) {
            BigInteger key = WideKeysTable.get(i).add(Q_const).mod(Module);
            WideKeysTable.add(key);
        }
    }

    private static void Split_Key_to_Words() {
        if (Key.length() % 2 != 0) {
            Key += "0";
        }

        b = Key.length() / 2;
        int u = W / 8;
        while (b % u != 0) {
            Key += "00";
            b++;
        }

        c = b / u;
        int len = b / c;
        for (int i = 0; i < c; i++) {
            RoundKeysWords.add(new BigInteger(Key.substring(i * len * 2, i * len * 2 + len * 2), 16));
            RoundKeysWordsStr.add(Key.substring(i * len * 2, i * len * 2 + len * 2));
        }
    }
}
