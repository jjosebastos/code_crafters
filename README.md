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

### Backend & DevOps
- **Java 17** – Linguagem principal
- **Spring Boot** – Estrutura da aplicação
- **Spring Security + OAuth2** – Autenticação via Google
- **PostgreSQL** – Banco de dados relacional
- **Docker** – Gerenciamento de containers
- **Gradle** – Ferramenta de build

### IoT & Real-Time (Arquitetura Disruptiva)
- **Arduino** – Hardware para captura de dados (Simulação de GPS e Proximidade)
- **Node-RED** – Middleware para fluxo de dados IoT (leitura serial e publicação MQTT)
- **MQTT** – Protocolo de mensageria leve para comunicação IoT (Broker: `broker.hivemq.com`)
- **WebSockets (STOMP)** – Para envio de dados em tempo real do backend (Java) para o dashboard (Thymeleaf)

### Frontend
- **Thymeleaf** – Motor de templates para renderização dos dashboards
- **HTML/CSS/JavaScript** – Estrutura das páginas de monitoramento e rastreamento


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

### Parte 5: Simulação IoT (Arduino + Node-RED)

Esta parte irá *capturar e enviar* os dados dos sensores para o backend.

1.  **Carregar o Código no Arduino:**
    Abra a IDE do Arduino, cole o código-fonte (`.ino`) do projeto e faça o upload para a sua placa (Ex: Arduino Uno).

2.  **Configurar o Node-RED:**
    - Inicie o Node-RED.
    - Importe o arquivo `flows.json` para o seu editor.
    - **Importante:** Configure o nó `serial in` (entrada serial). Você deve selecionar a porta COM correta em que o seu Arduino foi detectado (ex: `COM3`, `/dev/ttyACM0`, etc.).
    - Verifique se o nó `mqtt out` (saída MQTT) está configurado para o broker correto (`broker.hivemq.com`, porta 1883), o mesmo que a aplicação Java está ouvindo.

3.  **Iniciar o Fluxo:**
    - Faça o "Deploy" do fluxo no Node-RED.
    - Com o Arduino conectado, você deverá ver os dados JSON fluindo da porta serial, passando pelo Node-RED e sendo publicados no MQTT.

### Parte 3: Visualização (Resultado Final)

- Acesse a aplicação em [http://localhost:8081](http://localhost:8081) e faça login.
- Navegue até as páginas de **Monitoramento** e **Rastreamento**.
- Ao mexer no potenciômetro (GPS) e no sensor ultrassônico (Distância) do Arduino, você verá os dashboards (vagas e localização) atualizando em **tempo real** via WebSockets.

---

## 📌 Observações
- O login é feito com autenticação Google via OAuth2.  
- O projeto está pronto para integração e escalabilidade com outras soluções de mobilidade.  
