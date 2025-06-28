package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.service.LibroService;
import it.uniroma3.siw.service.RecensioneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RecensioneController {
    @Autowired
    private RecensioneService recensioneService;
    @Autowired
    private LibroService libroService;

    @PostMapping("/recensione/delete/{id}")
    public String deleteRecensione(@PathVariable("id") Long id) {
        Long libroId = recensioneService.deleteById(id);
        return "redirect:/libroAdmin/" + libroId;
    }

    @GetMapping("/recensione/add/{libroId}")
    public String showAddRecensioneForm(@PathVariable("libroId") Long libroId, Model model) {
        Libro libro = libroService.getLibroById(libroId);
        model.addAttribute("libro", libro);
        model.addAttribute("recensione", new Recensione());
        return "formNewRecensione.html";
    }

    /**
     * Salva una recensione nel database
     */
    @PostMapping("/recensione/add")
    public String addRecensione(@Valid @ModelAttribute("recensione") Recensione recensione,
                                BindingResult bindingResult,
                                @ModelAttribute("libroId") Long libroId,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("libro", libroService.getLibroById(libroId));
            return "formNewRecensione.html";
        }
        Libro libro = libroService.getLibroById(libroId);
        recensione.setLibro(libro);
        recensioneService.saveRecensione(recensione);
        return "redirect:/libro/" + libroId;
    }

}
