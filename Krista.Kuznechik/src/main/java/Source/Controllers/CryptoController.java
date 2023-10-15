package Source.Controllers;

import Source.Crypto_Services.Kuznechik.GrasshopperCipher;
import Source.Models.CipherModel;
import Source.Models.OperModel;
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
@RequestMapping("/algorithm")
public class CryptoController {

    @GetMapping()
    public String Index(Model model)  {
        CipherModel cipher = new CipherModel();
        List<OperModel> oper = new ArrayList<>();
        oper.add(new OperModel(1,"Зашифровать"));
        oper.add(new OperModel(2,"Расшифровать"));
        model.addAttribute("cipher", cipher);
        model.addAttribute("Operation", oper);
        return "index";
    }

    @PostMapping()
    public String Encrypt(Model model, @ModelAttribute("cipher") CipherModel cipherText) throws DecoderException, IOException {
        GrasshopperCipher grasshopperCipher = new GrasshopperCipher();

        switch (cipherText.getMode()){
            case 1:
                model.addAttribute("cipher", grasshopperCipher.Get_Cipher_Text(cipherText.getCipher()));
                break;
            case 2:
                model.addAttribute("decipher", grasshopperCipher.Get_Open_Text(cipherText.getCipher()));
                break;
        }

        return "ResultText";
    }
}
