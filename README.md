# 🚀 Digitalização de Pátios Mottu

**🎥 Vídeo de Apresentação Final:** [https://youtu.be/PBGoGo5Y_Is](https://youtu.be/PBGoGo5Y_Is)

**🌐 Aplicação em Produção:** [https://code-crafters-tl45.onrender.com](https://code-crafters-tl45.onrender.com)

---

## 📃 Descrição do Projeto

No dinâmico cenário da mobilidade urbana, a gestão de grandes frotas — como a da **Mottu** — enfrenta desafios significativos.  
A ausência de um sistema centralizado e em tempo real para monitorar veículos resulta em:

- Perda de tempo na localização de motos;  
- Ineficiência operacional;  
- Aumento de custos;  
- Decisões prejudicadas pela falta de dados precisos.  

Para solucionar esses problemas, desenvolvemos uma **plataforma inovadora** que oferece **mapeamento geográfico e rastreamento em tempo real** da frota de motos da Mottu.

---

### ✨ Principais Benefícios
a
- 🗺️ **Mapa interativo** com áreas delimitadas para cada pátio.  
- 📡 **Rastreamento em tempo real** com status operacional:
  - Disponível  
  - Em uso  
  - Em manutenção  
  - Aguardando retirada  
- 🔎 **Visualização clara da distribuição das motos** em cada local.  
- ⚡ **Eficiência operacional aprimorada**, eliminando buscas manuais.  
- 💰 **Redução de custos** e melhor aproveitamento de recursos.  
- 🌱 Base sólida para **inovações futuras na mobilidade urbana**.

---

## 🛠️ Tecnologias Utilizadas

### ⚙️ Backend & DevOps
- **Java 17** – Linguagem principal  
- **Spring Boot** – Estrutura da aplicação  
- **Spring Security + OAuth2** – Autenticação via Google  
- **PostgreSQL** – Banco de dados relacional  
- **Docker** – Gerenciamento de containers  
- **Gradle** – Ferramenta de build e automação  

### 🌐 IoT & Real-Time (Arquitetura Disruptiva)
- **Arduino** – Hardware para simulação de GPS e sensores de proximidade  
- **Node-RED** – Middleware para fluxo de dados IoT (leitura serial e publicação MQTT)  
- **MQTT** – Protocolo leve de mensageria (Broker: `broker.hivemq.com`)  
- **WebSockets (STOMP)** – Comunicação em tempo real entre backend (Java) e dashboard (Thymeleaf)  

### 💻 Frontend
- **Thymeleaf** – Motor de templates para dashboards  
- **HTML / CSS / JavaScript** – Interface visual de monitoramento e rastreamento  

---

## 👨‍💻 Squad: CodeCrafters
- **Nicolas Dobbeck Mendes**  
- **José Bezerra Bastos Neto**  
- **Thiago Henry Dias**

---

## ▶️ Como Executar a Solução Completa (Ponta a Ponta)

### 🔧 Pré-requisitos

Certifique-se de ter instalados:
- **Java 17+** e **Gradle**  
- **Docker Desktop**  
- **Git**  
- **Arduino IDE** (com placa Arduino conectada)  
- **Node-RED** (instalado e rodando localmente)

---

### 🧩 Parte 1: Backend e Banco de Dados (Java)

1. **Clonar o repositório**
    ```bash
    git clone https://github.com/jjosebastos/code_crafters.git
    cd code_crafters
    ```

2. **Configurar variáveis de ambiente**
    Edite o arquivo `application.properties` e insira seu **Client ID** e **Client Secret** do OAuth2:
    ```properties
    spring.security.oauth2.client.registration.google.client-id=SEU_CLIENT_ID_AQUI
    spring.security.oauth2.client.registration.google.client-secret=SEU_CLIENT_SECRET_AQUI
    ```
    ⚠️ *Atenção:* nunca exponha credenciais reais em repositórios públicos.

3. **Subir o banco de dados com Docker**
    ```bash
    docker-compose up -d
    ```

4. **Executar a aplicação Java**
    ```bash
    ./gradlew bootRun
    ```
    O backend iniciará e ficará ouvindo mensagens do broker MQTT.

---

### 📡 Parte 2: Simulação IoT (Arduino + Node-RED)

1. **Carregar o código no Arduino**
    - Abra a **IDE do Arduino**.  
    - Localize o arquivo `Protótipo_IoT.ino` na pasta `Arduino/`.  
    - Faça o upload para sua placa Arduino.

2. **Configurar o Node-RED**
    - Acesse `http://localhost:1880`.  
    - Importe o fluxo `flows.json` (disponível na raiz do projeto).

3. **Associar o Hardware ao Banco de Dados**
    O Arduino envia dados genéricos. Precisamos associá-los à moto e pátio corretos no banco.

    a. **Obtenha os UUIDs da moto e do pátio:**
    ```sql
    SELECT id_moto, nm_modelo, nr_placa FROM t_mtu_moto;
    ```
    Exemplo: `afd8d463-c4c1-44a6-be25-a7419ebfffba`

    b. **Configure o nó “Função” no Node-RED:**
    ```javascript
    var idRealDaMoto = "COLE_O_UUID_AQUI"; // <-- Cole o UUID da sua moto
    ```
    Este passo garante que os dados enviados sejam associados à moto correta.

4. **Ajustar Conexões e Fazer o Deploy**
    - **Serial In:** selecione a porta correta (`COM3`, `/dev/ttyACM0`, etc.)  
    - **MQTT Out:** confirme o broker `broker.hivemq.com`  
    - Clique em **Deploy**

Agora, os dados do seu Arduino são lidos, enriquecidos e publicados via MQTT para o backend Java.

---

### 🖥️ Parte 3: Visualização (Dashboard)

1. **Acesse o sistema:**
    👉 [http://localhost:8081](http://localhost:8081)

2. **Login com Google**
    - Autentique-se via OAuth2.

3. **Visualize os dados em tempo real**
    - Vá até as páginas **Monitoramento** ou **Rastreamento**.  
    - Interaja fisicamente com o Arduino (potenciômetro e sensor ultrassônico).  
    - Observe as atualizações **em tempo real** no dashboard, graças à comunicação WebSocket.

---

## 📌 Observações Finais

- O login é realizado via autenticação Google (OAuth2).  
- A arquitetura foi projetada para **escalabilidade**, **modularidade** e **integração futura** com outras soluções de mobilidade urbana.  
- A versão atual está **deployada no Render**:  
  👉 [https://code-crafters-tl45.onrender.com](https://code-crafters-tl45.onrender.com)
