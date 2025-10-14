package com.megaeletronicos.backend.dto;

import com.megaeletronicos.backend.entity.Aparelho;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AparelhoResponseDTO {
    private Long id;
    private String imei;
    private String modelo;
    private String marca;
    private Long clienteId;
    private String clienteNome;
    private String clienteCpf;
    private Long empresaId;
    private String empresaNome;
    private BigDecimal valorParcelado;
    private BigDecimal valorTotal;
    private Integer parcelas;
    private BigDecimal valorParcela;
    private Integer diasVencimento;
    private LocalDate dataVencimento;
    private String status;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
    
    public AparelhoResponseDTO() {}
    
    public AparelhoResponseDTO(Aparelho aparelho) {
        this.id = aparelho.getId();
        this.imei = aparelho.getImei();
        this.modelo = aparelho.getModelo();
        this.marca = aparelho.getMarca();
        this.clienteId = aparelho.getCliente().getId();
        this.clienteNome = aparelho.getCliente().getNome();
        this.clienteCpf = aparelho.getCliente().getCpf();
        this.empresaId = aparelho.getEmpresa().getId();
        this.empresaNome = aparelho.getEmpresa().getRazaoSocial();
        this.valorParcelado = aparelho.getValorParcelado();
        this.valorTotal = aparelho.getValorTotal();
        this.parcelas = aparelho.getParcelas();
        this.valorParcela = aparelho.getValorParcela();
        this.diasVencimento = aparelho.getDiasVencimento();
        this.dataVencimento = aparelho.getDataVencimento();
        this.status = aparelho.getStatus();
        this.dataCadastro = aparelho.getDataCadastro();
        this.dataAtualizacao = aparelho.getDataAtualizacao();
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
    
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getClienteNome() {
        return clienteNome;
    }
    
    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }
    
    public String getClienteCpf() {
        return clienteCpf;
    }
    
    public void setClienteCpf(String clienteCpf) {
        this.clienteCpf = clienteCpf;
    }
    
    public Long getEmpresaId() {
        return empresaId;
    }
    
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
    
    public String getEmpresaNome() {
        return empresaNome;
    }
    
    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
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
}