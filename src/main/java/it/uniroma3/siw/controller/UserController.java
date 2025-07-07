package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public String profiloUtente(Model model) {
        // Recupera l'autenticazione corrente
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Controlla se l'utente è autenticato
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            // Reindirizza alla pagina di login
            return "redirect:/login";
        }

        // Ottieni lo username dell'utente autenticato
        String username = authentication.getName();
        Credentials credentials = credentialsService.getCredentials(username);

        // Controlla se le credenziali esistono e aggiungile al modello
        if (credentials != null) {
            model.addAttribute("credentials", credentials);
            return "user.html"; // Restituisci la vista dell'utente
        } else {
            // Questo caso dovrebbe essere raro, ma se succede reindirizza alla homepage con un messaggio
            return "redirect:/";
        }
    }

    @GetMapping("/formUpdateUser")
    public String formModificaUtente(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        Credentials credentials = null;

        if (!(authentication == null || authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
                username = userDetails.getUsername();
                credentials = credentialsService.getCredentials(username);
            }
        }
        if (credentials == null) {
            return "redirect:/login";
        }

        User user = credentialsService.getUserByUsername(username);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("credentials", credentials);
        model.addAttribute("user", user);
        return "formUpdateUser.html";
    }

    @PostMapping("/formUpdateUser")
    public String modificaUtente(
            @RequestParam("nome") String nome,
            @RequestParam("cognome") String cognome,
            @RequestParam("email") String email,
            @RequestParam("vecchiaPassword") String vecchiaPassword,
            @RequestParam(value = "password", required = false) String nuovaPassword,
            @RequestParam(value = "username", required = false) String nuovoUsername,
            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Credentials credentials = credentialsService.getCredentials(username);

        if (credentials == null) {
            return "redirect:/login";
        }

        User user = credentials.getUser();

        // Verifica la vecchia password
        if (!passwordEncoder.matches(vecchiaPassword, credentials.getPassword())) {
            model.addAttribute("messaggioErrore", "La vecchia password non è corretta.");
            model.addAttribute("credentials", credentials);
            model.addAttribute("user", user);
            return "formUpdateUser.html";
        }

        // Verifica se esiste già un altro utente con lo stesso username
        if (credentialsService.existsByUsernameAndNotId(nuovoUsername, credentials.getId())) {
            model.addAttribute("messaggioErrore", "Il nome utente è già in uso.");
            model.addAttribute("credentials", credentials);
            model.addAttribute("user", user);
            return "formUpdateUser.html";
        }

        // Verifica se esiste già un altro utente con la stessa email
        if (userService.existsByEmailAndNotId(email, user.getId())) {
            model.addAttribute("messaggioErrore", "L'email è già in uso.");
            model.addAttribute("credentials", credentials);
            model.addAttribute("user", user);
            return "formUpdateUser.html";
        }


        // Aggiorna i dati dell'utente
        user.setName(nome);
        user.setSurname(cognome);
        user.setEmail(email);
        credentials.setUsername(nuovoUsername);

        // Aggiorna solo se la nuova password non è vuota
        if (nuovaPassword != null && !nuovaPassword.isBlank() && !nuovaPassword.isEmpty()) {
            credentialsService.aggiornaPassword(credentials, nuovaPassword);
        }

        userService.saveUser(user);
        credentialsService.updateCredentials(credentials);

        return "redirect:/user";
    }



}
