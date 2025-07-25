package src;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Responsável pela interação com o usuário através de caixas de diálogo (JOptionPane).
 * Esta classe atua como a camada de apresentação, solicitando entradas e exibindo informações,
 * e orquestra as operações chamando os métodos da classe {@link Sistema}.
 */
public class Interface {
    private Sistema sistema;

    /**
     * Construtor da classe Interface.
     * @param sistema A instância da classe {@link Sistema} que gerencia a lógica de negócio e os dados.
     */
    public Interface(Sistema sistema) {
        this.sistema = sistema;
    }

    /**
     * Inicia o loop principal da aplicação, exibindo o menu de opções para o usuário.
     * O programa continua a exibir o menu e processar as opções até que o usuário escolha sair.
     */
    public void iniciarAplicacao() {
        int opcao;
        do {
            String menu = "Bem-vindo à Estação Literária!\n\n" +
                          "1. Gerenciar Livros\n" +
                          "2. Gerenciar Jornais\n" +
                          "3. Gerenciar Autores\n" +
                          "4. Gerenciar Editoras\n" +
                          "5. Gerenciar Categorias\n" +
                          "6. Visualizar Catálogo Completo\n" +
                          "0. Sair\n\n" +
                          "Escolha uma opção:";

            String input = JOptionPane.showInputDialog(null, menu, "Menu Principal", JOptionPane.PLAIN_MESSAGE);

            if (input == null) { // Trata o clique no botão 'Cancelar' ou fechamento da janela.
                opcao = 0; // Interpreta como intenção de sair.
            } else {
                try {
                    opcao = Integer.parseInt(input); // Converte a entrada do usuário para um número inteiro.
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Opção inválida. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
                    opcao = -1; // Define uma opção inválida para continuar o loop.
                }
            }

            switch (opcao) {
                case 1: gerenciarLivros(); break;
                case 2: gerenciarJornais(); break;
                case 3: gerenciarAutores(); break;
                case 4: gerenciarEditoras(); break;
                case 5: gerenciarCategorias(); break;
                case 6: visualizarCatalogoCompleto(); break;
                case 0:
                    sistema.salvarTodosDados(); // Salva todos os dados antes de encerrar a aplicação.
                    JOptionPane.showMessageDialog(null, "Saindo da aplicação. Dados salvos!", "Adeus!", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    if (opcao != -1) { // Evita exibir mensagem de erro duplicada se já tratada pelo NumberFormatException.
                        JOptionPane.showMessageDialog(null, "Opção inválida. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
        } while (opcao != 0);
    }

    /**
     * Exibe o menu de opções para gerenciamento de Livros (cadastro, edição, exclusão, visualização, busca).
     * Permite ao usuário navegar e realizar operações específicas de livros.
     */
    private void gerenciarLivros() {
        int opcao;
        do {
            String menu = "Gerenciar Livros:\n\n" +
                  "1. Cadastrar Livro\n" +
                  "2. Editar Livro\n" +
                  "3. Excluir Livro\n" +
                  "4. Visualizar Livros\n" +
                  "5. Buscar Livro\n" +
                  "6. Adicionar ao Estoque\n" + 
                  "7. Remover do Estoque\n" +   
                  "0. Voltar ao Menu Principal\n\n" +
                  "Escolha uma opção:";
            String input = JOptionPane.showInputDialog(null, menu, "Gerenciar Livros", JOptionPane.PLAIN_MESSAGE);
            if (input == null) { opcao = 0; } else { try { opcao = Integer.parseInt(input); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(null, "Entrada inválida. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE); opcao = -1; }}

            switch (opcao) {
            case 1: cadastrarLivro(); break;
            case 2: editarLivro(); break;
            case 3: excluirLivro(); break;
            case 4: visualizarLivros(); break;
            case 5: buscarLivro(); break;
            case 6: adicionarEstoqueLivro(); break;
            case 7: removerEstoqueLivro(); break;  
            case 0: break; 
            default: JOptionPane.showMessageDialog(null, "Opção inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        } while (opcao != 0);
    }

    /**
     * Solicita os dados para o cadastro de um novo livro ao usuário
     * e chama o método correspondente na classe {@link Sistema}.
     * Realiza validações de formato de entrada e trata exceções de duplicidade.
     */
    private void cadastrarLivro() {
        String titulo = JOptionPane.showInputDialog("Título do Livro:");
        if (titulo == null) return; // Usuário cancelou.

        float preco;
        int paginas;

        try {
            preco = Float.parseFloat(JOptionPane.showInputDialog("Preço do Livro:"));
            paginas = Integer.parseInt(JOptionPane.showInputDialog("Quantidade de Páginas:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Certifique-se de usar valores válidos para preço e páginas.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String isbn = JOptionPane.showInputDialog("ISBN:");
        if (isbn == null) return;

        Editora editoraSelecionada = selecionarEditora(); // Permite ao usuário selecionar uma editora existente.
        if (editoraSelecionada == null) {
            JOptionPane.showMessageDialog(null, "Cadastro de Livro cancelado. Editora não selecionada ou não cadastrada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Autor> autoresSelecionados = selecionarMultiplosAutores(); // Permite ao usuário selecionar múltiplos autores existentes.
        if (autoresSelecionados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Livro deve ter pelo menos um autor. Cadastro cancelado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Categoria categoriaSelecionada = selecionarCategoria(); // Permite ao usuário selecionar uma categoria existente.
        if (categoriaSelecionada == null) {
            JOptionPane.showMessageDialog(null, "Cadastro de Livro cancelado. Categoria não selecionada ou não cadastrada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (sistema.cadastrarLivro(titulo, preco, editoraSelecionada, paginas, isbn, autoresSelecionados, categoriaSelecionada)) {
                JOptionPane.showMessageDialog(null, "Livro cadastrado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao cadastrar livro.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DuplicidadeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Solicita o ID de um livro a ser editado, coleta os novos dados do usuário
     * e chama o método de edição na classe {@link Sistema}.
     * Inclui validação de entrada, pré-preenchimento de campos e tratamento de exceções.
     */
    private void editarLivro() {
        String idStr = JOptionPane.showInputDialog("Digite o ID do livro a ser editado:");
        if (idStr == null) return;
        try {
            int id = Integer.parseInt(idStr);
            Livro livro = sistema.buscarLivroPorId(id);
            if (livro == null) {
                JOptionPane.showMessageDialog(null, "Livro não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Solicita novos dados, exibindo os valores atuais como sugestão.
            String novoTitulo = JOptionPane.showInputDialog("Novo Título (" + livro.getTitulo() + "):", livro.getTitulo());
            if (novoTitulo == null) return;

            float novoPreco;
            int novaPaginas;

            try {
                novoPreco = Float.parseFloat(JOptionPane.showInputDialog("Novo Preço (" + livro.getPreco() + "):", String.valueOf(livro.getPreco())));
                novaPaginas = Integer.parseInt(JOptionPane.showInputDialog("Nova Quantidade de Páginas (" + livro.getQuantidadePaginas() + "):", String.valueOf(livro.getQuantidadePaginas())));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida. Certifique-se de usar valores válidos para preço e páginas.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String novoIsbn = JOptionPane.showInputDialog("Novo ISBN (" + livro.getIsbn() + "):", livro.getIsbn());
            if (novoIsbn == null) return;

            Editora novaEditora = selecionarEditora(livro.getEditora()); // Pré-seleciona a editora atual.
            if (novaEditora == null) {
                 JOptionPane.showMessageDialog(null, "Edição de Livro cancelada. Editora não selecionada ou não cadastrada.", "Aviso", JOptionPane.WARNING_MESSAGE);
                 return;
            }

            List<Autor> novosAutores = selecionarMultiplosAutores(livro.getAutores()); // Pré-seleciona os autores atuais.
            if (novosAutores.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Livro deve ter pelo menos um autor. Edição cancelada.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Categoria novaCategoria = selecionarCategoria(livro.getCategoria()); // Pré-seleciona a categoria atual.
            if (novaCategoria == null) {
                JOptionPane.showMessageDialog(null, "Edição de Livro cancelada. Categoria não selecionada ou não cadastrada.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                if (sistema.editarLivro(id, novoTitulo, novoPreco, novaEditora, novaPaginas, novoIsbn, novosAutores, novaCategoria)) {
                    JOptionPane.showMessageDialog(null, "Livro editado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao editar livro.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DuplicidadeException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Edição", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Solicita o ID de um livro a ser excluído ao usuário
     * e chama o método de exclusão na classe {@link Sistema}.
     */
    private void excluirLivro() {
        String idStr = JOptionPane.showInputDialog("Digite o ID do livro a ser excluído:");
        if (idStr == null) return;
        try {
            int id = Integer.parseInt(idStr);
            if (sistema.excluirLivro(id)) {
                JOptionPane.showMessageDialog(null, "Livro excluído com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Livro não encontrado ou falha ao excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exibe uma lista formatada de todos os livros atualmente cadastrados no sistema.
     */
    private void visualizarLivros() {
        List<Livro> livros = sistema.getTodosLivros();
        if (livros.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum livro cadastrado.", "Visualizar Livros", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("--- Lista de Livros ---\n\n");
        for (Livro livro : livros) {
            sb.append(livro.toString()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Visualizar Livros", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Permite ao usuário buscar livros por diferentes critérios (título, autor ou categoria)
     * e exibe os resultados da busca.
     */
    private void buscarLivro() {
        String tipoBusca = JOptionPane.showInputDialog("Buscar Livro por:\n1. Título\n2. Autor\n3. Categoria\n0. Voltar");
        if (tipoBusca == null) return;

        List<Livro> resultados = new ArrayList<>();
        try {
            int opcao = Integer.parseInt(tipoBusca);
            if (opcao == 0) return; // Voltar.

            String termo = JOptionPane.showInputDialog("Digite o termo de busca:");
            if (termo == null || termo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Termo de busca não pode ser vazio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            switch (opcao) {
                case 1: resultados = sistema.buscarLivrosPorTitulo(termo); break;
                case 2: resultados = sistema.buscarLivrosPorAutor(termo); break;
                case 3: resultados = sistema.buscarLivrosPorCategoria(termo); break;
                default: JOptionPane.showMessageDialog(null, "Opção de busca inválida.", "Erro", JOptionPane.ERROR_MESSAGE); return;
            }
            exibirResultsadosBuscaLivro(resultados, termo);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Opção inválida. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exibe os resultados de uma busca por livros em uma caixa de diálogo.
     * @param resultados A lista de objetos {@link Livro} encontrados pela busca.
     * @param termo O termo de busca utilizado.
     */
    private void exibirResultsadosBuscaLivro(List<Livro> resultados, String termo) {
        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum livro encontrado para o termo: '" + termo + "'", "Resultado da Busca", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder("--- Resultados da Busca por '" + termo + "' ---\n\n");
            for (Livro livro : resultados) {
                sb.append(livro.toString()).append("\n\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Resultado da Busca", JOptionPane.PLAIN_MESSAGE);
        }
    }

    /**
     * Exibe o menu de opções para gerenciamento de Jornais (cadastro, edição, exclusão, visualização, busca).
     * Permite ao usuário navegar e realizar operações específicas de jornais.
     */
    private void gerenciarJornais() {
        int opcao;
        do {
            String menu = "Gerenciar Jornais:\n\n" +
                        "1. Cadastrar Jornal\n" +
                        "2. Editar Jornal\n" +
                        "3. Excluir Jornal\n" +
                        "4. Visualizar Jornais\n" +
                        "5. Buscar Jornal\n" +
                        "6. Adicionar ao Estoque\n" + 
                        "7. Remover do Estoque\n" +   
                        "0. Voltar ao Menu Principal\n\n" +
                        "Escolha uma opção:";
            String input = JOptionPane.showInputDialog(null, menu, "Gerenciar Jornais", JOptionPane.PLAIN_MESSAGE);
            if (input == null) { opcao = 0; } else { try { opcao = Integer.parseInt(input); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(null, "Entrada inválida.", "Erro", JOptionPane.ERROR_MESSAGE); opcao = -1; }}

            switch (opcao) {
                    case 1: cadastrarJornal(); break;
                    case 2: editarJornal(); break;
                    case 3: excluirJornal(); break;
                    case 4: visualizarJornais(); break;
                    case 5: buscarJornal(); break;
                    case 6: adicionarEstoqueJornal(); break; 
                    case 7: removerEstoqueJornal(); break;   
                    case 0: break;
                    default: JOptionPane.showMessageDialog(null, "Opção inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (opcao != 0);
    }

    /**
     * Solicita os dados para o cadastro de um novo jornal ao usuário
     * e chama o método correspondente na classe {@link Sistema}.
     * Realiza validações de formato de entrada e trata exceções de duplicidade.
     */
    private void cadastrarJornal() {
        String titulo = JOptionPane.showInputDialog("Título do Jornal:");
        if (titulo == null) return;

        float preco;

        try {
            preco = Float.parseFloat(JOptionPane.showInputDialog("Preço do Jornal:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Certifique-se de usar valores válidos para preço.", "Erro", JOptionPane.ERROR_MESSAGE); // TEXTO AJUSTADO
            return;
        }

        Editora editoraSelecionada = selecionarEditora();
        if (editoraSelecionada == null) {
            JOptionPane.showMessageDialog(null, "Cadastro de Jornal cancelado. Editora não selecionada ou não cadastrada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate dataPublicacao = null;
        String dataStr = JOptionPane.showInputDialog("Data de Publicação (DD/MM/AAAA):");
        if (dataStr == null) return;
        try {
            dataPublicacao = LocalDate.parse(dataStr, Util.DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de data inválido. Use DD/MM/AAAA.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (sistema.cadastrarJornal(titulo, preco, editoraSelecionada, dataPublicacao)) {
                JOptionPane.showMessageDialog(null, "Jornal cadastrado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao cadastrar jornal.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DuplicidadeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Solicita o ID de um jornal a ser editado, coleta os novos dados do usuário
     * e chama o método de edição na classe {@link Sistema}.
     * Inclui validação de entrada, pré-preenchimento de campos e tratamento de exceções.
     */
    private void editarJornal() {
        String idStr = JOptionPane.showInputDialog("Digite o ID do jornal a ser editado:");
        if (idStr == null) return;
        
        try {
            int id = Integer.parseInt(idStr);
            Jornal jornal = sistema.buscarJornalPorId(id);
            if (jornal == null) {
                JOptionPane.showMessageDialog(null, "Jornal não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String novoTitulo = JOptionPane.showInputDialog("Novo Título (" + jornal.getTitulo() + "):", jornal.getTitulo());
            if (novoTitulo == null) return;

            float novoPreco;

            try {
                novoPreco = Float.parseFloat(JOptionPane.showInputDialog("Novo Preço (" + jornal.getPreco() + "):", String.valueOf(jornal.getPreco())));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida. Certifique-se de usar valores válidos para preço.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Editora novaEditora = selecionarEditora(jornal.getEditora());
            if (novaEditora == null) {
                 JOptionPane.showMessageDialog(null, "Edição de Jornal cancelada. Editora não selecionada ou não cadastrada.", "Aviso", JOptionPane.WARNING_MESSAGE);
                 return;
            }

            LocalDate novaDataPublicacao = null;
            String dataStr = JOptionPane.showInputDialog(
                "Nova Data de Publicação (DD/MM/AAAA) (" + jornal.getDataPublicacao().format(Util.DATE_FORMATTER) + "):",
                jornal.getDataPublicacao().format(Util.DATE_FORMATTER)
            );
            if (dataStr == null) return;
            try {
                novaDataPublicacao = LocalDate.parse(dataStr, Util.DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Formato de data inválido. Use DD/MM/AAAA.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (sistema.editarJornal(id, novoTitulo, novoPreco, novaEditora, novaDataPublicacao)) {
                    JOptionPane.showMessageDialog(null, "Jornal editado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao editar jornal.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DuplicidadeException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Edição", JOptionPane.ERROR_MESSAGE);
            }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida. Certifique-se de usar valores válidos para preço e estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
    }

    /**
     * Solicita o ID de um jornal a ser excluído ao usuário
     * e chama o método de exclusão na classe {@link Sistema}.
     */
    private void excluirJornal() {
        String idStr = JOptionPane.showInputDialog("Digite o ID do jornal a ser excluído:");
        if (idStr == null) return;
        try {
            int id = Integer.parseInt(idStr);
            if (sistema.excluirJornal(id)) {
                JOptionPane.showMessageDialog(null, "Jornal excluído com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Jornal não encontrado ou falha ao excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exibe uma lista formatada de todos os jornais atualmente cadastrados no sistema.
     */
    private void visualizarJornais() {
        List<Jornal> jornais = sistema.getTodosJornais();
        if (jornais.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum jornal cadastrado.", "Visualizar Jornais", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("--- Lista de Jornais ---\n\n");
        for (Jornal jornal : jornais) {
            sb.append(jornal.toString()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Visualizar Jornais", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Permite ao usuário buscar jornais por diferentes critérios (título ou data de publicação)
     * e exibe os resultados da busca.
     */
    private void buscarJornal() {
        String tipoBusca = JOptionPane.showInputDialog("Buscar Jornal por:\n1. Título\n2. Data de Publicação\n0. Voltar");
        if (tipoBusca == null) return;

        List<Jornal> resultados = new ArrayList<>();
        try {
            int opcao = Integer.parseInt(tipoBusca);
            if (opcao == 0) return; // Voltar.

            String termo = JOptionPane.showInputDialog("Digite o termo de busca:");
            if (termo == null || termo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Termo de busca não pode ser vazio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            switch (opcao) {
                case 1: resultados = sistema.buscarJornaisPorTitulo(termo); break;
                case 2:
                    try {
                        LocalDate data = LocalDate.parse(termo, Util.DATE_FORMATTER);
                        resultados = sistema.buscarJornaisPorData(data);
                    } catch (DateTimeParseException e) {
                        JOptionPane.showMessageDialog(null, "Formato de data inválido. Use DD/MM/AAAA.", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
                default: JOptionPane.showMessageDialog(null, "Opção de busca inválida.", "Erro", JOptionPane.ERROR_MESSAGE); return;
            }
            exibirResultsadosBuscaJornal(resultados, termo);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Opção inválida. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exibe os resultados de uma busca por jornais em uma caixa de diálogo.
     * @param resultados A lista de objetos {@link Jornal} encontrados pela busca.
     * @param termo O termo de busca utilizado.
     */
    private void exibirResultsadosBuscaJornal(List<Jornal> resultados, String termo) {
        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum jornal encontrado para o termo: '" + termo + "'", "Resultado da Busca", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder("--- Resultados da Busca por '" + termo + "' ---\n\n");
            for (Jornal jornal : resultados) {
                sb.append(jornal.toString()).append("\n\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Resultado da Busca", JOptionPane.PLAIN_MESSAGE);
        }
    }

    /**
     * Exibe o menu de opções para gerenciamento de Autores (cadastro e visualização).
     * Permite ao usuário navegar e realizar operações específicas de autores.
     */
    private void gerenciarAutores() {
        int opcao;
        do {
            String menu = "Gerenciar Autores:\n\n" +
                          "1. Cadastrar Autor\n" +
                          "2. Visualizar Autores\n" +
                          "0. Voltar ao Menu Principal\n\n" +
                          "Escolha uma opção:";
            String input = JOptionPane.showInputDialog(null, menu, "Gerenciar Autores", JOptionPane.PLAIN_MESSAGE);
            if (input == null) { opcao = 0; } else { try { opcao = Integer.parseInt(input); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(null, "Entrada inválida.", "Erro", JOptionPane.ERROR_MESSAGE); opcao = -1; }}

            switch (opcao) {
                case 1: cadastrarAutor(); break;
                case 2: visualizarAutores(); break;
                case 0: break;
                default: JOptionPane.showMessageDialog(null, "Opção inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (opcao != 0);
    }

    /**
     * Solicita os dados para o cadastro de um novo autor ao usuário
     * e chama o método correspondente na classe {@link Sistema}.
     * Realiza validações de formato de entrada e trata exceções de duplicidade.
     */
    private void cadastrarAutor() {
        String nome = JOptionPane.showInputDialog("Nome do Autor:");
        if (nome == null) return;
        String nacionalidade = JOptionPane.showInputDialog("Nacionalidade:");
        if (nacionalidade == null) return;
        LocalDate dataNascimento = null;
        String dataStr = JOptionPane.showInputDialog("Data de Nascimento (DD/MM/AAAA):");
        if (dataStr == null) return;
        try {
            dataNascimento = LocalDate.parse(dataStr, Util.DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de data inválido. Use DD/MM/AAAA.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (sistema.cadastrarAutor(nome, nacionalidade, dataNascimento)) {
                JOptionPane.showMessageDialog(null, "Autor cadastrado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao cadastrar autor.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DuplicidadeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exibe uma lista formatada de todos os autores atualmente cadastrados no sistema.
     */
    private void visualizarAutores() {
        List<Autor> autores = sistema.getTodosAutores();
        if (autores.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum autor cadastrado.", "Visualizar Autores", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("--- Lista de Autores ---\n\n");
        for (Autor autor : autores) {
            sb.append(autor.toString()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Visualizar Autores", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Exibe o menu de opções para gerenciamento de Editoras (cadastro e visualização).
     * Permite ao usuário navegar e realizar operações específicas de editoras.
     */
    private void gerenciarEditoras() {
        int opcao;
        do {
            String menu = "Gerenciar Editoras:\n\n" +
                          "1. Cadastrar Editora\n" +
                          "2. Visualizar Editoras\n" +
                          "0. Voltar ao Menu Principal\n\n" +
                          "Escolha uma opção:";
            String input = JOptionPane.showInputDialog(null, menu, "Gerenciar Editoras", JOptionPane.PLAIN_MESSAGE);
            if (input == null) { opcao = 0; } else { try { opcao = Integer.parseInt(input); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(null, "Entrada inválida.", "Erro", JOptionPane.ERROR_MESSAGE); opcao = -1; }}

            switch (opcao) {
                case 1: cadastrarEditora(); break;
                case 2: visualizarEditoras(); break;
                case 0: break;
                default: JOptionPane.showMessageDialog(null, "Opção inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (opcao != 0);
    }

    /**
     * Solicita o nome para o cadastro de uma nova editora ao usuário
     * e chama o método correspondente na classe {@link Sistema}.
     * Trata exceções de duplicidade.
     */
    private void cadastrarEditora() {
        String nome = JOptionPane.showInputDialog("Nome da Editora:");
        if (nome == null) return;

        try {
            if (sistema.cadastrarEditora(nome)) {
                JOptionPane.showMessageDialog(null, "Editora cadastrada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao cadastrar editora.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DuplicidadeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exibe uma lista formatada de todas as editoras atualmente cadastradas no sistema.
     */
    private void visualizarEditoras() {
        List<Editora> editoras = sistema.getTodasEditoras();
        if (editoras.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma editora cadastrada.", "Visualizar Editoras", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("--- Lista de Editoras ---\n\n");
        for (Editora editora : editoras) {
            sb.append(editora.toString()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Visualizar Editoras", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Exibe o menu de opções para gerenciamento de Categorias (cadastro e visualização).
     * Permite ao usuário navegar e realizar operações específicas de categorias.
     */
    private void gerenciarCategorias() {
        int opcao;
        do {
            String menu = "Gerenciar Categorias:\n\n" +
                          "1. Cadastrar Categoria\n" +
                          "2. Visualizar Categorias\n" +
                          "0. Voltar ao Menu Principal\n\n" +
                          "Escolha uma opção:";
            String input = JOptionPane.showInputDialog(null, menu, "Gerenciar Categorias", JOptionPane.PLAIN_MESSAGE);
            if (input == null) { opcao = 0; } else { try { opcao = Integer.parseInt(input); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(null, "Entrada inválida.", "Erro", JOptionPane.ERROR_MESSAGE); opcao = -1; }}

            switch (opcao) {
                case 1: cadastrarCategoria(); break;
                case 2: visualizarCategorias(); break;
                case 0: break;
                default: JOptionPane.showMessageDialog(null, "Opção inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (opcao != 0);
    }

    /**
     * Solicita o nome para o cadastro de uma nova categoria ao usuário
     * e chama o método correspondente na classe {@link Sistema}.
     * Trata exceções de duplicidade.
     */
    private void cadastrarCategoria() {
        String nome = JOptionPane.showInputDialog("Nome da Categoria:");
        if (nome == null) return;

        try {
            if (sistema.cadastrarCategoria(nome)) {
                JOptionPane.showMessageDialog(null, "Categoria cadastrada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao cadastrar categoria.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DuplicidadeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exibe uma lista formatada de todas as categorias atualmente cadastrados no sistema.
     */
    private void visualizarCategorias() {
        List<Categoria> categorias = sistema.getTodasCategorias();
        if (categorias.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma categoria cadastrada.", "Visualizar Categorias", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("--- Lista de Categorias ---\n\n");
        for (Categoria categoria : categorias) {
            sb.append(categoria.toString()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Visualizar Categorias", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Permite ao usuário selecionar uma {@link Editora} de uma lista de editoras disponíveis.
     * Este método sobrecarregado não oferece uma editora pré-selecionada.
     * @return A {@link Editora} selecionada pelo usuário, ou {@code null} se o usuário cancelar
     * ou se não houver editoras cadastradas.
     */
    private Editora selecionarEditora() {
        return selecionarEditora(null);
    }

    /**
     * Permite ao usuário selecionar uma {@link Editora} de uma lista de editoras disponíveis.
     * Permite pré-selecionar uma editora existente para edição.
     * @param editoraAtual A {@link Editora} atualmente associada, para ser pré-selecionada (pode ser {@code null}).
     * @return A {@link Editora} selecionada pelo usuário, ou {@code null} se o usuário cancelar
     * ou se não houver editoras cadastradas.
     */
    private Editora selecionarEditora(Editora editoraAtual) {
        List<Editora> editorasDisponiveis = sistema.getTodasEditoras();
        if (editorasDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma editora cadastrada. Cadastre uma editora primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // Converte a lista de editoras para um array de Object para que o JOptionPane possa exibir as opções.
        Object[] opcoes = editorasDisponiveis.toArray();
        Editora selecionada = (Editora) JOptionPane.showInputDialog(
            null,
            "Selecione a Editora:",
            "Seleção de Editora",
            JOptionPane.QUESTION_MESSAGE,
            null, // Ícone padrão.
            opcoes,
            editoraAtual != null ? editoraAtual : opcoes[0] // Pré-seleciona a editora atual ou a primeira da lista.
        );
        return selecionada;
    }

    /**
     * Permite ao usuário selecionar uma {@link Categoria} de uma lista de categorias disponíveis.
     * Este método sobrecarregado não oferece uma categoria pré-selecionada.
     * @return A {@link Categoria} selecionada pelo usuário, ou {@code null} se o usuário cancelar
     * ou se não houver categorias cadastradas.
     */
    private Categoria selecionarCategoria() {
        return selecionarCategoria(null);
    }

    /**
     * Permite ao usuário selecionar uma {@link Categoria} de uma lista de categorias disponíveis.
     * Permite pré-selecionar uma categoria existente para edição.
     * @param categoriaAtual A {@link Categoria} atualmente associada, para ser pré-selecionada (pode ser {@code null}).
     * @return A {@link Categoria} selecionada pelo usuário, ou {@code null} se o usuário cancelar
     * ou se não houver categorias cadastradas.
     */
    private Categoria selecionarCategoria(Categoria categoriaAtual) {
        List<Categoria> categoriasDisponiveis = sistema.getTodasCategorias();
        if (categoriasDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma categoria cadastrada. Cadastre uma categoria primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Object[] opcoes = categoriasDisponiveis.toArray();
        Categoria selecionada = (Categoria) JOptionPane.showInputDialog(
            null,
            "Selecione a Categoria:",
            "Seleção de Categoria",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcoes,
            categoriaAtual != null ? categoriaAtual : opcoes[0]
        );
        return selecionada;
    }

    /**
     * Permite ao usuário selecionar múltiplos {@link Autor}es de uma lista de autores disponíveis.
     * Este método sobrecarregado não oferece autores pré-selecionados.
     * @return Uma lista de objetos {@link Autor} selecionados. Retorna uma lista vazia se nenhum for selecionado
     * ou se não houver autores cadastrados.
     */
    private List<Autor> selecionarMultiplosAutores() {
        return selecionarMultiplosAutores(new ArrayList<>());
    }

    /**
     * Permite ao usuário selecionar múltiplos {@link Autor}es de uma lista de autores disponíveis.
     * Oferece a opção de pré-selecionar autores já associados, útil durante a edição.
     * @param autoresAtuais Uma lista de objetos {@link Autor} atualmente associados, para pré-seleção.
     * @return Uma lista de objetos {@link Autor} selecionados. Retorna uma lista vazia se nenhum for selecionado
     * ou se não houver autores cadastrados.
     */
    private List<Autor> selecionarMultiplosAutores(List<Autor> autoresAtuais) {
        List<Autor> autoresDisponiveis = sistema.getTodosAutores();
        if (autoresDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum autor cadastrado. Cadastre um autor primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return new ArrayList<>();
        }

        // Cria um array de nomes dos autores para exibição na JList.
        String[] opcoesNomes = autoresDisponiveis.stream()
                                            .map(Autor::getNome)
                                            .toArray(String[]::new);

        // Cria a JList que permite seleção múltipla.
        javax.swing.JList<String> list = new javax.swing.JList<>(opcoesNomes);
        list.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Pré-seleciona os autores que já estão na lista de 'autoresAtuais'.
        List<Integer> indicesSelecionados = new ArrayList<>();
        for (Autor autorAtual : autoresAtuais) {
            for (int i = 0; i < autoresDisponiveis.size(); i++) {
                if (autoresDisponiveis.get(i).getId() == autorAtual.getId()) {
                    indicesSelecionados.add(i);
                    break;
                }
            }
        }
        int[] selectedIndices = indicesSelecionados.stream().mapToInt(Integer::intValue).toArray();
        list.setSelectedIndices(selectedIndices);

        // Exibe a JList dentro de um JOptionPane para que o usuário faça a seleção.
        JOptionPane.showMessageDialog(null, new javax.swing.JScrollPane(list), "Selecione um ou mais Autores:", JOptionPane.PLAIN_MESSAGE);

        // Coleta os objetos Autor correspondentes aos índices selecionados pelo usuário.
        List<Autor> selecionados = new ArrayList<>();
        if (list.getSelectedIndices().length > 0) {
            for (int index : list.getSelectedIndices()) {
                selecionados.add(autoresDisponiveis.get(index));
            }
        }
        return selecionados;
    }

    /**
     * Exibe o catálogo completo de todas as publicações (livros e jornais)
     * cadastradas no sistema em uma única caixa de diálogo.
     */
    private void visualizarCatalogoCompleto() {
        StringBuilder sb = new StringBuilder("--- CATÁLOGO COMPLETO ---\n\n");
        sb.append("--- LIVROS ---\n\n");
        List<Livro> livros = sistema.getTodosLivros();
        if (livros.isEmpty()) {
            sb.append("Nenhum livro cadastrado.\n\n");
        } else {
            for (Livro livro : livros) {
                sb.append(livro.toString()).append("\n\n");
            }
        }

        sb.append("--- JORNAIS ---\n\n");
        List<Jornal> jornais = sistema.getTodosJornais();
        if (jornais.isEmpty()) {
            sb.append("Nenhum jornal cadastrado.\n\n");
        } else {
            for (Jornal jornal : jornais) {
                sb.append(jornal.toString()).append("\n\n");
            }
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Catálogo Completo", JOptionPane.PLAIN_MESSAGE);
    }

        /**
     * Solicita o ID de um livro e a quantidade a ser adicionada ao seu estoque.
     * Realiza validações de entrada e chama o método correspondente no {@link Sistema}.
     */
    private void adicionarEstoqueLivro() {
        String idStr = JOptionPane.showInputDialog("Digite o ID do livro para adicionar estoque:");
        if (idStr == null) return;
        try {
            int id = Integer.parseInt(idStr);
            Livro livro = sistema.buscarLivroPorId(id);
            if (livro == null) {
                JOptionPane.showMessageDialog(null, "Livro não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String qtdStr = JOptionPane.showInputDialog("Quantidade a adicionar ao estoque (atual: " + livro.getEstoqueDisponivel() + "):");
            if (qtdStr == null) return;
            int quantidade;
            try {
                quantidade = Integer.parseInt(qtdStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantidade inválida. Digite um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Chama o novo método no Sistema
                if (sistema.adicionarEstoquePublicacao(livro.getId(), quantidade, "Livro")) {
                    JOptionPane.showMessageDialog(null, quantidade + " unidades adicionadas ao estoque do livro '" + livro.getTitulo() + "'.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao adicionar estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DuplicidadeException e) { // Reutilizando para validação de quantidade
                JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Estoque", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Solicita o ID de um livro e a quantidade a ser removida de seu estoque.
     * Realiza validações de entrada e chama o método correspondente no {@link Sistema}.
     */
    private void removerEstoqueLivro() {
        String idStr = JOptionPane.showInputDialog("Digite o ID do livro para remover estoque:");
        if (idStr == null) return;
        try {
            int id = Integer.parseInt(idStr);
            Livro livro = sistema.buscarLivroPorId(id);
            if (livro == null) {
                JOptionPane.showMessageDialog(null, "Livro não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String qtdStr = JOptionPane.showInputDialog("Quantidade a remover do estoque (atual: " + livro.getEstoqueDisponivel() + "):");
            if (qtdStr == null) return;
            int quantidade;
            try {
                quantidade = Integer.parseInt(qtdStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantidade inválida. Digite um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Chama o novo método no Sistema
                if (sistema.removerEstoquePublicacao(livro.getId(), quantidade, "Livro")) {
                    JOptionPane.showMessageDialog(null, quantidade + " unidades removidas do estoque do livro '" + livro.getTitulo() + "'.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao remover estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DuplicidadeException e) { // Reutilizando para validação de quantidade/estoque insuficiente
                JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Estoque", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Solicita o ID de um jornal e a quantidade a ser adicionada ao seu estoque.
     * Realiza validações de entrada e chama o método correspondente no {@link Sistema}.
     */
    private void adicionarEstoqueJornal() {
        String idStr = JOptionPane.showInputDialog("Digite o ID do jornal para adicionar estoque:");
        if (idStr == null) return;
        try {
            int id = Integer.parseInt(idStr);
            Jornal jornal = sistema.buscarJornalPorId(id);
            if (jornal == null) {
                JOptionPane.showMessageDialog(null, "Jornal não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String qtdStr = JOptionPane.showInputDialog("Quantidade a adicionar ao estoque (atual: " + jornal.getEstoqueDisponivel() + "):");
            if (qtdStr == null) return;
            int quantidade;
            try {
                quantidade = Integer.parseInt(qtdStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantidade inválida. Digite um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Chama o novo método no Sistema
                if (sistema.adicionarEstoquePublicacao(jornal.getId(), quantidade, "Jornal")) {
                    JOptionPane.showMessageDialog(null, quantidade + " unidades adicionadas ao estoque do jornal '" + jornal.getTitulo() + "'.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao adicionar estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DuplicidadeException e) { // Reutilizando para validação de quantidade
                JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Estoque", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Solicita o ID de um jornal e a quantidade a ser removida de seu estoque.
     * Realiza validações de entrada e chama o método correspondente no {@link Sistema}.
     */
    private void removerEstoqueJornal() {
        String idStr = JOptionPane.showInputDialog("Digite o ID do jornal para remover estoque:");
        if (idStr == null) return;
        try {
            int id = Integer.parseInt(idStr);
            Jornal jornal = sistema.buscarJornalPorId(id);
            if (jornal == null) {
                JOptionPane.showMessageDialog(null, "Jornal não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String qtdStr = JOptionPane.showInputDialog("Quantidade a remover do estoque (atual: " + jornal.getEstoqueDisponivel() + "):");
            if (qtdStr == null) return;
            int quantidade;
            try {
                quantidade = Integer.parseInt(qtdStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantidade inválida. Digite um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Chama o novo método no Sistema
                if (sistema.removerEstoquePublicacao(jornal.getId(), quantidade, "Jornal")) {
                    JOptionPane.showMessageDialog(null, quantidade + " unidades removidas do estoque do jornal '" + jornal.getTitulo() + "'.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao remover estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DuplicidadeException e) { // Reutilizando para validação de quantidade/estoque insuficiente
                JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Estoque", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

}