# Engenharia de Software II - Influência de boas práticas

## Objetivo
O sistema consiste em uma API REST que extrai e analisa, utilizando uma LLM do tipo text-generation ([Mistral 7B](https://ollama.com/library/mistral)) por meio do framework Spring AI, issues relacionadas a Bugs em projetos de software (no caso, para a atividade, foi escolhido o projeto PyTorch) e os classifica em uma ou mais classes listadas de possíveis problemas de software: Problemas na Arquitetura de Software e/ou Padrões e Estilos Arquiteturais e/ou Padrões de Projeto.

## Tecnologias utlizadas
- Spring Boot
- Spring JPA
- Spring AI
- Ollama
- PostgreSQL
