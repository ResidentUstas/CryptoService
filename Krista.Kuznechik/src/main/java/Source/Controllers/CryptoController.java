package Source.Controllers;

import Source.Crypto_Services.Kuznechik.GrasshopperCipher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/algorithm")
public class CryptoController {

    @GetMapping()
    public String hello(Model model){
        GrasshopperCipher grasshopperCipher = new GrasshopperCipher();
        model.addAttribute("cipher", grasshopperCipher.Get_Cipher_Text());
        model.addAttribute("decipher", grasshopperCipher.Get_Open_Text());
        return "index";
    }
}
