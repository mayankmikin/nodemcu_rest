package com.whitehall.esp.microservices.messagebroker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Producer {

	  
	@Value("${spring.kafka.users-topic}")
    private String UserTopic;

	
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessageUser(String message) {
        log.info(String.format("#### -> Producing message -> %s", message));
        this.kafkaTemplate.send(UserTopic, message);
    }
}