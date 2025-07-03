package br.com.exemplo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreditoController.class)
public class CreditoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetCreditos() throws Exception {
        // Simula um endpoint que retorna uma lista de créditos
        mockMvc.perform(get("/creditos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateCredito() throws Exception {
        // Dados de exemplo para criar um crédito
        String newCreditoJson = "{\"id\":1,\"amount\":100.0}";

        mockMvc.perform(post("/creditos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newCreditoJson))
                .andExpect(status().isCreated());
    }

    // Você pode adicionar mais testes para validar outros comportamentos do controller, como validações e tratativas de erro.
}

