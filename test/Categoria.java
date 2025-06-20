package test;

import java.io.Serializable;

public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int proximoId = 1;
    private int id;
    private String nome;

    public Categoria(String nome) {
        this.id = proximoId++;
        this.nome = nome;
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