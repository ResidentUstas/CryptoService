package CryptoService.Controllers;

import CryptoService.Crypto_Services.BlowFish.BlowFish_cipher;
import CryptoService.Models.CipherModel;
import CryptoService.Models.OperModel;
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
@RequestMapping("/blowfish")
public class BlowFishController {
    @GetMapping()
    public String Index(Model model)  {
        CipherModel cipher = new CipherModel();
        List<OperModel> oper = new ArrayList<>();
        oper.add(new OperModel(1,"Зашифровать"));
        oper.add(new OperModel(2,"Расшифровать"));
        model.addAttribute("cipher", cipher);
        model.addAttribute("Operation", oper);
        return "views/blowfish/index";
    }

    @PostMapping()
    public String Encrypt(Model model, @ModelAttribute("cipher") CipherModel cipherText) throws DecoderException, IOException {
        BlowFish_cipher blowFishCipher = new BlowFish_cipher();

        switch (cipherText.getMode()){
            case 1:
                model.addAttribute("cipher", blowFishCipher.Get_Cipher_Text(cipherText.getCipher()));
                model.addAttribute("path", "D:\\Block_Algorithms\\Block_Ciphers\\cipher\\BlowFish\\blowfish_cipher_result.txt");
                break;
            case 2:
                model.addAttribute("cipher", blowFishCipher.Get_Open_Text(cipherText.getCipher()));
                model.addAttribute("path", "D:\\Block_Algorithms\\Block_Ciphers\\decipher\\BlowFish\\blowfish_decipher_result.txt");
                break;
        }

        return "views/idea/ResultText";
    }
}
