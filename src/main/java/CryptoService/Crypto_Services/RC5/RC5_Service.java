package CryptoService.Crypto_Services.RC5;

import CryptoService.Services.ConvertService;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RC5_Service {
    private static String Key = "cca86c95d579197eec83352b066e933c";
    public static final List<List<Integer>> RoundKeys = new LinkedList<>();
    private static ConvertService convertService = new ConvertService();

    private static int W;
    private static int R;
    private static int b;

    RC5_Service(int w, int r, int b) {
        this.W = w;
        this.R = r;
        this.b = b;
    }

    public static String Get_Cipher_Text() {
        Split_Key_to_Words();

        return "ffff";
    }

    private static void Split_Key_to_Words() {
        if (Key.length() % 2 != 0) {
            Key += "0";
        }

        int[] byteKey = convertService.Get_ByteRow_From_String(Key, Key.length() / 2);
        List<Integer> keyRow = Arrays.stream(byteKey).boxed().toList();
        b = byteKey.length;
        int u = W / 8;
        while (b % u != 0) {
            keyRow.add(0);
            b++;
        }

        int c = b / u;
        int len = b / c;
        for (int i = 0; i < c; i++) {
            RoundKeys.add(keyRow.subList(i * len, i * len + len));
        }
    }
}
