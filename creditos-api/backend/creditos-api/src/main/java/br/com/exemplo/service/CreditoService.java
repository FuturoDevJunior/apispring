package br.com.exemplo.service;

import br.com.exemplo.dto.ConsultaEventDTO;
import br.com.exemplo.dto.CreditoResponseDTO;
import br.com.exemplo.entity.Credito;
import br.com.exemplo.messaging.ConsultaPublisher;
import br.com.exemplo.repository.CreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditoService {

    @Autowired
    private CreditoRepository creditoRepository;

    @Autowired
    private ConsultaPublisher consultaPublisher;

    /**
     * Obtém uma lista de créditos com base no número da NFS-e.
     */
    public List<CreditoResponseDTO> obterCreditosPorNfse(String numeroNfse) {
        List<Credito> creditos = creditoRepository.findByNumeroNfse(numeroNfse);
        consultaPublisher.publishConsultaEvent(new ConsultaEventDTO("NUMERO_NFSE", numeroNfse, creditos.size(), "localhost", "API"));
        return creditos.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Obtém os detalhes de um crédito com base no número do crédito.
     */
    public CreditoResponseDTO obterCreditoPorNumero(String numeroCredito) {
        Credito credito = creditoRepository.findByNumeroCredito(numeroCredito).orElseThrow(() -> new RuntimeException("Crédito não encontrado"));
        consultaPublisher.publishConsultaEvent(new ConsultaEventDTO("NUMERO_CREDITO", numeroCredito, 1, "localhost", "API"));
        return mapToDTO(credito);
    }

    /**
     * Mapeia uma entidade Credito para um DTO de resposta.
     */
    private CreditoResponseDTO mapToDTO(Credito credito) {
        return new CreditoResponseDTO(
                credito.getNumeroCredito(),
                credito.getNumeroNfse(),
                credito.getDataConstituicao(),
                credito.getValorIssqn(),
                credito.getTipoCredito(),
                credito.isSimplesNacional(),
                credito.getAliquota(),
                credito.getValorFaturado(),
                credito.getValorDeducao(),
                credito.getBaseCalculo()
        );
    }
}
