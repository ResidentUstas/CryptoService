package Source.Controllers;

import Source.Crypto_Services.Kuznechik.GrasshopperCipher;
import Source.Models.CipherModel;
import org.apache.commons.codec.DecoderException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/algorithm")
public class CryptoController {

    @GetMapping()
    public String Index(Model model)  {
        CipherModel cipher = new CipherModel();
        model.addAttribute("cipher", cipher);
        return "index";
    }

    @PostMapping()
    public String Encrypt(Model model, @ModelAttribute("cipher") CipherModel cipherText) throws DecoderException, IOException {
        GrasshopperCipher grasshopperCipher = new GrasshopperCipher();
        //model.addAttribute("cipher", grasshopperCipher.Get_Cipher_Text());
        model.addAttribute("decipher", grasshopperCipher.Get_Open_Text(cipherText.getCipher()));
        return "openText";
    }
}
