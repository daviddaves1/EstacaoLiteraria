package src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma Editora no sistema da Estação Literária.
 * Editoras possuem um ID único, um nome
 * e mantêm uma lista de {@link Publicacao}ões (Livros e Jornais) associadas.
 */
public class Editora implements Serializable {
    private static final long serialVersionUID = 1L; // Necessário para a serialização do objeto.

    /**
     * Campo estático que controla a geração sequencial de IDs para novas editoras.
     */
    private static int proximoId = 1;
    /**
     * O identificador único da editora.
     */
    private int id;
    /**
     * O nome da editora.
     */
    private String nome;
    /**
     * Uma lista de objetos {@link Publicacao} (Livros e Jornais) publicados por esta editora.
     */
    private List<Publicacao> publicacoes;

    /**
     * Construtor para criar uma nova instância de Editora.
     * O ID é automaticamente atribuído de forma sequencial.
     * @param nome O nome da editora.
     */
    public Editora(String nome) {
        this.id = proximoId++;
        this.nome = nome;
        this.publicacoes = new ArrayList<>();
    }

    /**
     * Retorna o ID único da editora.
     * @return O ID da editora.
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o nome da editora.
     * @return O nome da editora.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da editora.
     * @param nome O novo nome da editora.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna a lista de publicações associadas a esta editora.
     * @return Uma lista de objetos {@link Publicacao} publicados por esta editora.
     */
    public List<Publicacao> getPublicacoes() {
        return publicacoes;
    }

    /**
     * Adiciona uma {@link Publicacao} à lista de publicações desta editora.
     * A publicação não será adicionada se for nula ou já estiver presente na lista.
     * @param publicacao A {@link Publicacao} a ser adicionada.
     */
    public void adicionarPublicacao(Publicacao publicacao) {
        if (publicacao != null && !this.publicacoes.contains(publicacao)) {
            this.publicacoes.add(publicacao);
        }
    }

    /**
     * Remove uma {@link Publicacao} da lista de publicações desta editora.
     * @param publicacao A {@link Publicacao} a ser removida.
     */
    public void removerPublicacao(Publicacao publicacao) {
        if (publicacao != null) {
            this.publicacoes.remove(publicacao);
        }
    }

    /**
     * Define o próximo ID estático a ser utilizado para a criação de novos objetos Editora.
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
     * Retorna uma representação em String do objeto Editora.
     * @return Uma String contendo o ID e o Nome da editora.
     */
    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome;
    }
}