package com.whitehall.esp.microservices.model.paytm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PaymentMode {
//	public  List<String> mode= Arrays.asList("PAYTM_DIGITAL_CREDIT","UPI","PPBL","BALANCE");
//	public  List<String> channels=new ArrayList<String>();
private  String mode;
private  String channels[];

PaymentMode(String mode){
	this.mode = mode;
}

}
