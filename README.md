# 🚀 Digitalização de Pátios Mottu

**Vídeo de Apresentação Final:** [INSERIR LINK DO VÍDEO NO YOUTUBE AQUI]

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

## ▶️ Como Executar a Solução Completa (Ponta-a-Ponta)

### Pré-requisitos
Para executar a solução completa, você precisará de:
* **Java 17+** e **Gradle**
* **Docker Desktop** (para o banco de dados)
* **Git**
* **Arduino IDE** (com uma placa Arduino conectada)
* **Node-RED** (instalado e rodando localmente)

---

### Parte 1: Backend e Banco de Dados (Java)

1.  **Clonar o repositório**
    O repositório do projeto Java (`code_crafters`) está dentro do repositório principal.
    ```bash
    git clone https://github.com/jjosebastos/code_crafters.git
    cd code_crafters
    ```

2.  **Configurar variáveis de ambiente**
    No arquivo `application.properties`, substitua as variáveis de OAuth2 pelo **Client ID** e **Client Secret**:
    ```properties
    spring.security.oauth2.client.registration.google.client-id=SEU_CLIENT_ID_AQUI
    spring.security.oauth2.client.registration.google.client-secret=SEU_CLIENT_SECRET_AQUI
    ```
    ⚠️ **Atenção:** Nunca exponha credenciais reais em repositórios públicos.

3.  **Subir o Banco de Dados com Docker**
    Rode o `docker-compose.yaml` (que está na raiz do `code_crafters`) para iniciar o container do PostgreSQL:
    ```bash
    docker-compose up -d
    ```

4.  **Executar a aplicação Java**
    Inicie o backend Spring Boot (projeto `code_crafters`):
    ```bash
    ./gradlew bootRun
    ```
    O backend estará rodando e ouvindo o broker MQTT.
---

### Parte 2: Simulação IoT (Arduino + Node-RED)

1.  **Carregar o Código no Arduino**
    * Abra a **IDE do Arduino**.
    * O código (`Protótipo_IoT.ino`) está na pasta `Arduino/` do repositório.
    * Cole o código-fonte na IDE e faça o upload para a sua placa Arduino.

2.  **Configurar o Node-RED**
    * Abra o **Node-RED** no seu navegador (geralmente `http://localhost:1880`).
    * O fluxo está no arquivo `flows.json` na raiz do repositório. Importe este arquivo para o seu editor.

3.  **Vincular o Hardware ao Banco de Dados (Passo Essencial)**
    O Arduino envia dados genéricos. Precisamos "dizer" ao Node-RED qual moto e qual pátio do banco de dados esse Arduino representa.

    * **a. Obtenha os UUIDs:** Com o projeto Java (`Parte 1`) rodando, acesse seu banco de dados (DBeaver, pgAdmin, etc.) para encontrar os IDs que você irá simular.

        ```sql
        -- 1. Encontre o ID da moto que você quer simular
        SELECT id_moto, nm_modelo, nr_placa FROM t_mtu_moto;
        -- (Copie o id_moto desejado, por exemplo: "afd8d463-c4c1-44a6-be25-a7419ebfffba")

    * **c. Configure o "Tradutor" no Node-RED:** No fluxo do Node-RED, encontre o nó "Change" (ou "Função") que é usado para "traduzir" a mensagem. Dê um duplo clique para editar.

    * **d. Insira os UUIDs:** Dentro deste nó, você encontrará a lógica para anexar os IDs. Procure pela variável que define o ID da moto, como `var idRealDaMoto = "...";`. **É aqui que você deve colar o `id_moto`** que copiou do banco de dados:

        ```javascript
        // Exemplo dentro do nó "Função" do Node-RED
        var idRealDaMoto = "COLE_O_UUID_AQUI"; // <-- COLE O UUID DA SUA MOTO AQUI
        ```
    
    * As imagens (também presentes no repositório) mostram como a configuração do `payload` é feita nesse nó.
    
    * Este passo é crucial: ele transforma dados genéricos do sensor em uma atualização específica para a "Moto X" no "Pátio Y". Isso permite ao Java processar a entidade correta e evitar spam de logs.

4.  **Configurar Conexões e Fazer o Deploy**
    * **a. Conexão Serial:** Dê um duplo clique no nó de entrada (roxo) `serial in`. Selecione a porta USB correta onde seu Arduino foi detectado (ex: `COM3`, `/dev/ttyACM0`, etc.).
    * **b. Conexão MQTT:** Confirme que os nós de saída (verdes) `mqtt out` estão configurados para o broker `broker.hivemq.com`.
    * **c. Deploy:** Clique no botão **Deploy** no canto superior direito do Node-RED.

Neste ponto, os dados do seu Arduino físico estão sendo lidos, enriquecidos com os UUIDs do banco de dados e publicados no MQTT.

---

### Parte 3: Visualização (Resultado Final)

1.  **Acesse o Dashboard**
    Com o Java e o Node-RED rodando, abra a aplicação no seu navegador:
    👉 [http://localhost:8081](http://localhost:8081)

2.  **Faça o Login**
    * Use o sistema de autenticação Google (OAuth2).

3.  **Veja a Mágica Acontecer**
    * Navegue para as páginas de **Monitoramento** ou **Rastreamento**.
    * **Mexa fisicamente** no potenciômetro (GPS) e no sensor ultrassônico (Distância) do seu Arduino.
    * Você verá os status das vagas e a localização das motos no dashboard mudando em **tempo real**, graças à comunicação via WebSockets.

---

## 📌 Observações
- O login é feito com autenticação Google via OAuth2.
- O projeto está pronto para integração e escalabilidade com outras soluções de mobilidade.
