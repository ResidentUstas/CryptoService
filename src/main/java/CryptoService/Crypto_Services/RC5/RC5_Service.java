package CryptoService.Crypto_Services.RC5;

import CryptoService.Services.ConvertService;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RC5_Service {
    private static String Key = "cca86c95d579197eec83352b066e933c";
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

    public static String Get_Cipher_Text() {
        Split_Key_to_Words();
        Get_Wide_Keys();
        Mixing();
        return "ffff";
    }

    private static void Make_Cipher() {

    }

    private static void Mixing() {
        int N = 3 * c > 3 * 2 * (R + 1) ? 3 * c : 3 * 2 * (R + 1);
        BigInteger G = new BigInteger("0");
        BigInteger H = new BigInteger("0");
        int i = 0;
        int j = 0;

        for (int index = 0; index < N; index++) {
            G = (WideKeysTable.get(i).add(G)).add(H);

            String G_str = Get_String_View(G.toByteArray());
            G_str = Left_Shift_String(G_str, 3);
            G = new BigInteger(G_str, 2);

            H = (RoundKeysWords.get(j).add(G)).add(H);

            int temp = (G.add(H)).mod(new BigInteger(String.valueOf(W))).intValue();
            int len = Integer.parseInt("" + temp);
            String H_str = Get_String_View(H.toByteArray());
            H_str = Left_Shift_String(G_str, len);
            H = new BigInteger(H_str, 2);

            WideKeysTable.set(i, G);
            RoundKeysWords.set(j, H);

            i = (i + 1) % (2 * (R + 1));
            j = (j + 1) % c;
        }
    }

    public static String Left_Shift_String(String byteRow, int shift) {
        for (int i = 0; i < shift; i++) {
            String value0 = byteRow.substring(0, 1);
            byteRow = byteRow.substring(1, byteRow.length());
            byteRow = byteRow + value0;
        }

        return byteRow;
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
