package br.com.zup.edu.livraria.autores;

import base.SpringBootIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RemoveAutorControllerTest extends SpringBootIntegrationTest {

    private static final String SCOPE = "SCOPE_autores:write";

    @Autowired
    private AutorRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void deveRemoverAutorExistente() throws Exception {
        // cenário
        Autor autor = new Autor("Rafael","rafael.ponte@zup.com.br", "dev cansado");
        repository.save(autor);

        // ação
        mockMvc.perform(DELETE("/api/autores/{id}", criaAccessToken(), SCOPE, autor.getId()))
                .andExpect(status().isNoContent())
        ;

        // validação
        assertEquals(0, repository.count(), "total de autores");
    }

    @Test
    public void naoDeveRemoverAutor_quandoNaoEncontrado() throws Exception {
        // cenário
        Autor autor = new Autor("Rafael","rafael.ponte@zup.com.br", "dev cansado");
        repository.save(autor);

        // ação
        mockMvc.perform(DELETE("/api/autores/{id}", criaAccessToken(), SCOPE, -99999))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("autor não encontrado"))
        ;

        // validação
        assertEquals(1, repository.count(), "total de autores");
    }

    @Test
    public void naoDeveRemoverAutorExistenteQuandoAccessTokenNaoEnviado() throws Exception {
        // cenário
        Autor autor = new Autor("Rafael","rafael.ponte@zup.com.br", "dev cansado");
        repository.save(autor);

        // ação
        mockMvc.perform(DELETE("/api/autores/{id}", autor.getId()))
                .andExpect(status().isUnauthorized())
        ;

        // validação
        assertEquals(1, repository.count(), "total de autores");
    }

    @Test
    public void naoDeveRemoverAutorExistenteQuandoAccessTokenNaoPossuiScopeApropriado() throws Exception {
        // cenário
        Autor autor = new Autor("Rafael","rafael.ponte@zup.com.br", "dev cansado");
        repository.save(autor);

        // ação
        mockMvc.perform(DELETE("/api/autores/{id}", criaAccessToken(), autor.getId()))
                .andExpect(status().isForbidden())
        ;

        // validação
        assertEquals(1, repository.count(), "total de autores");
    }

}