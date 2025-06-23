package src;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

/**
 * Classe principal do sistema de gerenciamento da Estação Literária.
 * Responsável por gerenciar todas as coleções de dados (livros, jornais, autores, editoras, categorias),
 * realizar as operações de negócio (CRUD - Inclusão, Exclusão, Alteração, Consulta, Lista),
 * gerenciar a persistência em arquivos
 * e inicializar IDs sequenciais após o carregamento de dados.
 */
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

    private static final Pattern ISBN_PATTERN = Pattern.compile("^\\d{3}-\\d{2}-\\d{3}-\\d{4}-\\d{1}$");

    /**
     * Construtor da classe Sistema.
     * Ao ser instanciado, ele tenta carregar todos os dados persistidos dos arquivos
     * e inicializa os geradores de ID estáticos para garantir a continuidade correta da numeração.
     */
    public Sistema(){
        carregarTodosDados();
        inicializarProximosIds();
    }

    /**
     * Carrega todos os dados das entidades (Livros, Jornais, Autores, Editoras, Categorias)
     * a partir de seus respectivos arquivos persistidos, utilizando a classe utilitária {@link Util}.
     */
    private void carregarTodosDados() {
        livros = Util.carregarDados(ARQUIVO_LIVROS);
        jornais = Util.carregarDados(ARQUIVO_JORNAIS);
        autores = Util.carregarDados(ARQUIVO_AUTORES);
        editoras = Util.carregarDados(ARQUIVO_EDITORAS);
        categorias = Util.carregarDados(ARQUIVO_CATEGORIAS);
    }

    /**
     * Inicializa os contadores estáticos de ID para cada tipo de entidade
     * (`Publicacao`, `Autor`, `Editora`, `Categoria`) com base no maior ID existente
     * nos dados que foram carregados. Isso é crucial para evitar a duplicação de IDs
     * ao adicionar novos itens após o reinício da aplicação.
     */
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

    /**
     * Salva todos os dados das entidades (Livros, Jornais, Autores, Editoras, Categorias)
     * de volta em seus respectivos arquivos persistidos, utilizando a classe utilitária {@link Util}.
     */
    public void salvarTodosDados() {
        Util.salvarDados(livros, ARQUIVO_LIVROS);
        Util.salvarDados(jornais, ARQUIVO_JORNAIS);
        Util.salvarDados(autores, ARQUIVO_AUTORES);
        Util.salvarDados(editoras, ARQUIVO_EDITORAS);
        Util.salvarDados(categorias, ARQUIVO_CATEGORIAS);
    }

    // Métodos de Verificação de Existência (Para validação de duplicidade)

    /**
     * Verifica se já existe um autor cadastrado com o nome fornecido (a comparação não diferencia maiúsculas de minúsculas).
     * @param nome O nome do autor a ser verificado.
     * @return {@code true} se um autor com o nome já existe, {@code false} caso contrário.
     */
    public boolean existeAutorComNome(String nome) {
        return autores.stream().anyMatch(a -> a.getNome().equalsIgnoreCase(nome));
    }

    /**
     * Verifica se já existe uma editora cadastrada com o nome fornecido (a comparação não diferencia maiúsculas de minúsculas).
     * @param nome O nome da editora a ser verificado.
     * @return {@code true} se uma editora com o nome já existe, {@code false} caso contrário.
     */
    public boolean existeEditoraComNome(String nome) {
        return editoras.stream().anyMatch(e -> e.getNome().equalsIgnoreCase(nome));
    }

    /**
     * Verifica se já existe uma categoria cadastrada com o nome fornecido (a comparação não diferencia maiúsculas de minúsculas).
     * @param nome O nome da categoria a ser verificado.
     * @return {@code true} se uma categoria com o nome já existe, {@code false} caso contrário.
     */
    public boolean existeCategoriaComNome(String nome) {
        return categorias.stream().anyMatch(c -> c.getNome().equalsIgnoreCase(nome));
    }

    /**
     * Verifica se já existe um livro cadastrado com o título fornecido (a comparação não diferencia maiúsculas de minúsculas).
     * @param titulo O título do livro a ser verificado.
     * @return {@code true} se um livro com o título já existe, {@code false} caso contrário.
     */
    public boolean existeLivroComTitulo(String titulo) {
        return livros.stream().anyMatch(l -> l.getTitulo().equalsIgnoreCase(titulo));
    }

    /**
     * Verifica se já existe um livro cadastrado com o ISBN fornecido (a comparação não diferencia maiúsculas de minúsculas).
     * @param isbn O ISBN do livro a ser verificado.
     * @return {@code true} se um livro com o ISBN já existe, {@code false} caso contrário.
     */
    public boolean existeLivroComIsbn(String isbn) {
        return livros.stream().anyMatch(l -> l.getIsbn().equalsIgnoreCase(isbn));
    }

    /**
     * Verifica se já existe um livro com um título ou ISBN duplicado,
     * excluindo da verificação o livro cujo ID é passado como parâmetro.
     * Isso é útil durante a edição, para que o próprio livro que está sendo editado não seja considerado uma duplicidade.
     * @param titulo O título a ser verificado.
     * @param isbn O ISBN a ser verificado.
     * @param idExcluir O ID do livro a ser excluído da verificação de duplicidade.
     * @return {@code true} se houver um livro com o mesmo título ou ISBN (excluindo o ID informado), {@code false} caso contrário.
     */
    public boolean existeLivroComTituloEIsbnExcluindoId(String titulo, String isbn, int idExcluir) {
        return livros.stream()
                     .anyMatch(l -> (l.getTitulo().equalsIgnoreCase(titulo) || l.getIsbn().equalsIgnoreCase(isbn)) && l.getId() != idExcluir);
    }

    /**
     * Verifica se já existe um jornal cadastrado com o título e a data de publicação fornecidos
     * (a comparação do título não diferencia maiúsculas de minúsculas).
     * @param titulo O título do jornal a ser verificado.
     * @param data A data de publicação do jornal a ser verificada.
     * @return {@code true} se um jornal com o título e a data já existe, {@code false} caso contrário.
     */
    public boolean existeJornalComTituloEData(String titulo, LocalDate data) {
        return jornais.stream().anyMatch(j -> j.getTitulo().equalsIgnoreCase(titulo) && j.getDataPublicacao().equals(data));
    }

    /**
     * Verifica se já existe um jornal com um título e data de publicação duplicados,
     * excluindo da verificação o jornal cujo ID é passado como parâmetro.
     * Isso é útil durante a edição.
     * @param titulo O título a ser verificado.
     * @param data A data de publicação a ser verificada.
     * @param idExcluir O ID do jornal a ser excluído da verificação de duplicidade.
     * @return {@code true} se houver um jornal com o mesmo título e data (excluindo o ID informado), {@code false} caso contrário.
     */
    public boolean existeJornalComTituloEDataExcluindoId(String titulo, LocalDate data, int idExcluir) {
        return jornais.stream()
                     .anyMatch(j -> j.getTitulo().equalsIgnoreCase(titulo) && j.getDataPublicacao().equals(data) && j.getId() != idExcluir);
    }

    // Métodos de Cadastro (R.F._1, R.F._6, R.F._7, R.F._8)

    /**
     * Cadastra um novo livro no sistema.
     * Antes do cadastro, verifica se já existe um livro com o mesmo título ou ISBN.
     * Lança uma {@link DuplicidadeException} se alguma duplicidade for encontrada.
     *
     * @param titulo O título do livro.
     * @param preco O preço do livro.
     * @param estoque A quantidade inicial em estoque.
     * @param editora A {@link Editora} do livro.
     * @param paginas A quantidade de páginas do livro.
     * @param isbn O ISBN do livro.
     * @param autoresAssociar Uma lista de {@link Autor}es a serem associados ao livro.
     * @param categoriaAssociar A {@link Categoria} a ser associada ao livro.
     * @return {@code true} se o livro foi cadastrado com sucesso, {@code false} caso contrário.
     * @throws DuplicidadeException Se já existir um livro com o mesmo título ou ISBN.
     */
    public boolean cadastrarLivro(String titulo, float preco, int estoque, Editora editora, int paginas, String isbn, List<Autor> autoresAssociar, Categoria categoriaAssociar) {
        if (preco < 15.00) {
            throw new DuplicidadeException("Preço do livro deve ser no mínimo R$ 15,00.");
        }
        if (estoque < 0) {
            throw new DuplicidadeException("Estoque do livro não pode ser negativo.");
        }
        if (paginas < 10) {
            throw new DuplicidadeException("Quantidade de páginas do livro deve ser no mínimo 10.");
        }
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new DuplicidadeException("ISBN do livro não pode ser vazio.");
        }
        if (!ISBN_PATTERN.matcher(isbn.trim()).matches()) {
            throw new DuplicidadeException("Formato de ISBN inválido. Use o padrão XXX-XX-XXX-XXXX-X.");
        }
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
            }
        }
        novoLivro.setCategoria(categoriaAssociar);
        boolean adicionado = livros.add(novoLivro);
        if (adicionado) salvarTodosDados(); // Persiste os dados após o cadastro.
        return adicionado;
    }

    /**
     * Cadastra um novo jornal no sistema.
     * Antes do cadastro, verifica se já existe um jornal com o mesmo título e data de publicação.
     * Lança uma {@link DuplicidadeException} se uma duplicidade for encontrada.
     *
     * @param titulo O título do jornal.
     * @param preco O preço do jornal.
     * @param estoque A quantidade inicial em estoque.
     * @param editora A {@link Editora} do jornal.
     * @param dataPublicacao A data de publicação do jornal.
     * @return {@code true} se o jornal foi cadastrado com sucesso, {@code false} caso contrário.
     * @throws DuplicidadeException Se já existir um jornal com o mesmo título e data de publicação.
     */
    public boolean cadastrarJornal(String titulo, float preco, int estoque, Editora editora, LocalDate dataPublicacao) {
        if (preco < 3.00) {
            throw new DuplicidadeException("Preço do jornal deve ser no mínimo R$ 3,00.");
        }
        if (estoque < 0) {
            throw new DuplicidadeException("Estoque do jornal não pode ser negativo.");
        }
        if (preco <= 0) {
            throw new DuplicidadeException("Preço do jornal deve ser maior que zero.");
        }
        if (estoque < 0) {
            throw new DuplicidadeException("Estoque do jornal não pode ser negativo.");
        }
        if (existeJornalComTituloEData(titulo, dataPublicacao)) {
            throw new DuplicidadeException("Jornal com o título '" + titulo + "' e data '" + dataPublicacao + "' já existe.");
        }
        Jornal novoJornal = new Jornal(titulo, preco, estoque, editora, dataPublicacao);
        boolean adicionado = jornais.add(novoJornal);
        if (adicionado) salvarTodosDados(); // Persiste os dados após o cadastro.
        return adicionado;
    }

    /**
     * Cadastra um novo autor no sistema.
     * Antes do cadastro, verifica se já existe um autor com o mesmo nome.
     * Lança uma {@link DuplicidadeException} se uma duplicidade for encontrada.
     *
     * @param nome O nome do autor.
     * @param nacionalidade A nacionalidade do autor.
     * @param dataNascimento A data de nascimento do autor.
     * @return {@code true} se o autor foi cadastrado com sucesso, {@code false} caso contrário.
     * @throws DuplicidadeException Se já existir um autor com o mesmo nome.
     */
    public boolean cadastrarAutor(String nome, String nacionalidade, LocalDate dataNascimento) {
        if (existeAutorComNome(nome)) {
            throw new DuplicidadeException("Autor com o nome '" + nome + "' já existe.");
        }
        Autor novoAutor = new Autor(nome, nacionalidade, dataNascimento);
        boolean adicionado = autores.add(novoAutor);
        if (adicionado) salvarTodosDados(); // Persiste os dados após o cadastro.
        return adicionado;
    }

    /**
     * Cadastra uma nova editora no sistema.
     * Antes do cadastro, verifica se já existe uma editora com o mesmo nome.
     * Lança uma {@link DuplicidadeException} se uma duplicidade for encontrada.
     *
     * @param nome O nome da editora.
     * @return {@code true} se a editora foi cadastrada com sucesso, {@code false} caso contrário.
     * @throws DuplicidadeException Se já existir uma editora com o mesmo nome.
     */
    public boolean cadastrarEditora(String nome) {
        if (existeEditoraComNome(nome)) {
            throw new DuplicidadeException("Editora com o nome '" + nome + "' já existe.");
        }
        Editora novaEditora = new Editora(nome);
        boolean adicionado = editoras.add(novaEditora);
        if (adicionado) salvarTodosDados(); // Persiste os dados após o cadastro.
        return adicionado;
    }

    /**
     * Cadastra uma nova categoria no sistema.
     * Antes do cadastro, verifica se já existe uma categoria com o mesmo nome.
     * Lança uma {@link DuplicidadeException} se uma duplicidade for encontrada.
     *
     * @param nome O nome da categoria.
     * @return {@code true} se a categoria foi cadastrada com sucesso, {@code false} caso contrário.
     * @throws DuplicidadeException Se já existir uma categoria com o mesmo nome.
     */
    public boolean cadastrarCategoria(String nome) {
        if (existeCategoriaComNome(nome)) {
            throw new DuplicidadeException("Categoria com o nome '" + nome + "' já existe.");
        }
        Categoria novaCategoria = new Categoria(nome);
        boolean adicionado = categorias.add(novaCategoria);
        if (adicionado) salvarTodosDados(); // Persiste os dados após o cadastro.
        return adicionado;
    }

    // Métodos de Edição (R.F._2)

    /**
     * Edita os dados de um livro existente no sistema.
     * Verifica se o novo título ou ISBN não são duplicados de *outros* livros (excluindo o próprio livro em edição).
     * Lança uma {@link DuplicidadeException} se a edição resultar em um título ou ISBN já existente em outro livro.
     *
     * @param idLivro O ID do livro a ser editado.
     * @param novoTitulo O novo título do livro.
     * @param novoPreco O novo preço do livro.
     * @param novoEstoque O novo estoque disponível do livro.
     * @param novaEditora A nova {@link Editora} do livro.
     * @param novaPaginas A nova quantidade de páginas do livro.
     * @param novoIsbn O novo ISBN do livro.
     * @param novosAutores A nova lista de {@link Autor}es do livro.
     * @param novaCategoria A nova {@link Categoria} do livro.
     * @return {@code true} se o livro foi editado com sucesso, {@code false} caso não seja encontrado ou falhe na edição.
     * @throws DuplicidadeException Se o novo título ou ISBN já pertencer a outro livro.
     */
    public boolean editarLivro(int idLivro, String novoTitulo, float novoPreco, int novoEstoque, Editora novaEditora, int novaPaginas, String novoIsbn, List<Autor> novosAutores, Categoria novaCategoria) {
        Livro livro = buscarLivroPorId(idLivro);
        if (livro != null) {
            if (novoPreco < 15.00) {
            throw new DuplicidadeException("Preço do livro deve ser no mínimo R$ 15,00.");
            }
            if (novoEstoque < 0) {
                throw new DuplicidadeException("Estoque do livro não pode ser negativo.");
            }
            if (novaPaginas < 10) {
                throw new DuplicidadeException("Quantidade de páginas do livro deve ser no mínimo 10.");
            }
            if (novoIsbn == null || novoIsbn.trim().isEmpty()) {
            throw new DuplicidadeException("ISBN do livro não pode ser vazio.");
            }
            if (!ISBN_PATTERN.matcher(novoIsbn.trim()).matches()) {
                throw new DuplicidadeException("Formato de ISBN inválido. Use o padrão XXX-XX-XXX-XXXX-X.");
            }
            if (existeLivroComTituloEIsbnExcluindoId(novoTitulo, novoIsbn, livro.getId())) {
                throw new DuplicidadeException("O título ou ISBN '" + novoTitulo + "' / '" + novoIsbn + "' já pertence a outro livro.");
            }

            livro.setTitulo(novoTitulo);
            livro.setPreco(novoPreco);
            livro.setEstoqueDisponivel(novoEstoque);
            livro.setEditora(novaEditora);
            livro.setQuantidadePaginas(novaPaginas);
            livro.setIsbn(novoIsbn);
            livro.getAutores().clear(); // Limpa autores antigos
            if (novosAutores != null) {
                for (Autor autor : novosAutores) {
                    livro.addAutor(autor); // Adiciona os novos autores.
                }
            }
            livro.setCategoria(novaCategoria);
            salvarTodosDados(); // Persiste os dados após a edição.
            return true;
        }
        return false;
    }

    /**
     * Edita os dados de um jornal existente no sistema.
     * Verifica se o novo título e data de publicação não são duplicados de *outros* jornais.
     * Lança uma {@link DuplicidadeException} se a edição resultar em um título e data já existente em outro jornal.
     *
     * @param idJornal O ID do jornal a ser editado.
     * @param novoTitulo O novo título do jornal.
     * @param novoPreco O novo preço do jornal.
     * @param novoEstoque O novo estoque disponível do jornal.
     * @param novaEditora A nova {@link Editora} do jornal.
     * @param novaDataPublicacao A nova data de publicação do jornal.
     * @return {@code true} se o jornal foi editado com sucesso, {@code false} caso não seja encontrado ou falhe na edição.
     * @throws DuplicidadeException Se o novo título e data de publicação já pertencerem a outro jornal.
     */
    public boolean editarJornal(int idJornal, String novoTitulo, float novoPreco, int novoEstoque, Editora novaEditora, LocalDate novaDataPublicacao) {
        Jornal jornal = buscarJornalPorId(idJornal);
            if (jornal != null) {
                if (novoPreco < 3.00) {
                throw new DuplicidadeException("Preço do jornal deve ser no mínimo R$ 3,00.");
            }
            if (novoEstoque < 0) {
                throw new DuplicidadeException("Estoque do jornal não pode ser negativo.");
            }
            if (existeJornalComTituloEDataExcluindoId(novoTitulo, novaDataPublicacao, jornal.getId())) {
                throw new DuplicidadeException("Jornal com o título '" + novoTitulo + "' e data '" + novaDataPublicacao + "' já existe.");
            }

            jornal.setTitulo(novoTitulo);
            jornal.setPreco(novoPreco);
            jornal.setEstoqueDisponivel(novoEstoque);
            jornal.setEditora(novaEditora);
            jornal.setDataPublicacao(novaDataPublicacao);
            salvarTodosDados(); // Persiste os dados após a edição.
            return true;
        }
        return false;
    }

    // Métodos de Exclusão (R.F._3)

    /**
     * Exclui um livro do sistema com base no seu ID.
     * @param idLivro O ID do livro a ser excluído.
     * @return {@code true} se o livro foi encontrado e removido com sucesso, {@code false} caso contrário.
     */
    public boolean excluirLivro(int idLivro) {
        Livro livroParaRemover = buscarLivroPorId(idLivro);
        if (livroParaRemover != null) {
            boolean removido = livros.remove(livroParaRemover);
            if (removido) salvarTodosDados(); // Persiste os dados após a exclusão.
            return removido;
        }
        return false;
    }

    /**
     * Exclui um jornal do sistema com base no seu ID.
     * @param idJornal O ID do jornal a ser excluído.
     * @return {@code true} se o jornal foi encontrado e removido com sucesso, {@code false} caso contrário.
     */
    public boolean excluirJornal(int idJornal) {
        Jornal jornalParaRemover = buscarJornalPorId(idJornal);
        if (jornalParaRemover != null) {
            boolean removido = jornais.remove(jornalParaRemover);
            if (removido) salvarTodosDados(); // Persiste os dados após a exclusão.
            return removido;
        }
        return false;
    }

    // Métodos de Visualização (R.F._4)

    /**
     * Retorna uma nova lista contendo todos os livros cadastrados no sistema.
     * @return Uma {@code List} de objetos {@link Livro}.
     */
    public List<Livro> getTodosLivros() {
        return new ArrayList<>(livros);
    }

    /**
     * Retorna uma nova lista contendo todos os jornais cadastrados no sistema.
     * @return Uma {@code List} de objetos {@link Jornal}.
     */
    public List<Jornal> getTodosJornais() {
        return new ArrayList<>(jornais);
    }

    /**
     * Retorna uma nova lista contendo todos os autores cadastrados no sistema.
     * @return Uma {@code List} de objetos {@link Autor}.
     */
    public List<Autor> getTodosAutores() {
        return new ArrayList<>(autores);
    }

    /**
     * Retorna uma nova lista contendo todas as editoras cadastradas no sistema.
     * @return Uma {@code List} de objetos {@link Editora}.
     */
    public List<Editora> getTodasEditoras() {
        return new ArrayList<>(editoras);
    }

    /**
     * Retorna uma nova lista contendo todas as categorias cadastradas no sistema.
     * @return Uma {@code List} de objetos {@link Categoria}.
     */
    public List<Categoria> getTodasCategorias() {
        return new ArrayList<>(categorias);
    }

    // Métodos de Busca (R.F._5)

    /**
     * Busca e retorna um livro pelo seu ID único.
     * @param id O ID do livro a ser buscado.
     * @return O objeto {@link Livro} correspondente ao ID, ou {@code null} se não for encontrado.
     */
    public Livro buscarLivroPorId(int id) {
        return livros.stream().filter(l -> l.getId() == id).findFirst().orElse(null);
    }

    /**
     * Busca e retorna um jornal pelo seu ID único.
     * @param id O ID do jornal a ser buscado.
     * @return O objeto {@link Jornal} correspondente ao ID, ou {@code null} se não for encontrado.
     */
    public Jornal buscarJornalPorId(int id) {
        return jornais.stream().filter(j -> j.getId() == id).findFirst().orElse(null);
    }

    /**
     * Busca e retorna um autor pelo seu ID único.
     * @param id O ID do autor a ser buscado.
     * @return O objeto {@link Autor} correspondente ao ID, ou {@code null} se não for encontrado.
     */
    public Autor buscarAutorPorId(int id) {
        return autores.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
    }

    /**
     * Busca e retorna uma editora pelo seu ID único.
     * @param id O ID da editora a ser buscada.
     * @return O objeto {@link Editora} correspondente ao ID, ou {@code null} se não for encontrado.
     */
    public Editora buscarEditoraPorId(int id) {
        return editoras.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    /**
     * Busca e retorna uma categoria pelo seu ID único.
     * @param id O ID da categoria a ser buscada.
     * @return O objeto {@link Categoria} correspondente ao ID, ou {@code null} se não for encontrado.
     */
    public Categoria buscarCategoriaPorId(int id) {
        return categorias.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    /**
     * Busca e retorna uma lista de livros cujos títulos contêm o termo de busca fornecido
     * (a busca não diferencia maiúsculas de minúsculas).
     * @param titulo O termo de busca para o título do livro.
     * @return Uma {@code List} de objetos {@link Livro} que correspondem ao critério de busca.
     */
    public List<Livro> buscarLivrosPorTitulo(String titulo) {
        return livros.stream()
                .filter(l -> l.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Busca e retorna uma lista de livros cujos autores contêm o nome de autor fornecido
     * (a busca não diferencia maiúsculas de minúsculas).
     * @param nomeAutor O termo de busca para o nome do autor.
     * @return Uma {@code List} de objetos {@link Livro} que correspondem ao critério de busca.
     */
    public List<Livro> buscarLivrosPorAutor(String nomeAutor) {
        return livros.stream()
                .filter(l -> l.getAutores().stream()
                        .anyMatch(a -> a.getNome().toLowerCase().contains(nomeAutor.toLowerCase())))
                .collect(Collectors.toList());
    }

    /**
     * Busca e retorna uma lista de livros cujas categorias contêm o nome de categoria fornecido
     * (a busca não diferencia maiúsculas de minúsculas).
     * @param nomeCategoria O termo de busca para o nome da categoria.
     * @return Uma {@code List} de objetos {@link Livro} que correspondem ao critério de busca.
     */
    public List<Livro> buscarLivrosPorCategoria(String nomeCategoria) {
        return livros.stream()
                .filter(l -> l.getCategoria() != null && l.getCategoria().getNome().toLowerCase().contains(nomeCategoria.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Busca e retorna uma lista de jornais cujos títulos contêm o termo de busca fornecido
     * (a busca não diferencia maiúsculas de minúsculas).
     * @param titulo O termo de busca para o título do jornal.
     * @return Uma {@code List} de objetos {@link Jornal} que correspondem ao critério de busca.
     */
    public List<Jornal> buscarJornaisPorTitulo(String titulo) {
        return jornais.stream()
                .filter(j -> j.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Busca e retorna uma lista de jornais com a data de publicação fornecida.
     * @param data A data de publicação a ser buscada.
     * @return Uma {@code List} de objetos {@link Jornal} que correspondem ao critério de busca.
     */
    public List<Jornal> buscarJornaisPorData(LocalDate data) {
        return jornais.stream()
                .filter(j -> j.getDataPublicacao().equals(data))
                .collect(Collectors.toList());
    }
}