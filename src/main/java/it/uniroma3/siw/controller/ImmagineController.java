package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Immagine;
import it.uniroma3.siw.service.ImmagineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/immagini")
public class ImmagineController {

    private final ImmagineService immagineService;

    public ImmagineController(ImmagineService immagineService) {
        this.immagineService = immagineService;
    }

    @PostMapping("/upload")
    public String caricaImmagine(@RequestParam("file") MultipartFile file) throws Exception {
        immagineService.saveImmagine(file);
        return "Upload completato";
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> scaricaImmagine(@PathVariable Long id) {
        Immagine img = immagineService.getImmagineById(id);
        return ResponseEntity.ok()
                .header("Content-Type", img.getTipoMime())
                .body(img.getDati());
    }
}
