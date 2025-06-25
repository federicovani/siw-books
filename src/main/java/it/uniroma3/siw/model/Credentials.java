package it.uniroma3.siw.model;

import jakarta.persistence.*;

@Entity
public class Credentials {
    public static final String DEFAULT_ROLE = "DEFAULT";
    public static final String ADMIN_ROLE = "ADMIN";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false, unique=true)
    private String username;
    @Column(nullable=false)
    private String password;
    private String role;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    public Credentials(String username, String password, String role, User user) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.user = user;
    }

    public Credentials() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
