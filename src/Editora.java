package src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Editora implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int proximoId = 1;
    private int id;
    private String nome;
    private List<Publicacao> publicacoes;

    public Editora(String nome) {
        this.id = proximoId++;
        this.nome = nome;
        this.publicacoes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Publicacao> getPublicacoes() {
        return publicacoes;
    }

    public void adicionarPublicacao(Publicacao publicacao) {
        if (publicacao != null && !this.publicacoes.contains(publicacao)) {
            this.publicacoes.add(publicacao);
        }
    }

    public void removerPublicacao(Publicacao publicacao) {
        if (publicacao != null) {
            this.publicacoes.remove(publicacao);
        }
    }

    public static void setProximoIdEstatico(int id) {
        if (id > proximoId) {
            proximoId = id;
        }
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome;
    }
}