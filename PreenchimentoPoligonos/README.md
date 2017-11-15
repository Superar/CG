# Trabalho 1 - Computação Gráfica

#### Alunos:

#### Felipe Sampaio de Souza - 619523

#### Marcio Lima Inácio - 587265

#### Pedro Henrique Migliatti - 619744

---

## Pré-requisitos

Para compilar o programa, deve-se ter a ferramenta `Apache Maven` instalada. Para instalá-la, basta executar a seguinte linha de comando:

`apt install maven`

Para checar a instalação, basta executar:

`mvn --version`

Este trabalho foi implementado com o uso do `Apache Maven 3.3.9`

## Como compilar

Para gerar o arquivo `.jar`, deve-se inicializar o terminal na pasta inicial do projeto. Esta pasta deve conter os seguintes arquivos:

`pom.xml    PreenchimentoPoligonos.iml  README.md   src`

Para compilar, o seguinte comando deve ser executado:

`mvn clean package`

Uma pasta `target` será criada com o arquivo `PreenchimentoPoligonos-1.0-jar-with-dependencies.jar`.

## Como executar

Existem duas maneiras de executar o projeto após a compilação.

- Através do Maven: `mvn exec:exec`
- Através da linha da linha de comando: `java -jar target/PreenchimentoPoligonos-1.0-jar-with-dependencies.jar`