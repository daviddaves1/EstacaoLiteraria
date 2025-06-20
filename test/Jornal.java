package test;

import java.io.Serializable;
import java.time.LocalDate;

public class Jornal extends Publicacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate dataPublicacao;

    public Jornal(String titulo, float preco, int estoqueDisponivel, Editora editora, LocalDate dataPublicacao) {
        super(titulo, preco, estoqueDisponivel, editora);
        this.dataPublicacao = dataPublicacao;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    @Override
    public String toString() {
        String editoraNome = (getEditora() != null) ? getEditora().getNome() : "N/A";

        return "--- JORNAL ---\n" +
               "ID: " + getId() + "\n" +
               "Título: " + getTitulo() + "\n" +
               "Editora: " + editoraNome + "\n" +
               "Data de Publicação: " + dataPublicacao + "\n" +
               "Preço: R$" + String.format("%.2f", getPreco()) + "\n" +
               "Estoque Disponível: " + getEstoqueDisponivel() + "\n" +
               "--------------------";
    }
}