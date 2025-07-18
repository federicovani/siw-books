package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class AuthenticationController {
    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private AutoreService autoreService;

    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("libri", libroService.getAllLibri());

        List<Autore> allAutori = (List<Autore>) autoreService.getAllAutori();
        // Seleziona 3 autori casuali
        Collections.shuffle(allAutori);
        List<Autore> randomAutori = allAutori.stream()
                .limit(3)
                .toList();
        model.addAttribute("autori", randomAutori);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return "index.html";
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            if (credentials != null && credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
                return "index.html";
            }
        }

        return "index.html";
    }

    @GetMapping(value = "/login")
    public String showLoginForm (Model model) {
        return "formLogin";
    }

    @GetMapping(value = "/register")
    public String showRegisterForm (Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "formRegister";
    }

    @PostMapping(value = { "/register" })
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult userBindingResult, @Valid
                               @ModelAttribute("credentials") Credentials credentials,
                               BindingResult credentialsBindingResult,
                               Model model) {
        // Controllo se lo username esiste già
        if (credentialsService.getCredentials(credentials.getUsername()) != null) {
            model.addAttribute("usernameError", "Il nome utente è già registrato.");
            return "formRegister";
        }

        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
            return "registrationSuccessful";
        }
        return "formRegister";
    }

    @GetMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("messaggioErrore", "Si è verificato un errore, riprova più tardi.");
        return "error.html";
    }

}
