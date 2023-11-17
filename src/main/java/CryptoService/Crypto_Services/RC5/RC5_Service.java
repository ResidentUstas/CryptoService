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
    }

    public static String Get_Cipher_Text() {
        Split_Key_to_Words();
        Get_Wide_Keys();
        return "ffff";
    }

    private static void Mixing(){
        int N = 3 * c > 3 * 2 * (R + 1) ? 3 * c : 3 * 2 * (R + 1);
        BigInteger G = new BigInteger("0"); BigInteger H = new BigInteger("0"); int i = 0; int j = 0;

        for(int index = 0; index < N; index++){
            G = WideKeysTable.get(i) = WideKeysTable.get(i).add(G) + G + H
        }
    }

    private static void Get_Wide_Keys() {
        int index = 2 * (R + 1) - 1;
        WideKeysTable.add(P_const);
        for(int i = 0; i < index; i++){
            BigInteger key = WideKeysTable.get(i).add(Q_const);
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
