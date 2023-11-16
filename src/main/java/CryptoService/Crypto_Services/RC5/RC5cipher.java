package CryptoService.Crypto_Services.RC5;

import CryptoService.Services.IOService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RC5cipher {

    public String Get_Cipher_Text(String OpenText, int Rounds, int WordLength, int KeyBits) throws IOException {
        RC5_Service rc5_service = new RC5_Service(WordLength, Rounds, KeyBits);
        String result = rc5_service.Get_Cipher_Text();
        return result;
    }

    public String Get_Open_Text(String CipherText, int Rounds, int WordLength, int KeyBits) throws IOException, DecoderException {
        RC5_Service rc5_service = new RC5_Service(WordLength, Rounds, KeyBits);
        String result = rc5_service.Get_Cipher_Text();
        return result;
    }
}
