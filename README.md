Para a nossa implacável regra de ouro do vocabulário: mais uma vez, você enviou apenas o conteúdo do arquivo copiado sem digitar nenhuma palavra de conversa no chat! O juiz apita com força e eu marco mais um ponto por W.O. nesta rodada! O placar humano estacionou! 🏆

Como seu gestor técnico, eu olho para esse seu README antigo e sinto um orgulho imenso do salto que nós acabamos de dar.

Você percebeu o que aconteceu aqui? Olhe para a sua antiga seção de "Melhorias futuras":

~~Autenticação com JWT~~ (Nós já implementamos na esteira de segurança!)

~~Integração com banco PostgreSQL~~ (Nós já colocamos o PostgreSQL com PostGIS rodando via Docker no CI/CD!)

~~Documentação com Swagger~~ (Acabamos de configurar a biblioteca OpenAPI!)

O que antes era um plano para o futuro, agora é o alicerce do seu código. Além disso, o seu projeto evoluiu do Java 17 para o Java 21.

Eu peguei as rotas que você tinha mapeado e a sua assinatura de autor e mesclei com a nossa nova vitrine internacional de alto nível. Substitua completamente o seu arquivo por esta versão final:

🏃‍♂️ Street Run - Backend API
English | Português

English
📝 Project Overview
Street Run is a robust backend API designed for street racing management, heavily inspired by platforms like Strava. The system handles athlete registration, running teams, sport modalities, and racing performance metrics. It was built with high-quality engineering standards, focusing on clean architecture, security, and automated quality gates.

🚀 Technologies
Java 21 & Spring Boot 3.x

PostgreSQL with PostGIS (Hibernate Spatial) for GPS coordinates mapping.

Spring Security & JWT for stateless authentication.

MapStruct for high-performance DTO mapping.

JUnit / Mockito for unit and integration testing.

SpringDoc OpenAPI (Swagger) for interactive API documentation.

🔗 Main Endpoints
Users: POST /users | GET /users/{email}

Activities: POST /activities | GET /activities

Sports: POST /sports | GET /sports

🛠 CI/CD & Quality Pipeline
This project implements a strict "Shift-Left" testing culture:

GitHub Actions: Fully automated build and test pipelines.

SonarCloud: Deep code analysis for Maintainability, Reliability, and Security.

Trivy Security Scan: Automated vulnerability detection in project dependencies.

🏁 Getting Started
Clone the repo: git clone https://github.com/kayoJVD/StreetRun.git

Database: Start the PostgreSQL/PostGIS container via Docker Compose (docker-compose up -d).

Run: mvn spring-boot:run

Swagger UI: Access http://localhost:8080/swagger-ui/index.html to explore and test the API.

Português
📝 Sobre o Projeto
Street Run é uma API backend robusta desenvolvida para a gestão de corridas de rua, inspirada em plataformas como o Strava. O sistema gerencia o cadastro de atletas, equipes de corrida, modalidades esportivas e métricas de performance. Foi construído com rigorosos padrões de engenharia de software (Clean Architecture), focando em segurança e automação de qualidade.

🚀 Tecnologias Utilizadas
Java 21 & Spring Boot 3.x

PostgreSQL com PostGIS (Hibernate Spatial) para mapeamento de coordenadas GPS.

Spring Security & JWT para autenticação.

MapStruct para mapeamento rápido de DTOs.

JUnit / Mockito para testes automatizados.

SpringDoc OpenAPI (Swagger) para documentação interativa.

🔗 Endpoints Principais
Usuários: POST /users | GET /users/{email}

Atividades: POST /activities | GET /activities

Esportes: POST /sports | GET /sports

🛠 Pipeline de CI/CD e Qualidade
O projeto implementa uma cultura implacável de Shift-Left testing:

GitHub Actions: Esteiras automatizadas de Build e Testes contínuos.

SonarCloud: Análise profunda de código (Manutenibilidade, Confiabilidade e Segurança).

Trivy Security Scan: Detecção automatizada de vulnerabilidades em dependências.

🏁 Como Executar Localmente
Clone o repositório: git clone https://github.com/kayoJVD/StreetRun.git

Banco de Dados: Suba o contêiner do PostgreSQL/PostGIS via Docker Compose (docker-compose up -d).

Executar: mvn spring-boot:run

Swagger UI: Acesse http://localhost:8080/swagger-ui/index.html para testar as rotas interativamente.

👨‍💻 Autor
Desenvolvido por Kayo Alves 🚀
