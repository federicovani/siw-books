package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.LibroService;
import it.uniroma3.siw.service.RecensioneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RecensioneController {
    @Autowired
    private RecensioneService recensioneService;
    @Autowired
    private LibroService libroService;
    @Autowired
    private CredentialsService credentialsService;

    @PostMapping("/recensione/delete/{id}")
    public String deleteRecensione(@PathVariable("id") Long id) {
        Long libroId = recensioneService.deleteById(id);
        return "redirect:/libro/" + libroId;
    }

    @GetMapping("/recensione/add/{libroId}")
    public String showAddRecensioneForm(@PathVariable("libroId") Long libroId, Model model) {
        Libro libro = libroService.getLibroById(libroId);
        model.addAttribute("libro", libro);
        model.addAttribute("recensione", new Recensione());
        return "formNewRecensione.html";
    }

    @PostMapping("/recensione/add")
    public String addRecensione(@Valid @ModelAttribute("recensione") Recensione recensione,
                                @RequestParam("libroId") Long libroId,
                                Authentication authentication,
                                Model model) {
        String username = (String) model.getAttribute("username");
        User user = credentialsService.getUserByUsername(username);
        Libro libro = libroService.getLibroById(libroId);
        if(!recensioneService.existsByUserAndLibro(user, libro)){
            recensione.setUser(user);
            recensione.setLibro(libro);
            recensioneService.saveRecensione(recensione);
            return "redirect:/libro/" + libroId;
        } else {
            model.addAttribute("messaggioErrore", "Hai già lasciato una recensione per questo libro.");
            model.addAttribute("libro", libro);
            return "formNewRecensione.html";
        }
    }

}
