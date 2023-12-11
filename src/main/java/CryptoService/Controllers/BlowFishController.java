package CryptoService.Controllers;

import CryptoService.Crypto_Services.BlowFish.BlowFish_cipher;
import CryptoService.Models.CipherModel;
import CryptoService.Models.OperModel;
import CryptoService.Models.paramModel;
import CryptoService.Services.ConvertService;
import CryptoService.Services.IOService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
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
        oper.add(new OperModel(3,"Расстояние Хемминга"));
        List<paramModel> params = new ArrayList<>();
        for (int i = 1; i < 17; i++) {
            params.add(new paramModel(i, i));
        }
        model.addAttribute("Params", params);
        model.addAttribute("cipher", cipher);
        model.addAttribute("Operation", oper);
        return "views/blowfish/index";
    }

    @PostMapping()
    public String Encrypt(Model model, @ModelAttribute("cipher") CipherModel cipherText) throws DecoderException, IOException {
        BlowFish_cipher blowFishCipher = new BlowFish_cipher(cipherText.getRounds());

        switch (cipherText.getMode()){
            case 1:
                model.addAttribute("cipher", blowFishCipher.Get_Cipher_Text(cipherText.getCipher()));
                model.addAttribute("path", "D:\\Diplom\\CryptoService\\Block_Ciphers\\cipher\\BlowFish\\blowfish_cipher_result.txt");
                break;
            case 2:
                model.addAttribute("cipher", blowFishCipher.Get_Open_Text(cipherText.getCipher()));
                model.addAttribute("path", "D:\\Diplom\\CryptoService\\Block_Ciphers\\decipher\\BlowFish\\blowfish_decipher_result.txt");
                break;
            case 3:
                byte[] HemmingBytes0 = cipherText.getCipher().getBytes();
                String cipherTxt = blowFishCipher.Get_Cipher_Text(cipherText.getCipher());
                byte[] HemmingBytes1 = cipherTxt.getBytes();
                int h_distance = ConvertService.Get_Hemming_Distance(HemmingBytes0, HemmingBytes1);
                model.addAttribute("cipher", "Расстояние Хемминга для данного текста равняется: " + h_distance + "\r\nвсего бит: " + HemmingBytes0.length * 8);
                model.addAttribute("path", "D:\\Diplom\\CryptoService\\Block_Ciphers\\hemming\\BlowFish\\blowfish_hemming_result.txt");
                break;
        }

        return "views/blowfish/ResultText";
    }
}
