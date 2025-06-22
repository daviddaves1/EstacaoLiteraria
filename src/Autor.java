package src;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um Autor de publicações no sistema da Estação Literária.
 * Contém informações como ID único, nome, nacionalidade, data de nascimento
 * e a lista de livros associados a este autor.
 */
public class Autor implements Serializable {
    private static final long serialVersionUID = 1L; // Necessário para a serialização do objeto.

    /**
     * Campo estático que controla a geração sequencial de IDs para novos autores,
     * garantindo a unicidade de cada registro.
     */
    private static int proximoId = 1;
    /**
     * O identificador único do autor.
     */
    private int id;
    /**
     * O nome completo do autor.
     */
    private String nome;
    /**
     * A nacionalidade do autor.
     */
    private String nacionalidade;
    /**
     * A data de nascimento do autor.
     */
    private LocalDate dataNascimento;
    /**
     * Uma lista de objetos {@link Livro} publicados por este autor.
     */
    private List<Livro> livrosPublicados;

    /**
     * Construtor para criar uma nova instância de Autor.
     * O ID é automaticamente atribuído de forma sequencial.
     *
     * @param nome O nome do autor.
     * @param nacionalidade A nacionalidade do autor.
     * @param dataNascimento A data de nascimento do autor.
     */
    public Autor(String nome, String nacionalidade, LocalDate dataNascimento) {
        this.id = proximoId++;
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.dataNascimento = dataNascimento;
        this.livrosPublicados = new ArrayList<>();
    }

    /**
     * Retorna o ID único do autor.
     * @return O ID do autor.
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o nome do autor.
     * @return O nome do autor.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do autor.
     * @param nome O novo nome do autor.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna a nacionalidade do autor.
     * @return A nacionalidade do autor.
     */
    public String getNacionalidade() {
        return nacionalidade;
    }

    /**
     * Define a nacionalidade do autor.
     * @param nacionalidade A nova nacionalidade do autor.
     */
    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    /**
     * Retorna a data de nascimento do autor.
     * @return A data de nascimento do autor.
     */
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    /**
     * Define a data de nascimento do autor.
     * @param dataNascimento A nova data de nascimento do autor.
     */
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    /**
     * Retorna a lista de livros publicados por este autor.
     * @return Uma lista de objetos {@link Livro} publicados pelo autor.
     */
    public List<Livro> getLivrosPublicados() {
        return livrosPublicados;
    }

    /**
     * Adiciona um livro à lista de livros publicados por este autor.
     * O livro não será adicionado se for nulo ou já estiver presente na lista.
     * @param livro O {@link Livro} a ser adicionado.
     */
    public void adicionarLivro(Livro livro) {
        if (livro != null && !this.livrosPublicados.contains(livro)) {
            this.livrosPublicados.add(livro);
        }
    }

    /**
     * Remove um livro da lista de livros publicados por este autor.
     * @param livro O {@link Livro} a ser removido.
     */
    public void removerLivro(Livro livro) {
        if (livro != null) {
            this.livrosPublicados.remove(livro);
        }
    }

    /**
     * Define o próximo ID estático a ser utilizado para a criação de novos objetos Autor.
     * Este método é utilizado principalmente durante a inicialização do sistema após o carregamento de dados persistidos,
     * garantindo que os novos IDs gerados não entrem em conflito com IDs já existentes.
     * O ID só será atualizado se o valor fornecido for maior que o {@code proximoId} atual.
     * @param id O valor mínimo para o próximo ID a ser gerado.
     */
    public static void setProximoIdEstatico(int id) {
        if (id > proximoId) {
            proximoId = id;
        }
    }

    /**
     * Retorna uma representação em String do objeto Autor.
     * @return Uma String contendo o ID, Nome, Nacionalidade e Data de Nascimento do autor.
     */
    @Override
    public String toString() {
        String dataNascFormatada = (dataNascimento != null) ? dataNascimento.format(Util.DATE_FORMATTER) : "N/A";
        return "ID: " + id + ", Nome: " + nome + ", Nacionalidade: " + nacionalidade + ", Data Nasc.: " + dataNascFormatada;
    }
}