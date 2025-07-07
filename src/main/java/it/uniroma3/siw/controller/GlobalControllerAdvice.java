package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    CredentialsService credentialsService;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        // Ottieni l'oggetto Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Determina se l'utente è anonimo
        boolean isAnonymous = authentication == null || authentication instanceof AnonymousAuthenticationToken;

        // Aggiungi l'attributo globale
        model.addAttribute("isAnonymous", isAnonymous);

        // Se l'utente non è anonimo, puoi aggiungere altre informazioni
        if (!isAnonymous) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
                String username = userDetails.getUsername();
                model.addAttribute("username", username);

                // Recupera l'utente completo e aggiungilo al modello
                User user = credentialsService.getUserByUsername(username);
                model.addAttribute("user", user);


                boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
                model.addAttribute("isAdmin", isAdmin);
            }
        } else {
            model.addAttribute("isAdmin", false);
        }
    }
}

