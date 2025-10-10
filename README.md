# 🚀 Digitalização de Pátios Mottu

## 📃 Descrição do Projeto
No dinâmico cenário da mobilidade urbana, a gestão de grandes frotas como a da **Mottu** enfrenta desafios significativos.  
A ausência de um sistema centralizado e em tempo real para monitorar veículos resulta em perda de tempo na localização de motos, ineficiência operacional, aumento de custos e decisões prejudicadas pela falta de dados precisos.  

Identificamos essa lacuna e desenvolvemos uma **solução inovadora** para revolucionar o mapeamento geográfico e o rastreamento em tempo real da frota de motos da Mottu.

### ✨ Benefícios
- 🗺️ **Mapa interativo** com áreas delimitadas para cada pátio.  
- 📡 **Rastreamento em tempo real** com status operacional:
  - Disponível  
  - Em uso  
  - Em manutenção  
  - Aguardando retirada  
- 🔎 **Visão clara da distribuição das motos** em cada local.  
- ⚡ **Eficiência operacional aprimorada**: elimina buscas manuais.  
- 💰 **Redução de custos** e melhor aproveitamento de recursos.  
- 🌱 Abre espaço para **inovações futuras na mobilidade urbana**.  

---

## 🛠️ Tecnologias Utilizadas
- **Java 17** – linguagem principal  
- **Spring Boot** – estrutura para desenvolvimento da aplicação  
- **Spring Security + OAuth2** – autenticação via **Google Console**  
- **PostgreSQL** – banco de dados relacional  
- **Docker** – criação e gerenciamento de containers  
- **Thymeleaf** – motor de templates para renderização no front-end  
- **Gradle** – ferramenta de automação e build  

---

## 👨‍💻 Squad: CodeCrafters
- Nicolas Dobbeck Mendes  
- José Bezerra Bastos Neto  
- Thiago Henry Dias  

---

## ▶️ Como Executar a Aplicação

### 1. Clonar o repositório
```bash
git clone https://github.com/seu-usuario/digitalizacao-patios-mottu.git
cd digitalizacao-patios-mottu
```

### 2. Configurar variáveis de ambiente  
No arquivo `application.properties` (ou `application.yml`), substitua as variáveis de OAuth2 pelo **Client ID** e **Client Secret**:  

```properties
spring.security.oauth2.client.registration.google.client-id=165709244169-70fojjbs4ispq95307u0mfk0b7es9t98.apps.googleusercontent.com
spring.security.oauth2.client.registration.google.client-secret=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
```

⚠️ **Atenção:** Nunca exponha credenciais reais em repositórios públicos.  
Utilize variáveis de ambiente em produção.

---

### 3. Subir o ambiente com Docker
É necessário ter o **Docker Desktop** instalado.  
Com ele, basta rodar:

```bash
docker-compose up -d
```

Isso criará os containers necessários para a aplicação e o banco de dados.

---

### 4. Executar a aplicação
Com o ambiente pronto, execute o comando abaixo para iniciar a aplicação localmente:

```bash
./gradlew bootRun
```

A aplicação ficará disponível em:  
👉 [http://localhost:8081](http://localhost:8081)

---

## 📌 Observações
- O login é feito com autenticação Google via OAuth2.  
- O projeto está pronto para integração e escalabilidade com outras soluções de mobilidade.  
