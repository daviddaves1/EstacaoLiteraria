package src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um Livro cadastrado no sistema da Estação Literária.
 * Estende a classe abstrata {@link Publicacao}, herdando seus atributos e comportamentos comuns,
 * e adiciona atributos específicos como quantidade de páginas, ISBN, autores e categoria.
 */
public class Livro extends Publicacao implements Serializable {
    private static final long serialVersionUID = 1L; // Necessário para a serialização do objeto.

    /**
     * O número total de páginas do livro.
     */
    private int quantidadePaginas;
    /**
     * O ISBN (International Standard Book Number), um identificador único para livros.
     */
    private String isbn;
    /**
     * Uma lista de objetos {@link Autor} associados a este livro.
     * Um livro pode ter um ou mais autores.
     */
    private List<Autor> autores;
    /**
     * A {@link Categoria} à qual este livro pertence.
     */
    private Categoria categoria;

/**
     * Construtor para criar uma nova instância de Livro.
     * Os atributos comuns de publicação são passados para o construtor da superclasse.
     * O estoque inicial do livro é definido como 0 por padrão.
     * @param titulo O título do livro.
     * @param preco O preço de venda do livro.
     * @param editora A {@link Editora} responsável pela publicação do livro.
     * @param quantidadePaginas A quantidade de páginas do livro.
     * @param isbn O ISBN do livro.
     */
    public Livro(String titulo, float preco, Editora editora, int quantidadePaginas, String isbn) {
        super(titulo, preco, editora);
        this.quantidadePaginas = quantidadePaginas;
        this.isbn = isbn;
        this.autores = new ArrayList<>();
    }

    /**
     * Retorna a quantidade de páginas do livro.
     * @return A quantidade de páginas.
     */
    public int getQuantidadePaginas() {
        return quantidadePaginas;
    }

    /**
     * Define a quantidade de páginas do livro.
     * @param quantidadePaginas A nova quantidade de páginas.
     */
    public void setQuantidadePaginas(int quantidadePaginas) {
        this.quantidadePaginas = quantidadePaginas;
    }

    /**
     * Retorna o ISBN do livro.
     * @return O ISBN do livro.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Define o ISBN do livro.
     * @param isbn O novo ISBN do livro.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Retorna a lista de autores associados a este livro.
     * @return Uma lista de objetos {@link Autor} do livro.
     */
    public List<Autor> getAutores() {
        return autores;
    }

    /**
     * Adiciona um {@link Autor} à lista de autores deste livro.
     * O autor não será adicionado se for nulo ou já estiver presente na lista.
     * @param autor O {@link Autor} a ser adicionado.
     */
    public void addAutor(Autor autor) {
        if (autor != null && !this.autores.contains(autor)) {
            this.autores.add(autor);
            // Opcional: autor.adicionarLivro(this); // Para manter a bidirecionalidade, se Autor tiver essa lógica.
        }
    }

    /**
     * Remove um {@link Autor} da lista de autores deste livro.
     * @param autor O {@link Autor} a ser removido.
     */
    public void removerAutor(Autor autor) {
        if (autor != null) {
            this.autores.remove(autor);
            // Opcional: autor.removerLivro(this); // Para manter a bidirecionalidade.
        }
    }

    /**
     * Retorna a {@link Categoria} à qual este livro pertence.
     * @return A {@link Categoria} do livro.
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Define a {@link Categoria} à qual este livro pertence.
     * @param categoria A nova {@link Categoria} do livro.
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Fornece uma representação em String detalhada do objeto Livro.
     * Inclui ID, título, autores, editora, categoria, ISBN, preço, quantidade de páginas e estoque disponível.
     * @return Uma String formatada com os detalhes do livro.
     */
    @Override
    public String toString() {
        StringBuilder autoresStr = new StringBuilder();
        if (autores != null && !autores.isEmpty()) {
            for (int i = 0; i < autores.size(); i++) {
                autoresStr.append(autores.get(i).getNome());
                if (i < autores.size() - 1) {
                    autoresStr.append(", ");
                }
            }
        } else {
            autoresStr.append("N/A");
        }

        String categoriaNome = (categoria != null) ? categoria.getNome() : "N/A";
        String editoraNome = (getEditora() != null) ? getEditora().getNome() : "N/A";

        return "--- LIVRO ---\n" +
               "ID: " + getId() + "\n" +
               "Título: " + getTitulo() + "\n" +
               "Autor(es): " + autoresStr.toString() + "\n" +
               "Editora: " + editoraNome + "\n" +
               "Categoria: " + categoriaNome + "\n" +
               "ISBN: " + isbn + "\n" +
               "Preço: R$" + String.format("%.2f", getPreco()) + "\n" +
               "Quantidade de Páginas: " + quantidadePaginas + "\n" +
               "Estoque Disponível: " + getEstoqueDisponivel() + "\n" +
               "--------------------";
    }
}