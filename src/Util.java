package src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitária responsável por operações de persistência de dados em arquivos.
 * Contém métodos genéricos para salvar e carregar listas de objetos serializáveis.
 */
public class Util {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Salva uma lista de objetos serializáveis em um arquivo binário.
     * A lista é escrita no caminho "data/" + nomeArquivo.
     * Em caso de sucesso, uma mensagem é impressa no console. Em caso de erro, uma mensagem de erro é exibida.
     *
     * @param <T> O tipo dos objetos na lista, que deve implementar {@link Serializable}.
     * @param lista A lista de objetos a ser salva.
     * @param nomeArquivo O nome do arquivo onde a lista será persistida.
     */
    public static <T extends Serializable> void salvarDados(List<T> lista, String nomeArquivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/" + nomeArquivo))) {
            oos.writeObject(lista);
            System.out.println("Dados salvos com sucesso em: " + "data/" + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados em " + nomeArquivo + ": " + e.getMessage());
        }
    }

    /**
     * Carrega uma lista de objetos serializáveis de um arquivo binário.
     * Procura o arquivo no caminho "data/" + nomeArquivo.
     * Se o arquivo não existir, uma nova lista vazia é retornada.
     * Em caso de erro de leitura ou desserialização, uma mensagem de erro é exibida e uma lista vazia é retornada.
     *
     * @param <T> O tipo dos objetos na lista a ser carregada, que deve implementar {@link Serializable}.
     * @param nomeArquivo O nome do arquivo de onde a lista será carregada.
     * @return Uma {@code List} de objetos do tipo {@code T} carregada do arquivo, ou uma lista vazia em caso de falha ou arquivo inexistente.
     */
    @SuppressWarnings("unchecked") // Suprime o aviso de unchecked cast ao converter Object para List<T>.
    public static <T extends Serializable> List<T> carregarDados(String nomeArquivo) {
        File file = new File("data/" + nomeArquivo);
        if (!file.exists()) {
            System.out.println("Arquivo " + nomeArquivo + " não encontrado. Criando nova lista.");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<T> lista = (List<T>) ois.readObject();
            System.out.println("Dados carregados com sucesso de: " + "data/" + nomeArquivo);
            return lista;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados de " + nomeArquivo + ": " + e.getMessage());
            System.out.println("Retornando lista vazia devido ao erro.");
            return new ArrayList<>();
        }
    }
}