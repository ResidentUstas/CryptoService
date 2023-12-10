package CryptoService.Controllers;

import CryptoService.Models.CipherModel;
import CryptoService.Models.OperModel;
import CryptoService.Models.paramModel;
import CryptoService.Services.ConvertService;
import CryptoService.Services.IOService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/crypto")
public class CryptoController {
    @GetMapping()
    public String Index(Model model) {
        return "views/Crypto/index";
    }

    @PostMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("path") String path) throws IOException {
        File file = new File(path);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(new MediaType("text", "plain"))
                .contentLength(file.length())
                .body(resource);
    }

    @PostMapping("/upload")
    public String handleFileUpload(Model model, @RequestParam("file") MultipartFile[] file, @RequestParam("Alg") String alg, @RequestParam("Rounds") int rounds) throws IOException {
        if (file.length == 1) {
            try {
                byte[] bytes = file[0].getBytes();
                String text = new String(bytes, StandardCharsets.UTF_8);
                return returnUpload(alg, model, text, rounds);
            } catch (Exception e) {
                return returnUpload(alg, model, "Ошибка загрузки!!!", 0);
            }
        } else {
            if (file.length == 2){
                byte[] bytes0 = file[0].getBytes();
                String Hemming0 = ConvertService.Get_Bit_View_Bytes(bytes0);
                byte[] bytes1 = file[1].getBytes();
                String Hemming1 = ConvertService.Get_Bit_View_Bytes(bytes1);
                int h_distance = IOService.FindHammingDistance(Hemming0, Hemming1);
                model.addAttribute("cipher", "Расстояние Хемминга для данного текста равняется: " + h_distance + "\r\nвсего бит: " + Hemming0.length());
                return "views/hemming/index";
            }

            model.addAttribute("cipher", "Ошибка! Файлы не выбраны или выбрано более двух");
            return "views/hemming/index";
        }
    }

    @GetMapping("/hemming")
    public String HemmingDistance(Model model){
        model.addAttribute("cipher", "");
        return "views/hemming/index";
    }

    private String returnUpload(String alg, Model model, String text, int rounds) {
        CipherModel cipher = new CipherModel();
        List<OperModel> oper = new ArrayList<>();
        oper.add(new OperModel(1, "Зашифровать"));
        oper.add(new OperModel(2, "Расшифровать"));
        oper.add(new OperModel(3,"Расстояние Хемминга"));
        List<paramModel> params = new ArrayList<>();
        for (int i = 1; i < rounds; i++) {
            params.add(new paramModel(i, i));
        }
        model.addAttribute("Params", params);
        cipher.setCipher(text);
        model.addAttribute("cipher", cipher);
        model.addAttribute("Operation", oper);
        switch (alg) {
            case "des":
                return "views/des/index";
            case "blowfish":
                return "views/blowfish/index";
            case "idea":
                return "views/idea/index";
            case "grasshopper":
                return "views/kuznechick/index";
            case "rc5":
                params = new ArrayList<>();
                List<paramModel> dopParams = new ArrayList<>();
                params.add(new paramModel(16, 16));
                params.add(new paramModel(32, 32));
                params.add(new paramModel(64, 64));
                model.addAttribute("Params", params);

                for (int i = 0; i < 256; i++) {
                    dopParams.add(new paramModel(i, i));
                }

                model.addAttribute("Parameters", dopParams);
                return "views/rc5/index";
        }
        return "views/Crypto/index";
    }
}
