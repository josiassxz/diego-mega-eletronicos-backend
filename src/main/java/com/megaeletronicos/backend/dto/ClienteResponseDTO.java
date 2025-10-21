
package com.megaeletronicos.backend.dto;

import com.megaeletronicos.backend.entity.Cliente;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String whatsapp;
    private String possuiCarteiraAssinadaOuAposentado;
    private String estadoCivil;
    private LocalDate dataNascimento;
    private String status;
    private LocalDateTime dataCadastro;
    private String cidade;
    private String estado;
    private String comprovacaoRenda;
    
    // Primeira Referência
    private String referencia1Nome;
    private String referencia1Relacao;
    private String referencia1Whatsapp;
    private Boolean referencia1Conhece;
    
    // Segunda Referência
    private String referencia2Nome;
    private String referencia2Relacao;
    private String referencia2Whatsapp;
    private Boolean referencia2Conhece;
    
    // Terceira Referência
    private String referencia3Nome;
    private String referencia3Relacao;
    private String referencia3Whatsapp;
    private Boolean referencia3Conhece;
    
    public ClienteResponseDTO() {}
    
    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.cpf = cliente.getCpf();
        this.whatsapp = cliente.getWhatsapp();
        this.possuiCarteiraAssinadaOuAposentado = cliente.getPossuiCarteiraAssinadaOuAposentado();
        this.estadoCivil = cliente.getEstadoCivil();
        this.dataNascimento = cliente.getDataNascimento();
        this.status = cliente.getStatus();
        this.dataCadastro = cliente.getDataCadastro();
        this.cidade = cliente.getCidade();
        this.estado = cliente.getEstado();
        this.comprovacaoRenda = cliente.getComprovacaoRenda();
        
        // Mapear referências expandidas
        this.referencia1Nome = cliente.getReferencia1Nome();
        this.referencia1Relacao = cliente.getReferencia1Relacao();
        this.referencia1Whatsapp = cliente.getReferencia1Whatsapp();
        this.referencia1Conhece = cliente.getReferencia1Conhece();
        
        this.referencia2Nome = cliente.getReferencia2Nome();
        this.referencia2Relacao = cliente.getReferencia2Relacao();
        this.referencia2Whatsapp = cliente.getReferencia2Whatsapp();
        this.referencia2Conhece = cliente.getReferencia2Conhece();
        
        this.referencia3Nome = cliente.getReferencia3Nome();
        this.referencia3Relacao = cliente.getReferencia3Relacao();
        this.referencia3Whatsapp = cliente.getReferencia3Whatsapp();
        this.referencia3Conhece = cliente.getReferencia3Conhece();
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }

    public String getPossuiCarteiraAssinadaOuAposentado() { return possuiCarteiraAssinadaOuAposentado; }
    public void setPossuiCarteiraAssinadaOuAposentado(String possuiCarteiraAssinadaOuAposentado) { this.possuiCarteiraAssinadaOuAposentado = possuiCarteiraAssinadaOuAposentado; }
    
    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }
    
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getComprovacaoRenda() { return comprovacaoRenda; }
    public void setComprovacaoRenda(String comprovacaoRenda) { this.comprovacaoRenda = comprovacaoRenda; }
    
    // Getters e Setters para Primeira Referência
    public String getReferencia1Nome() { return referencia1Nome; }
    public void setReferencia1Nome(String referencia1Nome) { this.referencia1Nome = referencia1Nome; }
    
    public String getReferencia1Relacao() { return referencia1Relacao; }
    public void setReferencia1Relacao(String referencia1Relacao) { this.referencia1Relacao = referencia1Relacao; }
    
    public String getReferencia1Whatsapp() { return referencia1Whatsapp; }
    public void setReferencia1Whatsapp(String referencia1Whatsapp) { this.referencia1Whatsapp = referencia1Whatsapp; }
    
    public Boolean getReferencia1Conhece() { return referencia1Conhece; }
    public void setReferencia1Conhece(Boolean referencia1Conhece) { this.referencia1Conhece = referencia1Conhece; }
    
    // Getters e Setters para Segunda Referência
    public String getReferencia2Nome() { return referencia2Nome; }
    public void setReferencia2Nome(String referencia2Nome) { this.referencia2Nome = referencia2Nome; }
    
    public String getReferencia2Relacao() { return referencia2Relacao; }
    public void setReferencia2Relacao(String referencia2Relacao) { this.referencia2Relacao = referencia2Relacao; }
    
    public String getReferencia2Whatsapp() { return referencia2Whatsapp; }
    public void setReferencia2Whatsapp(String referencia2Whatsapp) { this.referencia2Whatsapp = referencia2Whatsapp; }
    
    public Boolean getReferencia2Conhece() { return referencia2Conhece; }
    public void setReferencia2Conhece(Boolean referencia2Conhece) { this.referencia2Conhece = referencia2Conhece; }
    
    // Getters e Setters para Terceira Referência
    public String getReferencia3Nome() { return referencia3Nome; }
    public void setReferencia3Nome(String referencia3Nome) { this.referencia3Nome = referencia3Nome; }
    
    public String getReferencia3Relacao() { return referencia3Relacao; }
    public void setReferencia3Relacao(String referencia3Relacao) { this.referencia3Relacao = referencia3Relacao; }
    
    public String getReferencia3Whatsapp() { return referencia3Whatsapp; }
    public void setReferencia3Whatsapp(String referencia3Whatsapp) { this.referencia3Whatsapp = referencia3Whatsapp; }
    
    public Boolean getReferencia3Conhece() { return referencia3Conhece; }
    public void setReferencia3Conhece(Boolean referencia3Conhece) { this.referencia3Conhece = referencia3Conhece; }
}
