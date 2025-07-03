package it.uniroma3.siw.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImmagineService {

    @Value("${images.path}")
    private String uploadDir;

    // Metodo chiamato automaticamente all'avvio
    @PostConstruct
    public void init() {
        try {
            // Controlla e crea la directory di upload
            Path uploadPath = Path.of(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Impossibile creare la directory per l'upload: " + uploadDir, e);
        }
    }

    // Salva un'immagine e restituisce il nome del file univoco
    public String saveImage(MultipartFile file) {
        try {
            // Genera un nome file unico
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Path.of(uploadDir, fileName);

            // Salva il file nella directory configurata
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName; // Ritorna il nome del file salvato
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il salvataggio del file", e);
        }
    }

    // Elimina un'immagine
    public void deleteImage(String filename) {
        try {
            Path filePath = Path.of(uploadDir, filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Errore durante l'eliminazione del file", e);
        }
    }

}
