package src;

import java.io.Serializable;

/**
 * Classe abstrata que serve como base para todas as publicações
 * (Livros e Jornais) no sistema da Estação Literária.
 * Define atributos e comportamentos comuns a todas as publicações,
 * como título, preço, estoque, editora e métodos para gerenciamento de estoque.
 */
public abstract class Publicacao implements Serializable {
    private static final long serialVersionUID = 1L; // Necessário para a serialização do objeto.

    /**
     * Campo estático que controla a geração sequencial de IDs para novas publicações,
     * garantindo que cada nova publicação (Livro ou Jornal) tenha um ID único.
     */
    private static int proximoId = 1;
    /**
     * O identificador único da publicação.
     */
    private int id;
    /**
     * O título da publicação.
     */
    private String titulo;
    /**
     * O preço de venda da publicação.
     */
    private float preco;
    /**
     * A quantidade de exemplares disponíveis em estoque.
     */
    private int estoqueDisponivel;
    /**
     * A {@link Editora} responsável por esta publicação.
     */
    private Editora editora;

    /**
     * Construtor para criar uma nova instância de Publicacao.
     * O ID é automaticamente atribuído de forma sequencial.
     * @param titulo O título da publicação.
     * @param preco O preço de venda da publicação.
     * @param estoqueDisponivel A quantidade inicial de exemplares em estoque.
     * @param editora A {@link Editora} associada a esta publicação.
     */
    public Publicacao(String titulo, float preco, int estoqueDisponivel, Editora editora) {
        this.id = proximoId++;
        this.titulo = titulo;
        this.preco = preco;
        this.estoqueDisponivel = estoqueDisponivel;
        this.editora = editora;
    }

    /**
     * Retorna o ID único da publicação.
     * @return O ID da publicação.
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o título da publicação.
     * @return O título da publicação.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define o título da publicação.
     * @param titulo O novo título da publicação.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Retorna o preço da publicação.
     * @return O preço da publicação.
     */
    public float getPreco() {
        return preco;
    }

    /**
     * Define o preço da publicação.
     * @param preco O novo preço da publicação.
     */
    public void setPreco(float preco) {
        this.preco = preco;
    }

    /**
     * Retorna a quantidade de exemplares disponíveis em estoque.
     * @return O estoque disponível.
     */
    public int getEstoqueDisponivel() {
        return estoqueDisponivel;
    }

    /**
     * Define a quantidade de exemplares disponíveis em estoque.
     * @param estoqueDisponivel A nova quantidade em estoque.
     */
    public void setEstoqueDisponivel(int estoqueDisponivel) {
        this.estoqueDisponivel = estoqueDisponivel;
    }

    /**
     * Retorna a editora da publicação.
     * @return A {@link Editora} da publicação.
     */
    public Editora getEditora() {
        return editora;
    }

    /**
     * Define a editora da publicação.
     * @param editora A nova {@link Editora} da publicação.
     */
    public void setEditora(Editora editora) {
        this.editora = editora;
    }

    /**
     * Adiciona uma quantidade especificada ao estoque da publicação.
     * A quantidade deve ser um valor positivo.
     * Uma mensagem é exibida no console informando a adição e o novo estoque.
     * @param quantidade A quantidade a ser adicionada.
     */
    public void addEstoque(int quantidade) {
        if (quantidade > 0) {
            this.estoqueDisponivel += quantidade;
            System.out.println(quantidade + " unidades adicionadas ao estoque de '" + this.titulo + "'. Novo estoque: " + this.estoqueDisponivel);
        } else {
            System.out.println("Quantidade para adicionar deve ser maior que zero.");
        }
    }

    /**
     * Remove uma quantidade especificada do estoque da publicação.
     * A quantidade deve ser um valor positivo e não exceder o estoque disponível.
     * Uma mensagem é exibida no console informando a remoção e o novo estoque.
     * @param quantidade A quantidade a ser removida.
     */
    public void remEstoque(int quantidade) {
        if (quantidade > 0 && this.estoqueDisponivel >= quantidade) {
            this.estoqueDisponivel -= quantidade;
            System.out.println(quantidade + " unidades removidas do estoque de '" + this.titulo + "'. Novo estoque: " + this.estoqueDisponivel);
        } else if (quantidade <= 0) {
            System.out.println("Quantidade para remover deve ser maior que zero.");
        } else {
            System.out.println("Estoque insuficiente para remover " + quantidade + " unidades de '" + this.titulo + "'. Estoque atual: " + this.estoqueDisponivel);
        }
    }

    /**
     * Define o próximo ID estático a ser utilizado para a criação de novos objetos Publicacao.
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
     * Método abstrato que deve ser implementado por todas as subclasses
     * para fornecer uma representação em String detalhada da publicação.
     * @return Uma String formatada com os detalhes da publicação.
     */
    @Override
    public abstract String toString();
}