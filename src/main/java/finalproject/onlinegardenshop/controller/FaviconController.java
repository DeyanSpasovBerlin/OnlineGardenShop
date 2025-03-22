package finalproject.onlinegardenshop.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FaviconController {
    @GetMapping("favicon.ico")
    public ResponseEntity<Resource> favicon() throws IOException {
        Resource resource = new ClassPathResource("static/favicon.ico");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/x-icon"))
                .body(resource);
    }

}
//GET  http://localhost:8080/favicon.ico in Postman