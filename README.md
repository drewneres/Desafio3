# 📌 **Projeto de Microsserviços - Gerenciamento de Eventos e Ingressos**

Este repositório contém dois microsserviços desenvolvidos em **Java 17** com **Spring Boot 3.3.x**, conectados ao **MongoDB**, e preparados para deploy em **AWS EC2**.

---

## 📋 **Índice**
- [📌 Requisitos](#-requisitos)
- [🚀 Configuração do Ambiente](#-configuração-do-ambiente)
- [📦 Estrutura do Projeto](#-estrutura-do-projeto)
- [☁️ Criando Máquinas Virtuais no AWS EC2](#️-criando-máquinas-virtuais-no-aws-ec2)
- [🛢️ Configuração do Banco MongoDB Atlas](#️-configuração-do-banco-mongodb-atlas)
- [▶️ Como Rodar o Projeto](#️-como-rodar-o-projeto)
- [🛠️ Endpoints Disponíveis](#️-endpoints-disponíveis)

---

## 📌 **Requisitos**
Antes de iniciar, certifique-se de ter instalado:
- ✅ **Java 17 (LTS)**
- ✅ **Maven**
- ✅ **Git**
- ✅ **MongoDB Atlas (ou MongoDB local)**
- ✅ **AWS CLI (opcional para deploy na AWS)**
- ✅ **Docker (opcional para rodar os microsserviços em containers)**

---

## 🚀 **Configuração do Ambiente**
### **1️⃣ Clone o Repositório**
```sh
git clone https://github.com/drewneres/Desafio3.git
cd seu-repositorio
```

---

## 📦 **Estrutura do Projeto**
```
projeto_3/
│── ms-event-manager/         # Microsserviço de Eventos
│   ├── src/main/java/com/eventmanager/
│   ├── src/main/resources/application.properties
│   ├── pom.xml
│── ms-ticket-manager/        # Microsserviço de Ingressos
│   ├── src/main/java/com/ticketmanager/
│   ├── src/main/resources/application.properties
│   ├── pom.xml
│── docker-compose.yml        # Configuração para rodar MongoDB via Docker
│── README.md                 # Documentação
│── .gitignore                # Arquivos ignorados pelo Git
```

---

## ☁️ **Criando Máquinas Virtuais no AWS EC2**
### **1️⃣ Criar uma Instância EC2**
1. Acesse o console da AWS: [EC2](https://aws.amazon.com/console/)
2. Clique em **Launch Instance**.
3. Escolha a AMI: **Amazon Linux 2 ou Ubuntu 22.04**.
4. Escolha **t2.micro** (grátis no Free Tier).
5. Configure o Security Group:
   - **Liberar porta 22 (SSH)**
   - **Liberar porta 8080 (Aplicação Spring Boot)**
   - **Liberar porta 27017 (MongoDB)**
6. Baixe a **chave SSH** (`.pem`).

### **2️⃣ Conectar via SSH**
```sh
chmod 400 sua-chave.pem
ssh -i "sua-chave.pem" ec2-user@SEU_IP_PUBLICO
```

### **3️⃣ Instalar Dependências na Instância**
```sh
sudo yum update -y
sudo yum install git maven java-17-amazon-corretto -y
```

---

## 🛢️ **Configuração do Banco MongoDB Atlas**
### **1️⃣ Criar um Cluster no MongoDB Atlas**
1. Acesse: [MongoDB Atlas](https://www.mongodb.com/atlas/database)
2. Crie um **Free Cluster**.
3. Configure um usuário **admin/admin**.
4. Adicione **0.0.0.0/0** na whitelist do IP para permitir conexões externas.

### **2️⃣ Configurar `application.properties`**
**ms-event-manager**
```properties
spring.data.mongodb.uri=mongodb+srv://admin:admin@seu-cluster.mongodb.net/db_event
```

**ms-ticket-manager**
```properties
spring.data.mongodb.uri=mongodb+srv://admin:admin@seu-cluster.mongodb.net/db_ticket
```

---

## ▶️ **Como Rodar o Projeto**
### **1️⃣ Rodando com Maven**
#### **Rodar `ms-event-manager`**
```sh
cd ms-event-manager
mvn spring-boot:run
```

#### **Rodar `ms-ticket-manager`**
```sh
cd ms-ticket-manager
mvn spring-boot:run
```

---

### **2️⃣ Rodando com Docker**
Se preferir rodar o MongoDB localmente sem Atlas:
```sh
docker-compose up -d
```

---

## 🛠️ **Endpoints Disponíveis**
### 📌 **Microsserviço: `ms-event-manager`**
| Método | Endpoint | Descrição |
|--------|---------|-------------|
| `POST` | `/create-event` | Criar evento |
| `GET` | `/get-event/{id}` | Buscar evento por ID |
| `GET` | `/get-all-events` | Listar todos eventos |
| `DELETE` | `/delete-event/{id}` | Excluir evento |

### 📌 **Microsserviço: `ms-ticket-manager`**
| Método | Endpoint | Descrição |
|--------|---------|-------------|
| `POST` | `/create-ticket` | Criar ingresso |
| `GET` | `/get-ticket/{id}` | Buscar ingresso |
| `DELETE` | `/cancel-ticket/{id}` | Cancelar ingresso |

---

## **🚀 Deploy na AWS**
### **1️⃣ Enviar Código para a AWS**
```sh
scp -i sua-chave.pem -r ms-event-manager/ ec2-user@SEU_IP:/home/ec2-user/
scp -i sua-chave.pem -r ms-ticket-manager/ ec2-user@SEU_IP:/home/ec2-user/
```

### **2️⃣ Rodar os Microsserviços na AWS**
```sh
cd ms-event-manager
mvn spring-boot:run &

cd ../ms-ticket-manager
mvn spring-boot:run &
```

Agora os microsserviços estão rodando na **AWS EC2** e conectados ao **MongoDB Atlas**! 🚀🎉
