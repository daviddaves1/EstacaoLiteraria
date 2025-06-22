package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import src.Autor;
import src.Categoria;
import src.Editora;
import src.Livro;
import src.Publicacao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LivroTest {

    private Livro livro;
    private Editora editora;
    private Autor autor1;
    private Autor autor2;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        Publicacao.setProximoIdEstatico(1);
        Autor.setProximoIdEstatico(1);
        Editora.setProximoIdEstatico(1);
        Categoria.setProximoIdEstatico(1);

        editora = new Editora("Editora Livros");
        autor1 = new Autor("Autor Teste 1", "Brasileiro", LocalDate.of(1980, 1, 1));
        autor2 = new Autor("Autor Teste 2", "Portugues", LocalDate.of(1975, 5, 10));
        categoria = new Categoria("Ficção");
        livro = new Livro("Título do Livro", 49.90f, 100, editora, 300, "978-1234567890");
    }

    @Test
    @DisplayName("Deve criar livro com atributos corretos e herdar de Publicacao")
    void deveCriarLivroComAtributosCorretos() {
        assertNotNull(livro);
        assertTrue(livro.getId() > 0);
        assertEquals("Título do Livro", livro.getTitulo());
        assertEquals(49.90f, livro.getPreco(), 0.001);
        assertEquals(100, livro.getEstoqueDisponivel());
        assertEquals(editora, livro.getEditora());
        assertEquals(300, livro.getQuantidadePaginas());
        assertEquals("978-1234567890", livro.getIsbn());
        assertNotNull(livro.getAutores());
        assertTrue(livro.getAutores().isEmpty());
        assertNull(livro.getCategoria());
    }

    @Test
    @DisplayName("Deve adicionar um autor ao livro")
    void deveAdicionarUmAutor() {
        livro.addAutor(autor1);
        assertTrue(livro.getAutores().contains(autor1));
        assertEquals(1, livro.getAutores().size());
    }

    @Test
    @DisplayName("Deve adicionar múltiplos autores ao livro")
    void deveAdicionarMultiplosAutores() {
        livro.addAutor(autor1);
        livro.addAutor(autor2);
        assertTrue(livro.getAutores().contains(autor1));
        assertTrue(livro.getAutores().contains(autor2));
        assertEquals(2, livro.getAutores().size());
    }

    @Test
    @DisplayName("Não deve adicionar o mesmo autor mais de uma vez")
    void naoDeveAdicionarAutorDuplicado() {
        livro.addAutor(autor1);
        livro.addAutor(autor1);
        assertEquals(1, livro.getAutores().size());
    }

    @Test
    @DisplayName("Deve remover um autor do livro")
    void deveRemoverAutor() {
        livro.addAutor(autor1);
        livro.removerAutor(autor1);
        assertFalse(livro.getAutores().contains(autor1));
        assertTrue(livro.getAutores().isEmpty());
    }

    @Test
    @DisplayName("Deve setar e obter categoria corretamente")
    void deveSetarECategorias() {
        livro.setCategoria(categoria);
        assertEquals(categoria, livro.getCategoria());
    }

    @Test
    @DisplayName("Deve retornar string formatada em toString() sem autores/categoria/editora")
    void deveRetornarStringFormatadaEmToStringSemAssociacoes() {
        Livro livroSemAssociacoes = new Livro("Livro sem Dados", 10.0f, 10, null, 50, "111-222-333");
        String expected = "--- LIVRO ---\n" +
                          "ID: " + livroSemAssociacoes.getId() + "\n" +
                          "Título: Livro sem Dados\n" +
                          "Autor(es): N/A\n" +
                          "Editora: N/A\n" +
                          "Categoria: N/A\n" +
                          "ISBN: 111-222-333\n" +
                          "Preço: R$10,00\n" +
                          "Quantidade de Páginas: 50\n" +
                          "Estoque Disponível: 10\n" +
                          "--------------------";
        assertEquals(expected, livroSemAssociacoes.toString());
    }

    @Test
    @DisplayName("Deve retornar string formatada em toString() com todas as associacoes")
    void deveRetornarStringFormatadaEmToStringComAssociacoes() {
        livro.addAutor(autor1);
        livro.addAutor(autor2);
        livro.setCategoria(categoria);

        String expectedAutores = autor1.getNome() + ", " + autor2.getNome();
        String expected = "--- LIVRO ---\n" +
                          "ID: " + livro.getId() + "\n" +
                          "Título: Título do Livro\n" +
                          "Autor(es): " + expectedAutores + "\n" +
                          "Editora: " + editora.getNome() + "\n" +
                          "Categoria: " + categoria.getNome() + "\n" +
                          "ISBN: 978-1234567890\n" +
                          "Preço: R$49,90\n" +
                          "Quantidade de Páginas: 300\n" +
                          "Estoque Disponível: 100\n" +
                          "--------------------";
        assertEquals(expected, livro.toString());
    }
}