package src;

import java.io.Serializable;

/**
 * Representa uma Categoria de livros no sistema da Estação Literária.
 * Categorias são identificadas por um ID único e um nome.
 */
public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L; // Necessário para a serialização do objeto.

    /**
     * Campo estático que controla a geração sequencial de IDs para novas categorias.
     */
    private static int proximoId = 1;
    /**
     * O identificador único da categoria.
     */
    private int id;
    /**
     * O nome da categoria (e.g., "Ficção", "Suspense").
     */
    private String nome;

    /**
     * Construtor para criar uma nova instância de Categoria.
     * O ID é automaticamente atribuído de forma sequencial.
     * @param nome O nome da categoria.
     */
    public Categoria(String nome) {
        this.id = proximoId++;
        this.nome = nome;
    }

    /**
     * Retorna o ID único da categoria.
     * @return O ID da categoria.
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o nome da categoria.
     * @return O nome da categoria.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da categoria.
     * @param nome O novo nome da categoria.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Define o próximo ID estático a ser utilizado para a criação de novos objetos Categoria.
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
     * Retorna uma representação em String do objeto Categoria.
     * @return Uma String contendo o ID e o Nome da categoria.
     */
    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome;
    }
}