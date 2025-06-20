package it.uniroma3.siw.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String titolo;
    private int annoPubblicazione;
    @ManyToMany(mappedBy = "libri")
    private List<Autore> autori;
    @OneToMany
    private List<Immagine> immagini;
    @OneToMany
    private List<Recensione> recensioni;
}
