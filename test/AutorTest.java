package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import src.Autor;
import src.Livro;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AutorTest {

    private Autor autor;

    @BeforeEach
    void setUp() {
        Autor.setProximoIdEstatico(1);
        autor = new Autor("Machado de Assis", "Brasileiro", LocalDate.of(1839, 6, 21));
    }

    @Test
    @DisplayName("Deve criar autor com ID e atributos corretos")
    void deveCriarAutorComAtributosCorretos() {
        assertNotNull(autor);
        assertEquals("Machado de Assis", autor.getNome());
        assertEquals("Brasileiro", autor.getNacionalidade());
        assertEquals(LocalDate.of(1839, 6, 21), autor.getDataNascimento());
        assertTrue(autor.getId() > 0);
    }

    @Test
    @DisplayName("Deve adicionar livro à lista de livros publicados do autor")
    void deveAdicionarLivro() {
        Livro livro = new Livro("Dom Casmurro", 50.0f, 100, null, 250, "978-85-8086-061- Chave", null);
        autor.adicionarLivro(livro);
        assertTrue(autor.getLivrosPublicados().contains(livro));
        assertEquals(1, autor.getLivrosPublicados().size());
    }

    @Test
    @DisplayName("Não deve adicionar o mesmo livro duas vezes")
    void naoDeveAdicionarMesmoLivroDuasVezes() {
        Livro livro = new Livro("Dom Casmurro", 50.0f, 100, null, 250, "978-85-8086-061- Chave", null);
        autor.adicionarLivro(livro);
        autor.adicionarLivro(livro);
        assertEquals(1, autor.getLivrosPublicados().size());
    }

    @Test
    @DisplayName("Deve remover livro da lista de livros publicados do autor")
    void deveRemoverLivro() {
        Livro livro = new Livro("Memórias Póstumas de Brás Cubas", 45.0f, 80, null, 200, "978-85-8086-062- Chave", null);
        autor.adicionarLivro(livro);
        assertTrue(autor.getLivrosPublicados().contains(livro));

        autor.removerLivro(livro);
        assertFalse(autor.getLivrosPublicados().contains(livro));
        assertTrue(autor.getLivrosPublicados().isEmpty());
    }

    @Test
    @DisplayName("Deve retornar string formatada em toString()")
    void deveRetornarStringFormatadaEmToString() {
        String expected = "ID: " + autor.getId() + ", Nome: Machado de Assis, Nacionalidade: Brasileiro, Data Nasc.: 1839-06-21";
        assertEquals(expected, autor.toString());
    }

    @Test
    @DisplayName("Deve setar proximoId estatico corretamente")
    void deveSetarProximoIdEstatico() {
        Autor.setProximoIdEstatico(100);
        Autor novoAutor = new Autor("José de Alencar", "Brasileiro", LocalDate.of(1829, 5, 1));
        assertEquals(100, novoAutor.getId());
        Autor.setProximoIdEstatico(5);
        Autor outroAutor = new Autor("Clarice Lispector", "Ucraniana", LocalDate.of(1920, 12, 10));
        assertEquals(101, outroAutor.getId());
    }
}