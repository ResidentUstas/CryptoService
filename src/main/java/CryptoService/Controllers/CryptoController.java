package CryptoService.Controllers;

import CryptoService.Models.CipherModel;
import CryptoService.Models.OperModel;
import CryptoService.Models.paramModel;
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
    public String handleFileUpload(Model model, @RequestParam("file") MultipartFile file, @RequestParam("Alg") String alg) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String text = new String(bytes, StandardCharsets.UTF_8);
                CipherModel cipher = new CipherModel();
                List<OperModel> oper = new ArrayList<>();
                oper.add(new OperModel(1, "Зашифровать"));
                oper.add(new OperModel(2, "Расшифровать"));
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
                        List<paramModel> params = new ArrayList<>();
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
            } catch (Exception e) {
                return "Вам не удалось загрузить файл";
            }
        } else {
            return "Вам не удалось загрузить файл";
        }
    }
}
