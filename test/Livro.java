package test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Livro extends Publicacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private int quantidadePaginas;
    private String isbn;
    private List<Autor> autores;
    private Categoria categoria;

    public Livro(String titulo, float preco, int estoqueDisponivel, Editora editora, int quantidadePaginas, String isbn) {
        super(titulo, preco, estoqueDisponivel, editora);
        this.quantidadePaginas = quantidadePaginas;
        this.isbn = isbn;
        this.autores = new ArrayList<>();
    }

    public int getQuantidadePaginas() {
        return quantidadePaginas;
    }

    public void setQuantidadePaginas(int quantidadePaginas) {
        this.quantidadePaginas = quantidadePaginas;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void addAutor(Autor autor) {
        if (autor != null && !this.autores.contains(autor)) {
            this.autores.add(autor);
            // Opcional: autor.adicionarLivro(this); // Para manter a bidirecionalidade, se Autor tiver essa lógica
        }
    }

    public void removerAutor(Autor autor) {
        if (autor != null) {
            this.autores.remove(autor);
            // Opcional: autor.removerLivro(this); // Para manter a bidirecionalidade
        }
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

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