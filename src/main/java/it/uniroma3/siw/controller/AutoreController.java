package it.uniroma3.siw.controller;

import it.uniroma3.siw.service.AutoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
        model.addAttribute("autori", autoreService.getAllAutori());
        return "autori.html";
    }
}
