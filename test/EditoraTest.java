package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import src.Editora;
import src.Livro;
import src.Publicacao;

import static org.junit.jupiter.api.Assertions.*;

public class EditoraTest {

    private Editora editora;

    @BeforeEach
    void setUp() {
        Editora.setProximoIdEstatico(1);
        editora = new Editora("Companhia das Letras");
    }

    @Test
    @DisplayName("Deve criar editora com ID e nome corretos")
    void deveCriarEditoraComAtributosCorretos() {
        assertNotNull(editora);
        assertEquals("Companhia das Letras", editora.getNome());
        assertTrue(editora.getId() > 0);
        assertNotNull(editora.getPublicacoes());
        assertTrue(editora.getPublicacoes().isEmpty());
    }

    @Test
    @DisplayName("Deve adicionar publicacao à lista de publicações da editora")
    void deveAdicionarPublicacao() {
        // Usando Livro como uma Publicacao para o teste
        Livro livro = new Livro("A Hora da Estrela", 40.0f, 50, editora, 100, "978-85-359-0268- Chave");
        editora.adicionarPublicacao(livro);
        assertTrue(editora.getPublicacoes().contains(livro));
        assertEquals(1, editora.getPublicacoes().size());
    }

    @Test
    @DisplayName("Não deve adicionar a mesma publicacao duas vezes")
    void naoDeveAdicionarMesmaPublicacaoDuasVezes() {
        Livro livro = new Livro("O Alquimista", 30.0f, 70, editora, 200, "978-85-7542-001- Chave");
        editora.adicionarPublicacao(livro);
        editora.adicionarPublicacao(livro); // Tenta adicionar novamente
        assertEquals(1, editora.getPublicacoes().size());
    }

    @Test
    @DisplayName("Deve remover publicacao da lista de publicações da editora")
    void deveRemoverPublicacao() {
        Livro livro = new Livro("Ensaio sobre a Cegueira", 55.0f, 60, editora, 300, "978-85-359-0270- Chave");
        editora.adicionarPublicacao(livro);
        assertTrue(editora.getPublicacoes().contains(livro));

        editora.removerPublicacao(livro);
        assertFalse(editora.getPublicacoes().contains(livro));
        assertTrue(editora.getPublicacoes().isEmpty());
    }

    @Test
    @DisplayName("Deve retornar string formatada em toString()")
    void deveRetornarStringFormatadaEmToString() {
        String expected = "ID: " + editora.getId() + ", Nome: Companhia das Letras";
        assertEquals(expected, editora.toString());
    }

    @Test
    @DisplayName("Deve setar proximoId estatico corretamente")
    void deveSetarProximoIdEstatico() {
        Editora.setProximoIdEstatico(70);
        Editora novaEditora = new Editora("Editora Rocco");
        assertEquals(70, novaEditora.getId());
        Editora.setProximoIdEstatico(5); // Deve ignorar
        Editora outraEditora = new Editora("Editora Arqueiro");
        assertEquals(71, outraEditora.getId());
    }
}