package br.com.zup.edu.livraria.autores;

import base.SpringBootIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NovoAutorControllerTest extends SpringBootIntegrationTest {

    private static final String SCOPE = "SCOPE_autores:write";

    @Autowired
    private AutorRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void deveCadastrarNovoAutor() throws Exception {
        // cenário
        NovoAutorRequest novoAutor = new NovoAutorRequest("Alberto", "alberto.souza@zup.com.br", "CTO");

        // ação
        mockMvc.perform(POST("/api/autores", novoAutor, criaAccessToken(), SCOPE))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("**/api/autores/*"))
        ;

        // validação
        assertEquals(1, repository.count(), "total de autores");
    }

    @Test
    public void naoDeveCadastrarNovoAutor_quandoParametrosInvalidos() throws Exception {
        // cenário
        NovoAutorRequest autorInvalido = new NovoAutorRequest("", "", "");

        // ação
        mockMvc.perform(POST("/api/autores", autorInvalido, criaAccessToken(), SCOPE))
                .andExpect(status().isBadRequest())
        ;

        // validação
        assertEquals(0, repository.count(), "total de autores");
    }

    @Test
    public void naoDeveCadastrarNovoAutor_quandoAutorJaExistente() throws Exception {
        // cenário
        Autor existente = new Autor("Rafael","rafael.ponte@zup.com.br", "dev cansado");
        repository.save(existente);

        NovoAutorRequest autorInvalido = new NovoAutorRequest("outro nome", existente.getEmail(), "outra descricao");

        // ação
        mockMvc.perform(POST("/api/autores", autorInvalido, criaAccessToken(), SCOPE))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(status().reason("autor com este email já existente"))
        ;

        // validação
        assertEquals(1, repository.count(), "total de autores");
    }


    @Test
    public void naoDeveCadastrarNovoAutorQuandoAccessTokenNaoEnviado() throws Exception {
        // cenário
        NovoAutorRequest novoAutor = new NovoAutorRequest("Alberto", "alberto.souza@zup.com.br", "CTO");

        // ação
        mockMvc.perform(POST("/api/autores", novoAutor))
                .andExpect(status().isUnauthorized())
        ;

        // validação
        assertEquals(0, repository.count(), "total de autores");
    }

    @Test
    public void naoDeveCadastrarNovoAutorQuandoAccessTokenNaoPossuiScopeApropriado() throws Exception {
        // cenário
        NovoAutorRequest novoAutor = new NovoAutorRequest("Alberto", "alberto.souza@zup.com.br", "CTO");

        // ação
        mockMvc.perform(POST("/api/autores", novoAutor, criaAccessToken()))
                .andExpect(status().isForbidden())
        ;

        // validação
        assertEquals(0, repository.count(), "total de autores");
    }

}