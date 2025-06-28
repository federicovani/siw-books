package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/libro/search")
    public String searchLibri(@RequestParam("query") String query, Model model) {
        // Recupera tutti i libri il cui titolo contiene il termine di ricerca ignorando le maiuscole/minuscole
        List<Libro> libri = libroService.searchLibriByTitolo(query);

        // Convertiamo gli autori in una stringa di nomi separati da virgole
        for (Libro libro : libri) {
            libro.setAutoriString(
                    libro.getAutori().stream()
                            .map(autore -> autore.getNome() + " " + autore.getCognome())
                            .collect(Collectors.joining(", "))
            );
        }

        model.addAttribute("libri", libri); // Autori risultati dalla ricerca
        model.addAttribute("isSearch", true); // Flag per sapere se è una ricerca
        model.addAttribute("searchQuery", query); // Mostra il termine di ricerca usato

        return "libri.html";
    }

    @GetMapping("/libroAdmin/{id}")
    public String getLibroAdmin(@PathVariable("id") Long id, Model model) {
        model.addAttribute("libro", libroService.getLibroById(id));
        return "admin/libroAdmin.html";
    }

    @GetMapping("/libroAdmin")
    public String getLibriAdmin(Model model){
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
        return "admin/libriAdmin.html";
    }

    @PostMapping("/libroAdmin/delete/{id}")
    public String deleteLibro(@PathVariable Long id) {
        libroService.deleteById(id);
        return "redirect:/libroAdmin";
    }

    @GetMapping("/libroAdmin/search")
    public String searchLibriAdmin(@RequestParam("query") String query, Model model) {
        // Recupera tutti i libri il cui titolo contiene il termine di ricerca ignorando le maiuscole/minuscole
        List<Libro> libri = libroService.searchLibriByTitolo(query);

        // Convertiamo gli autori in una stringa di nomi separati da virgole
        for (Libro libro : libri) {
            libro.setAutoriString(
                    libro.getAutori().stream()
                            .map(autore -> autore.getNome() + " " + autore.getCognome())
                            .collect(Collectors.joining(", "))
            );
        }

        model.addAttribute("libri", libri); // Autori risultati dalla ricerca
        model.addAttribute("isSearch", true); // Flag per sapere se è una ricerca
        model.addAttribute("searchQuery", query); // Mostra il termine di ricerca usato

        return "admin/libriAdmin.html";
    }
    
    @GetMapping("/libroAdmin/formNewLibro")
    public String formNewLibro(Model model) {
        model.addAttribute("libro", new Libro());
        return "admin/formNewLibro.html";
    }

    @PostMapping("/libroAdmin/formNewLibro")
    public String createLibro(@ModelAttribute("libro") Libro libro, Model model) {
        if (!libroService.existsByTitolo(libro.getTitolo())) {
            libroService.saveLibro(libro);
            model.addAttribute("libro", libro);
            return "redirect:/libroAdmin/" + libro.getId();
        } else {
            model.addAttribute("messaggioErrore", "Questo libro esiste già");
            return "admin/formNewLibro.html";
        }
    }
}
