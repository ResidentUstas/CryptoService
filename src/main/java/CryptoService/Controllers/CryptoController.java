package CryptoService.Controllers;

import CryptoService.Models.CipherModel;
import CryptoService.Models.OperModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("/crypto")
public class CryptoController {
    @GetMapping()
    public String Index(Model model)  {
        return "views/Crypto/index";
    }
}
