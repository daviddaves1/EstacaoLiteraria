package src;

/**
 * Classe principal da aplicação "Estação Literária".
 * Atua como o ponto de entrada para o programa.
 */
public class Main {
    /**
     * O método {@code main} é o ponto de partida da execução da aplicação.
     * Ele inicializa o sistema de gerenciamento e a interface do usuário,
     * e então inicia o fluxo principal da interface gráfica.
     * @param args Argumentos de linha de comando (não utilizados nesta aplicação).
     */
    public static void main(String[] args) {
        Sistema sistema = new Sistema();
        Interface ui = new Interface(sistema);

        ui.iniciarAplicacao();
    }
}