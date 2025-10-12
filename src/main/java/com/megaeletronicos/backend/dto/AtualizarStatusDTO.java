
package com.megaeletronicos.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AtualizarStatusDTO {
    
    @NotBlank(message = "Status é obrigatório")
    @Pattern(regexp = "Pendente|Aprovado|Recusado|Em Análise|Vendido", 
             message = "Status deve ser: Pendente, Aprovado, Recusado, Em Análise ou Vendido")
    private String status;
    
    public AtualizarStatusDTO() {}
    
    public AtualizarStatusDTO(String status) {
        this.status = status;
    }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
