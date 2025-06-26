package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.service.AutoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AutoreController {
    @Autowired
    private AutoreService autoreService;

    @GetMapping("/autore/{id}")
    public String getAutore(@PathVariable("id") Long id, Model model) {
        model.addAttribute("autore", autoreService.getAutoreById(id));
        return "autore.html";
    }

    @GetMapping("/autore")
    public String showAutori(Model model){
        Iterable<Autore> autori = autoreService.getAllAutori();

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

}
