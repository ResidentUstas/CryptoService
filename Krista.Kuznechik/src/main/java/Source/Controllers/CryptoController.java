package Source.Controllers;

import Source.Crypto_Services.Kuznechik.Cipher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/algorithm")
public class CryptoController {

    @GetMapping()
    public String hello(Model model){
        Cipher cipher = new Cipher();
        model.addAttribute("cipher", cipher.Get_Cipher_Text());
        return "index";
    }
}
