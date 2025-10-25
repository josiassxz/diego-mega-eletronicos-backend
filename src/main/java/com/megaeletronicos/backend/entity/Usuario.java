
package com.megaeletronicos.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Login é obrigatório")
    @Column(nullable = false, unique = true)
    private String login;
    
    @NotBlank(message = "Senha é obrigatória")
    @Column(nullable = false)
    private String senha;
    
    @NotBlank(message = "Perfil é obrigatório")
    @Column(nullable = false)
    private String perfil; // ADMIN, VENDEDOR, etc.
    

    // Construtores
    public Usuario() {}

    public Usuario(String login, String senha) {
        this.login = login;
        this.senha = senha;
        this.perfil = "ADMIN"; // Perfil padrão para compatibilidade
    }
    
    public Usuario(String login, String senha, String perfil) {
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
}
