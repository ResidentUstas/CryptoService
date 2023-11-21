package CryptoService.Controllers;

import CryptoService.Crypto_Services.IDEA.IDEAcipher;
import CryptoService.Crypto_Services.RC5.RC5cipher;
import CryptoService.Crypto_Services.RC5.types.params;
import CryptoService.Crypto_Services.RC5.types.parameters;
import CryptoService.Models.CipherModel;
import CryptoService.Models.OperModel;
import CryptoService.Models.paramModel;
import org.apache.commons.codec.DecoderException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rc5")
public class RC5Controller {
    @GetMapping()
    public String Index(Model model) {
        CipherModel cipher = new CipherModel();
        List<OperModel> oper = new ArrayList<>();
        List<paramModel> params = new ArrayList<>();
        List<paramModel> dopParams = new ArrayList<>();
        params.add(new paramModel(16, 16));
        params.add(new paramModel(32, 32));
        params.add(new paramModel(64, 64));
        oper.add(new OperModel(1, "Зашифровать"));
        oper.add(new OperModel(2, "Расшифровать"));
        model.addAttribute("cipher", cipher);
        model.addAttribute("Operation", oper);
        model.addAttribute("Params", params);

        for (int i = 0; i < 256; i++) {
            dopParams.add(new paramModel(i, i));
        }

        model.addAttribute("Parameters", dopParams);
        return "views/rc5/index";
    }

    @PostMapping()
    public String Encrypt(Model model, @ModelAttribute("cipher") CipherModel cipherText) throws DecoderException, IOException {
        RC5cipher rc5Cipher = new RC5cipher();
        switch (cipherText.getMode()) {
            case 1:
                model.addAttribute("cipher", rc5Cipher.Get_Cipher_Text(cipherText.getCipher(), cipherText.getRounds(), cipherText.getWord(), cipherText.getKeyBits(), cipherText.getWord()));
            case 2:
                model.addAttribute("cipher", rc5Cipher.Get_Open_Text(cipherText.getCipher(), cipherText.getRounds(), cipherText.getWord(), cipherText.getKeyBits(), cipherText.getWord()));
                break;
        }

        return "views/idea/ResultText";
    }
}