package com.dominion.nodemcu.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dominion.nodemcu.constants.Constants;
import com.dominion.nodemcu.entity.Account;
import com.dominion.nodemcu.entity.ApiResponseMessage;
import com.dominion.nodemcu.entity.Role;
import com.dominion.nodemcu.entity.User;
import com.dominion.nodemcu.exceptions.UserNotFoundException;
import com.dominion.nodemcu.jwtsecurity.JwtGenerator;
import com.dominion.nodemcu.model.JwtAuthenticationToken;
import com.dominion.nodemcu.model.JwtUser;
import com.dominion.nodemcu.model.JwtUserDetails;
import com.dominion.nodemcu.model.Login;
import com.dominion.nodemcu.model.UserModel;
import com.dominion.nodemcu.repository.AccountRepository;
import com.dominion.nodemcu.repository.RoleRepository;
import com.dominion.nodemcu.repository.UserRepository;
import com.dominion.nodemcu.services.AppUserDetailsService;
import com.fasterxml.jackson.databind.JsonNode;
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
	@Autowired
	RoleRepository roleRepo;
	@Value("${server.endpoint}")
	private String serverPath;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private AppUserDetailsService customUserDetailsService;
	
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	 private JwtGenerator jwtGenerator;

	    public UserController(JwtGenerator jwtGenerator) {
	        this.jwtGenerator = jwtGenerator;
	    }

	 public static ObjectMapper mapper = new ObjectMapper();
	public static final String MAIL_BUTTON_STYLE = "\"color: #ffffff; text-align:center;text-decoration: none; text-transform: uppercase;\" target=\"_blank\" class=\"mobile-button\"";
	
	public static final String MAIL_BUTTON_TEXT_VERIFY = "CLICK TO VERIFY";
	
	   @GetMapping(value = "/sendemail")
	   public ResponseEntity<String> sendEmail(User user,Account account) throws AddressException, MessagingException, IOException {
		   UUID token= UUID.randomUUID();
		   String bodyHTML = "";
		   String linkPath=serverPath+"/api/user/verify";
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
			// set this account into user means path from user to account is created
			user.setAccount(account);
			userRepo.save(user);
			sendWelcomeEmail(user);
			 return new ResponseEntity<String>("Email Verified Successfully ",HttpStatus.OK);
		}

		 @PostMapping("/register")
			public ResponseEntity<User> register(@RequestBody UserModel userModel) throws AddressException, MessagingException, IOException
			{
			 	// first create a user 
			 	// then add that user into the account means path from account to user is created
			    // now hold on we need to set account into user as well 
			 	// which will be done when a user's email is verified
			 List<Role>roles=new ArrayList<Role>();
			 if(roles.isEmpty())
			 {
				 roles.add(roleRepo.findByRoleName("STANDARD_USER").get());
			 }
			 else
			 {
				 userModel.getRoles().forEach(e->{
					 roles.add(roleRepo.findById(e).get());
				 });
			 }
			 
			 User user=new User(userModel.getFirstname(), userModel.getLastname(), userModel.getAddress(), userModel.getIsactive(), userModel.getPhone(), userModel.getAccount(), userModel.getCreatedAt(), userModel.getUpdatedAt(), userModel.getEmail(), userModel.getPassword(), userModel.isEnabled(), userModel.getConfirmationToken(), userModel.getIsAccountOwner(), roles);
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
		 
		 @PostMapping("/login")
		 public ResponseEntity<?> authenticate(@RequestBody Login login)
		 {
			 
			 User u= userRepo.findByEmail(login.getUsername()).get();
			 return new ResponseEntity<JwtUserDetails>(new JwtUserDetails(u.getEmail(), u.getId(), u.getConfirmationToken(), null),HttpStatus.OK);
			/*Optional<User> u= userRepo.findByEmail(login.getUsername());
			
			if(u.isPresent())
			{
				User user=u.get(); 
				if(user.getPassword().equalsIgnoreCase(login.getPassword()))
				{
				

					return authenticateLogin(login);
					//JwtAuthenticationToken jwt= new JwtAuthenticationToken(authenticateLogi(new JwtUser(user.getEmail(),user.getId(),user.getRoles())));
					//return new ResponseEntity<JwtAuthenticationToken>(authenticateLogin(login),HttpStatus.CREATED);
				}
				try 
				{
					UserNotFoundException ex= new UserNotFoundException(Constants.UserConstants.INVALID_PASSWORD,Constants.PublicConstants.reason,Constants.PublicConstants.status,Constants.createDetailsForException("email or username", Constants.UserConstants.INVALID_PASSWORD));
					throw ex;
				}
				catch (UserNotFoundException e)
				{
					LOG.error("User not found");
					return new ResponseEntity<ApiResponseMessage>(new ApiResponseMessage(1, e.getMessage(), mapper.convertValue(e.toString(), JsonNode.class)),HttpStatus.BAD_REQUEST);
				}
				catch (Exception e) 
				{
					LOG.error("Unknown Exception");
					return new ResponseEntity<ApiResponseMessage>(new ApiResponseMessage(1, e.getMessage(), mapper.convertValue(e.getMessage(), JsonNode.class)),HttpStatus.BAD_REQUEST);
				}
				
			}
			else
			{
				try 
				{
					UserNotFoundException ex= new UserNotFoundException(Constants.UserConstants.USER_NOT_FOUND,Constants.PublicConstants.reason,Constants.PublicConstants.status,Constants.createDetailsForException("email or username", Constants.UserConstants.USER_NOT_FOUND));
					throw ex;
				}
				catch (UserNotFoundException e)
				{
					LOG.error("User not found");
					return new ResponseEntity<ApiResponseMessage>(new ApiResponseMessage(1, e.getMessage(), mapper.convertValue(e.toString(), JsonNode.class)),HttpStatus.BAD_REQUEST);
				}
				catch (Exception e) 
				{
					LOG.error("Unknown Exception");
					return new ResponseEntity<ApiResponseMessage>(new ApiResponseMessage(1, e.getMessage(), mapper.convertValue(e.getMessage(), JsonNode.class)),HttpStatus.BAD_REQUEST);
				}
			}*/
		 }
		 
		 @PostMapping("/outh/token")
		 public ResponseEntity<JwtAuthenticationToken> authenticateLogin(@RequestBody Login login)
		 {
			 String username = login.getUsername();
				String password = login.getPassword();
			 UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
				Authentication authentication = this.authenticationManager.authenticate(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

				List<String> roles = new ArrayList();

				for (GrantedAuthority authority : userDetails.getAuthorities()) {
					roles.add(authority.toString());
				}
				
				return new ResponseEntity<JwtAuthenticationToken>(new JwtAuthenticationToken(token.toString()),HttpStatus.CREATED);
				//return new ResponseEntity<UserTransfer>(new UserTransfer(userDetails.getUsername(), roles,
						//TokenUtil.createToken(userDetails), HttpStatus.OK), HttpStatus.OK);
			 
			 
		 }			
		 
		
	
	      
}
