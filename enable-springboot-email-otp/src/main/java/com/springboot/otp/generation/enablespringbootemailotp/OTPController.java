package com.springboot.otp.generation.enablespringbootemailotp;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OTPController {

	@Autowired
	public OTPService otpService;

	@Autowired
	public EmailService emailService;
	
	@Autowired
	public UserRepository userRepository;
	
//	@Autowired
//	public EmailRepository emailRepository;

//	@GetMapping("/generateOtp/{emailId}")
//	public String generateOTP(@PathVariable("emailId")String emailId) throws MessagingException {
//
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		
//		String username = auth.getName();
//		UserPojo userPojo = userRepository.findByUsername(username);
//		
//		int otp = otpService.generateOTP(username,emailId);
//		System.out.println("OTP : "+otp);
//		// Generate The Template to send OTP
//		EmailTemplate template = new EmailTemplate("SendOtp.html");
//		Map<String, String> replacements = new HashMap<String, String>();
//		replacements.put("user", username);
//		replacements.put("otpnum", String.valueOf(otp));
//		String message = template.getTemplate(replacements);
//		System.out.println("Message : "+message);
//		emailService.sendOtpMessage(emailId, "OTP -SpringBoot", message);
//
//		return "otppage";
//	}
	
	@GetMapping("/generateOtp")
	public String generateOTP() throws MessagingException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String username = auth.getName();
		
		int otp1 = otpService.generateOTP("money.bondada@gmail.com");
		int otp2 = otpService.generateOTP("manikanta149555@gmail.com");
		int otp3 = otpService.generateOTP("manikanta149222@gmail.com");
		
		System.out.println("OTP1 for money.bondada@gmail.com: "+otp1);
		System.out.println("OTP2 for manikanta149555@gmail.com: "+otp2);
		System.out.println("OTP3 for manikanta149222@gmail.com: "+otp3);
		// Generate The Template to send OTP
		EmailTemplate template = new EmailTemplate("SendOtp.html");
		
		Map<String, String> replacements1 = new HashMap<String, String>();
		replacements1.put("user", username);
		replacements1.put("otpnum", String.valueOf(otp1));
		String message1 = template.getTemplate(replacements1);
		System.out.println("Message1 : "+message1);
		
		Map<String, String> replacements2 = new HashMap<String, String>();
		replacements2.put("user", username);
		replacements2.put("otpnum", String.valueOf(otp2));
		String message2 = template.getTemplate(replacements2);
		System.out.println("Message2 : "+message2);
		
		Map<String, String> replacements3 = new HashMap<String, String>();
		replacements3.put("user", username);
		replacements3.put("otpnum", String.valueOf(otp3));
		String message3 = template.getTemplate(replacements3);
		System.out.println("Message3 : "+message3);
		
		emailService.sendOtpMessage("money.bondada@gmail.com", "OTP -SpringBoot", message1);
		emailService.sendOtpMessage("manikanta149555@gmail.com", "OTP -SpringBoot", message2);
		emailService.sendOtpMessage("manikanta149222@gmail.com", "OTP -SpringBoot", message3);

		return "otppage";
	}

	@RequestMapping(value = "/validateOtp/{otpnum}", method = RequestMethod.GET)
	public @ResponseBody String validateOtp(@RequestParam("emailId") String emailId,@PathVariable("otpnum") int otpnum) {
		

		final String SUCCESS = "Entered Otp is valid";
		final String FAIL = "Entered Otp is NOT valid. Please Retry!";
		// Validate the Otp
		if (otpnum >= 0) {

			int serverOtp = otpService.getOtp(emailId);
			if (serverOtp > 0) {
				if (otpnum == serverOtp) {
					otpService.clearOTP(emailId);

					return (SUCCESS);
				} else {
					return FAIL;
				}
			} else {
				return FAIL;
			}
		} else {
			return FAIL;
		}
	}
}
