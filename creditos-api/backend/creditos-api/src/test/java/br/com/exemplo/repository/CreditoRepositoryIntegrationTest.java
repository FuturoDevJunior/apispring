package br.com.exemplo.repository;

import br.com.exemplo.entity.Credito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditoRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.5")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CreditoRepository creditoRepository;

    private Credito credito1;
    private Credito credito2;
    private Credito credito3;

    @BeforeEach
    void setUp() {
        // Limpa dados anteriores
        creditoRepository.deleteAll();

        // Cria dados de teste
        credito1 = new Credito();
        credito1.setNumeroCredito("CR001");
        credito1.setNumeroNfse("NF001");
        credito1.setValorIssqn(new BigDecimal("1000.00"));
        credito1.setDataConstituicao(LocalDate.of(2024, 1, 1));
        credito1.setTipoCredito("ISSQN");
        credito1.setSimplesNacional(true);
        credito1.setAliquota(new BigDecimal("5.0"));
        credito1.setValorFaturado(new BigDecimal("20000.00"));
        credito1.setValorDeducao(new BigDecimal("0.00"));
        credito1.setBaseCalculo(new BigDecimal("20000.00"));

        credito2 = new Credito();
        credito2.setNumeroCredito("CR002");
        credito2.setNumeroNfse("NF001"); // Mesmo número de NFS-e
        credito2.setValorIssqn(new BigDecimal("2000.00"));
        credito2.setDataConstituicao(LocalDate.of(2024, 1, 2));
        credito2.setTipoCredito("ISSQN");
        credito2.setSimplesNacional(false);
        credito2.setAliquota(new BigDecimal("4.0"));
        credito2.setValorFaturado(new BigDecimal("50000.00"));
        credito2.setValorDeducao(new BigDecimal("0.00"));
        credito2.setBaseCalculo(new BigDecimal("50000.00"));

        credito3 = new Credito();
        credito3.setNumeroCredito("CR003");
        credito3.setNumeroNfse("NF002"); // Número de NFS-e diferente
        credito3.setValorIssqn(new BigDecimal("3000.00"));
        credito3.setDataConstituicao(LocalDate.of(2024, 1, 3));
        credito3.setTipoCredito("Outros");
        credito3.setSimplesNacional(true);
        credito3.setAliquota(new BigDecimal("3.5"));
        credito3.setValorFaturado(new BigDecimal("85714.29"));
        credito3.setValorDeducao(new BigDecimal("0.00"));
        credito3.setBaseCalculo(new BigDecimal("85714.29"));

        // Persiste os dados
        entityManager.persistAndFlush(credito1);
        entityManager.persistAndFlush(credito2);
        entityManager.persistAndFlush(credito3);
    }

    @Test
    void deveEncontrarCreditosPorNumeroNfse() {
        // When
        List<Credito> creditos = creditoRepository.findByNumeroNfse("NF001");

        // Then
        assertThat(creditos).hasSize(2);
        assertThat(creditos).extracting(Credito::getNumeroCredito)
                .containsExactly("CR002", "CR001"); // Ordenado por data de constituição DESC
        assertThat(creditos).extracting(Credito::getNumeroNfse)
                .containsOnly("NF001");
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoEncontrarNfse() {
        // When
        List<Credito> creditos = creditoRepository.findByNumeroNfse("NF999");

        // Then
        assertThat(creditos).isEmpty();
    }

    @Test
    void deveEncontrarCreditoPorNumeroCredito() {
        // When
        Optional<Credito> creditoOpt = creditoRepository.findByNumeroCredito("CR002");

        // Then
        assertThat(creditoOpt).isPresent();
        Credito credito = creditoOpt.get();
        assertThat(credito.getNumeroCredito()).isEqualTo("CR002");
        assertThat(credito.getNumeroNfse()).isEqualTo("NF001");
        assertThat(credito.getValorIssqn()).isEqualTo(new BigDecimal("2000.00"));
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarNumeroCredito() {
        // When
        Optional<Credito> creditoOpt = creditoRepository.findByNumeroCredito("CR999");

        // Then
        assertThat(creditoOpt).isEmpty();
    }

    @Test
    void deveVerificarSeExisteNumeroCredito() {
        // When & Then
        assertThat(creditoRepository.existsByNumeroCredito("CR001")).isTrue();
        assertThat(creditoRepository.existsByNumeroCredito("CR999")).isFalse();
    }

    @Test
    void deveVerificarSeExisteNumeroNfse() {
        // When & Then
        assertThat(creditoRepository.existsByNumeroNfse("NF001")).isTrue();
        assertThat(creditoRepository.existsByNumeroNfse("NF002")).isTrue();
        assertThat(creditoRepository.existsByNumeroNfse("NF999")).isFalse();
    }

    @Test
    void deveOrdenarCreditosPorDataConstituicaoDesc() {
        // When
        List<Credito> creditos = creditoRepository.findByNumeroNfse("NF001");

        // Then
        assertThat(creditos).hasSize(2);
        assertThat(creditos.get(0).getDataConstituicao())
                .isAfter(creditos.get(1).getDataConstituicao());
    }

    @Test
    void deveSalvarERecuperarCredito() {
        // Given
        Credito novoCredito = new Credito();
        novoCredito.setNumeroCredito("CR004");
        novoCredito.setNumeroNfse("NF003");
        novoCredito.setValorIssqn(new BigDecimal("4000.00"));
        novoCredito.setDataConstituicao(LocalDate.of(2024, 1, 4));
        novoCredito.setTipoCredito("ISSQN");
        novoCredito.setSimplesNacional(true);
        novoCredito.setAliquota(new BigDecimal("5.0"));
        novoCredito.setValorFaturado(new BigDecimal("80000.00"));
        novoCredito.setValorDeducao(new BigDecimal("0.00"));
        novoCredito.setBaseCalculo(new BigDecimal("80000.00"));

        // When
        Credito creditoSalvo = creditoRepository.save(novoCredito);
        entityManager.flush();
        entityManager.clear();

        Optional<Credito> creditoRecuperado = creditoRepository.findByNumeroCredito("CR004");

        // Then
        assertThat(creditoSalvo.getId()).isNotNull();
        assertThat(creditoRecuperado).isPresent();
        assertThat(creditoRecuperado.get().getNumeroCredito()).isEqualTo("CR004");
        assertThat(creditoRecuperado.get().getValorIssqn()).isEqualTo(new BigDecimal("4000.00"));
    }

    @Test
    void deveContarTodosOsCreditos() {
        // When
        long count = creditoRepository.count();

        // Then
        assertThat(count).isEqualTo(3);
    }
}
