package Source.Crypto_Services.Kuznechik;

public class Kuznechik_service {
    public static int[] Left_Shift(int[] byteRow){
        int value0 = byteRow[0];
        for (int i = 0; i < byteRow.length - 1; i++) {
            byteRow[i] = byteRow[i + 1];
        }
        byteRow[15] = value0;
        return byteRow;
    }
}
