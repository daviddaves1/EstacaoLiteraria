package test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import src.DuplicidadeException;

import static org.junit.jupiter.api.Assertions.*;

public class DuplicidadeExceptionTest {

    @Test
    @DisplayName("Deve criar DuplicidadeException com a mensagem correta")
    void deveCriarExcecaoComMensagemCorreta() {
        String mensagemEsperada = "Este item já existe no sistema.";
        DuplicidadeException exception = new DuplicidadeException(mensagemEsperada);

        assertNotNull(exception);
        assertEquals(mensagemEsperada, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Deve ser serializavel")
    void deveSerSerializavel() {
        assertTrue(java.io.Serializable.class.isAssignableFrom(DuplicidadeException.class));
    }
}