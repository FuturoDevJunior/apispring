package br.com.exemplo.repository;

import br.com.exemplo.entity.Credito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditoRepository extends JpaRepository<Credito, Long> {

    /**
     * Busca créditos pelo número da NFS-e
     */
    @Query("SELECT c FROM Credito c WHERE c.numeroNfse = :numeroNfse ORDER BY c.dataConstituicao DESC")
    List<Credito> findByNumeroNfse(@Param("numeroNfse") String numeroNfse);

    /**
     * Busca um crédito específico pelo número do crédito
     */
    @Query("SELECT c FROM Credito c WHERE c.numeroCredito = :numeroCredito")
    Optional<Credito> findByNumeroCredito(@Param("numeroCredito") String numeroCredito);

    /**
     * Verifica se existe um crédito com o número específico
     */
    boolean existsByNumeroCredito(String numeroCredito);

    /**
     * Verifica se existe algum crédito para uma NFS-e específica
     */
    boolean existsByNumeroNfse(String numeroNfse);
}
