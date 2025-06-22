package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import src.*;
import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SistemaTest {

    private Sistema sistema;
    private static final String TEST_DATA_DIR = "test_data_temp/";

    @BeforeEach
    void setUp() {
        File dataDir = new File(TEST_DATA_DIR);
        if (dataDir.exists()) {
            for (File file : dataDir.listFiles()) {
                file.delete();
            }
        } else {
            dataDir.mkdirs();
        }

        Publicacao.setProximoIdEstatico(1);
        Autor.setProximoIdEstatico(1);
        Editora.setProximoIdEstatico(1);
        Categoria.setProximoIdEstatico(1);
        sistema = new Sistema();
    }

    @AfterEach
    void tearDown() {

    }

    // Testes de Cadastro
    @Test
    @DisplayName("Deve cadastrar um novo autor com sucesso")
    void deveCadastrarAutorComSucesso() {
        assertTrue(sistema.cadastrarAutor("Autor Teste", "Brasileira", LocalDate.of(1990, 1, 1)));
        assertEquals(1, sistema.getTodosAutores().size());
        assertEquals("Autor Teste", sistema.getTodosAutores().get(0).getNome());
        assertTrue(sistema.existeAutorComNome("Autor Teste"));
    }

    @Test
    @DisplayName("Não deve cadastrar autor com nome duplicado")
    void naoDeveCadastrarAutorDuplicado() {
        sistema.cadastrarAutor("Autor Duplicado", "Brasileira", LocalDate.of(1990, 1, 1));
        assertThrows(DuplicidadeException.class, () ->
            sistema.cadastrarAutor("Autor Duplicado", "Portuguesa", LocalDate.of(1980, 1, 1)));
        assertEquals(1, sistema.getTodosAutores().size());
    }

    @Test
    @DisplayName("Deve cadastrar uma nova editora com sucesso")
    void deveCadastrarEditoraComSucesso() {
        assertTrue(sistema.cadastrarEditora("Editora Teste"));
        assertEquals(1, sistema.getTodasEditoras().size());
        assertEquals("Editora Teste", sistema.getTodasEditoras().get(0).getNome());
        assertTrue(sistema.existeEditoraComNome("Editora Teste"));
    }

    @Test
    @DisplayName("Não deve cadastrar editora com nome duplicado")
    void naoDeveCadastrarEditoraDuplicada() {
        sistema.cadastrarEditora("Editora Duplicada");
        assertThrows(DuplicidadeException.class, () ->
            sistema.cadastrarEditora("Editora Duplicada"));
        assertEquals(1, sistema.getTodasEditoras().size());
    }

    @Test
    @DisplayName("Deve cadastrar uma nova categoria com sucesso")
    void deveCadastrarCategoriaComSucesso() {
        assertTrue(sistema.cadastrarCategoria("Ficção"));
        assertEquals(1, sistema.getTodasCategorias().size());
        assertEquals("Ficção", sistema.getTodasCategorias().get(0).getNome());
        assertTrue(sistema.existeCategoriaComNome("Ficção"));
    }

    @Test
    @DisplayName("Não deve cadastrar categoria com nome duplicado")
    void naoDeveCadastrarCategoriaDuplicada() {
        sistema.cadastrarCategoria("Ficção");
        assertThrows(DuplicidadeException.class, () ->
            sistema.cadastrarCategoria("Ficção"));
        assertEquals(1, sistema.getTodasCategorias().size());
    }

    @Test
    @DisplayName("Deve cadastrar um novo livro com sucesso")
    void deveCadastrarLivroComSucesso() {
        Editora editora = new Editora("Editora Livro");
        sistema.cadastrarEditora(editora.getNome());
        editora = sistema.getTodasEditoras().get(0);

        Autor autor = new Autor("Autor Livro", "Brasileira", LocalDate.of(1970, 1, 1));
        sistema.cadastrarAutor(autor.getNome(), autor.getNacionalidade(), autor.getDataNascimento());
        autor = sistema.getTodosAutores().get(0);

        Categoria categoria = new Categoria("Aventura");
        sistema.cadastrarCategoria(categoria.getNome());
        categoria = sistema.getTodasCategorias().get(0);

        List<Autor> autores = new ArrayList<>();
        autores.add(autor);

        assertTrue(sistema.cadastrarLivro("Livro Teste", 50.0f, 10, editora, 300, "978-0000000001", autores, categoria));
        assertEquals(1, sistema.getTodosLivros().size());
        assertEquals("Livro Teste", sistema.getTodosLivros().get(0).getTitulo());
    }

    @Test
    @DisplayName("Não deve cadastrar livro com título duplicado")
    void naoDeveCadastrarLivroComTituloDuplicado() {
        Editora editora = new Editora("Editora Teste");
        sistema.cadastrarEditora(editora.getNome());
        editora = sistema.getTodasEditoras().get(0);

        Autor autor = new Autor("Autor Teste", "Brasileira", LocalDate.of(1970, 1, 1));
        sistema.cadastrarAutor(autor.getNome(), autor.getNacionalidade(), autor.getDataNascimento());
        autor = sistema.getTodosAutores().get(0);

        Categoria categoria = new Categoria("Ficção");
        sistema.cadastrarCategoria(categoria.getNome());
        categoria = sistema.getTodasCategorias().get(0);

        List<Autor> autores = new ArrayList<>();
        autores.add(autor);

        sistema.cadastrarLivro("Livro Duplicado", 50.0f, 10, editora, 300, "978-0000000001", autores, categoria);
        assertThrows(DuplicidadeException.class, () ->
            sistema.cadastrarLivro("Livro Duplicado", 60.0f, 20, editora, 350, "978-0000000002", autores, categoria));
        assertEquals(1, sistema.getTodosLivros().size());
    }

    @Test
    @DisplayName("Não deve cadastrar livro com ISBN duplicado")
    void naoDeveCadastrarLivroComIsbnDuplicado() {
        Editora editora = new Editora("Editora Teste");
        sistema.cadastrarEditora(editora.getNome());
        editora = sistema.getTodasEditoras().get(0);

        Autor autor = new Autor("Autor Teste", "Brasileira", LocalDate.of(1970, 1, 1));
        sistema.cadastrarAutor(autor.getNome(), autor.getNacionalidade(), autor.getDataNascimento());
        autor = sistema.getTodosAutores().get(0);

        Categoria categoria = new Categoria("Ficção");
        sistema.cadastrarCategoria(categoria.getNome());
        categoria = sistema.getTodasCategorias().get(0);

        List<Autor> autores = new ArrayList<>();
        autores.add(autor);

        sistema.cadastrarLivro("Livro A", 50.0f, 10, editora, 300, "978-0000000001", autores, categoria);
        assertThrows(DuplicidadeException.class, () ->
            sistema.cadastrarLivro("Livro B", 60.0f, 20, editora, 350, "978-0000000001", autores, categoria));
        assertEquals(1, sistema.getTodosLivros().size());
    }


    @Test
    @DisplayName("Deve cadastrar um novo jornal com sucesso")
    void deveCadastrarJornalComSucesso() {
        Editora editora = new Editora("Editora Jornal");
        sistema.cadastrarEditora(editora.getNome());
        editora = sistema.getTodasEditoras().get(0);

        assertTrue(sistema.cadastrarJornal("Jornal Teste", 5.0f, 50, editora, LocalDate.of(2025, 6, 22)));
        assertEquals(1, sistema.getTodosJornais().size());
        assertEquals("Jornal Teste", sistema.getTodosJornais().get(0).getTitulo());
    }

    @Test
    @DisplayName("Não deve cadastrar jornal com título e data duplicados")
    void naoDeveCadastrarJornalComTituloEDataDuplicados() {
        Editora editora = new Editora("Editora Jornal");
        sistema.cadastrarEditora(editora.getNome());
        editora = sistema.getTodasEditoras().get(0);

        sistema.cadastrarJornal("Jornal Duplicado", 5.0f, 50, editora, LocalDate.of(2025, 6, 22));
        assertThrows(DuplicidadeException.class, () ->
            sistema.cadastrarJornal("Jornal Duplicado", 6.0f, 60, editora, LocalDate.of(2025, 6, 22)));
        assertEquals(1, sistema.getTodosJornais().size());
    }

    @Test
    @DisplayName("Deve editar um livro existente com sucesso")
    void deveEditarLivroComSucesso() {
        Editora editora = new Editora("Editora Original");
        sistema.cadastrarEditora(editora.getNome());
        editora = sistema.getTodasEditoras().get(0);

        Autor autor = new Autor("Autor Original", "Brasileira", LocalDate.of(1970, 1, 1));
        sistema.cadastrarAutor(autor.getNome(), autor.getNacionalidade(), autor.getDataNascimento());
        autor = sistema.getTodosAutores().get(0);

        Categoria categoria = new Categoria("Ficção");
        sistema.cadastrarCategoria(categoria.getNome());
        categoria = sistema.getTodasCategorias().get(0);

        List<Autor> autores = new ArrayList<>();
        autores.add(autor);

        sistema.cadastrarLivro("Livro Antigo", 50.0f, 10, editora, 300, "978-0000000001", autores, categoria);
        Livro livroEditado = sistema.getTodosLivros().get(0);

        Editora novaEditora = new Editora("Editora Nova");
        sistema.cadastrarEditora(novaEditora.getNome());
        novaEditora = sistema.getTodasEditoras().get(1);

        Autor novoAutor = new Autor("Autor Novo", "Italiana", LocalDate.of(1980, 2, 2));
        sistema.cadastrarAutor(novoAutor.getNome(), novoAutor.getNacionalidade(), novoAutor.getDataNascimento());
        novoAutor = sistema.getTodosAutores().get(1);

        Categoria novaCategoria = new Categoria("Fantasia");
        sistema.cadastrarCategoria(novaCategoria.getNome());
        novaCategoria = sistema.getTodasCategorias().get(1);


        List<Autor> novosAutores = new ArrayList<>();
        novosAutores.add(novoAutor);

        assertTrue(sistema.editarLivro(livroEditado.getId(), "Livro Novo", 75.0f, 20, novaEditora, 400, "978-0000000009", novosAutores, novaCategoria));

        assertEquals("Livro Novo", livroEditado.getTitulo());
        assertEquals(75.0f, livroEditado.getPreco(), 0.001);
        assertEquals(20, livroEditado.getEstoqueDisponivel());
        assertEquals(novaEditora, livroEditado.getEditora());
        assertEquals(400, livroEditado.getQuantidadePaginas());
        assertEquals("978-0000000009", livroEditado.getIsbn());
        assertEquals(1, livroEditado.getAutores().size());
        assertTrue(livroEditado.getAutores().contains(novoAutor));
        assertEquals(novaCategoria, livroEditado.getCategoria());
    }

    @Test
    @DisplayName("Não deve editar livro para título/ISBN duplicado de outro livro")
    void naoDeveEditarLivroParaTituloIsbnDuplicado() {
        Editora editora = new Editora("Editora Teste");
        sistema.cadastrarEditora(editora.getNome());
        editora = sistema.getTodasEditoras().get(0);

        Autor autor = new Autor("Autor Teste", "Brasileira", LocalDate.of(1970, 1, 1));
        sistema.cadastrarAutor(autor.getNome(), autor.getNacionalidade(), autor.getDataNascimento());
        autor = sistema.getTodosAutores().get(0);

        Categoria categoria = new Categoria("Ficção");
        sistema.cadastrarCategoria(categoria.getNome());
        categoria = sistema.getTodasCategorias().get(0);

        List<Autor> autores = new ArrayList<>();
        autores.add(autor);

        sistema.cadastrarLivro("Livro A", 50.0f, 10, editora, 300, "978-LIVROA", autores, categoria);
        sistema.cadastrarLivro("Livro B", 60.0f, 20, editora, 350, "978-LIVROB", autores, categoria);

        Livro livroA = sistema.buscarLivroPorId(1);
        Livro livroB = sistema.buscarLivroPorId(2);

        assertThrows(DuplicidadeException.class, () ->
            sistema.editarLivro(livroB.getId(), "Livro A", 70.0f, 25, editora, 380, "978-NOVOISBN", autores, categoria));

        assertThrows(DuplicidadeException.class, () ->
            sistema.editarLivro(livroB.getId(), "Livro Novo Título", 70.0f, 25, editora, 380, "978-LIVROA", autores, categoria));
    }


    @Test
    @DisplayName("Deve editar um jornal existente com sucesso")
    void deveEditarJornalComSucesso() {
        Editora editora = new Editora("Editora Original");
        sistema.cadastrarEditora(editora.getNome());
        editora = sistema.getTodasEditoras().get(0);

        sistema.cadastrarJornal("Jornal Antigo", 5.0f, 50, editora, LocalDate.of(2025, 1, 1));
        Jornal jornalEditado = sistema.getTodosJornais().get(0);

        Editora novaEditora = new Editora("Editora Nova");
        sistema.cadastrarEditora(novaEditora.getNome());
        novaEditora = sistema.getTodasEditoras().get(1);

        assertTrue(sistema.editarJornal(jornalEditado.getId(), "Jornal Novo", 6.0f, 60, novaEditora, LocalDate.of(2025, 2, 1)));

        assertEquals("Jornal Novo", jornalEditado.getTitulo());
        assertEquals(6.0f, jornalEditado.getPreco(), 0.001);
        assertEquals(60, jornalEditado.getEstoqueDisponivel());
        assertEquals(novaEditora, jornalEditado.getEditora());
        assertEquals(LocalDate.of(2025, 2, 1), jornalEditado.getDataPublicacao());
    }

    @Test
    @DisplayName("Não deve editar jornal para título/data duplicado de outro jornal")
    void naoDeveEditarJornalParaTituloDataDuplicado() {
        Editora editora = new Editora("Editora Teste");
        sistema.cadastrarEditora(editora.getNome());
        editora = sistema.getTodasEditoras().get(0);

        sistema.cadastrarJornal("Jornal X", 5.0f, 10, editora, LocalDate.of(2025, 1, 1));
        sistema.cadastrarJornal("Jornal Y", 6.0f, 20, editora, LocalDate.of(2025, 1, 2));

        Jornal jornalY = sistema.buscarJornalPorId(2);

        assertThrows(DuplicidadeException.class, () ->
            sistema.editarJornal(jornalY.getId(), "Jornal X", 7.0f, 25, editora, LocalDate.of(2025, 1, 1)));
    }

    @Test
    @DisplayName("Deve excluir um livro com sucesso")
    void deveExcluirLivroComSucesso() {
        Editora editora = new Editora("E1");
        sistema.cadastrarEditora(editora.getNome());
        editora = sistema.getTodasEditoras().get(0);
        sistema.cadastrarLivro("Livro Excluir", 10.0f, 5, editora, 100, "978-EXCLUIR", new ArrayList<>(), null);
        Livro livroParaExcluir = sistema.getTodosLivros().get(0);

        assertTrue(sistema.excluirLivro(livroParaExcluir.getId()));
        assertTrue(sistema.getTodosLivros().isEmpty());
    }

    @Test
    @DisplayName("Não deve excluir livro inexistente")
    void naoDeveExcluirLivroInexistente() {
        assertFalse(sistema.excluirLivro(999));
    }

    @Test
    @DisplayName("Deve excluir um jornal com sucesso")
    void deveExcluirJornalComSucesso() {
        Editora editora = new Editora("E2");
        sistema.cadastrarEditora(editora.getNome());
        editora = sistema.getTodasEditoras().get(0);
        sistema.cadastrarJornal("Jornal Excluir", 2.0f, 10, editora, LocalDate.of(2025, 7, 1));
        Jornal jornalParaExcluir = sistema.getTodosJornais().get(0);

        assertTrue(sistema.excluirJornal(jornalParaExcluir.getId()));
        assertTrue(sistema.getTodosJornais().isEmpty());
    }

    @Test
    @DisplayName("Não deve excluir jornal inexistente")
    void naoDeveExcluirJornalInexistente() {
        assertFalse(sistema.excluirJornal(999));
    }

    @Test
    @DisplayName("Deve buscar livro por ID")
    void deveBuscarLivroPorId() {
        Editora editora = new Editora("E3");
        sistema.cadastrarEditora(editora.getNome());
        editora = sistema.getTodasEditoras().get(0);
        sistema.cadastrarLivro("Livro Buscavel", 20.0f, 15, editora, 200, "978-BUSCAID", new ArrayList<>(), null);
        Livro livroEsperado = sistema.getTodosLivros().get(0);

        Livro livroEncontrado = sistema.buscarLivroPorId(livroEsperado.getId());
        assertNotNull(livroEncontrado);
        assertEquals(livroEsperado.getTitulo(), livroEncontrado.getTitulo());
    }

    @Test
    @DisplayName("Deve retornar null ao buscar livro por ID inexistente")
    void deveRetornarNullAoBuscarLivroPorIdInexistente() {
        assertNull(sistema.buscarLivroPorId(999));
    }

    @Test
    @DisplayName("Deve buscar jornais por título")
    void deveBuscarJornaisPorTitulo() {
        Editora editora = new Editora("E4");
        sistema.cadastrarEditora(editora.getNome());
        editora = sistema.getTodasEditoras().get(0);
        sistema.cadastrarJornal("Manchete Teste", 3.0f, 20, editora, LocalDate.of(2025, 8, 1));
        sistema.cadastrarJornal("Outra Manchete", 3.0f, 20, editora, LocalDate.of(2025, 8, 2));

        List<Jornal> resultados = sistema.buscarJornaisPorTitulo("Manchete");
        assertEquals(2, resultados.size());
        assertTrue(resultados.stream().anyMatch(j -> j.getTitulo().equals("Manchete Teste")));
        assertTrue(resultados.stream().anyMatch(j -> j.getTitulo().equals("Outra Manchete")));
    }

    @Test
    @DisplayName("Deve persistir e carregar dados corretamente", timeout = 5000) // Timeout que vai evitar loop infinito em caso de erro
    void devePersistirECarregarDadosCorretamente(@TempDir Path tempDir) {

        // Simula o salvamento
        Editora editora = new Editora("Editora Persist");
        sistema.cadastrarEditora(editora.getNome());
        editora = sistema.getTodasEditoras().get(0); // Obtém a instância gerenciada pelo sistema

        Autor autor = new Autor("Autor Persist", "Brasileira", LocalDate.of(1990, 1, 1));
        sistema.cadastrarAutor(autor.getNome(), autor.getNacionalidade(), autor.getDataNascimento());
        autor = sistema.getTodosAutores().get(0);

        Categoria categoria = new Categoria("Terror");
        sistema.cadastrarCategoria(categoria.getNome());
        categoria = sistema.getTodasCategorias().get(0);

        List<Autor> autores = new ArrayList<>();
        autores.add(autor);

        sistema.cadastrarLivro("Livro Persistido", 50.0f, 10, editora, 300, "978-PERSIST", autores, categoria);
        sistema.cadastrarJornal("Jornal Persistido", 5.0f, 50, editora, LocalDate.of(2025, 9, 1));

        sistema.salvarTodosDados(); 

        Sistema novoSistema = new Sistema();

        assertEquals(1, novoSistema.getTodasEditoras().size());
        assertEquals("Editora Persist", novoSistema.getTodasEditoras().get(0).getNome());
        assertEquals(1, novoSistema.getTodosAutores().size());
        assertEquals("Autor Persist", novoSistema.getTodosAutores().get(0).getNome());
        assertEquals(1, novoSistema.getTodosLivros().size());
        assertEquals("Livro Persistido", novoSistema.getTodosLivros().get(0).getTitulo());
        assertEquals(1, novoSistema.getTodosJornais().size());
        assertEquals("Jornal Persistido", novoSistema.getTodosJornais().get(0).getTitulo());
        Livro livroCarregado = novoSistema.getTodosLivros().get(0);
        Jornal jornalCarregado = novoSistema.getTodosJornais().get(0);
    }
}