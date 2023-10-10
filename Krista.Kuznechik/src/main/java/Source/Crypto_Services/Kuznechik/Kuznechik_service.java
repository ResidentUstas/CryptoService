package Source.Crypto_Services.Kuznechik;

import java.util.ArrayList;
import java.util.List;

public class Kuznechik_service {
    public static int[] Left_Shift(int[] byteRow){
        int value0 = byteRow[0];
        for (int i = 0; i < byteRow.length - 1; i++) {
            byteRow[i] = byteRow[i + 1];
        }
        byteRow[15] = value0;
        return byteRow;
    }

    ////Заполняет таблицу степеней двойки для поля $GF(2^8)
    public List<Integer> Get_Mute_Table() {
        List<Integer> mute_tabl = new ArrayList<>();
        mute_tabl.add(1);
        for (int i = 1; i < 256; i++) {
            mute_tabl.add(mute_tabl.get(i - 1) * 2);
            if (mute_tabl.get(i) > 255) {
                int modul = mute_tabl.get(i) ^ 195;
                modul -= 256;
                mute_tabl.set(i, modul);
            }
        }

        return mute_tabl;
    }
}
