package src;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Interface {
    private Sistema sistema;

    public Interface(Sistema sistema) {
        this.sistema = sistema;
    }

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

            if (input == null) {
                opcao = 0;
            } else {
                try {
                    opcao = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Opção inválida. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
                    opcao = -1;
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
                    sistema.salvarTodosDados();
                    JOptionPane.showMessageDialog(null, "Saindo da aplicação. Dados salvos!", "Adeus!", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    if (opcao != -1) {
                        JOptionPane.showMessageDialog(null, "Opção inválida. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
        } while (opcao != 0);
    }

    //Métodos de Gerenciamento de Livros
    private void gerenciarLivros() {
        int opcao;
        do {
            String menu = "Gerenciar Livros:\n\n" +
                          "1. Cadastrar Livro\n" +
                          "2. Editar Livro\n" +
                          "3. Excluir Livro\n" +
                          "4. Visualizar Livros\n" +
                          "5. Buscar Livro\n" +
                          "0. Voltar ao Menu Principal\n\n" +
                          "Escolha uma opção:";
            String input = JOptionPane.showInputDialog(null, menu, "Gerenciar Livros", JOptionPane.PLAIN_MESSAGE);
            if (input == null) { opcao = 0; } else { try { opcao = Integer.parseInt(input); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(null, "Entrada inválida.", "Erro", JOptionPane.ERROR_MESSAGE); opcao = -1; }}

            switch (opcao) {
                case 1: cadastrarLivro(); break;
                case 2: editarLivro(); break;
                case 3: excluirLivro(); break;
                case 4: visualizarLivros(); break;
                case 5: buscarLivro(); break;
                case 0: break;
                default: JOptionPane.showMessageDialog(null, "Opção inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (opcao != 0);
    }

    private void cadastrarLivro() {
        String titulo = JOptionPane.showInputDialog("Título do Livro:");
        if (titulo == null) return;
        float preco;
        int estoque;
        int paginas;
        try {
            preco = Float.parseFloat(JOptionPane.showInputDialog("Preço do Livro:"));
            estoque = Integer.parseInt(JOptionPane.showInputDialog("Estoque Inicial:"));
            paginas = Integer.parseInt(JOptionPane.showInputDialog("Quantidade de Páginas:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada numérica inválida para preço, estoque ou páginas.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String isbn = JOptionPane.showInputDialog("ISBN:");
        if (isbn == null) return;

        Editora editoraSelecionada = selecionarEditora();
        if (editoraSelecionada == null) {
            JOptionPane.showMessageDialog(null, "Cadastro de Livro cancelado. Editora não selecionada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Autor> autoresSelecionados = selecionarMultiplosAutores();
        if (autoresSelecionados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Livro deve ter pelo menos um autor. Cadastro cancelado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Categoria categoriaSelecionada = selecionarCategoria();
        if (categoriaSelecionada == null) {
            JOptionPane.showMessageDialog(null, "Cadastro de Livro cancelado. Categoria não selecionada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (sistema.cadastrarLivro(titulo, preco, estoque, editoraSelecionada, paginas, isbn, autoresSelecionados, categoriaSelecionada)) {
                JOptionPane.showMessageDialog(null, "Livro cadastrado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao cadastrar livro.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DuplicidadeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
        }
    }

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

            String novoTitulo = JOptionPane.showInputDialog("Novo Título (" + livro.getTitulo() + "):", livro.getTitulo());
            if (novoTitulo == null) return;
            float novoPreco;
            int novoEstoque;
            int novaPaginas;
            try {
                novoPreco = Float.parseFloat(JOptionPane.showInputDialog("Novo Preço (" + livro.getPreco() + "):", String.valueOf(livro.getPreco())));
                novoEstoque = Integer.parseInt(JOptionPane.showInputDialog("Novo Estoque (" + livro.getEstoqueDisponivel() + "):", String.valueOf(livro.getEstoqueDisponivel())));
                novaPaginas = Integer.parseInt(JOptionPane.showInputDialog("Nova Quantidade de Páginas (" + livro.getQuantidadePaginas() + "):", String.valueOf(livro.getQuantidadePaginas())));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada numérica inválida para preço, estoque ou páginas.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String novoIsbn = JOptionPane.showInputDialog("Novo ISBN (" + livro.getIsbn() + "):", livro.getIsbn());
            if (novoIsbn == null) return;

            Editora novaEditora = selecionarEditora(livro.getEditora());
            if (novaEditora == null) {
                 JOptionPane.showMessageDialog(null, "Edição de Livro cancelada. Editora não selecionada.", "Aviso", JOptionPane.WARNING_MESSAGE);
                 return;
            }

            List<Autor> novosAutores = selecionarMultiplosAutores(livro.getAutores());
            if (novosAutores.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Livro deve ter pelo menos um autor. Edição cancelada.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Categoria novaCategoria = selecionarCategoria(livro.getCategoria());
            if (novaCategoria == null) {
                JOptionPane.showMessageDialog(null, "Edição de Livro cancelada. Categoria não selecionada.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                if (sistema.editarLivro(id, novoTitulo, novoPreco, novoEstoque, novaEditora, novaPaginas, novoIsbn, novosAutores, novaCategoria)) {
                    JOptionPane.showMessageDialog(null, "Livro editado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao editar livro.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DuplicidadeException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Edição", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

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
            JOptionPane.showMessageDialog(null, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

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

    private void buscarLivro() {
        String tipoBusca = JOptionPane.showInputDialog("Buscar Livro por:\n1. Título\n2. Autor\n3. Categoria\n0. Voltar");
        if (tipoBusca == null) return;

        List<Livro> resultados = new ArrayList<>();
        try {
            int opcao = Integer.parseInt(tipoBusca);
            String termo = JOptionPane.showInputDialog("Digite o termo de busca:");
            if (termo == null || termo.trim().isEmpty()) return;

            switch (opcao) {
                case 1: resultados = sistema.buscarLivrosPorTitulo(termo); break;
                case 2: resultados = sistema.buscarLivrosPorAutor(termo); break;
                case 3: resultados = sistema.buscarLivrosPorCategoria(termo); break;
                case 0: return;
                default: JOptionPane.showMessageDialog(null, "Opção de busca inválida.", "Erro", JOptionPane.ERROR_MESSAGE); return;
            }
            exibirResultadosBuscaLivro(resultados, termo);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Opção inválida. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exibirResultadosBuscaLivro(List<Livro> resultados, String termo) {
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

    //Métodos de Gerenciamento de Jornais
    private void gerenciarJornais() {
        int opcao;
        do {
            String menu = "Gerenciar Jornais:\n\n" +
                          "1. Cadastrar Jornal\n" +
                          "2. Editar Jornal\n" +
                          "3. Excluir Jornal\n" +
                          "4. Visualizar Jornais\n" +
                          "5. Buscar Jornal\n" +
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
                case 0: break;
                default: JOptionPane.showMessageDialog(null, "Opção inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (opcao != 0);
    }

    private void cadastrarJornal() {
        String titulo = JOptionPane.showInputDialog("Título do Jornal:");
        if (titulo == null) return;
        float preco;
        int estoque;
        try {
            preco = Float.parseFloat(JOptionPane.showInputDialog("Preço do Jornal:"));
            estoque = Integer.parseInt(JOptionPane.showInputDialog("Estoque Inicial:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada numérica inválida para preço ou estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Editora editoraSelecionada = selecionarEditora();
        if (editoraSelecionada == null) {
            JOptionPane.showMessageDialog(null, "Cadastro de Jornal cancelado. Editora não selecionada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate dataPublicacao = null;
        String dataStr = JOptionPane.showInputDialog("Data de Publicação (AAAA-MM-DD):");
        if (dataStr == null) return;
        try {
            dataPublicacao = LocalDate.parse(dataStr);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de data inválido. Use AAAA-MM-DD.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (sistema.cadastrarJornal(titulo, preco, estoque, editoraSelecionada, dataPublicacao)) {
                JOptionPane.showMessageDialog(null, "Jornal cadastrado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao cadastrar jornal.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DuplicidadeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
        }
    }

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
            int novoEstoque;
            try {
                novoPreco = Float.parseFloat(JOptionPane.showInputDialog("Novo Preço (" + jornal.getPreco() + "):", String.valueOf(jornal.getPreco())));
                novoEstoque = Integer.parseInt(JOptionPane.showInputDialog("Novo Estoque (" + jornal.getEstoqueDisponivel() + "):", String.valueOf(jornal.getEstoqueDisponivel())));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada numérica inválida para preço ou estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Editora novaEditora = selecionarEditora(jornal.getEditora());
            if (novaEditora == null) {
                 JOptionPane.showMessageDialog(null, "Edição de Jornal cancelada. Editora não selecionada.", "Aviso", JOptionPane.WARNING_MESSAGE);
                 return;
            }

            LocalDate novaDataPublicacao = null;
            String dataStr = JOptionPane.showInputDialog("Nova Data de Publicação (AAAA-MM-DD) (" + jornal.getDataPublicacao() + "):", jornal.getDataPublicacao().toString());
            if (dataStr == null) return;
            try {
                novaDataPublicacao = LocalDate.parse(dataStr);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Formato de data inválido. Use AAAA-MM-DD.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (sistema.editarJornal(id, novoTitulo, novoPreco, novoEstoque, novaEditora, novaDataPublicacao)) {
                    JOptionPane.showMessageDialog(null, "Jornal editado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao editar jornal.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DuplicidadeException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Edição", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

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
            JOptionPane.showMessageDialog(null, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

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

    private void buscarJornal() {
        String tipoBusca = JOptionPane.showInputDialog("Buscar Jornal por:\n1. Título\n2. Data de Publicação\n0. Voltar");
        if (tipoBusca == null) return;

        List<Jornal> resultados = new ArrayList<>();
        try {
            int opcao = Integer.parseInt(tipoBusca);
            String termo = JOptionPane.showInputDialog("Digite o termo de busca:");
            if (termo == null || termo.trim().isEmpty()) return;

            switch (opcao) {
                case 1: resultados = sistema.buscarJornaisPorTitulo(termo); break;
                case 2:
                    try {
                        LocalDate data = LocalDate.parse(termo);
                        resultados = sistema.buscarJornaisPorData(data);
                    } catch (DateTimeParseException e) {
                        JOptionPane.showMessageDialog(null, "Formato de data inválido. Use AAAA-MM-DD.", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
                case 0: return;
                default: JOptionPane.showMessageDialog(null, "Opção de busca inválida.", "Erro", JOptionPane.ERROR_MESSAGE); return;
            }
            exibirResultadosBuscaJornal(resultados, termo);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Opção inválida. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exibirResultadosBuscaJornal(List<Jornal> resultados, String termo) {
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

    //Métodos de Gerenciamento de Autores
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

    private void cadastrarAutor() {
        String nome = JOptionPane.showInputDialog("Nome do Autor:");
        if (nome == null) return;
        String nacionalidade = JOptionPane.showInputDialog("Nacionalidade:");
        if (nacionalidade == null) return;
        LocalDate dataNascimento = null;
        String dataStr = JOptionPane.showInputDialog("Data de Nascimento (AAAA-MM-DD):");
        if (dataStr == null) return;
        try {
            dataNascimento = LocalDate.parse(dataStr);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de data inválido. Use AAAA-MM-DD.", "Erro", JOptionPane.ERROR_MESSAGE);
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

    //Métodos de Gerenciamento de Editoras
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

    //Métodos de Gerenciamento de Categorias
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

    //Seletores para associação (Auxiliares da Interface)
    private Editora selecionarEditora() {
        return selecionarEditora(null);
    }

    private Editora selecionarEditora(Editora editoraAtual) {
        List<Editora> editorasDisponiveis = sistema.getTodasEditoras();
        if (editorasDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma editora cadastrada. Cadastre uma editora primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Object[] opcoes = editorasDisponiveis.toArray();
        Editora selecionada = (Editora) JOptionPane.showInputDialog(
            null,
            "Selecione a Editora:",
            "Seleção de Editora",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcoes,
            editoraAtual != null ? editoraAtual : opcoes[0]
        );
        return selecionada;
    }

    private Categoria selecionarCategoria() {
        return selecionarCategoria(null);
    }

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

    private List<Autor> selecionarMultiplosAutores() {
        return selecionarMultiplosAutores(new ArrayList<>());
    }

    private List<Autor> selecionarMultiplosAutores(List<Autor> autoresAtuais) {
        List<Autor> autoresDisponiveis = sistema.getTodosAutores();
        if (autoresDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum autor cadastrado. Cadastre um autor primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return new ArrayList<>();
        }

        String[] opcoesNomes = autoresDisponiveis.stream()
                                            .map(Autor::getNome)
                                            .toArray(String[]::new);

        javax.swing.JList<String> list = new javax.swing.JList<>(opcoesNomes);
        list.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

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

        JOptionPane.showMessageDialog(null, new javax.swing.JScrollPane(list), "Selecione um ou mais Autores:", JOptionPane.PLAIN_MESSAGE);

        List<Autor> selecionados = new ArrayList<>();
        if (list.getSelectedIndices().length > 0) {
            for (int index : list.getSelectedIndices()) {
                selecionados.add(autoresDisponiveis.get(index));
            }
        }
        return selecionados;
    }

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
}