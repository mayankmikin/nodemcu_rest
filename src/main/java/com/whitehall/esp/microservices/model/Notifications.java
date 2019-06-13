package com.whitehall.esp.microservices.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Notifications 
{
		//@Id
		private String notificationId=UUID.randomUUID().toString();
		private NotificationType notification_type;
		private String status;
		private String notification_content;
		private Instant notification_generation_time= Instant.now();
		private Instant notification_expire_time= Instant.now().plus(2, ChronoUnit.DAYS);
		private String cause;
		private String sender_email;
	
}
