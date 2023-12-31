package CryptoService.Controllers;

import CryptoService.Crypto_Services.DES.Des_cipher;
import CryptoService.Crypto_Services.Kuznechik.GrasshopperCipher;
import CryptoService.Models.CipherModel;
import CryptoService.Models.OperModel;
import CryptoService.Models.paramModel;
import CryptoService.Services.ConvertService;
import CryptoService.Services.IOService;
import jakarta.servlet.ServletContext;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/des")
public class DesController {
    private String Key0 = "AABB09182736CCDD";
    private int[] Secret = new int[] { 61, 4, 24, 42, 91, 184, 217, 121, 151, 61, 107, 163, 122, 193, 132, 53, 205, 130, 156, 49, 6, 218, 169, 100, 121, 62, 207, 117, 71, 166, 122, 21};


    @Autowired
    private ServletContext servletContext;

    @GetMapping()
    public String Index(Model model) {
        CipherModel cipher = new CipherModel();
        List<OperModel> oper = new ArrayList<>();
        oper.add(new OperModel(1, "Зашифровать"));
        oper.add(new OperModel(2, "Расшифровать"));
        oper.add(new OperModel(3, "Расстояние Хемминга"));
        List<OperModel> sysCh = new ArrayList<>();
        sysCh.add(new OperModel(1, "Dec"));
        sysCh.add(new OperModel(2, "Hex"));
        model.addAttribute("SystemCh", sysCh);
        List<paramModel> params = new ArrayList<>();
        for (int i = 1; i < 17; i++) {
            params.add(new paramModel(i, i));
        }
        model.addAttribute("Params", params);
        model.addAttribute("cipher", cipher);
        model.addAttribute("Operation", oper);
        return "views/des/index";
    }

    @PostMapping()
    public String Encrypt(Model model, @ModelAttribute("cipher") CipherModel cipherText) throws DecoderException, IOException {
        int[] Key = Get_Key();
        Des_cipher desCipher = new Des_cipher(cipherText.getRounds(), Key);
        switch (cipherText.getMode()) {
            case 1:
                model.addAttribute("cipher", desCipher.Get_Cipher_Text(cipherText.getCipher(), cipherText.getSystemCh()));
                model.addAttribute("path", "D:\\Diplom\\CryptoService\\Block_Ciphers\\cipher\\DES\\des_cipher_result.txt");
                break;
            case 2:
                model.addAttribute("cipher", desCipher.Get_Open_Text(cipherText.getCipher()));
                model.addAttribute("path", "D:\\Diplom\\CryptoService\\Block_Ciphers\\decipher\\DES\\des_decipher_result.txt");
                break;
            case 3:
                String OpenTextHex = IOService.ReadBytesFromString(cipherText.getCipher());
                byte[] HemmingBytes0 = Hex.decodeHex(OpenTextHex);
                String cipherTxt = desCipher.Get_Cipher_Text(cipherText.getCipher(), cipherText.getSystemCh());
                byte[] HemmingBytes1 = Hex.decodeHex(cipherTxt);
                int h_distance = ConvertService.Get_Hemming_Distance(HemmingBytes0, HemmingBytes1);
                model.addAttribute("cipher", "Расстояние Хемминга для данного текста равняется: " + h_distance + "\r\nвсего бит: " + HemmingBytes1.length * 8);
                model.addAttribute("path", "D:\\Diplom\\CryptoService\\Block_Ciphers\\hemming\\DES\\des_hemming_result.txt");
                break;
        }

        return "views/des/ResultText";
    }

    private int[] Get_Key() throws DecoderException, IOException {
        String key = IOService.ReadFile("D:\\Diplom\\CryptoService\\Block_Ciphers\\secrets\\DES\\des_password.txt");
        GrasshopperCipher grasshopperCipher = new GrasshopperCipher(9, Secret);
        if(key == "" || key =="0"){
            key = Key0;
        }
        else{
            key = grasshopperCipher.Get_Open_Text(key);
        }

        byte[] Key = Hex.decodeHex(key);
        int[] result = new int[Key.length];

        for (int i = 0;i<Key.length;i++){
            result[i] = Key[i];
        }

        return result;
    }
}
