package com.whitehall.esp.microservices;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.netflix.discovery.shared.Application;
@SpringBootApplication
//@EnableDiscoveryClient
public class SmartyApartmentsApplication {

	private static final Log LOGGER = LogFactory.getLog(Application.class);
	public static void main(String[] args) {
		SpringApplication.run(SmartyApartmentsApplication.class, args);
	}

//	public static void main(final String... args) throws MqttException {
//
//		LOGGER.info("\n========================================================="
//				  + "\n                                                         "
//				  + "\n          Welcome to Spring Integration!                 "
//				  + "\n                                                         "
//				  + "\n    For more information please visit:                   "
//				  + "\n    https://spring.io/projects/spring-integration        "
//				  + "\n                                                         "
//				  + "\n=========================================================" );
//
//		LOGGER.info("\n========================================================="
//				  + "\n                                                          "
//				  + "\n    This is the MQTT Sample -                             "
//				  + "\n                                                          "
//				  + "\n    Please enter some text and press return. The entered  "
//				  + "\n    Message will be sent to the configured MQTT topic,    "
//				  + "\n    then again immediately retrieved from the Message     "
//				  + "\n    Broker and ultimately printed to the command line.    "
//				  + "\n                                                          "
//				  + "\n=========================================================" );
//
//		SpringApplication.run(SmartyApartmentsApplication.class, args);
//		String publisherId = UUID.randomUUID().toString();
//		IMqttClient publisher = new MqttClient("tcp://192.168.0.13:1000",publisherId);
//		MqttConnectOptions options = new MqttConnectOptions();
//		options.setAutomaticReconnect(true);
//		options.setCleanSession(true);
//		options.setConnectionTimeout(10);
//		publisher.connect(options);
//		  MqttMessage msg = readEngineTemp();
//	        msg.setQos(0);
//	        msg.setRetained(true);
//	        publisher.publish("device/test", msg);  
//	}
//
//	 private static  MqttMessage readEngineTemp() { 
//		 Random rnd= new Random();
//	        double temp =  80 + rnd.nextDouble() * 20.0;        
//	        byte[] payload = String.format("T:%04.2f",temp)
//	          .getBytes();        
//	        return new MqttMessage(payload);           
//	    }
//	@Bean
//	public MqttPahoClientFactory mqttClientFactory() {
//		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//		MqttConnectOptions options = new MqttConnectOptions();
//		options.setServerURIs(new String[] { "tcp://192.168.0.13:1000" });
//		//options.setUserName("guest");
//		//options.setPassword("guest".toCharArray());
//		factory.setConnectionOptions(options);
//		return factory;
//	}
//	
//		@Bean
//		public IntegrationFlow mqttOutFlow() {
//			return IntegrationFlows.from(CharacterStreamReadingMessageSource.stdin(),
//							e -> e.poller(Pollers.fixedDelay(1000)))
//					.transform(p -> p + " sent to MQTT")
//					.handle(mqttOutbound())
//					.get();
//		}
//
//		@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
//	    public interface MyGateway {
//
//	        void sendToMqtt(String data);
//
//	    }
//		@Bean
//		public MessageHandler mqttOutbound() {
//			MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("siSamplePublisher", mqttClientFactory());
//			messageHandler.setAsync(true);
//			messageHandler.setDefaultTopic("device/test");
//			return messageHandler;
//		}
//	
//		// consumer
//	
//		@Bean
//		public IntegrationFlow mqttInFlow() {
//			return IntegrationFlows.from(mqttInbound())
//					.transform(p -> p + ", received from MQTT")
//					.handle(logger())
//					.get();
//		}
//	
//		private LoggingHandler logger() {
//			LoggingHandler loggingHandler = new LoggingHandler("INFO");
//			loggingHandler.setLoggerName("siSample");
//			return loggingHandler;
//		}
//	
//		@Bean
//		public MessageProducerSupport mqttInbound() {
//			MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("siSampleConsumer",
//					mqttClientFactory(), "device/test");
//			adapter.setCompletionTimeout(5000);
//			adapter.setConverter(new DefaultPahoMessageConverter());
//			adapter.setQos(1);
//			return adapter;
//		}
}
