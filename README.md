# Mini Autorizador

Projeto de autorização de transações desenvolvido com Spring Boot.

## Tecnologias utilizadas
- Java 17
- Spring Boot 3.x
- MySQL
- JPA/Hibernate

## Como executar

1. Clone o repositório:
```bash
git clone https://github.com/FlaSBenites/miniautorizador.git
```

2. Execute com Maven:
```bash
mvn spring-boot:run
```

## Endpoints

- POST `/cartoes` - Cria um novo cartão
- GET `/cartoes/{numero}` - Consulta saldo
- POST `/transacoes` - Autoriza transação
