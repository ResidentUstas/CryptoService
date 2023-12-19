package CryptoService.Controllers;

import CryptoService.Crypto_Services.DES.Des_Service;
import CryptoService.Crypto_Services.DES.Des_cipher;
import CryptoService.Crypto_Services.Kuznechik.GrasshopperCipher;
import CryptoService.Models.CipherModel;
import CryptoService.Models.OperModel;
import CryptoService.Services.ConvertService;
import CryptoService.Services.IOService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/lavina")
public class AvalancheController {
    private String Key0 = "AABB09182736CCDD";
    private int[] Secret = new int[] { 61, 4, 24, 42, 91, 184, 217, 121, 151, 61, 107, 163, 122, 193, 132, 53, 205, 130, 156, 49, 6, 218, 169, 100, 121, 62, 207, 117, 71, 166, 122, 21};

    @GetMapping()
    public String Index(Model model) {
        List<OperModel> oper = new ArrayList<>();
        oper.add(new OperModel(1, "Des"));
        oper.add(new OperModel(2, "BlowFish"));
        oper.add(new OperModel(3, "IDEA"));
        oper.add(new OperModel(4, "Grasshopper"));
        oper.add(new OperModel(5, "Rc-5"));
        model.addAttribute("SystemCh", oper);
        CipherModel cipher = new CipherModel();
        model.addAttribute("cipher", cipher);

        return "views/lavina/index";
    }

    @PostMapping()
    public String handleFileUpload(Model model, @ModelAttribute("cipher") CipherModel cipherText, @RequestParam("file") MultipartFile[] file, @RequestParam(value = "Rounds", required = false) int rounds) throws IOException, DecoderException {
        if (file.length == 2) {
            int[] Key = Get_Key();
            String s1 = IOService.ReadFileStraight(file[0].getInputStream());
            String s2 = IOService.ReadFileStraight(file[1].getInputStream());
//            s1 = IOService.ReadBytesFromString(s1);
//            s2 = IOService.ReadBytesFromString(s2);

            String lavina = "";
            for(int i = 1; i < 17;i++){
                Des_Service des_service = new Des_Service(i,Key);
                des_service.KeyExtension();
                String st1 = cipher_for_round(s1, des_service);
                String st2 = cipher_for_round(s2, des_service);
                byte[] HemmingBytes0 = Hex.decodeHex(st1);
                byte[] HemmingBytes1 = Hex.decodeHex(st2);
                int h_distance = ConvertService.Get_Hemming_Distance(HemmingBytes0, HemmingBytes1);
                lavina += "Расстояние Хемминга для " + i + " раундов равняется: " + h_distance + "\r\nвсего бит: " + HemmingBytes0.length * 8 + "\r\n";
            }

            model.addAttribute("cipher", lavina);
            return "views/hemming/index";
        }

        model.addAttribute("cipher", "Ошибка! Файлы не выбраны или выбрано более двух");
        return "views/hemming/index";
    }

    private String cipher_for_round(String OpenTextHex, Des_Service des_service){
        String result = "";
        while (OpenTextHex.length() > 0) {
            String openBlock = OpenTextHex.substring(0, 16);
            OpenTextHex = OpenTextHex.substring(16, OpenTextHex.length());

            //Дополним последний неполный блок
            if (OpenTextHex.length() < 16 && OpenTextHex.length() > 0) {
                while (OpenTextHex.length() < 16) {
                    OpenTextHex += "0";
                }
            }

            //Зашифруем полученный блок
            String cipher_result = des_service.Get_Cipher_Text(openBlock);
            result += cipher_result;
        }

        return result;
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

