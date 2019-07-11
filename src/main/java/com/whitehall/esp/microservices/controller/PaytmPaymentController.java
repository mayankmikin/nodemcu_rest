package com.whitehall.esp.microservices.controller;

import java.net.MalformedURLException;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.pg.merchant.CheckSumServiceHelper;
import com.whitehall.esp.microservices.model.Device;
import com.whitehall.esp.microservices.model.Plan;
import com.whitehall.esp.microservices.model.Transaction;
import com.whitehall.esp.microservices.model.User;
import com.whitehall.esp.microservices.model.paytm.LoginOTPRequestHead;
import com.whitehall.esp.microservices.model.paytm.OrderStatus_paytm;
import com.whitehall.esp.microservices.model.paytm.PaymentOptionRequestHead;
import com.whitehall.esp.microservices.model.paytm.PaytmRequestBody;
import com.whitehall.esp.microservices.model.paytm.PaytmRequestHeader;
import com.whitehall.esp.microservices.model.paytm.TransactionPaymentOption;
import com.whitehall.esp.microservices.model.paytm.FetchPaymentOptionRespond.BalanceInfo;
import com.whitehall.esp.microservices.model.paytm.FetchPaymentOptionRespond.FetchPaymentOptionRespond;
import com.whitehall.esp.microservices.model.paytm.InitiateTransactionRespond.InitiateTransactionRespond;
import com.whitehall.esp.microservices.model.paytm.LoginOTPRespond.LoginOTPRespond;
import com.whitehall.esp.microservices.model.paytm.ValidateLoginOTPRespond.ValidateLoginOTPRespond;
import com.whitehall.esp.microservices.model.paytm.payment_status_respond.PaymentStatusRespond;
import com.whitehall.esp.microservices.model.paytm.wallet_balance_respond.WalletBalanceRespond;
import com.whitehall.esp.microservices.services.PlanService;
import com.whitehall.esp.microservices.services.TransactionService;
import com.whitehall.esp.microservices.services.UserService;
import com.whitehall.esp.microservices.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api/v1/payment/paytm")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class PaytmPaymentController extends GenericController{
	
	@Autowired
	private JsonUtils mapper;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PlanService planService;

	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private RestTemplate restcall;
		
	@Value("${paytm.payment.sandbox.merchantId}")
	private String MERCHANT_MID;
	
	@Value("${paytm.payment.sandbox.merchantKey}")
    private String MERCHANT_KEY;
	
    final String MERCHANT_WEBSITE = "dineoutWEB";
        
    private String websiteName = "www.whitehalltechnologies.com";
    
    private String version = "V1";
    
    private String channelId = "SYSTEM";
    
    
    @GetMapping("/test")
	public String getAllOwnedGroupsOfAUser() {
    	log.info("inside funcion getAllOwnedGroupsOfAUser{}");
		
		return "test successfull";
	}
    
    @PostMapping("initiate/{planId}")
    public TransactionPaymentOption devicePayment(@RequestBody Device devices[],@PathVariable String planId){
    	log.info("inside devicePayment");
    	Plan plan =  planService.findPlanByID(planId).block();
    	String txnAmount = new Integer(devices.length * plan.getAmount()).toString();
    
    	return initiateTransaction(txnAmount);
    }
    
    TransactionPaymentOption initiateTransaction(@PathVariable String amount) {
	    	
    		String userEmail =    getEmailFromToken();
			 User user = userService.findByEmail(userEmail).block();
			 
			log.info("user={}",user);
    	   	
    	 	PaytmRequestBody paytmRequestBody = new PaytmRequestBody(MERCHANT_MID,websiteName,amount,user);
    	 	try {
    	    String paytmParams_body = objectMapper.writeValueAsString(paytmRequestBody);//"{"requestType":"Payment","mid":"" + MERCHANT_MID + "","websiteName":"" + MERCHANT_WEBSITE + "","orderId":"" + orderId + "","txnAmount":{"value":"100.00","currency":"INR"},"userInfo":{"custId":"CUST001"},"callbackUrl":"https://pg-stage.paytm.in/MerchantSite/bankResponse"}";

    	    PaytmRequestHeader paytmRequestHeader = new PaytmRequestHeader(user.getUserId(),version,channelId,MERCHANT_KEY,paytmParams_body);
    	    
    	    String paytmParams_head = objectMapper.writeValueAsString(paytmRequestHeader);//"<something that convert paytmRequestHeader to JSON>";
    	    
    	    String transactionURL = "https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid=" + MERCHANT_MID + "&orderId=" + paytmRequestBody.getOrderId();// for staging
    	    // URL transactionURL = new URL("https://securegw.paytm.in/theia/api/v1/initiateTransaction?mid=" + MERCHANT_MID + "&orderId=" + orderId); // for production
    	    
    	    String post_data = "{\"body\":" + paytmParams_body + " ,\"head\":" + paytmParams_head + "}";
    	    log.info("post_data={}",mapper.print(post_data));
    	    HttpHeaders headers = new HttpHeaders();
    	    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

    	    HttpEntity<String> entity = new HttpEntity<>(post_data, headers);

    	    
    	    InitiateTransactionRespond initiateTransactionRespond = restcall.exchange(transactionURL, HttpMethod.POST, entity, InitiateTransactionRespond.class).getBody();
    	    		
    	        log.info("initiateTransactionRespond info is :{} ",mapper.print(initiateTransactionRespond));
    	        if(initiateTransactionRespond.getBody().getResultInfo().getResultStatus().equals("S")) {
    	         	Transaction transaction = new Transaction(initiateTransactionRespond.getBody().getTxnToken(),user.getEmail(),"PENDING",amount.toString(),paytmRequestHeader.getRequestTimestamp(),paytmRequestBody.getOrderId());
    	        	transactionService.addTransaction(transaction).block();
    	        
    	        	return fetchPaymentOption(initiateTransactionRespond.getBody().getTxnToken(),paytmRequestBody.getOrderId());
    	        }
    	        
    	       // return responseData;
    	    } catch (Exception exception) {
    	        exception.printStackTrace();
    	    }
    	 	return null;
    }
    
    TransactionPaymentOption fetchPaymentOption(String txnToken,String orderId) throws JsonProcessingException, MalformedURLException {
    	
    	PaymentOptionRequestHead paymentOptionRequestHead = new PaymentOptionRequestHead(version,channelId,txnToken);
    	String paytmParams_head = objectMapper.writeValueAsString(paymentOptionRequestHead);
    	
    	String transactionURL = "https://securegw-stage.paytm.in/theia/api/v1/fetchPaymentOptions?mid=" + MERCHANT_MID + "&orderId=" + orderId;// for staging
    	// URL transactionURL = new URL("https://securegw.paytm.in/theia/api/v1/fetchPaymentOptions?mid=" + MERCHANT_MID + "&orderId=" + orderId); // for production
   
    	String post_data = "{\"head\":" + paytmParams_head + "}";
    	
    	try {
    		HttpHeaders headers = new HttpHeaders();
    	    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

    	    HttpEntity<String> entity = new HttpEntity<>(post_data, headers);

    	    
    	    FetchPaymentOptionRespond fetchPaymentOptionRespond = restcall.exchange(transactionURL, HttpMethod.POST, entity, FetchPaymentOptionRespond.class).getBody();
    	    	

	      //  FetchPaymentOptionRespond fetchPaymentOptionRespond = objectMapper.readValue(responseData.toString(), FetchPaymentOptionRespond.class);
	       // log.info("fetchPaymentOptionRespond= {}",fetchPaymentOptionRespond);
    	    return new TransactionPaymentOption(txnToken,fetchPaymentOptionRespond.getBody().getMerchantPayOption());
    	} catch (Exception exception) {
    	    exception.printStackTrace();
    	    
    	}
    	return null;
    }
    
    @PostMapping("/login/otp/{txnToken}")
    String sendLoginOTP(@PathVariable String txnToken, @RequestHeader(name=HttpHeaders.AUTHORIZATION) String token,@RequestBody String phone) {
    	log.info("inside sendLoginOTP, txnToken={}, token={} phone={}",txnToken,token,phone);
    	Transaction transaction = transactionService.findTransactionById(txnToken).block();
    	log.info("transaction={}",transaction);
    	String userEmail =    getEmailFromToken();
		 User user = userService.findByEmail(userEmail).block();
		 
    	try {
        	String paytmParams_body = "{\"mobileNumber\":\""+phone+"\"}";
        	LoginOTPRequestHead head = new LoginOTPRequestHead(user.getUserId(),version,new Long(System.currentTimeMillis()).toString(),channelId,txnToken);
        	String paytmParams_head = objectMapper.writeValueAsString(head);
    	String transactionURL = "https://securegw-stage.paytm.in/theia/api/v1/login/sendOtp?mid=" + MERCHANT_MID + "&orderId=" + transaction.getOrderId();// for staging
    	// URL transactionURL = new URL("https://securegw.paytm.in/theia/api/v1/login/sendOtp?mid=" + MERCHANT_MID + "&orderId=" + orderId); // for production
    	String post_data = "{\"body\":" + paytmParams_body + ",\"head\":" + paytmParams_head + "}";
    	
    	log.info("post_data={}",post_data);
    	HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

	    HttpEntity<String> entity = new HttpEntity<>(post_data, headers);

	    
	    LoginOTPRespond loginOTPRespond = restcall.exchange(transactionURL, HttpMethod.POST, entity, LoginOTPRespond.class).getBody();
	    
    	    return loginOTPRespond.getBody().getResultInfo().getResultMsg();
    	} catch (Exception exception) {
    	    exception.printStackTrace();
    	}
    	return null;
    }
    
    @RequestMapping("/login/otp/{txnToken}")
    Boolean validateLoginOTP(@PathVariable String txnToken,@RequestBody String otp, @RequestHeader(name=HttpHeaders.AUTHORIZATION) String token) {
    	
    	log.info("inside validateLoginOTP, txnToken={}, token={}",txnToken,token);
    	Transaction transaction = transactionService.findTransactionById(txnToken).block();
    	
    	String userEmail =    getEmailFromToken();
		 User user = userService.findByEmail(userEmail).block();
		 

    	String paytmParams_body = "{\"otp\":\""+otp+"\"}";
    	LoginOTPRequestHead head = new LoginOTPRequestHead(user.getUserId(),version,new Long(System.currentTimeMillis()).toString(),channelId,txnToken);
    	try {
    		
    	String paytmParams_head = objectMapper.writeValueAsString(head);
    	String transactionURL = "https://securegw-stage.paytm.in/theia/api/v1/login/validateOtp?mid=" + MERCHANT_MID + "&orderId=" + transaction.getOrderId();// for staging      
    	// URL transactionURL = new URL("https://securegw.paytm.in/theia/api/v1/login/validateOtp?mid=" + MERCHANT_MID + "&orderId=" + orderId); // for production
    	String post_data = "{\"body\":" + paytmParams_body + ",\"head\":" + paytmParams_head + "}";
    	log.info("post_data={}",post_data);

    	HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

	    HttpEntity<String> entity = new HttpEntity<>(post_data, headers);

	    
	    ValidateLoginOTPRespond validateLoginOTPRespond = restcall.exchange(transactionURL, HttpMethod.POST, entity, ValidateLoginOTPRespond.class).getBody();
	    
    	    log.info("validateLoginOTPRespond={}",validateLoginOTPRespond);
    	    return validateLoginOTPRespond.getBody().getAuthenticated();
    	} catch (Exception exception) {
    	    exception.printStackTrace();
    	}
    	return null;
    }
    
    @GetMapping("/walletbalance/{txnToken}")
    BalanceInfo fetchWalletBalance(@PathVariable String txnToken, @RequestHeader(name=HttpHeaders.AUTHORIZATION) String token) {
    	
    	Transaction transaction = transactionService.findTransactionById(txnToken).block();
    	String userEmail =    getEmailFromToken();
		User user = userService.findByEmail(userEmail).block();
		 
    	String paytmParams_body = "{\"paymentMode\":\"BALANCE\"}";
    	LoginOTPRequestHead head = new LoginOTPRequestHead(user.getUserId(),version,new Long(System.currentTimeMillis()).toString(),channelId,txnToken);
    	try {
    		
    	String paytmParams_head = objectMapper.writeValueAsString(head);
    	String transactionURL = "https://securegw-stage.paytm.in/userAsset/fetchBalanceInfo?mid="+MERCHANT_MID+"&orderId="+transaction.getOrderId();// for staging      
    	//String transactionURL = "https://securegw.paytm.in/userAsset/fetchBalanceInfo?mid="+MERCHANT_MID+"&orderId="+transaction.getOrderId(); // for production
    	String post_data = "{\"body\":" + paytmParams_body + ",\"head\":" + paytmParams_head + "}";
		 log.info("post_data={}",post_data);
		 log.info("transactionURL={}",transactionURL);
		 HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		    HttpEntity<String> entity = new HttpEntity<>(post_data, headers);
		    
		    //ResponseEntity<WalletBalanceRespond> walletBalanceRespondRes = restcall.exchange(transactionURL, HttpMethod.POST, entity,  WalletBalanceRespond.class);
		    WalletBalanceRespond walletBalanceRespond =  restcall.exchange(transactionURL, HttpMethod.POST, entity,  WalletBalanceRespond.class).getBody();
		    
		    log.info("json={}",restcall.exchange(transactionURL, HttpMethod.POST, entity,  WalletBalanceRespond.class).getBody());
		   // WalletBalanceRespond walletBalanceRespond = walletBalanceRespondRes.getBody();
		 //JsonNode node = restTemplate.getForObject(url, JsonNode.class);
		 log.info("WalletBalanceRespond={}",walletBalanceRespond);

    	 return walletBalanceRespond.getBody().getBalanceInfo();
    	} catch (Exception exception) {
    	    exception.printStackTrace();
    	}
    	return null;
    }
//    
//    @GetMapping("/paymentprocessing/paytmbalance/{txnToken}")
//    BalanceInfo paymentProcessing(@PathVariable String txnToken,@RequestHeader(name=HttpHeaders.AUTHORIZATION) String token) {
//    	
//    	log.info("inside fetchWalletBalance, txnToken={}, token={}",txnToken,token);
//    	Transaction transaction = transactionService.findTransactionById(txnToken).block();
//    	//log.info("transaction={}",transaction);
//    	String url=userServiceUri+"/api/v1/user/byemail/"+transaction.getUserEmail();
//		//log.info(url);
//		 User user = webClientBuilder.build().get().uri(url)
//				 .header(HttpHeaders.AUTHORIZATION, token)
//				 .header(HttpHeaders.CONTENT_TYPE, "application/json")
//				 	.retrieve()
//		    		.bodyToMono(User.class).block();
//		 //log.info("user={}",user);
//		 String paytmParams_body = "{\"requestType\":\"NATIVE\",\"mid\":\"" + MERCHANT_MID + "\",\"orderId\":\"" + transaction.getOrderId() + "\",\"paymentMode\":\"BALANCE\"}";
//
//		
//    	LoginOTPRequestHead head = new LoginOTPRequestHead(user.getUserId(),version,new Long(System.currentTimeMillis()).toString(),channelId,txnToken);
//
//    	try {
//       	 String paytmParams_head = objectMapper.writeValueAsString(head);
//    	//String paytmParams_head = objectMapper.writeValueAsString(head);
//    	String transactionURL = "https://securegw-stage.paytm.in/theia/api/v1/processTransaction?mid="+MERCHANT_MID+"&orderId="+transaction.getOrderId();// for staging      
//    	//String transactionURL = "https://securegw.paytm.in/userAsset/fetchBalanceInfo?mid="+MERCHANT_MID+"&orderId="+transaction.getOrderId(); // for production
//    	String post_data = "{\"body\":" + paytmParams_body + ",\"head\":" + paytmParams_head + "}";
//		 log.info("post_data={}",post_data);
//		 log.info("transactionURL={}",transactionURL);
//		 HttpHeaders headers = new HttpHeaders();
//		    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//
//		    HttpEntity<String> entity = new HttpEntity<>(post_data, headers);
//
//		    //ResponseEntity<WalletBalanceRespond> walletBalanceRespondRes = restcall.exchange(transactionURL, HttpMethod.POST, entity,  WalletBalanceRespond.class);
//		//    WalletBalanceRespond walletBalanceRespond =  restcall.exchange(transactionURL, HttpMethod.POST, entity,  WalletBalanceRespond.class).getBody();
//		    
//		    log.info("json={}",restcall.exchange(transactionURL, HttpMethod.POST, entity,  WalletBalanceRespond.class).getBody());
//		   // WalletBalanceRespond walletBalanceRespond = walletBalanceRespondRes.getBody();
//		 //JsonNode node = restTemplate.getForObject(url, JsonNode.class);
////		 log.info("WalletBalanceRespond={}",walletBalanceRespond);
////
////    	 return walletBalanceRespond.getBody().getBalanceInfo();
//    	} catch (Exception exception) {
//    	    exception.printStackTrace();
//    	}
//    	return null;
//    }

    @GetMapping("/paymentstatus/{txnToken}")
    PaymentStatusRespond paymentStatus(@PathVariable String txnToken,@RequestHeader(name=HttpHeaders.AUTHORIZATION) String token)  {
    	
     	
    	Transaction transaction = transactionService.findTransactionById(txnToken).block();
    	/* initialize a TreeMap object */
    	TreeMap<String, String> paytmParams = new TreeMap<String, String>();
    	
    	/* Find your MID in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys */
    	paytmParams.put("MID", MERCHANT_MID);

    	/* Enter your order id which needs to be check status for */
    	paytmParams.put("ORDERID", transaction.getOrderId());

    	/**
    	* Generate checksum by parameters we have in body
    	* You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
    	* Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys 
    	*/
    	try {
    		
    	String checksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(MERCHANT_KEY, paytmParams);

    	/* put generated checksum value here */
    	paytmParams.put("CHECKSUMHASH", checksum);

    	/* prepare JSON string for request */
    	JSONObject obj = new JSONObject(paytmParams);
    	String post_data = obj.toString();
    	log.info("post_data={}",mapper.print(post_data));
    	/* for Staging */
    	String transactionURL = "https://securegw-stage.paytm.in/order/status";

    	/* for Production */
    	// URL url = new URL("https://securegw.paytm.in/order/status");

    	 	HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		    OrderStatus_paytm requestBody= new OrderStatus_paytm();
		    requestBody.setMID(MERCHANT_MID);
		    requestBody.setORDERID(transaction.getOrderId());
		    requestBody.setCHECKSUMHASH(checksum);
		    HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

		    //ResponseEntity<PaymentStatusRespond> paymentStatusRespondRes = restcall.exchange(transactionURL, HttpMethod.POST, entity,  PaymentStatusRespond.class);
		    PaymentStatusRespond paymentStatusRespondRes = restcall.postForObject(transactionURL, requestBody,  PaymentStatusRespond.class);
		    log.info("requesBody={}",mapper.print(requestBody));
		    log.info("json={}",mapper.print(paymentStatusRespondRes));
		    return paymentStatusRespondRes;
    	} catch (Exception exception) {
    		exception.printStackTrace();
    	}
    	return null;
    }
}
