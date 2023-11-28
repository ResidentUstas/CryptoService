package CryptoService.Crypto_Services.BlowFish;

import CryptoService.Services.IOService;

import java.io.IOException;

public class BlowFish_cipher {
    BlowFish_Service blowFishService = new BlowFish_Service();
    public String Get_Cipher_Text(String OpenText) throws IOException {
        blowFishService.F_function(76735);
        return "dfdfdf";
    }
}
