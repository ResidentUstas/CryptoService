package CryptoService.Controllers;

import CryptoService.Crypto_Services.DES.Des_cipher;
import CryptoService.Models.CipherModel;
import CryptoService.Models.OperModel;
import jakarta.servlet.ServletContext;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.io.filefilter.DirectoryFileFilter.DIRECTORY;

@Controller
@RequestMapping("/des")
public class DesController {
    @Autowired
    private ServletContext servletContext;
    @GetMapping()
    public String Index(Model model)  {
        CipherModel cipher = new CipherModel();
        List<OperModel> oper = new ArrayList<>();
        oper.add(new OperModel(1,"Зашифровать"));
        oper.add(new OperModel(2,"Расшифровать"));
        model.addAttribute("cipher", cipher);
        model.addAttribute("Operation", oper);
        return "views/des/index";
    }

    @PostMapping()
    public String Encrypt(Model model, @ModelAttribute("cipher") CipherModel cipherText) throws DecoderException, IOException {
        Des_cipher desCipher = new Des_cipher();

        switch (cipherText.getMode()){
            case 1:
                model.addAttribute("cipher", desCipher.Get_Cipher_Text(cipherText.getCipher()));
                model.addAttribute("path", "D:\\Block_Algorithms\\Block_Ciphers\\cipher\\DES\\des_cipher_result.txt");
                break;
            case 2:
                model.addAttribute("cipher", desCipher.Get_Open_Text(cipherText.getCipher()));
                model.addAttribute("path", "D:\\Block_Algorithms\\Block_Ciphers\\decipher\\DES\\des_decipher_result.txt");
                break;
        }

        return "views/idea/ResultText";
    }

    @PostMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("path") String path) throws IOException {
        File file = new File(path);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(new MediaType("text","plain"))
                .contentLength(file.length())
                .body(resource);
    }
}
