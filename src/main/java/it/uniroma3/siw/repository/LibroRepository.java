package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Libro;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LibroRepository extends CrudRepository<Libro, Long> {
    public List<Libro> findByTitoloContainingIgnoreCase(String titolo);
}
