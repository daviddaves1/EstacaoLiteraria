package src;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa um Jornal cadastrado no sistema da Estação Literária.
 * Estende a classe abstrata {@link Publicacao}, herdando seus atributos e comportamentos comuns,
 * e adiciona o atributo específico da data de publicação.
 */
public class Jornal extends Publicacao implements Serializable {
    private static final long serialVersionUID = 1L; // Necessário para a serialização do objeto.

    /**
     * A data em que o jornal foi publicado.
     */
    private LocalDate dataPublicacao;

    /**
     * Construtor para criar uma nova instância de Jornal.
     * Os atributos comuns de publicação são passados para o construtor da superclasse.
     * @param titulo O título do jornal.
     * @param preco O preço de venda do jornal.
     * @param estoqueDisponivel A quantidade de exemplares disponíveis em estoque.
     * @param editora A {@link Editora} responsável pela publicação do jornal.
     * @param dataPublicacao A data específica de publicação do jornal.
     */
    public Jornal(String titulo, float preco, int estoqueDisponivel, Editora editora, LocalDate dataPublicacao) {
        super(titulo, preco, estoqueDisponivel, editora);
        this.dataPublicacao = dataPublicacao;
    }

    /**
     * Retorna a data de publicação do jornal.
     * @return A data de publicação do jornal.
     */
    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    /**
     * Define a data de publicação do jornal.
     * @param dataPublicacao A nova data de publicação do jornal.
     */
    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    /**
     * Fornece uma representação em String detalhada do objeto Jornal.
     * Inclui ID, título, nome da editora, data de publicação, preço e estoque disponível.
     * @return Uma String formatada com os detalhes do jornal.
     */
    @Override
    public String toString() {
        String editoraNome = (getEditora() != null) ? getEditora().getNome() : "N/A";
        String dataPubFormatada = (dataPublicacao != null) ? dataPublicacao.format(Util.DATE_FORMATTER) : "N/A";

        return "--- JORNAL ---\n" +
               "ID: " + getId() + "\n" +
               "Título: " + getTitulo() + "\n" +
               "Editora: " + editoraNome + "\n" +
               "Data de Publicação: " + dataPubFormatada + "\n" +
               "Preço: R$" + String.format("%.2f", getPreco()) + "\n" +
               "Estoque Disponível: " + getEstoqueDisponivel() + "\n" +
               "--------------------";
    }
}