package it.uniroma3.siw.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String titolo;
    private int annoPubblicazione;
    @ManyToMany
    private List<Autore> autori;
    private List<String> immagini;
    @OneToMany(mappedBy = "libro", cascade = CascadeType.REMOVE)
    private List<Recensione> recensioni;
    @Transient
    private String autoriString;
    @Transient
    private String immagineRandom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public int getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    public void setAnnoPubblicazione(int annoPubblicazione) {
        this.annoPubblicazione = annoPubblicazione;
    }

    public List<Autore> getAutori() {
        return autori;
    }

    public void setAutori(List<Autore> autori) {
        this.autori = autori;
    }

    public List<String> getImmagini() {
        return immagini;
    }

    public void setImmagini(List<String> immagini) {
        this.immagini = immagini;
    }

    public List<Recensione> getRecensioni() {
        return recensioni;
    }

    public void setRecensioni(List<Recensione> recensioni) {
        this.recensioni = recensioni;
    }

    @Transient
    public Double getVotoMedio() {
        if (this.recensioni == null || this.recensioni.isEmpty())
            return null; // Nessuna recensione
        return this.recensioni.stream()
                .mapToInt(Recensione::getVoto)
                .average()
                .orElse(0.0); // Ritorna 0.0 se nessuna recensione
    }


    public String getAutoriString() {
        return autoriString;
    }

    public void setAutoriString(String autoriString) {
        this.autoriString = autoriString;
    }

    public String getImmagineRandom() {
        return immagineRandom;
    }

    public void setImmagineRandom(String immagineRandom) {
        this.immagineRandom = immagineRandom;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return annoPubblicazione == libro.annoPubblicazione && Objects.equals(id, libro.id) && Objects.equals(titolo, libro.titolo) && Objects.equals(autori, libro.autori) && Objects.equals(immagini, libro.immagini) && Objects.equals(recensioni, libro.recensioni) && Objects.equals(autoriString, libro.autoriString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titolo, annoPubblicazione, autori, immagini, recensioni, autoriString);
    }
}
