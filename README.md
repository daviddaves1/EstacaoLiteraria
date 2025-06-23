# Estação Literária 📚

Projeto final da disciplina de Programação Orientada a Objetos (POO).

## Descrição

Sistema de gerenciamento de estoque de livros e jornais para uma livraria. Desenvolvido em Java com POO, persistência de dados em arquivos locais e interface gráfica com JOptionPane.

## Integrantes 🧑‍💻

Este projeto foi desenvolvido pelos integrantes:

| Matrícula | Nome | Usuário Git |
|------------|----------------------------|-------------------------------------------------|
| 202403067 | David Daves Ferreira Pinto | [daviddaves1](https://github.com/daviddaves1) |
| 202403080 | Kezer dos Santos Souza | [KezerSouza](https://github.com/KezerSouza) |
| 202403091 | Rodrigo Luiz Ferreira Ramos| [rodrigoluizf](https://github.com/rodrigoluizf) |

---

## Como Rodar a Aplicação Pela Primeira Vez (Guia Rápido) 🚀

Este tutorial te guiará para executar a "Estação Literária" no Windows ou Linux.

### 1. Pré-requisitos e Setup Inicial ✅

* **Java JDK (v11 ou superior):**
    * Abra o terminal.
    * Verifique a instalação do JDK:
        ```bash
        java -version
        javac -version
        ```
    * *Se não reconhecer, instale o JDK e configure seu `PATH`.*
* **Localize o Projeto:**
    * Encontre a pasta `EstacaoLiteraria/` no seu computador.
    * Navegue até ela no terminal:
        * Exemplo (Windows): `cd C:\Users\seunome\Downloads\EstacaoLiteraria`
        * Exemplo (Linux): `cd /home/seunome/Downloads/EstacaoLiteraria`

### 2. Preparar Pastas e Compilar 🛠️

* **Limpar e Criar `bin/` (para compilados):**
    * No terminal (dentro de `EstacaoLiteraria/`):
        * Windows (PowerShell):
            ```powershell
            Remove-Item -Path bin -Recurse -Force -ErrorAction SilentlyContinue
            mkdir bin
            ```
        * Windows (Prompt de Comando):
            ```cmd
            rmdir /s /q bin
            mkdir bin
            ```
        * Linux:
            ```bash
            rm -rf bin
            mkdir bin
            ```
* **Criar `data/` (para persistência):**
    * No terminal (dentro de `EstacaoLiteraria/`):
        * Windows / Linux:
            ```bash
            mkdir data
            ```
* **Compilar o Código Fonte:**
    * Compile no terminal (dentro de `EstacaoLiteraria/`):
        * Windows / Linux:
            ```bash
            javac -d bin -encoding UTF-8 -sourcepath src src/*.java
            ```

### 3. Gerar o JAR Executável 📦

* No terminal (dentro de `EstacaoLiteraria/`):
    * Windows / Linux:
        ```bash
        jar cfe EstacaoLiteraria.jar src.Main -C bin .
        ```

### 4. Rodar a Aplicação e Gerar Artefatos Finais! 🎉

* **Rodar a Aplicação:**
    * No terminal (dentro de `EstacaoLiteraria/`):
        * Windows / Linux:
            ```bash
            java -jar EstacaoLiteraria.jar
            ```
    * A interface gráfica (`JOptionPane`) aparecerá para você interagir.

* **Geração de Documentação (Javadoc):**
    * No terminal (dentro de `EstacaoLiteraria/`), execute o comando apropriado para o seu sistema:

    * **Windows (PowerShell):**
        ```powershell
        javadoc -d docs/javadoc -encoding UTF-8 -sourcepath src (Get-ChildItem -Path src -Recurse -Include *.java | Select-Object -ExpandProperty FullName)
        ```
    * **Windows (Prompt de Comando):**
        ```cmd
        FOR /R src %f IN (*.java) DO @javadoc -d docs/javadoc -encoding UTF-8 -sourcepath src "%f"
        ```
    * **Linux:**
        ```bash
        javadoc -d docs/javadoc -encoding UTF-8 -sourcepath src src/*.java
        ```
    * A documentação HTML será criada em `docs/javadoc/`. Você pode abri-la acessando `docs/javadoc/index.html` em seu navegador.

---

## Funcionalidades Principais ✨

A aplicação permite ao administrador gerenciar o estoque da livraria com:

* **Cadastro:** Livros, Jornais, Autores, Editoras, Categorias.
* **Edição e Exclusão:** Gerencie Livros e Jornais.
* **Visualização:** Listagens completas e específicas.
* **Busca:** Pesquise Livros (título, autor, categoria) e Jornais (título, data).
* **Persistência:** Dados salvos e carregados automaticamente em arquivos locais.
* **Tratamento de Exceções:** Alertas em caso de dados repetidos.