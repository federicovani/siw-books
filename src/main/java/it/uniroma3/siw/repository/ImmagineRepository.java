package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Immagine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImmagineRepository extends JpaRepository<Immagine, Long> {
}
