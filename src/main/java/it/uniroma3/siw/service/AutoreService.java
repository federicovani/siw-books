package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.repository.AutoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutoreService {
    @Autowired
    private AutoreRepository autoreRepository;

    public Object getAutoreById(Long id) { return autoreRepository.findById(id).get(); }

    public Iterable<Autore> getAllAutori() { return autoreRepository.findAll(); }
}
