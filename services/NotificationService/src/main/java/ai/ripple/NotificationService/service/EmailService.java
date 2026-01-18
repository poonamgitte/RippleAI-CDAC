package ai.ripple.NotificationService.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
	
	public void sendOtp(String toEmail, String otp, String purpose) {

        // Prepare the email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP for " + purpose);
        message.setText(buildOtpMessage(otp));
        message.setFrom("shakyashivam4510@gmail.com"); 

        // Send the email
        mailSender.send(message);
        System.out.println("OTP email sent to: " + toEmail);
    }

	private String buildOtpMessage(String otp) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Hello,\n\n")
	      .append("Your OTP is: ").append(otp).append("\n")
	      .append("It is valid for 5 minutes.\n\n")
	      .append("If you did not request this, please ignore this email.\n\n")
	      .append("Thanks!");
	    return sb.toString();
	}

    
}
