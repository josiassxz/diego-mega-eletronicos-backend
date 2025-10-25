package com.megaeletronicos.backend.dto;

import com.megaeletronicos.backend.entity.Vendedor;
import java.time.LocalDateTime;

public class VendedorResponseDTO {
    
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String status;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
    
    // Construtores
    public VendedorResponseDTO() {}
    
    public VendedorResponseDTO(Vendedor vendedor) {
        this.id = vendedor.getId();
        this.nome = vendedor.getNome();
        this.cpf = vendedor.getCpf();
        this.email = vendedor.getEmail();
        this.status = vendedor.getStatus().toString();
        this.dataCadastro = vendedor.getDataCadastro();
        this.dataAtualizacao = vendedor.getDataAtualizacao();
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}