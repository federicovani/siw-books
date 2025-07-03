package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.ImmagineService;
import it.uniroma3.siw.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LibroController {
    @Autowired
    private LibroService libroService;
    @Autowired
    private AutoreService autoreService;
    @Autowired
    private ImmagineService immagineService;

    @GetMapping("/libro/{id}")
    public String getLibro(@PathVariable("id") Long id, Model model) {
        model.addAttribute("libro", libroService.getLibroById(id));
        return "libro.html";
    }

    @GetMapping("/libro")
    public String showLibri(Model model){
        Iterable<Libro> libri = libroService.getAllLibri();

        model.addAttribute("libri", libri);
        return "libri.html";
    }

    @GetMapping("/libro/search")
    public String searchLibri(@RequestParam("query") String query, Model model) {
        // Recupera tutti i libri il cui titolo contiene il termine di ricerca ignorando le maiuscole/minuscole
        List<Libro> libri = libroService.searchLibriByTitolo(query);

        model.addAttribute("libri", libri); // Autori risultati dalla ricerca
        model.addAttribute("isSearch", true); // Flag per sapere se è una ricerca
        model.addAttribute("searchQuery", query); // Mostra il termine di ricerca usato

        return "libri.html";
    }

    @PostMapping("/admin/deleteLibro/{id}")
    public String deleteLibro(@PathVariable Long id) {
        libroService.deleteById(id);
        return "redirect:/libro";
    }
    
    @GetMapping("/admin/formNewLibro")
    public String formNewLibro(Model model) {
        model.addAttribute("libro", new Libro());
        return "admin/formNewLibro.html";
    }

    @PostMapping("/admin/formNewLibro")
    public String createLibro(@ModelAttribute("libro") Libro libro, @RequestParam("file") MultipartFile[] immagini, Model model) {
        if (!libroService.existsByTitolo(libro.getTitolo())) {
            // Lista per salvare i nomi dei file immagine
            List<String> nomiImmagini = new ArrayList<>();

            // Salva ogni immagine caricata
            for (MultipartFile file : immagini) {
                if (!file.isEmpty()) {
                    String nomeFile = immagineService.saveImage(file); // Salva l'immagine
                    nomiImmagini.add(nomeFile); // Aggiungi il nome del file alla lista
                }
            }

            // Imposta la lista dei nomi delle immagini sul libro
            libro.setImmagini(nomiImmagini);

            libroService.saveLibro(libro);
            model.addAttribute("libro", libro);
            return "redirect:/libro/" + libro.getId();
        } else {
            model.addAttribute("messaggioErrore", "Questo libro esiste già");
            return "admin/formNewLibro.html";
        }
    }

    @PostMapping("/admin/addImmagineLibro/{libroId}")
    public String addImmagine(@PathVariable("libroId") Long libroId, @RequestParam("file") MultipartFile[] immagini) {

        Libro libro = libroService.getLibroById(libroId);
        List<String> nomiImmagini;

        if(libro.getImmagini() != null)
            nomiImmagini = libro.getImmagini();
        else
            nomiImmagini = new ArrayList<>();

        // Salva ogni immagine caricata
        for (MultipartFile file : immagini) {
            if (!file.isEmpty()) {
                String nomeFile = immagineService.saveImage(file); // Salva l'immagine
                nomiImmagini.add(nomeFile); // Aggiungi il nome del file alla lista
            }
        }

        libro.setImmagini(nomiImmagini);

        libroService.saveLibro(libro);

        return "redirect:/libro/" + libroId;
    }

    @PostMapping("/admin/rimuoviImmagineLibro/{libroId}")
    public String removeImage(@PathVariable("libroId") Long libroId,
                              @RequestParam("nomeImmagine") String imageName) {

        Libro libro = libroService.getLibroById(libroId);

        if (libro.getImmagini().contains(imageName)) {

            libro.getImmagini().remove(imageName);

            immagineService.deleteImage(imageName);

            libroService.saveLibro(libro);
        }

        // Redirigi alla pagina del libro
        return "redirect:/libro/" + libroId;
    }


    @GetMapping("/admin/autoriToAdd/{libroId}")
    public String autoriToAdd(@PathVariable("libroId") Long libroId, Model model) {
        Libro libro = libroService.getLibroById(libroId);

        List<Autore> autoriToAdd = autoreService.findAutoriNotInLibro(libroId);

        model.addAttribute("libro", libro);
        model.addAttribute("autoriToAdd", autoriToAdd);

        return "admin/autoriToAdd.html";
    }

    @GetMapping(value="/admin/addAutoreToLibro/{autoreId}/{libroId}")
    public String addAutoreToLibro(@PathVariable("autoreId") Long autoreId, @PathVariable("libroId") Long libroId, Model model) {

        Autore autore = autoreService.getAutoreById(autoreId);
        Libro libro = libroService.getLibroById(libroId);

        List<Autore> autori = libro.getAutori();
        autori.add(autore);
        libroService.saveLibro(libro);

        List<Autore> autoriToAdd = autoreService.findAutoriNotInLibro(libroId);

        model.addAttribute("autore", autore);
        model.addAttribute("libro", libro);
        model.addAttribute("autoriToAdd", autoriToAdd);

        return "admin/autoriToAdd.html";
    }

    @PostMapping("/admin/removeAutoreFromLibro/{autoreId}/{libroId}")
    public String deleteAutoreFromLibro(@PathVariable("autoreId") Long autoreId, @PathVariable("libroId") Long libroId, Model model) {
        Libro libro = libroService.getLibroById(libroId);
        Autore autore = autoreService.getAutoreById(autoreId);

        libro.getAutori().remove(autore);
        libroService.saveLibro(libro);

        return "redirect:/libro/" + libroId;
    }
}
