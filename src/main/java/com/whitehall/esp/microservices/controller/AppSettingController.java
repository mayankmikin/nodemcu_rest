package com.whitehall.esp.microservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitehall.esp.microservices.exceptions.PaymentMethodNotFoundException;
import com.whitehall.esp.microservices.model.AppSetting;
import com.whitehall.esp.microservices.model.PaymentMethod;
import com.whitehall.esp.microservices.model.User;
import com.whitehall.esp.microservices.services.PaymentMethodService;
import com.whitehall.esp.microservices.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/appsetting")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AppSettingController extends GenericController {
	

	public static ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private UserService userService;
	
	@Autowired
	private PaymentMethodService paymentMethodService;
	
	@GetMapping("/paymentmethod/enabled")
	public Flux<PaymentMethod> getAllEnabledPaymentMethods(){
		return paymentMethodService.getAllEnabledPaymentMethods();
	}
	
	@PostMapping("/paymentmethod/add")
	public Mono<PaymentMethod> addPaymentMethod(@RequestBody PaymentMethod paymentMethod){
			return paymentMethodService.addPaymentMethod(paymentMethod);
	}
	
	@DeleteMapping("/paymentmethod/delete/{paymentmethodId}")
	public Flux<PaymentMethod> deletePaymentMethod(@PathVariable String paymentmethodId){
		PaymentMethod paymentmethod = paymentMethodService.findbyPaymentMethodId(paymentmethodId).block();
		if(paymentmethod==null) {
			throw new PaymentMethodNotFoundException("Payment Method with id:"+paymentmethodId+" doesn't exist");
		}
		return paymentMethodService.deletePaymentMethod(paymentmethod);
	}
	
	@PatchMapping("/paymentmethod/enable/{paymentmethodId}")
	public Mono<PaymentMethod> enablePaymentMethod(@PathVariable String paymentmethodId){
		PaymentMethod paymentmethod = paymentMethodService.findbyPaymentMethodId(paymentmethodId).block();
		paymentmethod.setEnablePaymentMethod(true);
		return paymentMethodService.updatePaymentMethod(paymentmethod);
	}
	
	@PatchMapping("/paymentmethod/false/{paymentmethodId}")
	public Mono<PaymentMethod> disablePaymentMethod(@PathVariable String paymentmethodId){
		PaymentMethod paymentmethod = paymentMethodService.findbyPaymentMethodId(paymentmethodId).block();
		paymentmethod.setEnablePaymentMethod(false);
		return paymentMethodService.updatePaymentMethod(paymentmethod);
	}
	
	@PatchMapping("/update")
	public Mono<User> updateAppSettings(@RequestBody AppSetting appSetting){
		User user = userService.findByEmail(getEmailFromToken()).block();
		user.setAppSetting(appSetting);
		return userService.updateUser(user);
	} 
	
}
