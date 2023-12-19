package CryptoService.Controllers;

import CryptoService.Crypto_Services.Kuznechik.GrasshopperCipher;
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
@RequestMapping("/grassHop")
public class GrassHopperController {
    private String Key0 = "3d04182a5bb8d979973d6ba37ac18435cd829c3106daa964793ecf7547a67a15";
    private int[] Secret = new int[] { 61, 4, 24, 42, 91, 184, 217, 121, 151, 61, 107, 163, 122, 193, 132, 53, 205, 130, 156, 49, 6, 218, 169, 100, 121, 62, 207, 117, 71, 166, 122, 21};

    @GetMapping()
    public String Index(Model model)  {
        CipherModel cipher = new CipherModel();
        List<OperModel> oper = new ArrayList<>();
        oper.add(new OperModel(1,"Зашифровать"));
        oper.add(new OperModel(2,"Расшифровать"));
        oper.add(new OperModel(3,"Расстояние Хемминга"));
        List<OperModel> sysCh = new ArrayList<>();
        sysCh.add(new OperModel(1, "Dec"));
        sysCh.add(new OperModel(2, "Hex"));
        model.addAttribute("SystemCh", sysCh);
        List<paramModel> params = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            params.add(new paramModel(i, i));
        }
        model.addAttribute("Params", params);
        model.addAttribute("cipher", cipher);
        model.addAttribute("Operation", oper);
        return "views/kuznechick/index";
    }

    @PostMapping()
    public String Encrypt(Model model, @ModelAttribute("cipher") CipherModel cipherText) throws DecoderException, IOException {
        int[] Key = Get_Key();
        GrasshopperCipher grasshopperCipher = new GrasshopperCipher(cipherText.getRounds(), Key);

        switch (cipherText.getMode()){
            case 1:
                model.addAttribute("cipher", grasshopperCipher.Get_Cipher_Text(cipherText.getCipher(), cipherText.getSystemCh()));
                model.addAttribute("path", "D:\\Diplom\\CryptoService\\Block_Ciphers\\cipher\\Grasshopper\\grasshopper_cipher_result.txt");
                break;
            case 2:
                model.addAttribute("cipher", grasshopperCipher.Get_Open_Text(cipherText.getCipher()));
                model.addAttribute("path", "D:\\Diplom\\CryptoService\\Block_Ciphers\\decipher\\Grasshopper\\grasshopper_decipher_result.txt");
                break;
            case 3:
                String OpenTextHex = IOService.ReadBytesFromString(cipherText.getCipher());
                byte[] HemmingBytes0 = Hex.decodeHex(OpenTextHex);
                String cipherTxt = grasshopperCipher.Get_Cipher_Text(cipherText.getCipher(), cipherText.getSystemCh());
                byte[] HemmingBytes1 = Hex.decodeHex(cipherTxt);
                int h_distance = ConvertService.Get_Hemming_Distance(HemmingBytes0, HemmingBytes1);
                model.addAttribute("cipher", "Расстояние Хемминга для данного текста равняется: " + h_distance + "\r\nвсего бит: " + HemmingBytes0.length * 8);
                model.addAttribute("path", "D:\\Diplom\\CryptoService\\Block_Ciphers\\hemming\\Grasshopper\\grasshopper_hemming_result.txt");
                break;
        }

        return "views/kuznechick/ResultText";
    }

    private int[] Get_Key() throws DecoderException, IOException {
        String key = IOService.ReadFile("D:\\Diplom\\CryptoService\\Block_Ciphers\\secrets\\Grasshopper\\grasshopper_password.txt");
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
