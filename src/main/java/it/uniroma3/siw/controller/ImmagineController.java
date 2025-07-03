package it.uniroma3.siw.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImmagineController {

    private final Path rootLocation = Paths.get("src/main/uploads/images");

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            if (!Files.exists(file) || !Files.isReadable(file)) {
                // Se il file non esiste, usa default.png dalla cartella static
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"default.png\"")
                        .body(new UrlResource("classpath:/static/default.png"));
            }

            // Altrimenti carica il file richiesto
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Impossibile leggere il file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Errore nel file richiesto!", e);
        }
    }
}
