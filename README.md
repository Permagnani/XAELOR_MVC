# Xaelor MVC — Checkpoint 4 (Parte II)

Aplicação **Spring MVC + Thymeleaf + Spring Security** para a Xaelor, uma perfumaria fictícia
do tipo *mercado express*. É a continuação, em projeto separado, do Checkpoint 4 – Parte I
(API REST com HATEOAS), reaproveitando o mesmo tema (perfumes e matérias-primas) e,
preferencialmente, o mesmo banco de dados Oracle FIAP.

> ⚠️ **Preencha antes de entregar:** integrantes/RM, link do GitHub desta Parte II, link do
> Deploy, plataforma usada no Deploy e IDE utilizada. Veja o arquivo `integrantes.txt` (modelo)
> e o rodapé deste README.

## Sumário
- [Tecnologias](#tecnologias)
- [Tema e modelo de dados](#tema-e-modelo-de-dados)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Rotas públicas e privadas (Spring Security)](#rotas-públicas-e-privadas-spring-security)
- [Como rodar localmente](#como-rodar-localmente)
- [CRUD na interface Web — exemplos](#crud-na-interface-web--exemplos)
- [Deploy](#deploy)
- [Vídeo de demonstração](#vídeo-de-demonstração)

## Tecnologias

- Java 17
- Spring Boot 3.3.x
- Spring MVC (`spring-boot-starter-web`)
- Thymeleaf (`spring-boot-starter-thymeleaf`) + `thymeleaf-extras-springsecurity6`
- Spring Data JPA (`spring-boot-starter-data-jpa`)
- Spring Security (`spring-boot-starter-security`) — login em formulário, rotas públicas/privadas
- Lombok
- Banco de dados: Oracle FIAP (mesmo banco da Parte I). Um perfil alternativo com **H2 em
  memória** (`application-h2.properties`) está disponível para facilitar o Deploy em
  plataformas externas quando o Oracle FIAP não está acessível fora da rede da faculdade.
- Maven

## Tema e modelo de dados

Mesma temática da Parte I: catálogo de **perfumes** da Xaelor e das **matérias-primas**
usadas nas fórmulas.

| Entidade | Tabela | Colunas |
|---|---|---|
| `Perfume` | `TDS_MVC_TB_PERFUME` | `PERFUME_ID`, `PERFUME_NOME`, `PERFUME_GENERO`, `PERFUME_DESCRICAO`, `PERFUME_PRECO` |
| `MateriaPrima` | `TDS_MVC_TB_MATERIAPRIMA` | `MATPRIMA_ID`, `MATPRIMA_NOME`, `MATPRIMA_TIPOUNIDADE` (enum: `ML`, `L`, `MG`, `G`, `GOTA`, `UNIDADE`), `MATPRIMA_DESCRICAO` |

As tabelas usam o prefixo `TDS_MVC_TB_` para não colidir com as tabelas da Parte I
(`TB_PERFUME`, `TB_MATERIAPRIMA`) dentro do mesmo schema Oracle FIAP. O Hibernate cria as
tabelas automaticamente (`spring.jpa.hibernate.ddl-auto=update`) na primeira execução.

## Estrutura do projeto

```
src/main/java/br/com/fiap/xaelor_mvc/
├── XaelorMvcApplication.java
├── config/
│   └── SecurityConfig.java        # rotas públicas/privadas, login, logout
├── controller/
│   ├── HomeController.java        # página inicial
│   ├── LoginController.java       # página de login
│   ├── PerfumeController.java     # CRUD via views (Create, Read, Update, Delete)
│   └── MateriaPrimaController.java
├── service/
│   ├── PerfumeService.java
│   └── MateriaPrimaService.java
├── repository/
│   ├── PerfumeRepository.java
│   └── MateriaPrimaRepository.java
├── model/
│   ├── Perfume.java
│   └── MateriaPrima.java
└── enums/
    └── TipoUnidade.java

src/main/resources/
├── application.properties         # Oracle FIAP (via variáveis de ambiente)
├── application-h2.properties      # perfil alternativo H2 para Deploy
├── static/css/style.css           # identidade visual da Xaelor
└── templates/
    ├── index.html, login.html, error.html
    ├── fragments/ (head, nav, footer)
    ├── perfumes/ (list, form, detail)
    └── materiaprima/ (list, form, detail)
```

## Rotas públicas e privadas (Spring Security)

O `SecurityConfig` define:

**Públicas (sem login):**
- `GET /` — página inicial
- `GET /perfumes` e `GET /perfumes/{id}` — listar e ver detalhe de um perfume (**Read**)
- `GET /materias-primas` e `GET /materias-primas/{id}` — listar e ver detalhe (**Read**)
- `GET /login`, arquivos estáticos (`/css/**`, `/js/**`, `/images/**`)

**Privadas (exigem login):**
- `GET/POST /perfumes/novo`, `POST /perfumes`, `GET/POST /perfumes/{id}/editar`, `POST /perfumes/{id}/deletar`
- `GET/POST /materias-primas/nova`, `POST /materias-primas`, `GET/POST /materias-primas/{id}/editar`, `POST /materias-primas/{id}/deletar`

Ou seja: **Create, Update e Delete exigem autenticação**; **Read é público**, como convém a
um catálogo que qualquer visitante pode consultar.

Usuário padrão (configurável por variáveis de ambiente, sem senha exposta em texto no
`SecurityConfig` — a senha é codificada em BCrypt em tempo de execução):

| Variável | Padrão |
|---|---|
| `ADMIN_USER` | `admin` |
| `ADMIN_PASSWORD` | `admin123` |

> Troque essas variáveis de ambiente na plataforma de Deploy antes de publicar o link de
> produção — não deixe a senha padrão em um ambiente público.

## Como rodar localmente

Pré-requisitos: JDK 17+ e acesso ao Oracle FIAP (ou use o perfil H2, veja abaixo).

```bash
# usando o wrapper do Maven, sem precisar instalar o Maven
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8083`.

Para rodar com o perfil H2 (não precisa do Oracle FIAP, útil para testar localmente):

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2
```

Para apontar para outro schema/senha do Oracle FIAP sem editar o código:

```bash
export DB_URL="jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL"
export DB_USER="seu_rm"
export DB_PASSWORD="sua_senha"
./mvnw spring-boot:run
```

## CRUD na interface Web — exemplos

> Adicione aqui os *prints* de tela pedidos no enunciado (`![descrição](caminho-da-imagem.png)`),
> um para cada operação abaixo.

### Create — Cadastrar um perfume
1. Acesse `http://localhost:8083/perfumes`, clique em **Entrar** e faça login (`admin` / `admin123`).
2. Clique em **+ Novo perfume**.
3. Preencha nome, gênero, descrição e preço e clique em **Cadastrar perfume**.

<img width="1917" height="957" alt="image" src="https://github.com/user-attachments/assets/26472ed5-1ea2-449f-8610-ca437612b85c" />

<img width="1912" height="971" alt="image" src="https://github.com/user-attachments/assets/f63b381f-6e84-4252-80e2-feb0ff44683a" />


### Read — Consultar perfumes e matérias-primas
- `GET /perfumes` — lista todos os perfumes (rota pública, sem necessidade de login).
- `GET /perfumes/{id}` — detalhe de um perfume específico.
- `GET /materias-primas` e `GET /materias-primas/{id}` — idem, para matérias-primas.

<img width="1897" height="962" alt="image" src="https://github.com/user-attachments/assets/c4cc63e5-6843-46e0-b7ce-717cbcc557f4" />


### Update — Editar uma matéria-prima
1. Logado, acesse `/materias-primas`, clique em **Editar** em um item.
2. Altere os campos e clique em **Salvar alterações**.

<img width="1900" height="963" alt="image" src="https://github.com/user-attachments/assets/548999c6-eed5-4b79-8814-5e1c3511b226" />


<img width="1895" height="962" alt="image" src="https://github.com/user-attachments/assets/5f27be1a-fea5-4a81-8380-732c06293ebb" />

### Delete — Remover um registro
1. Logado, clique em **Excluir** em um perfume ou matéria-prima na listagem (há uma
   confirmação via `confirm()` antes de excluir).

<img width="1908" height="962" alt="image" src="https://github.com/user-attachments/assets/9897ec8c-583c-4eb6-9bb0-0e1303ba2c4b" />



<img width="1902" height="963" alt="image" src="https://github.com/user-attachments/assets/c4ee4cf2-3489-4221-986d-604657e0ca15" />


## Deploy

O projeto inclui um `Dockerfile` (multi-stage: build com Maven + execução com JRE) pronto
para plataformas como **Render** ou **Fly.io**:

1. Suba este repositório no GitHub.
2. Na plataforma escolhida, crie um novo *Web Service* a partir do repositório, usando o
   `Dockerfile` incluso.
3. Configure as variáveis de ambiente: `DB_URL`, `DB_USER`, `DB_PASSWORD`, `ADMIN_USER`,
   `ADMIN_PASSWORD` (ou `SPRING_PROFILES_ACTIVE=h2` para usar o banco em memória, caso o
   Oracle FIAP não seja acessível pela plataforma de Deploy).
4. Após o Deploy, cole aqui o link de produção:

> **Link de produção:** https://xaelor-mvc.onrender.com
> **Plataforma utilizada:** Render


Mostrando: navegação pública (listagem e detalhe de perfumes/matérias-primas), login,
cadastro (Create), edição (Update) e exclusão (Delete) pela interface Web.

---

## Integrantes e informações de entrega

- **Integrantes / RM:** ver `integrantes.txt`
- **IDE utilizada:** `SUBSTITUA (IntelliJ, Eclipse ou NetBeans)`
- **Repositório GitHub (Parte II, separado da Parte I):** `SUBSTITUA-PELO-LINK`
- **Link do Deploy:** `SUBSTITUA-PELO-LINK`
