package CryptoService.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/crypto")
public class CryptoController {
    @GetMapping()
    public String Index(Model model)  {
        return "views/Crypto/index";
    }
}
