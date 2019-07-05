package com.whitehall.esp.microservices.config;


import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@IntegrationComponentScan
public class MqttOutboundConfig {
	@Autowired
	static ApplicationContext appContext;

	@Value("${spring.data.mqtt.uri}")
	private String host;
	
	@Value("${spring.data.mqtt.topic}")
	private String topic;
	
	@Value("${spring.data.mqtt.username}")
	private String username;
	
	@Value("${spring.data.mqtt.password}")
	private String password;
	
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { host });
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(String newTopic) {
        MqttPahoMessageHandler messageHandler =
                       new MqttPahoMessageHandler("nodeMCUService", mqttClientFactory());
        messageHandler.setAsync(true);
        topic=(newTopic!="")? newTopic:topic;
        messageHandler.setDefaultTopic(topic);
        
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface MyGateway {

        void sendToMqtt(String data,String newTopic);

    }
    
    public static void sendToMqtt(String data, String newTopic) {
    	MyGateway gateway =  appContext.getBean(MyGateway.class);
            gateway.sendToMqtt(data,newTopic);
   }
   
}