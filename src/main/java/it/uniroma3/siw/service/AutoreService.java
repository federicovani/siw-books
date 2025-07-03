package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.AutoreRepository;
import it.uniroma3.siw.repository.LibroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AutoreService {
    @Autowired
    private AutoreRepository autoreRepository;
    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private ImmagineService immagineService;

    public void saveAutore(Autore autore) { autoreRepository.save(autore); }

    public Autore getAutoreById(Long id) { return autoreRepository.findById(id).get(); }

    public Iterable<Autore> getAllAutori() { return autoreRepository.findAll(); }

    public List<Autore> searchAutoriByNomeOrCognome(String query) { return autoreRepository.findByNomeContainingIgnoreCaseOrCognomeContainingIgnoreCase(query, query); }

    @Transactional
    public void deleteById(Long id) {
        // Trova l'autore specifico
        Autore autore = autoreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autore non trovato"));

        // Rimuovi le associazioni con i libri
        for (Libro libro : autore.getLibri()) {
            libro.getAutori().remove(autore);  // Rimuovi l'autore dall'elenco dei libri
            libroRepository.save(libro);      // Salva il libro aggiornato
        }

        // Elimina l'immagine associata, se presente
        String immagineAutore = autore.getImmagine();
        if (immagineAutore != null) {
            immagineService.deleteImage(immagineAutore);
        }


        // Elimina l'autore
        autoreRepository.delete(autore);
    }

    @Transactional
    public void saveImmagineAutore(Autore autore, MultipartFile file) {

        //Verifica se è già presente un'immagine ed eliminala
        String immaginePrecedente = autore.getImmagine();
        if (immaginePrecedente != null) {
            immagineService.deleteImage(immaginePrecedente);
        }

        // Salva l'immagine sul file system
        String fileName = immagineService.saveImage(file);

        // Collega l'immagine al libro
        autore.setImmagine(fileName);
    }

    @Transactional
    public boolean existsByNomeAndCognome(String nome, String cognome) {
        return autoreRepository.existsByNomeAndCognomeIgnoreCase(nome, cognome);
    }

    public List<Autore> findAutoriNotInLibro(Long libroId) {
        List<Autore> autoriNotInLibro = (List<Autore>) autoreRepository.findAll();
        List<Autore> autoriInLibro = libroRepository.findById(libroId).get().getAutori();
        autoriNotInLibro.removeAll(autoriInLibro);
        return autoriNotInLibro;
    }
}
