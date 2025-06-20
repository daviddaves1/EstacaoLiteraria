package src;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Autor implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int proximoId = 1;
    private int id;
    private String nome;
    private String nacionalidade;
    private LocalDate dataNascimento;
    private List<Livro> livrosPublicados;

    public Autor(String nome, String nacionalidade, LocalDate dataNascimento) {
        this.id = proximoId++;
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.dataNascimento = dataNascimento;
        this.livrosPublicados = new ArrayList<>();
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

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public List<Livro> getLivrosPublicados() {
        return livrosPublicados;
    }

    public void adicionarLivro(Livro livro) {
        if (livro != null && !this.livrosPublicados.contains(livro)) {
            this.livrosPublicados.add(livro);
        }
    }

    public void removerLivro(Livro livro) {
        if (livro != null) {
            this.livrosPublicados.remove(livro);
        }
    }

    public static void setProximoIdEstatico(int id) {
        if (id > proximoId) {
            proximoId = id;
        }
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome + ", Nacionalidade: " + nacionalidade + ", Data Nasc.: " + dataNascimento;
    }
}