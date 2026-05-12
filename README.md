# Projeto - Cidades ESGInteligentes (Sustentabilidade API)

**Jenifer Lopes Ribas**  
**Fase 7 - Emerging Technologies**

Esta API é o motor de inteligência para monitoramento de sustentabilidade urbana, processando métricas de consumo energético, geração de energia solar e eficiência por setores. O projeto evoluiu de uma infraestrutura baseada em DevOps (Fase 6) para um ecossistema completo de **Qualidade de Software e Automação de Testes (Fase 7)**.

O foco atual é garantir a integridade dos dados e a governança dos pilares **ESG** através de testes de contrato, testes funcionais de API e BDD.

---

## 🚀 Novidades da Fase 7: Automação de Testes

Nesta fase, implementamos uma suíte de testes robusta para validar todos os endpoints da API, garantindo que as regras de negócio de sustentabilidade sejam respeitadas.

### Tecnologias Utilizadas
*   **BDD (Cucumber):** Escrita de cenários em Gherkin.
*   **Rest Assured:** Automação de requisições e validações REST.
*   **JUnit 5:** Runner dos testes.
*   **JSON Schema:** Validação de contrato (Governança de Dados).
*   **Lombok & Google Gson:** Manipulação de dados e serialização JSON.

### Estrutura do Projeto de Testes
```text
src/test/java
├── model       # POJOs para mapeamento de dados (Lombok)
├── services    # Lógica de chamadas Rest Assured (Service Objects)
├── steps       # Implementação dos passos Gherkin
├── hooks       # Setup de autenticação (Basic Auth) e URL
└── runner      # Classe disparadora do Cucumber (JUnit 5)

src/test/resources
├── features    # Arquivos .feature com cenários BDD
└── schemas     # Arquivos .json para validação de contrato
```

---

## 🛠 Como Executar Localmente (Docker + Testes)

O setup foi simplificado para que a infraestrutura suba via Docker e os testes rodem via Maven.

1.  **Subir a API e o Banco Oracle:**
    Certifique-se de estar na raiz do projeto onde está o `docker-compose.yml`.
    ```bash
    docker-compose up -d
    ```
    *Nota: A população inicial de dados (Seed) é feita automaticamente pelo Flyway.*

2.  **Executar a Suíte de Testes Automatizados:**
    ```bash
    ./mvnw clean test -Dtest=runner.TestRunner
    ```
    Este comando executará os cenários funcionais e as validações de contrato.

---

## ⛓ Pipeline CI/CD (GitHub Actions)

O workflow foi atualizado para integrar a automação de testes como um "Gate de Qualidade" antes do deploy.

### Fluxo Unificado (`ci-cd.yml`):
1.  **Build & Test:** Compilação do código Java 17.
2.  **Infraestrutura:** Inicialização do container Oracle XE 21c no runner.
3.  **QA (Novo):** Execução automática dos testes de **BDD e API (Rest Assured)**. O deploy só prossegue se 100% dos testes passarem.
4.  **Deploy Staging:** Criação da imagem Docker e publicação no ambiente de homologação (Porta 8080).
5.  **Deploy Production:** Publicação oficial restringida à branch `main` (Porta 8081).

---

## 🐳 Containerização

A estratégia utiliza **Multi-stage Build**, garantindo uma imagem final focada em segurança e performance (JRE).

**Estratégias ESG adotadas no Docker:**
*   **Eficiência Energética:** Redução do tamanho da imagem para menor consumo de recursos de rede e armazenamento.
*   **Segurança (JRE):** Menor superfície de ataque ao remover ferramentas de desenvolvimento (JDK/Maven) da imagem final.

---

## 📝 Documentação e Evidências

> **⚠️ AVISO:** Por motivos de compatibilidade e portabilidade do arquivo `.zip`, as imagens e prints de execução (Workflow do GitHub, Logs do Maven e Console do RestAssured) não estão presentes neste arquivo Markdown.
>
> **Consulte o arquivo `Relatório_Técnico_Testes.pdf` incluído na entrega para visualizar todas as evidências visuais do projeto em funcionamento.**

---

## Checklist de Entrega (Fase 7)

| Item | Status |
| :--- | :---: |
| Código-fonte completo em .ZIP | [x] |
| Mínimo de 3 cenários Gherkin (BDD) | [x] |
| Testes de API para todos os serviços (Rest Assured) | [x] |
| Validação de Contrato (JSON Schema) | [x] |
| Autenticação (Basic Auth) tratada nos testes | [x] |
| Pipeline CI/CD integrado com a suíte de QA | [x] |
| Documentação em PDF com prints e cenários | [x] |