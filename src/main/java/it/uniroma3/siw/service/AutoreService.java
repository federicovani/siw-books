package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.AutoreRepository;
import it.uniroma3.siw.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutoreService {
    @Autowired
    private AutoreRepository autoreRepository;
    @Autowired
    private LibroRepository libroRepository;

    public void saveAutore(Autore autore) { autoreRepository.save(autore); }

    public Object getAutoreById(Long id) { return autoreRepository.findById(id).get(); }

    public Iterable<Autore> getAllAutori() { return autoreRepository.findAll(); }

    public List<Autore> searchAutoriByNomeOrCognome(String query) { return autoreRepository.findByNomeContainingIgnoreCaseOrCognomeContainingIgnoreCase(query, query); }

    public void deleteById(Long id) {
        // Trova l'autore specifico
        Autore autore = autoreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autore non trovato"));

        // Rimuovi le associazioni con i libri
        for (Libro libro : autore.getLibri()) {
            libro.getAutori().remove(autore);  // Rimuovi l'autore dall'elenco dei libri
            libroRepository.save(libro);      // Salva il libro aggiornato
        }

        // Elimina l'autore
        autoreRepository.delete(autore);
    }

    public boolean existsByNomeAndCognome(String nome, String cognome) {
        return autoreRepository.existsByNomeAndCognomeIgnoreCase(nome, cognome);
    }
}
