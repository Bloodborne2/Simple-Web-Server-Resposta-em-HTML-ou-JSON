# Simple Web Server – Resposta em HTML ou JSON

Este projeto é uma implementação simples de um servidor HTTP em Java capaz de retornar dados no formato HTML ou JSON, dependendo do valor enviado no cabeçalho `Accept` da requisição.

## Descrição
O servidor lê a requisição HTTP recebida e identifica o valor do cabeçalho `Accept`.  
Se for `text/html`, ele envia uma resposta no formato HTML.  
Se for `application/json`, ele envia uma resposta no formato JSON.  
A lógica de formatação está desacoplada por meio da interface `OpcaoSaida` e suas implementações (`OpcaoSaidaHTML` e `OpcaoSaidaJSON`).

## Estrutura dos Arquivos
- `SimpleWebServer.java` → Classe principal que inicia o servidor, interpreta as requisições e define o formato de saída.
- `OpcaoSaida.java` → Interface para padronizar o método de geração de resposta.
- `OpcaoSaidaHTML.java` → Implementação que retorna resposta HTML.
- `OpcaoSaidaJSON.java` → Implementação que retorna resposta JSON.

## Exemplos de teste com cURL:

Requisição HTML:

http://localhost:8080/?nome=Fulano&email=fulano@email.com

Requisição JSON (cmd):

curl -v -H "Accept: application/json" "http://localhost:8080?nome=Fulano&email=fulano@email.com"


