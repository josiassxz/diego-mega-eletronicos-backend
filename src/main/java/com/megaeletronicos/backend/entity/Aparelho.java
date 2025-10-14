package com.megaeletronicos.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "aparelhos")
public class Aparelho {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "IMEI é obrigatório")
    @Column(nullable = false, unique = true, length = 15)
    private String imei;
    
    @NotBlank(message = "Modelo é obrigatório")
    @Column(nullable = false)
    private String modelo;
    
    @NotBlank(message = "Marca é obrigatória")
    @Column(nullable = false)
    private String marca;
    
    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @NotNull(message = "Empresa é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;
    
    @NotNull(message = "Valor parcelado é obrigatório")
    @Positive(message = "Valor parcelado deve ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorParcelado;
    
    @NotNull(message = "Valor total é obrigatório")
    @Positive(message = "Valor total deve ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @NotNull(message = "Número de parcelas é obrigatório")
    @Min(value = 1, message = "Número de parcelas deve ser no mínimo 1")
    @Column(nullable = false)
    private Integer parcelas;
    
    @NotNull(message = "Valor da parcela é obrigatório")
    @Positive(message = "Valor da parcela deve ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorParcela;
    
    @NotNull(message = "Dias de vencimento é obrigatório")
    @Column(nullable = false)
    private Integer diasVencimento;
    
    @NotNull(message = "Data de vencimento é obrigatória")
    @Column(nullable = false)
    private LocalDate dataVencimento;
    
    @Column(nullable = false)
    private String status = "Ativo"; // Ativo, Quitado, Cancelado
    
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
    public Aparelho() {}
    
    public Aparelho(String imei, String modelo, String marca, Cliente cliente, Empresa empresa,
                   BigDecimal valorParcelado, BigDecimal valorTotal, Integer parcelas,
                   BigDecimal valorParcela, Integer diasVencimento, LocalDate dataVencimento) {
        this.imei = imei;
        this.modelo = modelo;
        this.marca = marca;
        this.cliente = cliente;
        this.empresa = empresa;
        this.valorParcelado = valorParcelado;
        this.valorTotal = valorTotal;
        this.parcelas = parcelas;
        this.valorParcela = valorParcela;
        this.diasVencimento = diasVencimento;
        this.dataVencimento = dataVencimento;
        this.status = "Ativo";
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getImei() {
        return imei;
    }
    
    public void setImei(String imei) {
        this.imei = imei;
    }
    
    public String getModelo() {
        return modelo;
    }
    
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Empresa getEmpresa() {
        return empresa;
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    public BigDecimal getValorParcelado() {
        return valorParcelado;
    }
    
    public void setValorParcelado(BigDecimal valorParcelado) {
        this.valorParcelado = valorParcelado;
    }
    
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    public Integer getParcelas() {
        return parcelas;
    }
    
    public void setParcelas(Integer parcelas) {
        this.parcelas = parcelas;
    }
    
    public BigDecimal getValorParcela() {
        return valorParcela;
    }
    
    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }
    
    public Integer getDiasVencimento() {
        return diasVencimento;
    }
    
    public void setDiasVencimento(Integer diasVencimento) {
        this.diasVencimento = diasVencimento;
    }
    
    public LocalDate getDataVencimento() {
        return dataVencimento;
    }
    
    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
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
    
    @Override
    public String toString() {
        return "Aparelho{" +
                "id=" + id +
                ", imei='" + imei + '\'' +
                ", modelo='" + modelo + '\'' +
                ", marca='" + marca + '\'' +
                ", valorParcelado=" + valorParcelado +
                ", valorTotal=" + valorTotal +
                ", parcelas=" + parcelas +
                ", valorParcela=" + valorParcela +
                ", diasVencimento=" + diasVencimento +
                ", dataVencimento=" + dataVencimento +
                ", status='" + status + '\'' +
                ", dataCadastro=" + dataCadastro +
                '}';
    }
}