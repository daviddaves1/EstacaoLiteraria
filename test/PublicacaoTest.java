package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import src.Editora;
import src.Livro;
import src.Jornal;
import src.Publicacao;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PublicacaoTest {

    private Editora editoraMock;
    private Livro livro;
    private Jornal jornal;

    @BeforeEach
    void setUp() {
        Publicacao.setProximoIdEstatico(1);
        editoraMock = new Editora("Editora Teste");
        livro = new Livro("O Pequeno Príncipe", 35.0f, 50, editoraMock, 100, "978-85-325-0558- Cheva");
        jornal = new Jornal("Gazeta de Notícias", 5.0f, 200, editoraMock, LocalDate.of(2025, 6, 22));
    }

    @Test
    @DisplayName("Deve adicionar estoque corretamente a uma publicacao")
    void deveAdicionarEstoque() {
        int estoqueInicialLivro = livro.getEstoqueDisponivel();
        livro.addEstoque(10);
        assertEquals(estoqueInicialLivro + 10, livro.getEstoqueDisponivel());

        int estoqueInicialJornal = jornal.getEstoqueDisponivel();
        jornal.addEstoque(20);
        assertEquals(estoqueInicialJornal + 20, jornal.getEstoqueDisponivel());
    }

    @Test
    @DisplayName("Não deve adicionar estoque com quantidade zero ou negativa")
    void naoDeveAdicionarEstoqueInvalido() {
        int estoqueInicial = livro.getEstoqueDisponivel();
        livro.addEstoque(0);
        assertEquals(estoqueInicial, livro.getEstoqueDisponivel());
        livro.addEstoque(-5);
        assertEquals(estoqueInicial, livro.getEstoqueDisponivel());
    }

    @Test
    @DisplayName("Deve remover estoque corretamente de uma publicacao")
    void deveRemoverEstoque() {
        int estoqueInicialLivro = livro.getEstoqueDisponivel();
        livro.remEstoque(5);
        assertEquals(estoqueInicialLivro - 5, livro.getEstoqueDisponivel());

        int estoqueInicialJornal = jornal.getEstoqueDisponivel();
        jornal.remEstoque(10);
        assertEquals(estoqueInicialJornal - 10, jornal.getEstoqueDisponivel());
    }

    @Test
    @DisplayName("Não deve remover estoque com quantidade zero ou negativa")
    void naoDeveRemoverEstoqueInvalido() {
        int estoqueInicial = livro.getEstoqueDisponivel();
        livro.remEstoque(0);
        assertEquals(estoqueInicial, livro.getEstoqueDisponivel());
        livro.remEstoque(-5);
        assertEquals(estoqueInicial, livro.getEstoqueDisponivel());
    }

    @Test
    @DisplayName("Não deve remover estoque se for insuficiente")
    void naoDeveRemoverEstoqueInsuficiente() {
        int estoqueInicial = livro.getEstoqueDisponivel();
        livro.remEstoque(estoqueInicial + 1);
        assertEquals(estoqueInicial, livro.getEstoqueDisponivel());
    }

    @Test
    @DisplayName("Deve setar proximoId estatico corretamente para publicacoes")
    void deveSetarProximoIdEstatico() {
        Publicacao.setProximoIdEstatico(1000);
        Livro novoLivro = new Livro("Livro Teste", 10.0f, 10, editoraMock, 100, "123- Chave");
        assertEquals(1000, novoLivro.getId());
        Publicacao.setProximoIdEstatico(500);
        Jornal novoJornal = new Jornal("Jornal Teste", 2.0f, 50, editoraMock, LocalDate.now());
        assertEquals(1001, novoJornal.getId());
    }

    @Test
    @DisplayName("Deve atribuir ID sequencial a novas publicacoes")
    void deveAtribuirIdSequencial() {
        Livro livro2 = new Livro("Livro 2", 20.0f, 20, editoraMock, 200, "124- Chave");
        assertEquals(livro.getId() + 1, livro2.getId());

        Jornal jornal2 = new Jornal("Jornal 2", 3.0f, 30, editoraMock, LocalDate.now().plusDays(1));
        assertEquals(livro2.getId() + 1, jornal2.getId());
    }
}