package com.whitehall.esp.microservices.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.management.relation.RoleInfoNotFoundException;
import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitehall.esp.microservices.exceptions.EntityNotFoundException;
import com.whitehall.esp.microservices.exceptions.UserAlreadyExistException;
import com.whitehall.esp.microservices.exceptions.UserNotFoundException;
import com.whitehall.esp.microservices.messagebroker.Producer;
import com.whitehall.esp.microservices.model.Account;
import com.whitehall.esp.microservices.model.JWTTokenPayload;
import com.whitehall.esp.microservices.model.Login;
import com.whitehall.esp.microservices.model.NotificationType;
import com.whitehall.esp.microservices.model.Notifications;
import com.whitehall.esp.microservices.model.Role;
import com.whitehall.esp.microservices.model.User;
import com.whitehall.esp.microservices.services.AccountService;
import com.whitehall.esp.microservices.services.RolesService;
import com.whitehall.esp.microservices.services.UserService;
import com.whitehall.esp.microservices.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class UserController extends GenericController 
{

	// private static final Logger
	// log=LoggerFactory.getLogger(UserController.class);
	/*
	 * @Autowired private UserRepository UserRepo;
	 */

	public static final String MAIL_BUTTON_STYLE = "\"color: #ffffff; text-align:center;text-decoration: none; text-transform: uppercase;\" target=\"_blank\" class=\"mobile-button\"";

	public static final String MAIL_BUTTON_TEXT_VERIFY = "CLICK TO VERIFY";
	public static final String ACCEPT_INVITATION = "ACCEPT INVITATION";
	@Value("${server.endpoint}")
	private String serverPath;
	@Value("${server.front-endPoint}")
	private String frontEndPath;
	
	//private GenericController genericController;
//	@Autowired
//	private DiscoveryClient discoveryClient;

	@Autowired
	private UserService userService;

	@Autowired
	private RolesService roleService;

	@Autowired
	private Producer kafkaProducer;
	
	@Value("${spring.application.pub-sub}")
	private Boolean pubsubEnabled;
	
	@Autowired
	private JsonUtils mapper;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ObjectMapper objectmapper;
	
	/*
	 * @GetMapping("/code/{User}") public Mono<User>
	 * findByUsercode(@PathVariable("User") String code) {
	 * log.info("findByUser: UserCode={}", code); return
	 * userService.findByUserCode(code); }
	 */
//	@GetMapping("/service-instances/{applicationName}")
//	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
//		return this.discoveryClient.getInstances(applicationName);
//	}

	@GetMapping("/")
	public Flux<User> findAll() {
		log.info("findAll");
		
		return userService.findAll();
	}

	@GetMapping("/{id}")
	public Mono<User> findById(@PathVariable("id") String id) throws EntityNotFoundException {
		log.info("findById: id={}", id);
		return userService.getUser(id);
	}
	
	@GetMapping("/bytoken")
	public Mono<User> findFromToken() {
		log.info("inside findfromtoken");
		String email= getEmailFromToken();
		return userService.findByEmail(email);
	}
	

	@PostMapping("/")
	public Mono<User> create(@Valid @RequestBody User User) {
		return userService.createUser(User);
	}

	@GetMapping("/byphone/{phone}")
	public Mono<User> findByPhone(@PathVariable String phone) {
		return userService.findByPhone(phone);
	}

	@GetMapping("/byemail/{email}")
	public Mono<User> findByEmail(@PathVariable String email) {
		Mono<User> user = userService.findByEmail(email);
		log.info("account is: {}", user.block().getAccount().getAccountId());
		return user;
	}
	
	@GetMapping("/search/{regexp}")
	public Flux<User> searchUserByEmailAsRegexp(@PathVariable String regexp) {
		log.info("findByRegexp: {}");
		return userService.findUsersByRegexpName(regexp);
	}

	@GetMapping("/account/{accountId}")
	public Flux<User> findByAccountId(@PathVariable String accountId) {
		log.info("findAll find With AccountId: " + accountId);
		return userService.findByAccountAccountId(accountId);
	}

	@GetMapping("/accounts/all")
	public Flux<Account> findByAccounts() {
		return accountService.findAll();
	}

	
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody Login login) {
		try
		{
		return new ResponseEntity<JWTTokenPayload>(
				new JWTTokenPayload(userService.signin(login.getUsername(), login.getPassword())), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			return  new ResponseEntity<>(mapper.setError(ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/oauth2/callback/{registrationId}")
	public ResponseEntity<String> authenticate(@PathVariable String registrationId) {
		log.info("registrationId: " + registrationId);
		return new ResponseEntity<String>("fine", HttpStatus.OK);
	}
	@PatchMapping("/add/contact/{contactEmail}")
	public Mono<User> addAsContact(@PathVariable String contactEmail) 
	{
		String ownerEmail = getEmailFromToken();
		
		log.info("addAsContact: ownerEmail {},  contactEmail{}",ownerEmail,contactEmail);
		User owner = userService.findByEmail(ownerEmail).block();
		User contact=userService.findByEmail(contactEmail).block();
		log.info(userService.findByEmail(ownerEmail).block()+"  "+contact);
		if(null==owner || null==contact)
		{
			throw new UserNotFoundException("User Not Found");
		}
		
		if(!owner.getContacts_email().contains(contactEmail) && !contact.getContacts_email().contains(ownerEmail))
		{
			Notifications notification=new Notifications();
			notification.setNotification_content(owner.getFirstname()+" wants to add you as a Contact");
			notification.setNotification_type(NotificationType.CONTACT_REQUEST);
			notification.setSender_email(ownerEmail);
			notification.setStatus("unread");
			contact.getNotifications().add(notification);
			contact.getPending_requests_email().add(ownerEmail);
			//contact=userService.updateUser(contact).block();
			//owner=userService.updateUser(owner).block();
			//Mono<User> own=Mono.when(userService.updateUser(contact)).then(userService.updateUser(owner));
			
		}
		 
		return userService.updateUser(contact);
	}
	
	@PatchMapping("/accept/contact/{ownerEmail}")
	public Mono<User> acceptContactRequest(@PathVariable String ownerEmail) 
	{	
		String contactEmail =  getEmailFromToken();
		log.info("acceptAsContact: ownerEmail {},  contactEmail{}",ownerEmail,contactEmail);
		User owner = userService.findByEmail(ownerEmail).block();
		User contact=userService.findByEmail(contactEmail).block();
		
		if(null==owner || null==contact)
		{
			throw new UserNotFoundException("User Not Found");
		}
		
		if(!owner.getContacts_email().contains(contactEmail) && !contact.getContacts_email().contains(ownerEmail))
		{
			//contact.getContacts().add(owner);
			//contact.getPending_requests().remove(owner);
			//contact=userService.updateUser(contact).block();
			owner.getContacts_email().add(contactEmail);
			contact.getContacts_email().add(ownerEmail);
			contact.getPending_requests_email().remove(ownerEmail);
			
			Notifications notificationContact=new Notifications();
			notificationContact.setNotification_content(owner.getFirstname()+" has been added to your contact");
			notificationContact.setNotification_type(NotificationType.CONTACT_ADDED);
			notificationContact.setStatus("read");
			contact.getNotifications().add(notificationContact);
			
			Notifications notificationOwner=new Notifications();
			notificationOwner.setNotification_content(contact.getFirstname()+" has accepted your contact request");
			notificationOwner.setNotification_type(NotificationType.CONTACT_ADDED);
			notificationOwner.setStatus("unread");
			owner.getNotifications().add(notificationOwner);
		}
		// contact=userService.findByEmail(contactEmail).block();
		//log.info(contact.getContacts_email().toString());
		return userService.updateUser(owner).then(userService.updateUser(contact));
	}
	@PatchMapping("/decline/contact/{ownerEmail}")
	public Mono<User> declineContactRequest(@PathVariable String ownerEmail) 
	{	
		String contactEmail =  getEmailFromToken();
		log.info("acceptAsContact: ownerEmail {},  contactEmail{}",ownerEmail,contactEmail);
		User owner = userService.findByEmail(ownerEmail).block();
		User contact=userService.findByEmail(contactEmail).block();
		
		if(null==owner || null==contact)
		{
			throw new UserNotFoundException("User Not Found");
		}
		
		if(!owner.getContacts_email().contains(contactEmail) && !contact.getContacts_email().contains(ownerEmail) && !contact.getPending_requests_email().contains(ownerEmail))
		{
			contact.getPending_requests_email().remove(ownerEmail);
						
			Notifications notificationOwner=new Notifications();
			notificationOwner.setNotification_content(contact.getFirstname()+" has declined your contact request");
			notificationOwner.setNotification_type(NotificationType.CONTACT_REQUEST_REJECTED);
			notificationOwner.setStatus("unread");
			owner.getNotifications().add(notificationOwner);
		}
		// contact=userService.findByEmail(contactEmail).block();
		//log.info(contact.getContacts_email().toString());
		return userService.updateUser(owner).then(userService.updateUser(contact));
	}
	
	@PatchMapping("/remove/contact/{conatctEmail}")
	public Mono<User> removeContact(@PathVariable String contactEmail) 
	{	
		String ownerEmail =  getEmailFromToken();
		log.info("acceptAsContact: ownerEmail {},  contactEmail{}",ownerEmail,contactEmail);
		
		User owner = userService.findByEmail(ownerEmail).block();
		User contact=userService.findByEmail(contactEmail).block();
		
		if(null==owner || null==contact)
		{
			throw new UserNotFoundException("User Not Found");
		}
		
		owner.getContacts_email().remove(contactEmail);
		contact.getContacts_email().remove(ownerEmail);
		return userService.updateUser(contact).then(userService.updateUser(owner));
	}
	
	@GetMapping("/contacts/{userEmail}")
	public List<User> getUserContact(@PathVariable String userEmail){
		List<User> contacts =  new ArrayList<User>();
		User user = userService.findByEmail(userEmail).block();
		for(String email:user.getContacts_email()) {
			contacts.add(userService.findByEmail(email).block());
		}
		return contacts;
	}
	@PostMapping("/register")
	public ResponseEntity<Mono<User>> register(@RequestBody User userModel) throws JsonProcessingException,
			AddressException, MessagingException, IOException, RoleInfoNotFoundException, EntityNotFoundException {
		// first create an Account and then
		// create a user
		//
		List<Role> roles = new ArrayList<Role>();

		if (!userModel.getRoles().isEmpty()) {
			userModel.getRoles().forEach(rol -> {
				roles.add(roleService.findByRoleName(rol.getRoleName()).block());
			});

		} else {
			roles.add(roleService.findByRoleName("ROLE_USER_VIEW").block());
		}
		
    	if(userService.existsByEmailId(userModel.getEmail()).block())
		{
			throw new UserAlreadyExistException("User already exists with this email Id: "+userModel.getEmail());
		}
    	
    	if(userService.existsByPhone(userModel.getPhone()).block())
		{
			throw new UserAlreadyExistException("User already exists with this phone Id: "+userModel.getPhone());
		}
		
	
		Account account = new Account();
		account.setAccountName(userModel.getEmail());
		Mono<Account> createdAccount = accountService.createAccount(account);
		account = createdAccount.block();
		userModel.setAccount(account);	
		userModel.setRoles(roles);
		userModel.setIsAccountOwner(true);
		userModel.setIsactive(true);
		userModel.setPermissions(userModel.setPermissionsFromFile());
		log.info("account details");
		Mono<User> user = create(userModel);
		 //User u=user.block();
		// sendemail(u, account);

		log.info("making a call to create an account");
		log.info(mapper.print(account));
		log.info("is pubsub enabled : {}",pubsubEnabled);
		if(pubsubEnabled)
			this.kafkaProducer.sendMessageUser(mapper.print(user));
		else 
			sendEmail(userModel.getUserId(), account.getAccountId());
		
		log.info("user registered with these details");
		log.info(mapper.print(userModel));
		return new ResponseEntity<Mono<User>>(user.map(u->
		{
		this.kafkaProducer.sendMessageUser(mapper.print(u));
		mapper.print(u);
		return u;
		}		
		), HttpStatus.OK);
	}

	@PatchMapping("/notification/{notificationId}/status/read")
	public Mono<User> NotifcationStatusRead(@PathVariable String notificationId)throws JsonProcessingException 
	{	String userEmail = getEmailFromToken();
		User user =  userService.findByEmail(userEmail).block();
		List<Notifications> notifications = user.getNotifications();
//		for(Notifications notification: notifications) {
//			if(notification.getNotificationId().equalsIgnoreCase(notificationId)) {
//				notification.setStatus("read");
//				break;
//			}
//		}
		
		notifications.forEach(e -> {
			if(notificationId.equalsIgnoreCase(e.getNotificationId()))
					{
						e.setStatus("read");
					}
		});
		log.info("notifications: {}",mapper.print(notifications));
//		List<Notifications>read=notifications
//		.stream()
//		.filter(e->e.getStatus().equalsIgnoreCase("read"))
//		.collect(Collectors.toList());
		
		
		return userService.updateUser(user);
	}
	@PostMapping("/invite/register")
	public ResponseEntity<JsonNode> inviteregister(@RequestBody JsonNode data) throws JsonProcessingException,
			AddressException, MessagingException, IOException, RoleInfoNotFoundException {
		 Object json = objectmapper.readValue(data.toString(), Object.class);
	        log.info(mapper.print(json));
	        log.info(data.get("email").textValue());
		return new ResponseEntity<JsonNode>(mapper.setData("You have Successfully Registered....."+data.get("email").textValue()), HttpStatus.OK);
	}
	@PostMapping("/register/{accountId}")
	public ResponseEntity<Mono<User>> registerAndAddToAccount(@RequestBody User userModel,
			@PathVariable String accountId) throws JsonProcessingException, AccountNotFoundException, AddressException,
			MessagingException, IOException, RoleInfoNotFoundException, EntityNotFoundException {
		List<Role> roles = new ArrayList<Role>();
		if (!userModel.getRoles().isEmpty()) {
			userModel.getRoles().forEach(rol -> {
				roles.add(roleService.findByRoleName(rol.getRoleName()).block());
			});

		} else {
			roles.add(roleService.findByRoleName("ROLE_USER_VIEW").block());
		}
		log.info("user registered with these details");
		log.info(mapper.print(userModel));
		Mono<Account> createdAccount = accountService.getAccount(accountId);
		Account account = createdAccount.block();
		if (null == account) {
			throw new AccountNotFoundException();
		}
		userModel.setAccount(account);
		userModel.setRoles(roles);
		userModel.setPermissions(userModel.setPermissionsFromFile());
		userModel.setEnabled(true);
		userModel.setIsactive(true);
		log.info("account details");
		Mono<User> user = create(userModel);
		log.info("making a call to get an account");
		log.info(mapper.print(account));
		return new ResponseEntity<Mono<User>>(user, HttpStatus.OK);
	}

	@PatchMapping("/add/account/{accountId}")
	public Mono<User> addUserToAccount(@RequestBody User userModel, @PathVariable String accountId) {
		userModel.setAccount(new Account(accountId));
		return create(userModel);
	}

	@PatchMapping("/{emailId}/change/account/{accountId}")
	public Mono<User> addExistsingUserToAccount(@PathVariable String emailId, @PathVariable String accountId) {
		User u = userService.findByEmail(emailId).block();
		if (null == u) {
			throw new UserNotFoundException("User not found in the database");
		}
		u.setAccount(new Account(accountId));
		return userService.createUser(u);
	}

	@PatchMapping("/block/{userToBlockEmail}")
	public Mono<User> blockUser(@PathVariable String userToBlockEmail){

		User owner = removeContact(userToBlockEmail).block();//userService.findByEmail(emailID).block();
		User userToBlock = userService.findByEmail(userToBlockEmail).block();
		owner.getBlocked_user_email().add(userToBlockEmail);
		userToBlock.getBlocked_by_user_email().add(getEmailFromToken());
		return userService.updateUser(userToBlock).then(userService.updateUser(owner));
	}
	
	@PatchMapping("/unblock/{userToUnblockEmail}")
	public Mono<User> unblockUser( @PathVariable String userToUnblockEmail){
		String emailID = getEmailFromToken();
		User owner = userService.findByEmail(emailID).block();
		if(null== owner)
		{
			throw new UserNotFoundException("User with email: "+emailID+" doesnot exist");  
		}
		User userToUnblock = userService.findByEmail(userToUnblockEmail).block();
		if(null== userToUnblock)
		{
			throw new UserNotFoundException("User with email: "+userToUnblockEmail+" doesnot exist");  
		}
		
		owner.getBlocked_user_email().remove(userToUnblockEmail);
		userToUnblock.getBlocked_by_user_email().remove(emailID);
		
		return userService.updateUser(userToUnblock).then(userService.updateUser(owner));
	}
	
	@GetMapping(value = "/welcomeuser")
	public ResponseEntity<String> sendWelcomeEmail(User user) throws AddressException, MessagingException, IOException {
		String bodyHTML = "";
		bodyHTML += getHtmlContent("src/main/resources/htmlcontent/welcomeEmail.html");
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("whitehalltechnologies@gmail.com", "whitehall@123");
			}
		});
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("whitehalltechnologies@gmail.com", false));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
		msg.setSubject("Welcome " + user.getFirstname());
		msg.setContent(bodyHTML, "text/html");
		msg.setSentDate(new Date());
		Transport.send(msg);
		return new ResponseEntity<String>("Email sent successfully", HttpStatus.OK);
	}
	
	public static String getHtmlContent(String filePath) {
		log.info("MailerService.getHtmlContent : START");
		log.info("filePath [{}]", filePath);

		String message = "";
		try {
			log.info("Getting html file");
			UserController.class.getClassLoader();
			FileInputStream fis = new FileInputStream(filePath);
			message = IOUtils.toString(fis, "UTF-8");

			log.info("MailerService.getHtmlContent : END");
			return message;
		} catch (Exception ex) {
			log.info("Error occured while getHtmlContent : " + ex.getMessage());
			log.info("MailerService.getHtmlContent : END");
			return null;
		}
	}

	@GetMapping(value = "/sendemail/{userId}/{accountId}")
	public ResponseEntity<User> sendEmail(@PathVariable String userId, @PathVariable String accountId)
			throws AddressException, MessagingException, IOException, JsonProcessingException, EntityNotFoundException {
		User user = userService.getUser(userId).block();
		Account account = accountService.getAccount(accountId).block();//userService.findByAccountID(accountId).block();
		log.info("sending email to user with details as:");
		log.info(mapper.print(user));
		log.info("account details as:");
		log.info(mapper.print(account));
		sendemail(user, account);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	public void sendemail(User user, Account account) throws AddressException, MessagingException, IOException {
		log.info("sending account confirmation email");
		// UUID token= UUID.randomUUID();
		String token = user.getConfirmationToken();
		String bodyHTML = "";
		String linkPath = serverPath + "/api/v1/user/verify";
		bodyHTML += getHtmlContent("src/main/resources/htmlcontent/confirmationEmail.html");
		String linkContent = "<a href=\"" + linkPath + "" + "/" + token.toString() + "/" +
		// "5ca0a064353de9237868c765"+
				account.getAccountId() + "\" " + MAIL_BUTTON_STYLE + ">" + " " + MAIL_BUTTON_TEXT_VERIFY + " "
				+ "</a> ";
		bodyHTML += linkContent;
		bodyHTML += getHtmlContent("src/main/resources/htmlcontent/confirmationBottom.html");
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("whitehalltechnologies@gmail.com", "whitehall@123");
			}
		});
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("whitehalltechnologies@gmail.com", false));

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
		msg.setSubject("Email Verification");
		msg.setContent(bodyHTML, "text/html");
		msg.setSentDate(new Date());

		/*
		 * MimeBodyPart messageBodyPart = new MimeBodyPart();
		 * messageBodyPart.setContent("Tutorials point email", "text/html");
		 * 
		 * Multipart multipart = new MimeMultipart();
		 * multipart.addBodyPart(messageBodyPart); MimeBodyPart attachPart = new
		 * MimeBodyPart();
		 * 
		 * attachPart.attachFile("/var/tmp/image19.png");
		 * multipart.addBodyPart(attachPart); msg.setContent(multipart);
		 */
		Transport.send(msg);
		// user.setConfirmationToken(token.toString());
		// userService.createUser(user);
	}

	@GetMapping(value = "/sendemail/invite/{email}/{accountId}")
	public ResponseEntity<JsonNode> inviteUserThroughEmail(@PathVariable String email,@PathVariable String accountId)
			throws AddressException, MessagingException, IOException,AccountNotFoundException, EntityNotFoundException {
		String topEmailPath="src/main/resources/htmlcontent/invitationmail_top.html";
		String bottomEmailPath="src/main/resources/htmlcontent/invitationmail.html";
		Mono<Account> createdAccount = accountService.getAccount(accountId);
		Account account = createdAccount.block();
		if (null == account) {
			throw new AccountNotFoundException();
		}
		sendAcceptInvitationEmail(email, account, topEmailPath, bottomEmailPath);
		return new ResponseEntity<JsonNode>(mapper.setData("Email sent successfully to " + email), HttpStatus.OK);
	}

	public void sendAcceptInvitationEmail(String email, Account account, String topEmailPath,
			String bottomEmailPath) throws AddressException, MessagingException, IOException {

		log.info("sending account confirmation email");
		// UUID token= UUID.randomUUID();
		String bodyHTML = "";
		String linkPath = frontEndPath + "/assets/html/invitation-registration.html";
		bodyHTML += getHtmlContent(topEmailPath);
		String linkContent = "<a href=\"" + linkPath + "\" " + MAIL_BUTTON_STYLE
				+ ">" + " " + ACCEPT_INVITATION + " " + "</a> ";
		bodyHTML += linkContent;
		bodyHTML += getHtmlContent(bottomEmailPath);
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("whitehalltechnologies@gmail.com", "whitehall@123");
			}
		});
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("whitehalltechnologies@gmail.com", false));

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
		msg.setSubject(account.getAccountName()+" invited you to get added as a user and explore the world of IOT");
		msg.setContent(bodyHTML, "text/html");
		msg.setSentDate(new Date());

		/*
		 * MimeBodyPart messageBodyPart = new MimeBodyPart();
		 * messageBodyPart.setContent("Tutorials point email", "text/html");
		 * 
		 * Multipart multipart = new MimeMultipart();
		 * multipart.addBodyPart(messageBodyPart); MimeBodyPart attachPart = new
		 * MimeBodyPart();
		 * 
		 * attachPart.attachFile("/var/tmp/image19.png");
		 * multipart.addBodyPart(attachPart); msg.setContent(multipart);
		 */
		Transport.send(msg);
		// user.setConfirmationToken(token.toString());
		// userService.createUser(user);

	}

	@GetMapping(value = "/verify/{code}/{accountId}")
	public ResponseEntity<String> verifUserAndCreateAccountThenAddUser(@PathVariable String code,
			@PathVariable String accountId) throws AddressException, MessagingException, IOException {
		log.info("user verification code is{}", code);
		User user = userService.findByConfirmationToken(code).block();
		log.info("user found with these details");
		log.info(mapper.print(user));
		// Account account=accountRepository.findById(accountId).get();
		// set this account into user means path from user to account is created
		// user.setAccount(account);
		// user.setEnabled(true);
		// userService.createUser(user);

		userService.verifyUser(user).block();
		sendWelcomeEmail(user);
		return new ResponseEntity<String>("           Email Verified Successfully. Thanks !!", HttpStatus.OK);
	}

	// roles and permissions

	@GetMapping(value = "/getRoles")
	public ResponseEntity<Flux<Role>> getRolesList() throws AddressException, MessagingException, IOException {
		log.info("getRolesList{}");
		return new ResponseEntity<Flux<Role>>(roleService.findAll(), HttpStatus.OK);
	}
	@PatchMapping("/{emailId}/{language}")
	public Mono<User>changelanguage( @PathVariable String emailId, @PathVariable String language) {
		User u=userService.findByEmail(emailId).block();
		 if (null==u) {
			 throw new UserNotFoundException("User not found");
		 }
		 u.setFirst_visited(false);
		 u.setLanguage_preffered(language);
		  return userService.updateUser(u);
	}
	
	@GetMapping("test/googleassistant/{TextField}/{NumberField}")
	public void ifttChekc(String TextField,int NumberField) {
		log.info("ifttChekc: NumberField={},TextField={}", NumberField,TextField);
	}

}
