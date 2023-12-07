package CryptoService.Controllers;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/crypto")
public class CryptoController {
    @GetMapping()
    public String Index(Model model)  {
        return "views/Crypto/index";
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
