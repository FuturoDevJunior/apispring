/**
 * © 2025 Gabriel Ferreira | contato.ferreirag@outlook.com
 * GitHub: FuturoDevJunior | LinkedIn: DevFerreiraG
 */
package br.com.exemplo.controller;

import br.com.exemplo.dto.CreditoResponseDTO;
import br.com.exemplo.service.CreditoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creditos")
public class CreditoController {

    @Autowired
    private CreditoService creditoService;

    /**
     * Endpoint para obter lista de créditos por número da NFS-e.
     */
    @GetMapping("/{numeroNfse}")
    public ResponseEntity<List<CreditoResponseDTO>> getCreditosPorNfse(@PathVariable String numeroNfse) {
        List<CreditoResponseDTO> creditos = creditoService.buscarCreditosPorNfse(numeroNfse);
        return ResponseEntity.ok(creditos);
    }

    /**
     * Endpoint para obter detalhe de crédito por número do crédito.
     */
    @GetMapping("/credito/{numeroCredito}")
    public ResponseEntity<CreditoResponseDTO> getCreditoPorNumero(@PathVariable String numeroCredito) {
        java.util.Optional<CreditoResponseDTO> credito = creditoService.buscarCreditoPorNumero(numeroCredito);
        
        if (credito.isPresent()) {
            return ResponseEntity.ok(credito.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
