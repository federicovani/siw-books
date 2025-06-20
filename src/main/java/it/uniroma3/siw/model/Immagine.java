package it.uniroma3.siw.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;

@Entity
public class Immagine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nomeFile;

    private String tipoMime; // es. "image/png"

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] dati; // Qui vengono salvati i byte dell'immagine

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeFile() {
        return nomeFile;
    }

    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public byte[] getDati() {
        return dati;
    }

    public void setDati(byte[] dati) {
        this.dati = dati;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Immagine immagine = (Immagine) o;
        return Objects.equals(id, immagine.id) && Objects.equals(nomeFile, immagine.nomeFile) && Objects.equals(tipoMime, immagine.tipoMime) && Objects.deepEquals(dati, immagine.dati);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomeFile, tipoMime, Arrays.hashCode(dati));
    }
}
