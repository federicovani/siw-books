package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.stream.Collectors;

@Controller
public class LibroController {
    @Autowired
    private LibroService libroService;

    @GetMapping("/libro/{id}")
    public String getLibro(@PathVariable("id") Long id, Model model) {
        model.addAttribute("libro", libroService.getLibroById(id));
        return "libro.html";
    }

    @GetMapping("/libro")
    public String showLibri(Model model){
        Iterable<Libro> libri = libroService.getAllLibri();

        // Convertiamo gli autori in una stringa di nomi separati da virgole
        for (Libro libro : libri) {
            libro.setAutoriString(
                    libro.getAutori().stream()
                            .map(autore -> autore.getNome() + " " + autore.getCognome())
                            .collect(Collectors.joining(", "))
            );
        }

        model.addAttribute("libri", libri);
        return "libri.html";

    }
}
