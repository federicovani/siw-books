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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AutoreController {
    @Autowired
    private AutoreService autoreService;
    @Autowired
    private LibroService libroService;
    @Autowired
    private ImmagineService immagineService;

    @GetMapping("/autore/{id}")
    public String getAutore(@PathVariable("id") Long id, Model model) {
        model.addAttribute("autore", autoreService.getAutoreById(id));
        return "autore.html";
    }

    @GetMapping("/autore")
    public String getAutori(Model model){
        Iterable<Autore> autori = autoreService.getAllAutori();
        autori.forEach(autore -> autore.getImmagine());

        // Convertiamo i libri in una stringa di nomi separati da virgole
        for (Autore autore : autori) {
            autore.setLibriString(
                    autore.getLibri().stream()
                            .map(libro -> libro.getTitolo() + " " + libro.getAnnoPubblicazione())
                            .collect(Collectors.joining(", "))
            );
        }

        model.addAttribute("autori", autori);
        return "autori.html";
    }

    @GetMapping("/autore/search")
    public String searchAutori(@RequestParam("query") String query, Model model) {
        // Recupera tutti gli autori il cui nome o cognome contiene il termine di ricerca ignorando le maiuscole/minuscole
        List<Autore> autori = autoreService.searchAutoriByNomeOrCognome(query);

        // Convertiamo i libri dell'autore in una stringa leggibile
        for (Autore autore : autori) {
            autore.setLibriString(
                    autore.getLibri().stream()
                            .map(libro -> libro.getTitolo() + " " + libro.getAnnoPubblicazione())
                            .collect(Collectors.joining(", "))
            );
        }

        model.addAttribute("autori", autori); // Autori risultati dalla ricerca
        model.addAttribute("isSearch", true); // Flag per sapere se è una ricerca
        model.addAttribute("searchQuery", query); // Mostra il termine di ricerca usato

        return "autori.html";
    }

    @PostMapping("/admin/deleteAutore/{id}")
    public String deleteAutore(@PathVariable Long id) {
        autoreService.deleteById(id);
        return "redirect:/autore";
    }

    @GetMapping("/admin/formNewAutore")
    public String formNewAutore(Model model) {
        model.addAttribute("autore", new Autore());
        return "admin/formNewAutore.html";
    }

    @PostMapping("/admin/formNewAutore")
    public String createAutore(@ModelAttribute("autore") Autore autore,  @RequestParam("file") MultipartFile file, Model model) {
        if (!autoreService.existsByNomeAndCognome(autore.getNome(), autore.getCognome())) {
            if (file != null && !file.isEmpty())
                autoreService.saveImmagineAutore(autore, file);
            autoreService.saveAutore(autore);
            model.addAttribute("autore", autore);
            return "redirect:/autore/" + autore.getId();
        } else {
            model.addAttribute("messaggioErrore", "Questo autore esiste già");
            return "admin/formNewAutore.html";
        }
    }

    @PostMapping("/admin/addDataMorte/{autoreId}")
    public String addDataMorte(@PathVariable("autoreId") Long autoreId, @RequestParam("dataMorte") LocalDate dataMorte) {

        Autore autore = autoreService.getAutoreById(autoreId);

        autore.setDataMorte(dataMorte);

        autoreService.saveAutore(autore);

        return "redirect:/autore/" + autoreId;
    }

    @PostMapping("/admin/addImmagineAutore/{autoreId}")
    public String addImmagine(@PathVariable("autoreId") Long autoreId, @RequestParam("file") MultipartFile file) {

        Autore autore = autoreService.getAutoreById(autoreId);

        if (file != null && !file.isEmpty())
            autoreService.saveImmagineAutore(autore, file);

        autoreService.saveAutore(autore);

        return "redirect:/autore/" + autoreId;
    }

    @PostMapping("/admin/rimuoviImmagineAutore/{autoreId}")
    public String removeImage(@PathVariable("autoreId") Long autoreId) {

        Autore autore = autoreService.getAutoreById(autoreId);

        immagineService.deleteImage(autore.getImmagine());

        autore.setImmagine(null);

        autoreService.saveAutore(autore);

        return "redirect:/autore/" + autoreId;
    }


//    @GetMapping("/admin/formUpdateAutore/{autoreId}")
//    public String formUpdateAutore(@PathVariable("autoreId") Long autoreId, Model model) {
//        Autore autore = autoreService.getAutoreById(autoreId);
//        model.addAttribute("autore", autore);
//        return "admin/formUpdateAutore.html";
//    }
//
//    @PostMapping("/admin/formUpdateAutore/{autoreId}")
//    public String updateAutore(@ModelAttribute("autore") Autore autore, @PathVariable("autoreId") Long autoreId, Model model) {
//        if (!autoreService.existsByNomeAndCognome(autore.getNome(), autore.getCognome())) {
//            autoreService.saveAutore(autore);
//            model.addAttribute("autore", autore);
//            return "redirect:/autore/" + autore.getId();
//        } else {
//            model.addAttribute("messaggioErrore", "Questo artista esiste già");
//            return "admin/formNewAutore.html";
//        }
//    }

    @GetMapping("/admin/libriToAdd/{autoreId}")
    public String libriToAdd(@PathVariable("autoreId") Long autoreId, Model model) {
        Autore autore = autoreService.getAutoreById(autoreId);

        List<Libro> libriToAdd = libroService.findLibriNotByAutore(autoreId);

        model.addAttribute("autore", autore);
        model.addAttribute("libriToAdd", libriToAdd);

        return "admin/libriToAdd.html";
    }

    @GetMapping(value="/admin/addLibroToAutore/{libroId}/{autoreId}")
    public String addLibroToAutore(@PathVariable("libroId") Long libroId, @PathVariable("autoreId") Long autoreId, Model model) {

        Libro libro = libroService.getLibroById(libroId);
        Autore autore = autoreService.getAutoreById(autoreId);

        List<Autore> autori = libro.getAutori();
        autori.add(autore);
        libroService.saveLibro(libro);

        List<Libro> libriToAdd = libroService.findLibriNotByAutore(autoreId);

        model.addAttribute("autore", autore);
        model.addAttribute("libro", libro);
        model.addAttribute("libriToAdd", libriToAdd);

        return "admin/libriToAdd.html";
    }

    @PostMapping("/admin/removeLibroFromAutore/{libroId}/{autoreId}")
    public String deleteLibroFromAutore(@PathVariable("libroId") Long libroId, @PathVariable("autoreId") Long autoreId, Model model) {
        Libro libro = libroService.getLibroById(libroId);
        Autore autore = autoreService.getAutoreById(autoreId);

        libro.getAutori().remove(autore);
        libroService.saveLibro(libro);

        return "redirect:/autore/" + autoreId;
    }
}
