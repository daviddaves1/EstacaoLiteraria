package src;

import java.io.Serializable;

/**
 * Exceção personalizada que indica uma tentativa de criar ou modificar um registro
 * com informações que já existem no sistema e devem ser únicas.
 * Estende {@code RuntimeException} para indicar que a exceção não precisa ser declarada no cabeçalho dos métodos.
 */
public class DuplicidadeException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L; // Necessário para a serialização da exceção.

    /**
     * Construtor para criar uma nova {@code DuplicidadeException}.
     * @param message A mensagem detalhando a causa da exceção, informando qual dado está duplicado.
     */
    public DuplicidadeException(String message) {
        super(message);
    }
}