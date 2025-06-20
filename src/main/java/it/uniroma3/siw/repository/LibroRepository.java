package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Libro;
import org.springframework.data.repository.CrudRepository;

public interface LibroRepository extends CrudRepository<Libro, Long> {
}
