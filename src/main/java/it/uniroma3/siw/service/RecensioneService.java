package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.LibroRepository;
import it.uniroma3.siw.repository.RecensioneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecensioneService {
    @Autowired
    private RecensioneRepository recensioneRepository;
    @Autowired
    private LibroRepository libroRepository;

    @Transactional
    public Long deleteById(Long id) {
        // Recupera la recensione dal database
        Recensione recensione = recensioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recensione non trovata"));

        // Recupera il libro associato
        Libro libro = recensione.getLibro();
        Long libroId = libro.getId();
        if (libro != null) {
            // Rimuovi la recensione dalla lista delle recensioni del libro
            libro.getRecensioni().remove(recensione);

            // Salva il libro aggiornato
            libroRepository.save(libro);
        }

        // Elimina la recensione
        recensioneRepository.deleteById(id);

        //Ritorna l'id del libro
        return libroId;
    }

    @Transactional
    public void saveRecensione(Recensione recensione) {
        recensioneRepository.save(recensione);
    }

    public boolean existsByUserAndLibro(User user, Libro libro) {
        return recensioneRepository.existsByUserAndLibro(user, libro);
    }
}
