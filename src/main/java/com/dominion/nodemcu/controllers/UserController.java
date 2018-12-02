package com.dominion.nodemcu.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dominion.nodemcu.entity.Account;
import com.dominion.nodemcu.entity.User;
import com.dominion.nodemcu.model.DeviceRequest;
import com.dominion.nodemcu.repository.AccountRepository;
import com.dominion.nodemcu.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController 
{
	@Autowired
	UserRepository userRepo;
	@Autowired
	AccountRepository accountRepository;
	 public static ObjectMapper mapper = new ObjectMapper();
	public static final String MAIL_BUTTON_STYLE = "\"color: #ffffff; text-align:center;text-decoration: none; text-transform: uppercase;\" target=\"_blank\" class=\"mobile-button\"";
	
	public static final String MAIL_BUTTON_TEXT_VERIFY = "CLICK TO VERIFY";
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	   @GetMapping(value = "/sendemail")
	   public ResponseEntity<String> sendEmail(User user,Account account) throws AddressException, MessagingException, IOException {
		   UUID token= UUID.randomUUID();
		   String bodyHTML = "";
		   String linkPath="http://192.168.0.7:9000/api/user/verify";
		   bodyHTML+=getHtmlContent("src/main/resources/htmlcontent/confirmationEmail.html");
			String linkContent = "<a href=\"" + linkPath + ""
					+ "/" + token.toString()+ "/"+account.getId() + "\" "
					+ MAIL_BUTTON_STYLE + ">"
					+ " " + MAIL_BUTTON_TEXT_VERIFY + " "
					+ "</a> ";
			bodyHTML+=linkContent;
			bodyHTML+=getHtmlContent("src/main/resources/htmlcontent/confirmationBottom.html");
		   Properties props = new Properties();
		   props.put("mail.smtp.auth", "true");
		   props.put("mail.smtp.starttls.enable", "true");
		   props.put("mail.smtp.host", "smtp.gmail.com");
		   props.put("mail.smtp.port", "587");
		   
		   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication("upstartcommerce@gmail.com", "Vizion@123");
		      }
		   });
		   Message msg = new MimeMessage(session);
		   msg.setFrom(new InternetAddress("upstartcommerce@gmail.com", false));

		   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
		   msg.setSubject("Email Verification");
		   msg.setContent(bodyHTML, "text/html");
		   msg.setSentDate(new Date());
		   
		  /* MimeBodyPart messageBodyPart = new MimeBodyPart();
		   messageBodyPart.setContent("Tutorials point email", "text/html");

		   Multipart multipart = new MimeMultipart();
		   multipart.addBodyPart(messageBodyPart);
		   MimeBodyPart attachPart = new MimeBodyPart();

		   attachPart.attachFile("/var/tmp/image19.png");
		   multipart.addBodyPart(attachPart);
		   msg.setContent(multipart);*/
		   Transport.send(msg);
		   user.setConfirmationToken(token.toString());
		   userRepo.save(user);
	      return new ResponseEntity<String>("Email sent successfully",HttpStatus.OK);
	   }  
	   @GetMapping(value = "/welcomeuser")
	   public ResponseEntity<String> sendWelcomeEmail(User user) throws AddressException, MessagingException, IOException {
		   String bodyHTML = "";
		   bodyHTML+=getHtmlContent("src/main/resources/htmlcontent/welcomeEmail.html");
		   Properties props = new Properties();
		   props.put("mail.smtp.auth", "true");
		   props.put("mail.smtp.starttls.enable", "true");
		   props.put("mail.smtp.host", "smtp.gmail.com");
		   props.put("mail.smtp.port", "587");
		   
		   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication("upstartcommerce@gmail.com", "Vizion@123");
		      }
		   });
		   Message msg = new MimeMessage(session);
		   msg.setFrom(new InternetAddress("upstartcommerce@gmail.com", false));
		   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
		   msg.setSubject("Email Verification");
		   msg.setContent(bodyHTML, "text/html");
		   msg.setSentDate(new Date());
		   Transport.send(msg);
	      return new ResponseEntity<String>("Email sent successfully",HttpStatus.OK);
	   }
		public static String getHtmlContent(String filePath)
		{
			LOG.info("MailerService.getHtmlContent : START");
			LOG.info("filePath [{}]", filePath);
			
			String message = "";
			try
			{
				LOG.info("Getting html file");
				ClassLoader classLoader = UserController.class.getClassLoader();
				FileInputStream fis = new FileInputStream(filePath);
				message = IOUtils.toString(fis,"UTF-8");
				
				LOG.info("MailerService.getHtmlContent : END");
				return message;
			}
			catch (Exception ex)
			{
				LOG.info("Error occured while getHtmlContent : " + ex.getMessage());
				LOG.info("MailerService.getHtmlContent : END");
				return null;
			}
		}
		
		@GetMapping(value = "/verify/{code}/{accountId}")
		   public ResponseEntity<String> verifUserAndCreateAccountThenAddUser(@PathVariable String code,@PathVariable Long accountId) throws AddressException, MessagingException, IOException  
		{
			LOG.info("user verification code is{}",code);
			User user=userRepo.findByConfirmationToken(code).get();
			LOG.info("user found with these details");
			LOG.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user));
			Account account=accountRepository.findById(accountId).get();
			user.setAccount(account);
			userRepo.save(user);
			sendWelcomeEmail(user);
			 return new ResponseEntity<String>("Email Verified Successfully ",HttpStatus.OK);
		}

		 @PostMapping("/register")
			public ResponseEntity<User> register(@RequestBody User user) throws AddressException, MessagingException, IOException
			{
			 	
			 	 user.setEnabled(true);
			 	 user=userRepo.save(user);
			 	 Account account=new Account();
			 	 account.addUsers(user);
			 	 account=accountRepository.save(account);
			 	sendEmail(user,account);
			 	LOG.info("user registered with these details");
				 LOG.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user));
					LOG.info("acccount created with these details");
					 LOG.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(account));
				 return new ResponseEntity<User>(user,HttpStatus.OK);
			}
}
