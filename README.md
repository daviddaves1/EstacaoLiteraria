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

1.  **Limpar e Criar `bin/` (para compilados):**
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
2.  **Criar `data/` (para persistência):**
    * No terminal (dentro de `EstacaoLiteraria/`):
        * Windows / Linux:
            ```bash
            mkdir data
            ```
3.  **Compilar o Código Fonte:**
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
### 4. Rodar a Aplicação! 🎉

* No terminal (dentro de `EstacaoLiteraria/`):
    * Windows / Linux:
        ```bash
        java -jar EstacaoLiteraria.jar
        ```
* A interface gráfica (`JOptionPane`) aparecerá para você interagir.

---

## Funcionalidades Principais ✨

A aplicação permite ao administrador gerenciar o estoque da livraria com:

* **Cadastro:** Livros, Jornais, Autores, Editoras, Categorias.
* **Edição e Exclusão:** Gerencie Livros e Jornais.
* **Visualização:** Listagens completas e específicas.
* **Busca:** Pesquise Livros (título, autor, categoria) e Jornais (título, data).
* **Persistência:** Dados salvos e carregados automaticamente em arquivos locais.
* **Tratamento de Exceções:** Alertas em caso de dados repetidos.

---

## Documentação do Javadoc 📖

Para mais detalhes sobre o Javadoc:

* **Javadoc (Documentação da API):** Abra `EstacaoLiteraria\docs\javadoc\index.html` em seu navegador.