# Estação Literária

Projeto final da disciplina de Programação Orientada a Objetos (POO).

## Descrição

Sistema de gerenciamento de estoque de livros e jornais para uma livraria. Desenvolvido em Java utilizando Programação Orientada a Objetos (POO), persistência de dados em arquivos locais e interface gráfica com JOptionPane.

## Integrantes

Este projeto foi desenvolvido pelos integrantes:

| Matrícula | Nome | Usuário Git |
|------------|----------------------------|-------------------------------------------------|
| 202403067 | David Daves Ferreira Pinto | [daviddaves1](https://github.com/daviddaves1) |
| 202403080 | Kezer dos Santos Souza | [KezerSouza](https://github.com/KezerSouza) |
| 202403091 | Rodrigo Luiz Ferreira Ramos| [rodrigoluizf](https://github.com/rodrigoluizf) |

---

## Como Rodar a Aplicação Pela Primeira Vez (Tutorial Completo)

Este tutorial irá te guiar passo a passo para configurar e rodar a aplicação "Estação Literária" em seu computador, seja você um usuário de Windows ou Linux.

### Passo 1: Pré-requisitos e Preparação

Antes de começar, certifique-se de que você tem o ambiente Java configurado:

1.  **Verificar a Instalação do JDK (Java Development Kit):**
    * Abra um **terminal** (no Windows, pode ser o `Prompt de Comando` ou `PowerShell`; no Linux, o `Terminal` padrão).
    * Digite os seguintes comandos e pressione Enter após cada um:
        ```bash
        java -version
        javac -version
        ```
    * **Verificação:** Você deve ver uma saída indicando a versão do Java e do Javac (compilador Java), por exemplo, `javac 17.0.15`. Se um desses comandos não for reconhecido, você precisará instalar ou configurar o JDK (versão 11 ou superior) e adicioná-lo ao `PATH` do seu sistema.

2.  **Localizar a Pasta do Projeto:**
    * Encontre a pasta principal do projeto `EstacaoLiteraria/` no seu computador. Esta pasta contém as pastas `src/`, `data/`, `docs/`, `test/` e o arquivo `EstacaoLiteraria.jar`.
    * Abra o terminal e navegue até essa pasta.

    * **Exemplo (para Windows):** Se a pasta `EstacaoLiteraria` estiver em `C:\Users\SeuNome\Downloads\`, você digitaria:
        ```bash
        cd C:\Users\SeuNome\Downloads\EstacaoLiteraria
        ```
    * **Exemplo (para Linux):** Se a pasta estiver em `/home/seunome/projetos/`, você digitaria:
        ```bash
        cd /home/seunome/projetos/EstacaoLiteraria
        ```
    * Confirme que você está no diretório correto listando seu conteúdo:
        * Windows: `dir`
        * Linux: `ls`

### Passo 2: Preparar o Ambiente de Compilação e Dados

1.  **Limpar e Criar a Pasta `bin/` (para arquivos compilados):**
    * No terminal (já dentro da pasta `EstacaoLiteraria/`), execute o comando apropriado para o seu sistema:

    * **No Windows (PowerShell):**
        ```powershell
        Remove-Item -Path bin -Recurse -Force -ErrorAction SilentlyContinue
        mkdir bin
        ```
    * **No Windows (Prompt de Comando):**
        ```cmd
        rmdir /s /q bin
        mkdir bin
        ```
    * **No Linux (Bash / Zsh):**
        ```bash
        rm -rf bin
        mkdir bin
        ```
2.  **Criar a Pasta `data/` (para persistência de dados):**
    * No terminal (ainda dentro da pasta `EstacaoLiteraria/`), execute:

    * **No Windows (Prompt de Comando ou PowerShell):**
        ```bash
        mkdir data
        ```
    * **No Linux (Bash / Zsh):**
        ```bash
        mkdir data
        ```
    * *(É importante que esta pasta exista, caso contrário, a aplicação não conseguirá salvar os dados e reportará um erro.)*

### Passo 3: Compilar o Código Fonte

Agora vamos transformar o código Java (`.java` files) em código que a máquina virtual Java entende (`.class` files).

1.  **Verificar a Declaração de Pacote:**
    * Abra seus arquivos Java dentro da pasta `src/` (ex: `Main.java`, `Sistema.java`, `Livro.java`, etc.).
    * **Certifique-se de que a primeira linha em CADA arquivo `.java` é `package src;`.** Isso é fundamental para a organização e compilação correta.

2.  **Compilar:** No terminal (ainda no diretório raiz do projeto `EstacaoLiteraria/`), execute o comando de compilação apropriado para o seu sistema:

    * **No Windows (Prompt de Comando ou PowerShell):**
        ```bash
        javac -d bin -encoding UTF-8 -sourcepath src src/*.java
        ```
    * **No Linux (Bash / Zsh):**
        ```bash
        javac -d bin -encoding UTF-8 -sourcepath src src/*.java
        ```
### Passo 4: Gerar o Arquivo JAR Executável

Vamos empacotar todo o código compilado em um único arquivo JAR (`.jar`), que facilita a distribuição e execução.

1.  **Gerar o JAR:** No terminal (ainda no diretório raiz do projeto `EstacaoLiteraria/`), execute o comando (funciona tanto no Windows quanto no Linux):
    ```bash
    jar cfe EstacaoLiteraria.jar src.Main -C bin .
    ```
### Passo 5: Executar a Aplicação!

Com o JAR criado, a execução é simples.

1.  **Executar o JAR:** No terminal (ainda no diretório raiz do projeto `EstacaoLiteraria/`), digite:
    ```bash
    java -jar EstacaoLiteraria.jar
    ```

2.  **Interação:**
    * A interface gráfica da Estação Literária será exibida através de janelas `JOptionPane`.
    * Você pode começar a interagir com o sistema, cadastrando livros, jornais, autores, etc.
    * Ao sair da aplicação (opção `0` no menu principal), os dados serão salvos na pasta `data/`.

---

## Funcionalidades Principais

A aplicação permite ao administrador gerenciar o estoque da livraria com as seguintes operações:

* **Cadastro:** Livros, Jornais, Autores, Editoras, Categorias.
* **Edição:** Alteração de dados de Livros e Jornais.
* **Exclusão:** Remoção de Livros e Jornais do estoque.
* **Visualização:** Listagem completa de todos os itens cadastrados (Catálogo Completo) e listas específicas por tipo.
* **Busca:** Pesquisa de Livros (por título, autor, categoria) e Jornais (por título, data de publicação).
* **Persistência de Dados:** Todos os dados são automaticamente salvos em arquivos locais (`.dat`) e carregados ao iniciar a aplicação, garantindo que as informações não sejam perdidas.
* **Tratamento de Exceções:** O sistema inclui tratamento de erros, como a detecção de duplicidade de registros, alertando o usuário.

---

## Documentação do Javadoc

Para uma visão mais detalhada do Javadoc, consulte o seguinte caminho no projeto:

* **Javadoc (Documentação da API):** Abra o arquivo `docs/javadoc/index.html` em seu navegador web.