package CryptoService.Controllers;

import CryptoService.Crypto_Services.DES.Des_cipher;
import CryptoService.Models.CipherModel;
import CryptoService.Models.OperModel;
import CryptoService.Services.ConvertService;
import CryptoService.Services.IOService;
import jakarta.servlet.ServletContext;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/des")
public class DesController {
    @Autowired
    private ServletContext servletContext;

    @GetMapping()
    public String Index(Model model) {
        CipherModel cipher = new CipherModel();
        List<OperModel> oper = new ArrayList<>();
        oper.add(new OperModel(1, "Зашифровать"));
        oper.add(new OperModel(2, "Расшифровать"));
        oper.add(new OperModel(3, "Расстояние Хемминга"));
        model.addAttribute("cipher", cipher);
        model.addAttribute("Operation", oper);
        return "views/des/index";
    }

    @PostMapping()
    public String Encrypt(Model model, @ModelAttribute("cipher") CipherModel cipherText) throws DecoderException, IOException {
        Des_cipher desCipher = new Des_cipher();

        switch (cipherText.getMode()) {
            case 1:
                model.addAttribute("cipher", desCipher.Get_Cipher_Text(cipherText.getCipher()));
                model.addAttribute("path", "D:\\Block_Algorithms\\CryptoService\\Block_Ciphers\\cipher\\DES\\des_cipher_result.txt");
                break;
            case 2:
                model.addAttribute("cipher", desCipher.Get_Open_Text(cipherText.getCipher()));
                model.addAttribute("path", "D:\\Block_Algorithms\\CryptoService\\Block_Ciphers\\decipher\\DES\\des_decipher_result.txt");
                break;
            case 3:
                byte[] HemmingBytes0 = cipherText.getCipher().getBytes();
                String Hemming0 = ConvertService.Get_Bit_View_Bytes(HemmingBytes0);
                String cipherTxt = desCipher.Get_Cipher_Text(cipherText.getCipher());
                byte[] HemmingBytes1 = Hex.decodeHex(cipherTxt);
                String Hemming1 = ConvertService.Get_Bit_View_Bytes(HemmingBytes1);
                int h_distance = IOService.FindHammingDistance(Hemming0, Hemming1);
                model.addAttribute("cipher", "Расстояние Хемминга для данного текста равняется: " + h_distance + "\r\nвсего бит: " + Hemming0.length());
                model.addAttribute("path", "D:\\Block_Algorithms\\CryptoService\\Block_Ciphers\\Hemming\\DES\\des_hemming_result.txt");
                break;
        }

        return "views/idea/ResultText";
    }
}
