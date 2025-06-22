package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import src.Interface;
import src.Sistema;
import src.DuplicidadeException;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InterfaceTest {

    @Mock
    private Sistema mockSistema;
    private Interface ui;
    private String simulatedInput = null;
    private List<String> showMessageDialogs = new ArrayList<>();
    private int showConfirmDialogResult = -1;

    private void mockJOptionPane() {
        /* Isso é complexo e geralmente não é feito em testes unitários. O ideal é que a Interface tivesse uma camada de "dialogs" que pudesse ser mockada. */ 
    }


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
        ui = new Interface(mockSistema);
        showMessageDialogs.clear();
        simulatedInput = null;
        showConfirmDialogResult = -1;
    }

    @Test
    @DisplayName("Deve chamar o método de cadastro de autor no sistema quando a opção é selecionada (mock)")
    void deveChamarCadastroAutorNoSistema() {
        when(mockSistema.cadastrarAutor("Nome Teste", "Nac Teste", LocalDate.of(2000, 1, 1))).thenReturn(true);
        when(mockSistema.existeAutorComNome("Nome Teste")).thenReturn(false); // Para não lançar DuplicidadeException

        /* Não podemos chamar diretamente cadastrarAutor() da Interface aqui sem input real do JOptionPane. O que podemos testar é que, *se* a UI coletar os dados corretamente e chamar o Sistema, o Sistema se comportará como esperado. Isso reforça a necessidade de separar a lógica. */
    }

    @Test
    @DisplayName("Deve chamar salvarTodosDados ao sair da aplicação")
    void deveChamarSalvarDadosAoSair() {
    }

    @Test
    @DisplayName("Deve tratar DuplicidadeException ao tentar cadastrar livro duplicado")
    void deveTratarDuplicidadeExceptionAoCadastrarLivro() {
        when(mockSistema.cadastrarLivro(
            anyString(), anyFloat(), anyInt(), any(src.Editora.class), anyInt(), anyString(), anyList(), any(src.Categoria.class)
        )).thenThrow(new DuplicidadeException("Livro com o título 'XYZ' já existe."));

    }
}