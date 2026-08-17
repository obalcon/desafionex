# 🏥 Desafio FullStack - Nexdom Healthtech

Aplicação RESTful para controle de estoque desenvolvida como desafio técnico para a **Nexdom Healthtech**.

## 📋 Sobre o Projeto

Sistema de controle de estoque com:
- Cadastro de produtos (Eletrônico, Eletrodoméstico, Móvel)
- Movimentação de entrada e saída de estoque
- Consulta de produtos por tipo com saldo disponível
- Cálculo de lucro por produto

## 🚀 Tecnologias

### Backend
- **Java 21**
- **Spring Boot 4.1.0**
- **Spring Data JPA**
- **H2 Database** (em memória)
- **Lombok**
- **SpringDoc OpenAPI 3.1.0** (Swagger)
- **JUnit 5 + Mockito** (testes)

### Frontend
- **Vue.js 3**
- **TypeScript**
- **Vue Router**
- **Pinia**
- **Axios**

## 📁 Estrutura do Projeto

```
desafionex/
├── backend/                 # API Spring Boot
│   ├── src/main/java/...
│   └── src/test/java/...    # Testes unitários
├── frontend/                # Aplicação Vue.js
│   ├── src/
│   │   ├── api/             # Configuração Axios
│   │   ├── components/      # Componentes Vue
│   │   ├── router/          # Rotas
│   │   ├── types/           # Tipos TypeScript
│   │   └── views/           # Telas
│   └── package.json
└── README.md
```

## ⚙️ Pré-requisitos

- Java 21+
- Maven
- Node.js 18+
- npm

## 🔧 Como executar

### 1. Clone o repositório

```bash
git clone https://github.com/obalcon/desafionex.git
cd desafionex
```

### 2. Inicie o Backend

```bash
cd backend
./mvnw spring-boot:run
```

O backend estará disponível em: `http://localhost:8080`

### 3. Inicie o Frontend (em outro terminal)

```bash
cd frontend
npm install
npm run dev
```

O frontend estará disponível em: `http://localhost:5173`

---

## 📚 Documentação da API (Swagger)

Acesse a interface interativa do Swagger em:

```
http://localhost:8080/swagger-ui.html
```

## 🧪 Executando os Testes

```bash
cd backend
./mvnw test
```

### Resultados dos testes

| Suite | Testes | Status |
|-------|--------|--------|
| ProdutoServiceTest | 9 | ✅ Passando |
| MovimentoEstoqueServiceTest | 5 | ✅ Passando |
| **Total** | **14** | **✅ 14/14** |

---

## 🎯 Funcionalidades

### Produtos
- ✅ Criar produto (código único, descrição, tipo, valor fornecedor)
- ✅ Listar todos os produtos
- ✅ Editar produto
- ✅ Excluir produto (bloqueado se houver movimentações)
- ✅ Consultar por tipo com quantidade disponível e saídas

### Movimentações de Estoque
- ✅ Registrar entrada (aumenta estoque)
- ✅ Registrar saída (diminui estoque com validação de saldo)
- ✅ Validação: não permite saída sem saldo suficiente
- ✅ Histórico de movimentações por produto

### Lucro
- ✅ Consulta de lucro por produto
- ✅ Fórmula: `valorVenda - (valorFornecedor × quantidade)`

---

## 🗄️ Banco de Dados H2

Console H2 disponível em: `http://localhost:8080/h2-console`

| Configuração | Valor |
|--------------|-------|
| JDBC URL | `jdbc:h2:mem:estoque` |
| User | `sa` |
| Password | *(vazio)* |

---

## 📌 Dados Iniciais

Ao iniciar a aplicação, são carregados automaticamente:

| Produto | Estoque Final | Lucro |
|---------|--------------|-------|
| Notebook Dell | 12 | R$ 3.000,00 |
| Geladeira Brastemp | 6 | R$ 700,00 |

---

## 👤 Autor

**Omar Alejandro Balcon Benvenuto** - Desafio Técnico Nexdom Healthtech
