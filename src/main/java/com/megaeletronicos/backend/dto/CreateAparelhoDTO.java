package com.megaeletronicos.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateAparelhoDTO {
    
    @NotBlank(message = "IMEI é obrigatório")
    private String imei;
    
    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;
    
    @NotBlank(message = "Marca é obrigatória")
    private String marca;
    
    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;
    
    @NotNull(message = "ID da empresa é obrigatório")
    private Long empresaId;
    
    @NotNull(message = "Valor parcelado é obrigatório")
    @Positive(message = "Valor parcelado deve ser positivo")
    private BigDecimal valorParcelado;
    
    @NotNull(message = "Valor total é obrigatório")
    @Positive(message = "Valor total deve ser positivo")
    private BigDecimal valorTotal;
    
    @NotNull(message = "Número de parcelas é obrigatório")
    @Min(value = 1, message = "Número de parcelas deve ser no mínimo 1")
    private Integer parcelas;
    
    @NotNull(message = "Valor da parcela é obrigatório")
    @Positive(message = "Valor da parcela deve ser positivo")
    private BigDecimal valorParcela;
    
    @NotNull(message = "Dias de vencimento é obrigatório")
    private Integer diasVencimento;
    
    private LocalDate dataVencimento;
    
    public CreateAparelhoDTO() {}
    
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
}