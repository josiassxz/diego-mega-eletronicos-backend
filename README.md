# Mega Eletrônicos GO - Backend

Sistema de gerenciamento de clientes para a empresa Mega Eletrônicos GO, desenvolvido em Java Spring Boot com PostgreSQL.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL
- Swagger/OpenAPI 3
- Maven

## Configuração do Ambiente

### 1. Pré-requisitos
- Java 17 ou superior
- PostgreSQL 12 ou superior
- Maven 3.6 ou superior

### 2. Configuração do Banco de Dados
```sql
-- Criar banco de dados
CREATE DATABASE mega_eletronicos;

-- Criar usuário (opcional)
CREATE USER mega_user WITH PASSWORD 'mega123';
GRANT ALL PRIVILEGES ON DATABASE mega_eletronicos TO mega_user;
```

### 3. Executar a aplicação
```bash
# Compilar o projeto
mvn clean compile

# Executar a aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## Documentação da API

### Swagger UI
Acesse a documentação interativa da API em: `http://localhost:8080/swagger-ui.html`

### Endpoints Principais

#### Autenticação
- `POST /api/auth/login` - Fazer login
- `POST /api/auth/usuarios` - Criar usuário
- `GET /api/auth/usuarios` - Listar usuários
- `PUT /api/auth/usuarios/{id}` - Atualizar usuário
- `DELETE /api/auth/usuarios/{id}` - Deletar usuário

#### Clientes
- `GET /api/clientes` - Listar todos os clientes
- `GET /api/clientes/{id}` - Buscar cliente por ID
- `GET /api/clientes/buscar` - Buscar clientes com filtros
- `POST /api/clientes` - Criar novo cliente
- `PUT /api/clientes/{id}` - Atualizar cliente
- `DELETE /api/clientes/{id}` - Deletar cliente
- `POST /api/clientes/{id}/foto-documento` - Upload foto do documento
- `POST /api/clientes/{id}/foto-selfie` - Upload foto selfie
- `GET /api/clientes/{id}/fotos` - Obter fotos do cliente em base64
- `GET /api/clientes/uploads/{nomeArquivo}` - Visualizar arquivo

## Exemplos de Uso

### 1. Criar Usuário
```json
POST /api/auth/usuarios
{
  "login": "admin",
  "senha": "123456"
}
```

### 2. Fazer Login
```json
POST /api/auth/login
{
  "login": "admin",
  "senha": "123456"
}
```

### 3. Criar Cliente

#### Opção 1: Criar cliente sem fotos (JSON)
```json
POST /api/clientes
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@email.com",
  "cpf": "12345678901",
  "rg": "123456789",
  "telefone": "11999999999",
  "cep": "01234567",
  "rua": "Rua das Flores",
  "numero": "123",
  "bairro": "Centro",
  "cidade": "São Paulo",
  "estado": "SP",
  "nomeMae": "Maria Silva",
  "dataNascimento": "1990-01-15",
  "sexo": "M",
  "estadoCivil": "Solteiro",
  "naturezaOcupacao": "CLT",
  "profissao": "Desenvolvedor",
  "nomeEmpresa": "Tech Company",
  "rendaMensal": 5000.00
}
```

#### Opção 2: Criar cliente com fotos (Multipart Form Data)
```bash
POST /api/clientes
Content-Type: multipart/form-data

# Dados obrigatórios
nome=João Silva
email=joao@email.com
cpf=12345678901
rg=123456789
telefone=11999999999
cep=01234567
rua=Rua das Flores
numero=123
bairro=Centro
cidade=São Paulo
estado=SP
nomeMae=Maria Silva
dataNascimento=1990-01-15
sexo=M
estadoCivil=Solteiro
naturezaOcupacao=CLT
profissao=Desenvolvedor
rendaMensal=5000.00

# Dados opcionais
nomeEmpresa=Tech Company
fotoDocumento=@documento.jpg
fotoSelfie=@selfie.jpg
```

### 4. Buscar Clientes com Filtros
```
GET /api/clientes/buscar?nome=João
GET /api/clientes/buscar?email=joao@email.com
GET /api/clientes/buscar?cpf=12345678901
GET /api/clientes/buscar?cidade=São Paulo
GET /api/clientes/buscar?estado=SP
```

### 5. Upload de Fotos

#### Opção 1: Durante a criação do cliente (Recomendado)
```bash
# Criar cliente com fotos em uma única requisição
curl -X POST "http://localhost:8080/api/clientes" \
  -H "Content-Type: multipart/form-data" \
  -F "nome=João Silva" \
  -F "email=joao@email.com" \
  -F "cpf=12345678901" \
  -F "rg=123456789" \
  -F "telefone=11999999999" \
  -F "cep=01234567" \
  -F "rua=Rua das Flores" \
  -F "numero=123" \
  -F "complemento=Apto 12" \
  -F "bairro=Centro" \
  -F "cidade=São Paulo" \
  -F "estado=SP" \
  -F "nomeMae=Maria Silva" \
  -F "dataNascimento=1990-01-15" \
  -F "sexo=M" \
  -F "estadoCivil=Solteiro" \
  -F "naturezaOcupacao=CLT" \
  -F "profissao=Desenvolvedor" \
  -F "nomeEmpresa=Tech Company" \
  -F "rendaMensal=5000.00" \
  -F "fotoDocumento=@documento.jpg" \
  -F "fotoSelfie=@selfie.jpg"
```

#### Opção 2: Upload separado após criação
```bash
# Upload foto do documento
curl -X POST "http://localhost:8080/api/clientes/1/foto-documento" \
  -H "Content-Type: multipart/form-data" \
  -F "arquivo=@documento.jpg"

# Upload foto selfie
curl -X POST "http://localhost:8080/api/clientes/1/foto-selfie" \
  -H "Content-Type: multipart/form-data" \
  -F "arquivo=@selfie.jpg"
```

### 6. Atualizar Cliente

#### Opção 1: Atualização com JSON (sem fotos)
```bash
# Atualizar dados do cliente (sem alterar fotos)
curl -X PUT "http://localhost:8080/api/clientes/1" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva Santos",
    "email": "joao.santos@email.com",
    "cpf": "12345678901",
    "rg": "123456789",
    "telefone": "11999999999",
    "cep": "01234567",
    "rua": "Rua das Flores",
    "numero": "123",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP",
    "nomeMae": "Maria Silva",
    "dataNascimento": "1990-01-15",
    "sexo": "M",
    "estadoCivil": "Casado",
    "naturezaOcupacao": "CLT",
    "profissao": "Desenvolvedor Senior",
    "nomeEmpresa": "Tech Company",
    "rendaMensal": 7000.00
  }'
```

#### Opção 2: Atualização com fotos (multipart/form-data)
```bash
# Atualizar cliente incluindo novas fotos
curl -X PUT "http://localhost:8080/api/clientes/1" \
  -H "Content-Type: multipart/form-data" \
  -F "nome=João Silva Santos" \
  -F "email=joao.santos@email.com" \
  -F "cpf=12345678901" \
  -F "rg=123456789" \
  -F "telefone=11999999999" \
  -F "cep=01234567" \
  -F "rua=Rua das Flores" \
  -F "numero=123" \
  -F "complemento=Apto 12" \
  -F "bairro=Centro" \
  -F "cidade=São Paulo" \
  -F "estado=SP" \
  -F "nomeMae=Maria Silva" \
  -F "dataNascimento=1990-01-15" \
  -F "sexo=M" \
  -F "estadoCivil=Casado" \
  -F "naturezaOcupacao=CLT" \
  -F "profissao=Desenvolvedor Senior" \
  -F "nomeEmpresa=Tech Company" \
  -F "rendaMensal=7000.00" \
  -F "fotoDocumento=@novo_documento.jpg" \
  -F "fotoSelfie=@nova_selfie.jpg"
```

**Observações sobre a atualização com fotos:**
- As fotos são opcionais no PUT - você pode enviar apenas uma ou nenhuma
- Se uma nova foto for enviada, a foto anterior será automaticamente deletada
- Se não enviar uma foto específica, a foto atual do cliente será mantida
- Todos os outros campos são obrigatórios quando usar multipart/form-data

### 7. Obter Fotos do Cliente em Base64
```bash
# Obter fotos do cliente em formato base64
GET /api/clientes/{id}/fotos

# Exemplo de resposta:
{
  "clienteId": 1,
  "fotos": {
    "fotoDocumento": "iVBORw0KGgoAAAANSUhEUgAA...", // base64 da foto do documento
    "fotoSelfie": "iVBORw0KGgoAAAANSUhEUgAA..."     // base64 da foto selfie
  }
}

# Exemplo com curl:
curl -X GET "http://localhost:8080/api/clientes/1/fotos"
```

**Observações sobre o endpoint de fotos em base64:**
- Retorna apenas as fotos que existem (se o cliente não tem foto do documento, o campo não aparece na resposta)
- As imagens são codificadas em base64 e podem ser usadas diretamente em tags `<img>` no frontend
- Formato: `data:image/jpeg;base64,{base64_string}` (adicione o prefixo no frontend conforme necessário)

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/megaeletronicos/backend/
│   │   ├── config/          # Configurações
│   │   ├── controller/      # Controllers REST
│   │   ├── entity/          # Entidades JPA
│   │   ├── repository/      # Repositories
│   │   ├── service/         # Services
│   │   └── MegaEletronicosBackendApplication.java
│   └── resources/
│       └── application.properties
└── uploads/                 # Diretório de arquivos enviados
```

## Funcionalidades

### Gerenciamento de Clientes
- ✅ CRUD completo de clientes
- ✅ Validação de dados obrigatórios
- ✅ Validação de unicidade (email e CPF)
- ✅ Busca por múltiplos filtros
- ✅ Upload de fotos (documento e selfie)
- ✅ Gerenciamento de arquivos

### Sistema de Autenticação
- ✅ Login simples sem JWT
- ✅ Gerenciamento de usuários
- ✅ Múltiplos usuários suportados

### Recursos Técnicos
- ✅ API REST completa
- ✅ Documentação Swagger
- ✅ CORS configurado
- ✅ Validação de dados
- ✅ Tratamento de erros
- ✅ Upload de arquivos
- ✅ Integração com PostgreSQL

## Configurações Importantes

### Tamanho máximo de arquivos
- Arquivo individual: 10MB
- Requisição total: 10MB

### Diretório de uploads
- Localização: `uploads/` (raiz do projeto)
- Criado automaticamente se não existir

### CORS
- Configurado para aceitar requisições de qualquer origem
- Métodos permitidos: GET, POST, PUT, DELETE, OPTIONS

## Logs e Debugging

A aplicação está configurada com logs detalhados para desenvolvimento:
- Logs SQL do Hibernate
- Logs de requisições web
- Logs da aplicação em nível DEBUG

## Próximos Passos

1. Configurar banco de dados PostgreSQL
2. Executar a aplicação
3. Testar endpoints via Swagger UI
4. Integrar com frontend React
