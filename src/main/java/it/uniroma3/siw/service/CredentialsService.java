package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CredentialsService {
    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CredentialsRepository credentialsRepository;

    @Transactional
    public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Credentials getCredentials(String username) {
        Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
        return result.orElse(null);
    }

    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }

    @Transactional
    public User getUserByUsername(String username){
        Credentials credentials = this.getCredentials(username);
        if (credentials != null)
            return credentials.getUser();
        return null;
    }

    public boolean existsByUsernameAndNotId(String username, Long id) {
        return credentialsRepository.findByUsername(username)
                .filter(credentials -> !credentials.getId().equals(id))
                .isPresent();
    }

    @Transactional
    public void aggiornaPassword(Credentials credentials, String nuovaPassword) {
        String encodedPassword = passwordEncoder.encode(nuovaPassword);
        credentials.setPassword(encodedPassword);
        credentialsRepository.save(credentials);
    }

    @Transactional
    public Credentials updateCredentials(Credentials credentials) {
        return this.credentialsRepository.save(credentials);
    }
}
