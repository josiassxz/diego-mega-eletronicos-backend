
package com.megaeletronicos.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome completo é obrigatório")
    @Column(nullable = false)
    private String nome;
    
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    @Column(nullable = false, unique = true)
    private String email;
    
    @NotBlank(message = "CPF é obrigatório")
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;
    
    @NotBlank(message = "Estado civil é obrigatório")
    @Column(nullable = false)
    private String estadoCivil;
    
    @NotNull(message = "Data de nascimento é obrigatória")
    @Column(nullable = false)
    private LocalDate dataNascimento;
    
    @NotBlank(message = "WhatsApp é obrigatório")
    @Column(nullable = false, length = 15)
    private String whatsapp;
    
    // Campos opcionais para manter compatibilidade
    @Column(length = 20)
    private String rg;
    
    @Column(length = 15)
    private String telefone;
    
    @Column(length = 8)
    private String cep;
    
    @Column
    private String rua;

    @Column(length = 10)
    private String numero;

    @Column
    private String complemento;

    @Column
    private String bairro;
    
    @Column
    private String cidade;
    
    @Column(length = 2)
    private String estado;
    
    @Column
    private String nomeMae;
    
    @Column(length = 10)
    private String sexo;
    
    @Column
    private String naturezaOcupacao;
    
    @Column
    private String profissao;
    
    @Column
    private String nomeEmpresa;
    
    @Column
    private Double rendaMensal;

    @Column
    private String comprovacaoRenda;
    
    @Column
    private String fotoDocumento;
    
    @Column
    private String fotoSelfie;
    
    // Indica se possui carteira assinada ou é aposentado ("Sim"/"Não")
    @Column
    private String possuiCarteiraAssinadaOuAposentado;

    // Primeira Referência
    @Column(length = 100)
    private String referencia1Nome;
    
    @Column(length = 50)
    private String referencia1Relacao;
    
    @Column(length = 15)
    private String referencia1Whatsapp;

    // Indica se o cliente conhece a referência 1 ("Sim"/"Não")
    @Column
    private Boolean referencia1Conhece;

    // Referência 2
    @Column(length = 100)
    private String referencia2Nome;
    
    @Column(length = 50)
    private String referencia2Relacao;
    
    @Column(length = 15)
    private String referencia2Whatsapp;

    // Indica se o cliente conhece a referência 2 ("Sim"/"Não")
    @Column
    private Boolean referencia2Conhece;

    // Referência 3
    @Column(length = 100)
    private String referencia3Nome;
    
    @Column(length = 50)
    private String referencia3Relacao;
    
    @Column(length = 15)
    private String referencia3Whatsapp;

    // Indica se o cliente conhece a referência 3 ("Sim"/"Não")
    @Column
    private Boolean referencia3Conhece;
    
    // Campo de observação (máximo 1000 caracteres)
    @Column(length = 1000)
    private String observacao;
    
    // Status e datas
    @Column(nullable = false)
    private String status = "Pendente"; // Pendente, Aprovado, Recusado, Em Análise, Vendido
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    // Construtores
    public Cliente() {}

    public Cliente(String nome, String email, String cpf, String estadoCivil, 
                   LocalDate dataNascimento, String whatsapp) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.estadoCivil = estadoCivil;
        this.dataNascimento = dataNascimento;
        this.whatsapp = whatsapp;
        this.status = "Pendente";
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

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNomeMae() { return nomeMae; }
    public void setNomeMae(String nomeMae) { this.nomeMae = nomeMae; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }

    public String getNaturezaOcupacao() { return naturezaOcupacao; }
    public void setNaturezaOcupacao(String naturezaOcupacao) { this.naturezaOcupacao = naturezaOcupacao; }

    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }

    public String getNomeEmpresa() { return nomeEmpresa; }
    public void setNomeEmpresa(String nomeEmpresa) { this.nomeEmpresa = nomeEmpresa; }

    public Double getRendaMensal() { return rendaMensal; }
    public void setRendaMensal(Double rendaMensal) { this.rendaMensal = rendaMensal; }

    public String getComprovacaoRenda() { return comprovacaoRenda; }
    public void setComprovacaoRenda(String comprovacaoRenda) { this.comprovacaoRenda = comprovacaoRenda; }

    public String getFotoDocumento() { return fotoDocumento; }
    public void setFotoDocumento(String fotoDocumento) { this.fotoDocumento = fotoDocumento; }

    public String getFotoSelfie() { return fotoSelfie; }
    public void setFotoSelfie(String fotoSelfie) { this.fotoSelfie = fotoSelfie; }
    
    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }
    
    public String getPossuiCarteiraAssinadaOuAposentado() { return possuiCarteiraAssinadaOuAposentado; }
    public void setPossuiCarteiraAssinadaOuAposentado(String possuiCarteiraAssinadaOuAposentado) { this.possuiCarteiraAssinadaOuAposentado = possuiCarteiraAssinadaOuAposentado; }

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
    
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
