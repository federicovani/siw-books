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
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class LibroService {
    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutoreRepository autoreRepository;
    @Autowired
    private ImmagineService immagineService;

    public Libro getLibroById(Long id) { return libroRepository.findById(id).get(); }

    public Iterable<Libro> getAllLibri() {
        List<Libro> libri = (List<Libro>) libroRepository.findAll();
        setUpLibri(libri);
        return libri;
    }

    public void setUpLibri(List<Libro> libri) {
        for (Libro libro : libri) {
            // Convertiamo gli autori in una stringa di nomi separati da virgole
            libro.setAutoriString(
                    libro.getAutori().stream()
                            .map(autore -> autore.getNome() + " " + autore.getCognome())
                            .collect(Collectors.joining(", "))
            );

            // Seleziona un'immagine casuale dalle immagini del libro
            if (libro.getImmagini() != null && !libro.getImmagini().isEmpty()) {
                List<String> immagini = libro.getImmagini();
                String immagineRandom = immagini.get(new Random().nextInt(immagini.size()));
                libro.setImmagineRandom(immagineRandom); // Trasferiamola usando un campo transitorio o nel modello
            }
        }
    }

    public List<Libro> searchLibriByTitolo(String query) {
        List<Libro> libriByTitolo = libroRepository.findByTitoloContainingIgnoreCase(query);
        setUpLibri(libriByTitolo);
        return libriByTitolo;
    }

    @Transactional
    public void deleteById(Long id) {
        // Trova il libro
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro non trovato"));

        // Rimuovi le associazioni con gli autori
        for (Autore autore : libro.getAutori()) {
            autore.getLibri().remove(libro);  // Rimuovi il libro dall'elenco degli autori
            autoreRepository.save(autore);      // Salva l'autore aggiornato
        }

        // Elimina le immagini associate, se presenti
        List<String> immaginiLibro = libro.getImmagini();
        for(String immagine : immaginiLibro) {
            immagineService.deleteImage(immagine);
        }

        // Elimina il libro
        libroRepository.delete(libro);
    }

    @Transactional
    public void saveImmagineLibro(Libro libro, MultipartFile file) {

        // Salva l'immagine sul file system
        String fileName = immagineService.saveImage(file);

        // Collega l'immagine al libro
        libro.getImmagini().add(fileName);
    }

    @Transactional
    public boolean existsByTitolo(String titolo) {
        return libroRepository.existsByTitoloIgnoreCase(titolo);
    }

    @Transactional
    public void saveLibro(Libro libro) {
        libroRepository.save(libro);
    }


    @Transactional
    public List<Libro> findLibriNotByAutore(Long autoreId) {
        List<Libro> libriNotByAutore = (List<Libro>) libroRepository.findAll();
        List<Libro> libriByAutore = autoreRepository.findById(autoreId).get().getLibri();
        libriNotByAutore.removeAll(libriByAutore);
        return libriNotByAutore;
    }
}
