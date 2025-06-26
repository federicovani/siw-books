package it.uniroma3.siw.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
    @OneToMany
    private List<Immagine> immagini;
    @ManyToMany(mappedBy = "autori")
    private List<Libro> libri;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public LocalDate getDataMorte() {
        return dataMorte;
    }

    public void setDataMorte(LocalDate dataMorte) {
        this.dataMorte = dataMorte;
    }

    public String getNazionalita() {
        return nazionalita;
    }

    public void setNazionalita(String nazionalita) {
        this.nazionalita = nazionalita;
    }

    public List<Immagine> getImmagini() {
        return immagini;
    }

    public void setImmagini(List<Immagine> immagini) {
        this.immagini = immagini;
    }

    public List<Libro> getLibri() {
        return libri;
    }

    public void setLibri(List<Libro> libri) {
        this.libri = libri;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Autore autore = (Autore) o;
        return Objects.equals(id, autore.id) && Objects.equals(nome, autore.nome) && Objects.equals(cognome, autore.cognome) && Objects.equals(dataNascita, autore.dataNascita) && Objects.equals(dataMorte, autore.dataMorte) && Objects.equals(nazionalita, autore.nazionalita) && Objects.equals(immagini, autore.immagini) && Objects.equals(libri, autore.libri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, cognome, dataNascita, dataMorte, nazionalita, immagini, libri);
    }
}
