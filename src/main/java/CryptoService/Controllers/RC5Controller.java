package CryptoService.Controllers;

import CryptoService.Crypto_Services.Kuznechik.GrasshopperCipher;
import CryptoService.Crypto_Services.RC5.RC5cipher;
import CryptoService.Models.CipherModel;
import CryptoService.Models.OperModel;
import CryptoService.Models.paramModel;
import CryptoService.Services.ConvertService;
import CryptoService.Services.IOService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
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
    private static String Key0 = "915F4619BE41B2516355A50110A9CE91";
    String key1 = "3d04182a5bb8d979973d6ba37ac18435cd829c3106daa964793ecf7547a67a15";
    private int[] Secret = new int[] { 61, 4, 24, 42, 91, 184, 217, 121, 151, 61, 107, 163, 122, 193, 132, 53, 205, 130, 156, 49, 6, 218, 169, 100, 121, 62, 207, 117, 71, 166, 122, 21};
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
        int[] Key = Get_Key();
        RC5cipher rc5Cipher = new RC5cipher(cipherText.getRounds(), cipherText.getWord(), cipherText.getKeyBits(), cipherText.getWord(), Key);
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
                String cipherTxt = rc5Cipher.Get_Cipher_Text(cipherText.getCipher());
                byte[] HemmingBytes1 = cipherTxt.getBytes();
                int h_distance = ConvertService.Get_Hemming_Distance(HemmingBytes0, HemmingBytes1);
                model.addAttribute("cipher", "Расстояние Хемминга для данного текста равняется: " + h_distance + "\r\nвсего бит: " + HemmingBytes0.length * 8);
                model.addAttribute("path", "D:\\Diplom\\CryptoService\\Block_Ciphers\\hemming\\RC-5\\rc5_hemming_result.txt");
                break;
        }

        return "views/rc5/ResultText";
    }

    private int[] Get_Key() throws DecoderException, IOException {
        String key = IOService.ReadFile("D:\\Diplom\\CryptoService\\Block_Ciphers\\secrets\\RC-5\\rc5_password.txt");
        GrasshopperCipher grasshopperCipher = new GrasshopperCipher(9, Secret);
        if(key == "" || key =="0"){
            key = Key0;
        }
        else{
            key = grasshopperCipher.Get_Open_Text(key);
        }

        byte[] Key = key.getBytes();
        int[] result = new int[Key.length];

        for (int i = 0;i<Key.length;i++){
            result[i] = Key[i];
        }

        return result;
    }
}