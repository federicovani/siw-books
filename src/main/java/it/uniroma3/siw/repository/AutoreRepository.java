package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Autore;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AutoreRepository extends CrudRepository<Autore, Long> {
    List<Autore> findByNomeContainingIgnoreCaseOrCognomeContainingIgnoreCase(String nome, String cognome);

    boolean existsByNomeAndCognomeIgnoreCase(String nome, String cognome);
}
