//package com.whitehall.esp.microservices.config;
//
//import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
//import org.eclipse.paho.client.mqttv3.IMqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.integration.core.MessageProducer;
//import org.springframework.integration.mqtt.core.ConsumerStopAction;
//import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
//import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
//import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
//import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
//import org.springframework.integration.mqtt.support.MqttHeaders;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageHandler;
//import org.springframework.messaging.MessagingException;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Configuration
//public class MqttInboundConfig {
//
//	@Value("${spring.data.mqtt.uri}")
//	private String host;
//	
//	@Value("${spring.data.mqtt.topic}")
//	private String topic;
//	
//	@Value("${spring.data.mqtt.username}")
//	private String username;
//	
//	@Value("${spring.data.mqtt.password}")
//	private String password;
//	
//	@Autowired
//	private ObjectMapper objectMapper;
//	
//    @Bean
//    public MessageChannel mqttInputChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    public MessageProducer inbound() {
//    	DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
//    	clientFactory.getConnectionOptions().setUserName(username);
//    	clientFactory.getConnectionOptions().setPassword(password.toCharArray());
//    	
//        MqttPahoMessageDrivenChannelAdapter adapter =
//                new MqttPahoMessageDrivenChannelAdapter(host, "nodeMCUService",clientFactory,"device/+");
//      
//      
//        adapter.setCompletionTimeout(5000);
//        adapter.setConverter(new DefaultPahoMessageConverter());
//        adapter.setQos(1);
//        adapter.setOutputChannel(mqttInputChannel());
//        return adapter;
//    }
//
//    @Bean
//    @ServiceActivator(inputChannel = "mqttInputChannel")
//    public MessageHandler handler() {
//        return new MessageHandler() {
//
//            @Override
//            public void handleMessage(Message<?> message) throws MessagingException {
//            	//MqttHeaders header =  objectMapper.convertValue(message.getHeaders(),MqttHeaders.class);
//            	String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
//            	System.out.println("Topic:"+topic+"  Message:"+message.getPayload());
//            }
//
//        };
//    }
//
//}