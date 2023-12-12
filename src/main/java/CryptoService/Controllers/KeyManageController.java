package CryptoService.Controllers;

import CryptoService.Crypto_Services.DES.Des_cipher;
import CryptoService.Crypto_Services.Kuznechik.GrasshopperCipher;
import CryptoService.Models.CipherModel;
import CryptoService.Models.OperModel;
import CryptoService.Models.paramModel;
import CryptoService.Services.ConvertService;
import CryptoService.Services.IOService;
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
@RequestMapping("/key")
public class KeyManageController {
    @GetMapping()
    public String Index(Model model)  {
        CipherModel cipher = new CipherModel();
        List<OperModel> oper = new ArrayList<>();
        oper.add(new OperModel(1,"Des"));
        oper.add(new OperModel(2,"BlowFish"));
        oper.add(new OperModel(3,"IDEA"));
        oper.add(new OperModel(4,"Grasshopper"));
        oper.add(new OperModel(5,"Rc-5"));

        model.addAttribute("cipher", cipher);
        model.addAttribute("Operation", oper);
        return "views/key_manage/index";
    }

    @PostMapping()
    public String Encrypt(Model model, @ModelAttribute("cipher") CipherModel cipherText) throws DecoderException, IOException {
        String key = cipherText.getCipher();
        GrasshopperCipher grasshopperCipher = new GrasshopperCipher(9);
        switch (cipherText.getMode()) {
            case 1:
                key = grasshopperCipher.Get_Cipher_Text(key);
                IOService.WriteStringToFile("", "D:\\Diplom\\CryptoService\\Block_Ciphers\\secrets\\DES\\des_password.txt");
                IOService.WriteStringToFile(key,"D:\\Diplom\\CryptoService\\Block_Ciphers\\secrets\\DES\\des_password.txt");
                break;
            case 2:
                key = grasshopperCipher.Get_Cipher_Text(key);
                IOService.WriteStringToFile("","D:\\Diplom\\CryptoService\\Block_Ciphers\\secrets\\BlowFish\\blowfish_password.txt");
                IOService.WriteStringToFile(key,"D:\\Diplom\\CryptoService\\Block_Ciphers\\secrets\\BlowFish\\blowfish_password.txt");
                break;
            case 3:
                key = grasshopperCipher.Get_Cipher_Text(key);
                IOService.WriteStringToFile("","D:\\Diplom\\CryptoService\\Block_Ciphers\\secrets\\IDEA\\idea_password.txt");
                IOService.WriteStringToFile(key,"D:\\Diplom\\CryptoService\\Block_Ciphers\\secrets\\IDEA\\idea_password.txt");
                break;
            case 4:
                key = grasshopperCipher.Get_Cipher_Text(key);
                IOService.WriteStringToFile("","D:\\Diplom\\CryptoService\\Block_Ciphers\\secrets\\Grasshopper\\grasshopper_password.txt");
                IOService.WriteStringToFile(key,"D:\\Diplom\\CryptoService\\Block_Ciphers\\secrets\\Grasshopper\\grasshopper_password.txt");
                break;
            case 5:
                key = grasshopperCipher.Get_Cipher_Text(key);
                IOService.WriteStringToFile("","D:\\Diplom\\CryptoService\\Block_Ciphers\\secrets\\RC-5\\rc5_password.txt");
                IOService.WriteStringToFile(key,"D:\\Diplom\\CryptoService\\Block_Ciphers\\secrets\\RC-5\\rc5_password.txt");
                break;
        }

        return "views/key_manage/ResultKey";
    }
}
