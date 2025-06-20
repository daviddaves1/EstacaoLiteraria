package src;

import java.io.Serializable;

public abstract class Publicacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int proximoId = 1;
    private int id;
    private String titulo;
    private float preco;
    private int estoqueDisponivel;
    private Editora editora;

    // Construtor
    public Publicacao(String titulo, float preco, int estoqueDisponivel, Editora editora) {
        this.id = proximoId++;
        this.titulo = titulo;
        this.preco = preco;
        this.estoqueDisponivel = estoqueDisponivel;
        this.editora = editora;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public int getEstoqueDisponivel() {
        return estoqueDisponivel;
    }

    public void setEstoqueDisponivel(int estoqueDisponivel) {
        this.estoqueDisponivel = estoqueDisponivel;
    }

    public Editora getEditora() {
        return editora;
    }

    public void setEditora(Editora editora) {
        this.editora = editora;
    }

    public void addEstoque(int quantidade) {
        if (quantidade > 0) {
            this.estoqueDisponivel += quantidade;
            System.out.println(quantidade + " unidades adicionadas ao estoque de '" + this.titulo + "'. Novo estoque: " + this.estoqueDisponivel);
        } else {
            System.out.println("Quantidade para adicionar deve ser maior que zero.");
        }
    }

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

    public static void setProximoIdEstatico(int id) {
        if (id > proximoId) {
            proximoId = id;
        }
    }

    @Override
    public abstract String toString();
}