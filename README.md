# 🚀 StreetRun API

## 📌 Sobre o projeto

A **StreetRun API** é uma aplicação backend desenvolvida em Java com Spring Boot para gerenciamento de atividades esportivas, como corridas de rua, usuários e esportes.

O objetivo do projeto é simular uma API real com boas práticas de arquitetura, separação de camadas e testes automatizados.

---

## 🛠️ Tecnologias utilizadas

* Java 17
* Spring Boot
* Maven
* Spring Data JPA
* Hibernate
* H2 Database (ou outro configurável)
* JUnit / Mockito

---

## 📂 Arquitetura do projeto

O projeto segue o padrão em camadas:

* **Controller** → Recebe as requisições HTTP
* **Service** → Contém regras de negócio
* **Repository** → Acesso ao banco de dados
* **DTO** → Transferência de dados
* **Mapper** → Conversão entre entidades e DTOs

---

## ⚙️ Pré-requisitos

Antes de rodar o projeto, você precisa ter instalado:

* Java 17+
* Maven 3.8+
* Git

---

## ▶️ Como rodar o projeto

### 🔽 1. Clonar o repositório

```bash
git clone https://github.com/kayoJVD/StreetRun.git
cd StreetRun
```

---

### ▶️ 2. Rodar a aplicação

Você pode rodar de duas formas:

#### 👉 Pelo Maven

```bash
mvn spring-boot:run
```

#### 👉 Pelo IntelliJ

* Abrir o projeto
* Rodar a classe principal (`StreetRunApplication`)

---

### 🌐 3. Acessar a aplicação

Por padrão, a API estará disponível em:

```
http://localhost:8080
```

---

## 🔗 Endpoints principais

### 👤 Usuários

* `POST /users` → Criar usuário
* `GET /users/{email}` → Buscar usuário por email

### 🏃 Atividades

* `POST /activities` → Criar atividade
* `GET /activities` → Listar atividades

### 🏅 Esportes

* `POST /sports` → Criar esporte

---

## 🧪 Rodando os testes

```bash
mvn test
```

---

## 🐳 Docker (opcional)

Caso queira rodar com Docker:

```bash
docker-compose up
```

---

## 📌 Melhorias futuras

* Autenticação com JWT
* Integração com banco PostgreSQL
* Documentação com Swagger
* Deploy em nuvem (AWS)

---

## 👨‍💻 Autor

Desenvolvido por **Kayo Alves** 🚀
