package com.megaeletronicos.backend.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateAparelhoDTO {
    
    private String imei;
    private String modelo;
    private String marca;
    private Long clienteId;
    private Long empresaId;
    
    @Positive(message = "Valor parcelado deve ser positivo")
    private BigDecimal valorParcelado;
    
    @Positive(message = "Valor total deve ser positivo")
    private BigDecimal valorTotal;
    
    @Min(value = 1, message = "Número de parcelas deve ser no mínimo 1")
    private Integer parcelas;
    
    @Positive(message = "Valor da parcela deve ser positivo")
    private BigDecimal valorParcela;
    
    private Integer diasVencimento;
    private LocalDate dataVencimento;
    private String status;
    
    public UpdateAparelhoDTO() {}
    
    // Getters e Setters
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
    
    public Long getEmpresaId() {
        return empresaId;
    }
    
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
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
}