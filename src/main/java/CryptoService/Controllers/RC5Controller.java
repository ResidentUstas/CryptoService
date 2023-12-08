package CryptoService.Controllers;

import CryptoService.Crypto_Services.RC5.RC5cipher;
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
@RequestMapping("/rc5")
public class RC5Controller {
    @GetMapping()
    public String Index(Model model) {
        CipherModel cipher = new CipherModel();
        List<OperModel> oper = new ArrayList<>();
        List<paramModel> params = new ArrayList<>();
        List<paramModel> dopParams = new ArrayList<>();
        params.add(new paramModel(16, 16));
        params.add(new paramModel(32, 32));
        params.add(new paramModel(64, 64));
        oper.add(new OperModel(1, "Зашифровать"));
        oper.add(new OperModel(2, "Расшифровать"));
        oper.add(new OperModel(3,"Расстояние Хемминга"));
        model.addAttribute("cipher", cipher);
        model.addAttribute("Operation", oper);
        model.addAttribute("Params", params);

        for (int i = 0; i < 256; i++) {
            dopParams.add(new paramModel(i, i));
        }

        model.addAttribute("Parameters", dopParams);
        return "views/rc5/index";
    }

    @PostMapping()
    public String Encrypt(Model model, @ModelAttribute("cipher") CipherModel cipherText) throws DecoderException, IOException {
        RC5cipher rc5Cipher = new RC5cipher(cipherText.getRounds(), cipherText.getWord(), cipherText.getKeyBits(), cipherText.getWord());
        switch (cipherText.getMode()) {
            case 1:
                model.addAttribute("cipher", rc5Cipher.Get_Cipher_Text(cipherText.getCipher()));
                model.addAttribute("path", "D:\\Diplom\\CryptoService\\Block_Ciphers\\cipher\\RC-5\\rc5_cipher_result.txt");
                break;
            case 2:
                model.addAttribute("cipher", rc5Cipher.Get_Open_Text(cipherText.getCipher()));
                model.addAttribute("path", "D:\\Diplom\\CryptoService\\Block_Ciphers\\decipher\\RC-5\\rc5_decipher_result.txt");
                break;
            case 3:
                byte[] HemmingBytes0 = cipherText.getCipher().getBytes();
                String Hemming0 = ConvertService.Get_Bit_View_Bytes(HemmingBytes0);
                String cipherTxt = rc5Cipher.Get_Cipher_Text(cipherText.getCipher());
                byte[] HemmingBytes1 = Hex.decodeHex(cipherTxt);
                String Hemming1 = ConvertService.Get_Bit_View_Bytes(HemmingBytes1);
                int h_distance = IOService.FindHammingDistance(Hemming0, Hemming1);
                model.addAttribute("cipher", "Расстояние Хемминга для данного текста равняется: " + h_distance + "\r\nвсего бит: " + Hemming0.length());
                model.addAttribute("path", "D:\\Diplom\\CryptoService\\Block_Ciphers\\hemming\\RC-5\\rc5_hemming_result.txt");
                break;
        }

        return "views/idea/ResultText";
    }
}