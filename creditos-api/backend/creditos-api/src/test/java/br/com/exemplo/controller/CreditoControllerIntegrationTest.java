package br.com.exemplo.controller;

import br.com.exemplo.entity.Credito;
import br.com.exemplo.repository.CreditoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureWebMvc
@Transactional
class CreditoControllerIntegrationTest {

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
        registry.add("spring.kafka.bootstrap-servers", () -> "localhost:9093"); // Porta diferente para testes
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CreditoRepository creditoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private br.com.exemplo.messaging.ConsultaPublisher consultaPublisher;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Limpa e popula dados de teste
        creditoRepository.deleteAll();
        
        Credito credito1 = new Credito();
        credito1.setNumeroCredito("123456");
        credito1.setNumeroNfse("7891011");
        credito1.setDataConstituicao(LocalDate.of(2024, 2, 25));
        credito1.setValorIssqn(new BigDecimal("1500.75"));
        credito1.setTipoCredito("ISSQN");
        credito1.setSimplesNacional(true);
        credito1.setAliquota(new BigDecimal("5.0"));
        credito1.setValorFaturado(new BigDecimal("30000.00"));
        credito1.setValorDeducao(new BigDecimal("5000.00"));
        credito1.setBaseCalculo(new BigDecimal("25000.00"));
        creditoRepository.save(credito1);

        Credito credito2 = new Credito();
        credito2.setNumeroCredito("789012");
        credito2.setNumeroNfse("7891011");
        credito2.setDataConstituicao(LocalDate.of(2024, 2, 26));
        credito2.setValorIssqn(new BigDecimal("1200.50"));
        credito2.setTipoCredito("ISSQN");
        credito2.setSimplesNacional(false);
        credito2.setAliquota(new BigDecimal("4.5"));
        credito2.setValorFaturado(new BigDecimal("25000.00"));
        credito2.setValorDeducao(new BigDecimal("4000.00"));
        credito2.setBaseCalculo(new BigDecimal("21000.00"));
        creditoRepository.save(credito2);
    }

    @Test
    void deveRetornarCreditosPorNfse() throws Exception {
        mockMvc.perform(get("/api/creditos/7891011")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].numeroCredito").value("789012")) // Ordenado por data DESC
                .andExpect(jsonPath("$[0].numeroNfse").value("7891011"))
                .andExpect(jsonPath("$[0].valorIssqn").value(1200.50))
                .andExpect(jsonPath("$[0].simplesNacional").value("Não"))
                .andExpect(jsonPath("$[1].numeroCredito").value("123456"))
                .andExpect(jsonPath("$[1].simplesNacional").value("Sim"));
    }

    @Test
    void deveRetornarListaVaziaQuandoNfseNaoEncontrada() throws Exception {
        mockMvc.perform(get("/api/creditos/999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void deveRetornarCreditoPorNumero() throws Exception {
        mockMvc.perform(get("/api/creditos/credito/123456")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numeroCredito").value("123456"))
                .andExpect(jsonPath("$.numeroNfse").value("7891011"))
                .andExpect(jsonPath("$.dataConstituicao").value("2024-02-25"))
                .andExpect(jsonPath("$.valorIssqn").value(1500.75))
                .andExpect(jsonPath("$.tipoCredito").value("ISSQN"))
                .andExpect(jsonPath("$.simplesNacional").value("Sim"))
                .andExpect(jsonPath("$.aliquota").value(5.0))
                .andExpect(jsonPath("$.valorFaturado").value(30000.00))
                .andExpect(jsonPath("$.valorDeducao").value(5000.00))
                .andExpect(jsonPath("$.baseCalculo").value(25000.00));
    }

    @Test
    void deveRetornar404QuandoCreditoNaoEncontrado() throws Exception {
        mockMvc.perform(get("/api/creditos/credito/999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        // Não há body na resposta 404, conforme implementação no Controller
    }
}
