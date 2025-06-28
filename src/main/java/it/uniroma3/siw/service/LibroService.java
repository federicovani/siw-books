package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.AutoreRepository;
import it.uniroma3.siw.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {
    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutoreRepository autoreRepository;

    public Libro getLibroById(Long id) { return libroRepository.findById(id).get(); }

    public Iterable<Libro> getAllLibri() { return libroRepository.findAll(); }

    public List<Libro> searchLibriByTitolo(String query) { return libroRepository.findByTitoloContainingIgnoreCase(query); }

    public void deleteById(Long id) {
        // Trova il libro
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro non trovato"));

        // Rimuovi le associazioni con gli autori
        for (Autore autore : libro.getAutori()) {
            autore.getLibri().remove(libro);  // Rimuovi il libro dall'elenco degli autori
            autoreRepository.save(autore);      // Salva l'autore aggiornato
        }

        // Elimina l'autore
        libroRepository.delete(libro);
    }

    public boolean existsByTitolo(String titolo) {
        return libroRepository.existsByTitoloIgnoreCase(titolo);
    }

    public void saveLibro(Libro libro) {
        libroRepository.save(libro);
    }
}
