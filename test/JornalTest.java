package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import src.Editora;
import src.Jornal;
import src.Publicacao;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class JornalTest {

    private Jornal jornal;
    private Editora editora;

    @BeforeEach
    void setUp() {
        Publicacao.setProximoIdEstatico(1);
        Editora.setProximoIdEstatico(1);

        editora = new Editora("Editora Jornais");
        jornal = new Jornal("Notícias Diárias", 7.50f, 200, editora, LocalDate.of(2025, 6, 22));
    }

    @Test
    @DisplayName("Deve criar jornal com atributos corretos e herdar de Publicacao")
    void deveCriarJornalComAtributosCorretos() {
        assertNotNull(jornal);
        assertTrue(jornal.getId() > 0);
        assertEquals("Notícias Diárias", jornal.getTitulo());
        assertEquals(7.50f, jornal.getPreco(), 0.001);
        assertEquals(200, jornal.getEstoqueDisponivel());
        assertEquals(editora, jornal.getEditora());
        assertEquals(LocalDate.of(2025, 6, 22), jornal.getDataPublicacao());
    }

    @Test
    @DisplayName("Deve permitir alterar a data de publicacao")
    void deveAlterarDataPublicacao() {
        LocalDate novaData = LocalDate.of(2025, 6, 23);
        jornal.setDataPublicacao(novaData);
        assertEquals(novaData, jornal.getDataPublicacao());
    }

    @Test
    @DisplayName("Deve retornar string formatada em toString()")
    void deveRetornarStringFormatadaEmToString() {
        String expected = "--- JORNAL ---\n" +
                          "ID: " + jornal.getId() + "\n" +
                          "Título: Notícias Diárias\n" +
                          "Editora: " + editora.getNome() + "\n" +
                          "Data de Publicação: 2025-06-22\n" +
                          "Preço: R$7,50\n" +
                          "Estoque Disponível: 200\n" +
                          "--------------------";
        assertEquals(expected, jornal.toString());
    }

    @Test
    @DisplayName("Deve retornar string formatada em toString() sem editora")
    void deveRetornarStringFormatadaEmToStringSemEditora() {
        Jornal jornalSemEditora = new Jornal("Jornal Sem Editora", 5.0f, 100, null, LocalDate.of(2025, 1, 1));
        String expected = "--- JORNAL ---\n" +
                          "ID: " + jornalSemEditora.getId() + "\n" +
                          "Título: Jornal Sem Editora\n" +
                          "Editora: N/A\n" +
                          "Data de Publicação: 2025-01-01\n" +
                          "Preço: R$5,00\n" +
                          "Estoque Disponível: 100\n" +
                          "--------------------";
        assertEquals(expected, jornalSemEditora.toString());
    }
}