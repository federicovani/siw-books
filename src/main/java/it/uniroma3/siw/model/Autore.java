package it.uniroma3.siw.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Autore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String cognome;
    private java.time.LocalDate dataNascita;
    private java.time.LocalDate dataMorte;
    private String nazionalita;
    @OneToOne
    private Immagine foto;
    @ManyToMany(mappedBy = "autori")
    private List<Libro> libri;
}
