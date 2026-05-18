# Nexus Feedback Processor

Sistema inteligente de processamento assíncrono de feedbacks desenvolvido com Java moderno, Spring Boot e integração com Large Language Models (LLMs) utilizando Spring AI + Gemini.

O projeto foi construído com foco em:

- Arquitetura enterprise
- Alta concorrência
- Escalabilidade
- Processamento inteligente de linguagem natural
- Microsserviços modernos
- Clean Code
- Imutabilidade
- APIs REST performáticas

---

# Visão Geral

O Nexus Feedback Processor é um microserviço responsável por:

- Receber feedbacks de usuários
- Validar dados de entrada
- Persistir eventos de feedback
- Gerenciar status de processamento
- Executar análise textual inteligente via IA
- Extrair palavras semanticamente válidas
- Preparar arquitetura para ambientes distribuídos

A aplicação foi desenhada para servir como base para plataformas SaaS, CRMs, sistemas de suporte, análise de sentimento, automação de atendimento e pipelines inteligentes orientados por IA.

---

# Arquitetura do Projeto

Estrutura baseada em separação clara de responsabilidades:

```text
src/main/java/nexus/feedback/processor
│
├── controller
│   └── Camada REST da aplicação
│
├── dto
│   └── Contratos de entrada e saída
│
├── service
│   ├── Regras de negócio
│   └── Integração com IA
│
├── repository
│   └── Persistência concorrente
│
├── model
│   └── Entidades imutáveis
│
├── exception
│   └── Tratamento centralizado de falhas
│
└── Application.java
```

---

# Tecnologias Utilizadas

## Backend

- Java 21
- Spring Boot 3.3.1
- Spring Web
- Spring Validation
- Spring AI
- Redis Starter
- Maven

---

## Inteligência Artificial

- Gemini 2.5 Flash
- API compatível OpenAI
- Spring AI ChatClient

---

## Concorrência e Performance

- Virtual Threads
- ConcurrentHashMap
- Arquitetura stateless
- Thread-safe repository

---

# Principais Conceitos Aplicados

## Imutabilidade

A entidade principal utiliza Java Records:

```java
public record Feedback(...)
```

Benefícios:

- Thread safety
- Menor mutabilidade acidental
- Código mais limpo
- Melhor legibilidade
- Menor boilerplate

---

## Virtual Threads

A aplicação utiliza Virtual Threads do Java moderno:

```yaml
spring:
  threads:
    virtual:
      enabled: true
```

Benefícios:

- Escalabilidade massiva
- Melhor throughput
- Menor consumo de memória
- Melhor gerenciamento de concorrência

---

## Processamento Inteligente

O módulo `FeedbackAnalysisService` utiliza IA para:

- Processamento semântico
- Identificação de palavras válidas
- Limpeza textual
- Interpretação de linguagem natural

---

# Endpoints REST

Base URL:

```text
http://localhost:8081/api/v1/feedbacks
```

## Criar Feedback

```http
POST /api/v1/feedbacks
```

Payload:

```json
{
  "title": "Erro no checkout",
  "description": "Pagamento falhou ao finalizar compra",
  "customerEmail": "cliente@email.com"
}
```

## Buscar por ID

```http
GET /api/v1/feedbacks/{id}
```

## Listar Todos

```http
GET /api/v1/feedbacks
```

## Atualizar Status

```http
PATCH /api/v1/feedbacks/{id}/status?status=RESOLVED
```

## Remover Feedback

```http
DELETE /api/v1/feedbacks/{id}
```

## Análise Inteligente

```http
GET /api/v1/feedbacks/analyze-words
```

---

# Configuração da IA

```yaml
spring:
  ai:
    openai:
      base-url: https://generativelanguage.googleapis.com/v1beta/openai/
      api-key: YOUR_API_KEY
      chat:
        options:
          model: gemini-2.5-flash
```

---

# Executando o Projeto

## Build

```bash
mvn clean install
```

## Run

```bash
mvn spring-boot:run
```

Servidor:

```text
http://localhost:8081
```

---

# Diferenciais Técnicos

- Arquitetura moderna e desacoplada
- Imutabilidade com Records
- Alta concorrência com Virtual Threads
- Integração nativa com IA
- Estrutura enterprise-ready
- Preparado para microsserviços
- Fácil evolução para Redis/Kafka/RabbitMQ

---

# Melhorias Futuras

- Docker
- Kubernetes
- Kafka
- RabbitMQ
- OpenTelemetry
- Observabilidade
- Rate Limiting
- JWT/OAuth2
- RAG Pipelines
- Análise de sentimento
- Moderação automática

---

# Autor

Cristiano Camejo Origem

Backend Engineer | Java | Distributed Systems | AI-native Engineering
