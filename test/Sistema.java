package test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class Sistema {
    private List<Livro> livros;
    private List<Jornal> jornais;
    private List<Autor> autores;
    private List<Editora> editoras;
    private List<Categoria> categorias;

    private final String ARQUIVO_LIVROS = "livros.dat";
    private final String ARQUIVO_JORNAIS = "jornais.dat";
    private final String ARQUIVO_AUTORES = "autores.dat";
    private final String ARQUIVO_EDITORAS = "editoras.dat";
    private final String ARQUIVO_CATEGORIAS = "categorias.dat";

    public Sistema() {
        carregarTodosDados();
        inicializarProximosIds();
    }

    private void carregarTodosDados() {
        livros = Util.carregarDados(ARQUIVO_LIVROS);
        jornais = Util.carregarDados(ARQUIVO_JORNAIS);
        autores = Util.carregarDados(ARQUIVO_AUTORES);
        editoras = Util.carregarDados(ARQUIVO_EDITORAS);
        categorias = Util.carregarDados(ARQUIVO_CATEGORIAS);
    }

    private void inicializarProximosIds() {
        int maiorIdPublicacao = 0;
        if (!livros.isEmpty()) {
            maiorIdPublicacao = Math.max(maiorIdPublicacao, livros.stream().mapToInt(Publicacao::getId).max().orElse(0));
        }
        if (!jornais.isEmpty()) {
            maiorIdPublicacao = Math.max(maiorIdPublicacao, jornais.stream().mapToInt(Publicacao::getId).max().orElse(0));
        }
        Publicacao.setProximoIdEstatico(maiorIdPublicacao + 1);

        int maiorIdAutor = autores.stream().mapToInt(Autor::getId).max().orElse(0);
        Autor.setProximoIdEstatico(maiorIdAutor + 1);

        int maiorIdEditora = editoras.stream().mapToInt(Editora::getId).max().orElse(0);
        Editora.setProximoIdEstatico(maiorIdEditora + 1);

        int maiorIdCategoria = categorias.stream().mapToInt(Categoria::getId).max().orElse(0);
        Categoria.setProximoIdEstatico(maiorIdCategoria + 1);
    }

    public void salvarTodosDados() {
        Util.salvarDados(livros, ARQUIVO_LIVROS);
        Util.salvarDados(jornais, ARQUIVO_JORNAIS);
        Util.salvarDados(autores, ARQUIVO_AUTORES);
        Util.salvarDados(editoras, ARQUIVO_EDITORAS);
        Util.salvarDados(categorias, ARQUIVO_CATEGORIAS);
    }

    //Métodos de Verificação de Existência (PARA VALIDAÇÃO DE DUPLICIDADE)
    public boolean existeAutorComNome(String nome) {
        return autores.stream().anyMatch(a -> a.getNome().equalsIgnoreCase(nome));
    }

    public boolean existeEditoraComNome(String nome) {
        return editoras.stream().anyMatch(e -> e.getNome().equalsIgnoreCase(nome));
    }

    public boolean existeCategoriaComNome(String nome) {
        return categorias.stream().anyMatch(c -> c.getNome().equalsIgnoreCase(nome));
    }

    public boolean existeLivroComTitulo(String titulo) {
        return livros.stream().anyMatch(l -> l.getTitulo().equalsIgnoreCase(titulo));
    }

    public boolean existeLivroComIsbn(String isbn) {
        return livros.stream().anyMatch(l -> l.getIsbn().equalsIgnoreCase(isbn));
    }

    public boolean existeLivroComTituloEIsbnExcluindoId(String titulo, String isbn, int idExcluir) {
        return livros.stream()
                     .anyMatch(l -> (l.getTitulo().equalsIgnoreCase(titulo) || l.getIsbn().equalsIgnoreCase(isbn)) && l.getId() != idExcluir);
    }

    public boolean existeJornalComTituloEData(String titulo, LocalDate data) {
        return jornais.stream().anyMatch(j -> j.getTitulo().equalsIgnoreCase(titulo) && j.getDataPublicacao().equals(data));
    }

    public boolean existeJornalComTituloEDataExcluindoId(String titulo, LocalDate data, int idExcluir) {
        return jornais.stream()
                     .anyMatch(j -> j.getTitulo().equalsIgnoreCase(titulo) && j.getDataPublicacao().equals(data) && j.getId() != idExcluir);
    }

    //Métodos de Cadastro (R.F._1, R.F._6, R.F._7, R.F._8)
    public boolean cadastrarLivro(String titulo, float preco, int estoque, Editora editora, int paginas, String isbn, List<Autor> autoresAssociar, Categoria categoriaAssociar) {
        if (existeLivroComTitulo(titulo)) {
            throw new DuplicidadeException("Livro com o título '" + titulo + "' já existe.");
        }
        if (existeLivroComIsbn(isbn)) {
            throw new DuplicidadeException("Livro com o ISBN '" + isbn + "' já existe.");
        }
        Livro novoLivro = new Livro(titulo, preco, estoque, editora, paginas, isbn);
        if (autoresAssociar != null) {
            for (Autor autor : autoresAssociar) {
                novoLivro.addAutor(autor);
                // Opcional: autor.adicionarLivro(novoLivro); // Bidirecional
            }
        }
        novoLivro.setCategoria(categoriaAssociar);
        boolean adicionado = livros.add(novoLivro);
        if (adicionado) salvarTodosDados();
        return adicionado;
    }

    public boolean cadastrarJornal(String titulo, float preco, int estoque, Editora editora, LocalDate dataPublicacao) {
        if (existeJornalComTituloEData(titulo, dataPublicacao)) {
            throw new DuplicidadeException("Jornal com o título '" + titulo + "' e data '" + dataPublicacao + "' já existe.");
        }
        Jornal novoJornal = new Jornal(titulo, preco, estoque, editora, dataPublicacao);
        boolean adicionado = jornais.add(novoJornal);
        if (adicionado) salvarTodosDados();
        return adicionado;
    }

    public boolean cadastrarAutor(String nome, String nacionalidade, LocalDate dataNascimento) {
        if (existeAutorComNome(nome)) {
            throw new DuplicidadeException("Autor com o nome '" + nome + "' já existe.");
        }
        Autor novoAutor = new Autor(nome, nacionalidade, dataNascimento);
        boolean adicionado = autores.add(novoAutor);
        if (adicionado) salvarTodosDados();
        return adicionado;
    }

    public boolean cadastrarEditora(String nome) {
        if (existeEditoraComNome(nome)) {
            throw new DuplicidadeException("Editora com o nome '" + nome + "' já existe.");
        }
        Editora novaEditora = new Editora(nome);
        boolean adicionado = editoras.add(novaEditora);
        if (adicionado) salvarTodosDados();
        return adicionado;
    }

    public boolean cadastrarCategoria(String nome) {
        if (existeCategoriaComNome(nome)) {
            throw new DuplicidadeException("Categoria com o nome '" + nome + "' já existe.");
        }
        Categoria novaCategoria = new Categoria(nome);
        boolean adicionado = categorias.add(novaCategoria);
        if (adicionado) salvarTodosDados();
        return adicionado;
    }

    //Métodos de Edição (R.F._2)
    public boolean editarLivro(int idLivro, String novoTitulo, float novoPreco, int novoEstoque, Editora novaEditora, int novaPaginas, String novoIsbn, List<Autor> novosAutores, Categoria novaCategoria) {
        Livro livro = buscarLivroPorId(idLivro);
        if (livro != null) {
            if (existeLivroComTituloEIsbnExcluindoId(novoTitulo, novoIsbn, livro.getId())) {
                throw new DuplicidadeException("O título ou ISBN '" + novoTitulo + "' / '" + novoIsbn + "' já pertence a outro livro.");
            }

            livro.setTitulo(novoTitulo);
            livro.setPreco(novoPreco);
            livro.setEstoqueDisponivel(novoEstoque);
            livro.setEditora(novaEditora);
            livro.setQuantidadePaginas(novaPaginas);
            livro.setIsbn(novoIsbn);
            livro.getAutores().clear();
            if (novosAutores != null) {
                for (Autor autor : novosAutores) {
                    livro.addAutor(autor);
                }
            }
            livro.setCategoria(novaCategoria);
            salvarTodosDados();
            return true;
        }
        return false;
    }

    public boolean editarJornal(int idJornal, String novoTitulo, float novoPreco, int novoEstoque, Editora novaEditora, LocalDate novaDataPublicacao) {
        Jornal jornal = buscarJornalPorId(idJornal);
        if (jornal != null) {
            if (existeJornalComTituloEDataExcluindoId(novoTitulo, novaDataPublicacao, jornal.getId())) {
                throw new DuplicidadeException("Jornal com o título '" + novoTitulo + "' e data '" + novaDataPublicacao + "' já existe.");
            }

            jornal.setTitulo(novoTitulo);
            jornal.setPreco(novoPreco);
            jornal.setEstoqueDisponivel(novoEstoque);
            jornal.setEditora(novaEditora);
            jornal.setDataPublicacao(novaDataPublicacao);
            salvarTodosDados();
            return true;
        }
        return false;
    }

    //Métodos de Exclusão (R.F._3)
    public boolean excluirLivro(int idLivro) {
        Livro livroParaRemover = buscarLivroPorId(idLivro);
        if (livroParaRemover != null) {
            boolean removido = livros.remove(livroParaRemover);
            if (removido) salvarTodosDados();
            return removido;
        }
        return false;
    }

    public boolean excluirJornal(int idJornal) {
        Jornal jornalParaRemover = buscarJornalPorId(idJornal);
        if (jornalParaRemover != null) {
            boolean removido = jornais.remove(jornalParaRemover);
            if (removido) salvarTodosDados();
            return removido;
        }
        return false;
    }

    //Métodos de Visualização (R.F._4)
    public List<Livro> getTodosLivros() {
        return new ArrayList<>(livros);
    }

    public List<Jornal> getTodosJornais() {
        return new ArrayList<>(jornais);
    }

    public List<Autor> getTodosAutores() {
        return new ArrayList<>(autores);
    }

    public List<Editora> getTodasEditoras() {
        return new ArrayList<>(editoras);
    }

    public List<Categoria> getTodasCategorias() {
        return new ArrayList<>(categorias);
    }

    //Métodos de Busca (R.F._5)
    public Livro buscarLivroPorId(int id) {
        return livros.stream().filter(l -> l.getId() == id).findFirst().orElse(null);
    }

    public Jornal buscarJornalPorId(int id) {
        return jornais.stream().filter(j -> j.getId() == id).findFirst().orElse(null);
    }

    public Autor buscarAutorPorId(int id) {
        return autores.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
    }

    public Editora buscarEditoraPorId(int id) {
        return editoras.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    public Categoria buscarCategoriaPorId(int id) {
        return categorias.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public List<Livro> buscarLivrosPorTitulo(String titulo) {
        return livros.stream()
                .filter(l -> l.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Livro> buscarLivrosPorAutor(String nomeAutor) {
        return livros.stream()
                .filter(l -> l.getAutores().stream()
                        .anyMatch(a -> a.getNome().toLowerCase().contains(nomeAutor.toLowerCase())))
                .collect(Collectors.toList());
    }

    public List<Livro> buscarLivrosPorCategoria(String nomeCategoria) {
        return livros.stream()
                .filter(l -> l.getCategoria() != null && l.getCategoria().getNome().toLowerCase().contains(nomeCategoria.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Jornal> buscarJornaisPorTitulo(String titulo) {
        return jornais.stream()
                .filter(j -> j.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Jornal> buscarJornaisPorData(LocalDate data) {
        return jornais.stream()
                .filter(j -> j.getDataPublicacao().equals(data))
                .collect(Collectors.toList());
    }
}