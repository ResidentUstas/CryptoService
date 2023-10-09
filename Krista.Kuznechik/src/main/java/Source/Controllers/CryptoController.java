package Source.Controllers;

import Source.Crypto_Services.Kuznechik.Cipher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/algorithm")
public class CryptoController {

    @GetMapping()
    public String hello(){
        Cipher cipher = new Cipher();
        cipher.Generate_Constants_Ci();
        return "index";
    }
}
