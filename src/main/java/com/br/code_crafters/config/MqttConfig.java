package com.br.code_crafters.config;

import com.br.code_crafters.monitoringiot.IotMonitoringService;
import com.br.code_crafters.monitoringiot.IotVagaService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;


@Configuration
public class  MqttConfig {

    @Autowired
    private IotMonitoringService iotMonitoringService;

    @Autowired
    private IotVagaService iotVagaService;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{"tcp://broker.hivemq.com:1883"});
        factory.setConnectionOptions(options);

        return factory;
    }


    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }


    @Bean
    public MqttPahoMessageDrivenChannelAdapter inbound() {
        String[] topicosParaOuvir = {
                "mottu/motos/+/localizacao",
                "mottu/patios/+/vagas/+/status"
        };

        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter("springBootClientId-patios-app",
                        mqttClientFactory(),
                        topicosParaOuvir);

        adapter.setCompletionTimeout(5000);
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
            String payload = (String) message.getPayload();

            System.out.println("[MQTT HANDLER] Mensagem recebida!");
            System.out.println("Tópico: " + topic);
            System.out.println("Payload: " + payload);

            if (topic.startsWith("mottu/motos/")) {
                iotMonitoringService.processarAtualizacaoLocalizacao(topic, payload);
            } else if (topic.startsWith("mottu/patios/")) {
                iotVagaService.processarAtualizacaoVagaFromMqtt(topic, payload);
            } else {
                System.err.println("Tópico MQTT não reconhecido: " + topic);
            }
        };
    }
}