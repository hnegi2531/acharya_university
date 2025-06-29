package com.au.response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.payme.OrderDetailsForOrderIdGeneration;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ResponseHandler {
	
	
//	@Autowired
//	private JavaMailSender mailSender;
	
	@Autowired
	private Environment env;

    @Autowired
    private JavaMailSender mailSenderPrimary;  // First email sender (noreply@acharya.ac.in)

    @Autowired
    private JavaMailSender mailSenderSecondary;
    
    @Autowired
    private JavaMailSender mailSenderThird;
    
    
    @Autowired
    @Qualifier("mailSenderPrimary")  // ✅ Specify which bean to use
    private JavaMailSender mailSender;


	private static JwtTokenService jwtService=new JwtTokenService();
	
	public static String message1 = "Request Limit Exceeded! Please wait...";
	

	public static ResponseEntity<Object> generateResponse(Boolean message, HttpStatus status, Object responseObj) {

		Map<String, Object> map = new LinkedHashMap<>();
		map.put("success", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);

	}
	
	public static ResponseEntity<Object> generateResponse1(Boolean message,HttpStatus status, String message1) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("success", message);
		map.put("status", status.value());
		map.put("data", message1);

		return new ResponseEntity<Object>(map, status);
	}

	public static ResponseEntity<Object> generateResponseForIndex(Boolean message, HttpStatus status,
			Page<Object> responseObj) {

		Map<String, Object> map = new LinkedHashMap<>();
		Map<String, Page<Object>> map1 = new LinkedHashMap<>();
		map.put("success", message);
		map.put("status", status.value());
		map1.put("Paginated_data", responseObj);
		map.put("data", map1);

		return new ResponseEntity<Object>(map, status);

	}
	
	public static LinkedHashMap<String, Object> generateResponseOFRedisForIndex(Boolean message, HttpStatus status,
			Page<Object> responseObj) {

		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		LinkedHashMap<String, Page<Object>> map1 = new LinkedHashMap<>();
		map.put("success", message);
		map.put("status", status.value());
		map1.put("Paginated_data", responseObj);
		map.put("data", map1);

		return map;

	}

	public static ResponseEntity<Object> generateResponseForPutApiAndDeleteApi(Boolean message, HttpStatus status) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("success", message);
		map.put("status", status.value());
		return new ResponseEntity<Object>(map, status);
	}

	public static ResponseEntity<Object> generateResponseForPutApiAndDeleteApiWithFalse(Boolean message,
			HttpStatus status) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("success", message);
		map.put("status", status.value());
		return new ResponseEntity<Object>(map, status);
	}
	
	public static String nameCapital(String s) {
		String final_name = "";
		String name =s.trim();
		if(name.contains(" ") == true) {
		   String[] name1 =name.split(" ");
		   for(int i= 0 ; i<name1.length;i++) {
				   	final_name += name1[i].substring(0, 1).toUpperCase() + name1[i].substring(1) + " " ;
				   	System.out.println("########################### " +final_name);
			}
		}else {
			final_name += s.substring(0, 1).toUpperCase() + s.substring(1) ;
		}
		
		return final_name;
	}
	
	public static String removeSpaceBetweenWords(String s) {
		String final_name = s.replace(" ", "");
		return final_name;
	}
	
	public static List<Integer> toConvertCommaSeperatedIdsAsList(String ids){
		List<Integer> convertedStingInLIst = Stream.of(ids.split(","))
				  .map(String::trim)
				  .map(Integer::parseInt)
				  .collect(Collectors.toList());
		return convertedStingInLIst;
	}
	
	public void sendSimpleEmail(String toEmail, String body, String subject) {

		MimeMessagePreparator mailMessage = mimeMessage -> {

		    MimeMessageHelper message = new MimeMessageHelper(
		            mimeMessage, true);
		    try {
		message.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"),"ERP-Info");
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		// message.setCc("vikashkumar@acharya.ac.in");
		    } catch (Exception e) {
		        throw new Exception(toEmail, e);
		    }
		};
		mailSender.send(mailMessage);
		System.out.println("Mail Send...");
	}
	
	// ✅ Overloaded method for backward compatibility (Default sender)
    public void sendSimpleEmailWithHtmlContent(String toEmail, String body, String subject) {
        sendSimpleEmailWithHtmlContent(toEmail, body, subject, false); // Default sender
    }
//
//    public void sendSimpleEmailWithHtmlContent(String toEmail, String body, String subject, boolean useSecondEmail) {
//
//        // ✅ Select correct mail sender
//        JavaMailSender selectedMailSender = useSecondEmail ? mailSenderSecondary : mailSenderPrimary;
//        String fromEmail = useSecondEmail ? "acerp@acharya.ac.in" : "noreply@acharya.ac.in";
//
//        MimeMessagePreparator mailMessage = mimeMessage -> {
//            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
//            try {
//                message.setFrom(fromEmail, "ERP-Info");  // ✅ Make sure this email matches the sender
//                message.setTo(toEmail);
//                message.setSubject(subject);
//                message.setText(body, true);
//            } catch (Exception e) {
//                throw new RuntimeException("Error sending email to " + toEmail, e);
//            }
//        };
//
//        // ✅ Send the email
//        selectedMailSender.send(mailMessage);
//        System.out.println("Mail sent from: " + fromEmail);
//    }

    public void sendSimpleEmailWithHtmlContent(String toEmail, String body, String subject, boolean useSecondEmail) {
        JavaMailSender selectedMailSender = useSecondEmail ? mailSenderSecondary : mailSenderPrimary;
        String fromEmail = useSecondEmail ? "acerp@acharya.ac.in" : "noreply@acharya.ac.in";
//        String password = useSecondEmail ? "teamerp@1" : "P@ssword12345";  // Use App Password here if 2FA is enabled

        MimeMessagePreparator mailMessage = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            try {
                message.setFrom(fromEmail, "ERP-Info");
                message.setTo(toEmail);
                message.setSubject(subject);
                message.setText(body, true);
            } catch (Exception e) {
                throw new RuntimeException("Error sending email to " + toEmail, e);
            }
        };

        try {
            selectedMailSender.send(mailMessage);
            System.out.println("Mail sent from: " + fromEmail);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }
	
	public void sendSimpleEmailWithoutAdditionalName(String toEmail, String body, String subject) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"));
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		// message.setCc("vikashkumar@acharya.ac.in");
		mailSender.send(message);
		System.out.println("Mail Send...");

	}
	
	public void sendMultipleSimpleEmail(String[] toEmail, String content, String subject) {

		MimeMessagePreparator mailMessage = mimeMessage -> {

		    MimeMessageHelper message = new MimeMessageHelper(
		            mimeMessage, true);
		    try {
		message.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"),"ERP-Info");
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(content, true);
		
		// message.setCc("vikashkumar@acharya.ac.in");
		    } catch (MessagingException e) {
		    	e.printStackTrace();
		    }
		};
		mailSender.send(mailMessage);
		System.out.println("Mail Send...");
	}
	
	public void sendMultipleSimpleEmailWithCc(String[] toEmail, String content, String subject, String cc_email) {

		MimeMessagePreparator mailMessage = mimeMessage -> {

		    MimeMessageHelper message = new MimeMessageHelper(
		            mimeMessage, true);
		    try {
		message.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"),"ERP-Info");
		message.setTo(toEmail);
		message.setCc(cc_email);
		message.setSubject(subject);
		message.setText(content, true);
		
		// message.setCc("vikashkumar@acharya.ac.in");
		    } catch (MessagingException e) {
		    	e.printStackTrace();
		    }
		};
		mailSender.send(mailMessage);
		System.out.println("Mail Send...");
	}
	
	public void sendMailsWithAttachment(String[] emails, String content, String subject, S3ObjectInputStream stream,
			String attachment_name) throws IOException {

		MimeMessage message = mailSender.createMimeMessage();
		File file = new File("Resume");
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
			helper.setTo(emails);
			helper.setSubject(subject);
			helper.setText(content,true);
			FileCopyUtils.copy(IOUtils.toByteArray(stream), file);
			helper.addAttachment(attachment_name, file);
			
		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		mailSender.send(message);
		System.out.println("Mail Send...");
		file.delete();
	}
	
	public void sendMailWithTableFormat(String email, String contents, String subject) throws IOException {

		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
			helper.setTo(email);
			helper.setSubject(subject);
			helper.setText(contents, true);
		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		mailSender.send(message);

	}
	
	
	
	public void sendMailWithTableFormatNew(String email, String contents, String subject, boolean useSecondEmail) throws IOException {

        // ✅ Select the correct mail sender
        JavaMailSender selectedMailSender = useSecondEmail ? mailSenderSecondary : mailSenderPrimary;
        String fromEmail = useSecondEmail
                ? env.getProperty("second.mail.properties.mail.smtp.from")  // Use acerp@acharya.ac.in
                : env.getProperty("spring.mail.properties.mail.smtp.from"); // Use noreply@acharya.ac.in

        MimeMessage message = selectedMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail, "ERP-Info");  // ✅ Ensure the correct sender is used
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(contents, true);
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }

        // ✅ Send the email using the selected sender
        selectedMailSender.send(message);
        System.out.println("Mail sent from: " + fromEmail);
    }
	
	public void sendMailWithTableFormatWithCC(String email, String approverEmail, String contents, String subject) throws IOException {

		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
			helper.setTo(email);
			helper.setCc(approverEmail);
			helper.setSubject(subject);
			helper.setText(contents, true);
		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		mailSender.send(message);

	}
	
	public void sendEmailWithAttachment(MultipartFile file, String counselorEmail, String toEmail, String mailBody, String subject) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
        helper.setTo(toEmail);
        helper.setCc(counselorEmail);
        helper.setSubject(subject);
        helper.setText(mailBody, true);

        // Attach the file
        helper.addAttachment(file.getOriginalFilename(), new ByteArrayResource(file.getBytes()));

        mailSender.send(message);
	}
	
	public void sendEmailWithMultipleAttachment(List<MultipartFile> files, String[] counselorEmail, String toEmail, String mailBody, String subject) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
        helper.setTo(toEmail);
        helper.setCc(counselorEmail);
        helper.setSubject(subject);
        helper.setText(mailBody, true);

        // Attach the file
        files.stream().forEach(file -> {
        	 try {
				helper.addAttachment(file.getOriginalFilename(), new ByteArrayResource(file.getBytes()));
			} catch (MessagingException | IOException e) {
				e.printStackTrace();
			}
        });

        mailSender.send(message);
	}
	
	public void delete(long nday, String extension) {
		 
        File fold = new File("C:\\Offer and Salary Breakup");
 
        if (fold.exists()) {
 
            File[] listAllFiles = fold.listFiles();
            
            long Deletion = System.currentTimeMillis() -
                (nday * 24 * 60 * 60 * 1000L);
 
            for (File listFile: listAllFiles) {
 
                if (listFile.getName().endsWith(extension) &&
                    listFile.lastModified() < Deletion) {
 
                    if (!listFile.delete()) {
 
                        System.out.println("Sorry can't delete");
 
                    }
                }
            }
        }
    }
	
	public void sendMailWithTableFormatWithCC(String email,String[] cc_emails, String contents, String subject) throws IOException {

		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
			helper.setTo(email);
			helper.setCc(cc_emails);
			helper.setSubject(subject);
			helper.setText(contents, true);
		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		mailSender.send(message);

	}
	
	public void sendMailWithTableFormat(String[] email, String contents, String subject) throws IOException {

		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
			helper.setTo(email);
			helper.setSubject(subject);
			helper.setText(contents, true);
		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		mailSender.send(message);

	}
	
//	public void sendMailsWithGeneratedPdf(String[] emails, String content, String subject, Object attachment_name) throws IOException {
//
//		MimeMessage message = mailSender.createMimeMessage();
//		try {
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//			helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
//			helper.setTo(emails);
//			helper.setSubject(subject);
//			helper.setText(content,true);
//			//File file = new File("Resume");
//			//FileCopyUtils.copy(IOUtils.toByteArray(stream), file);
//			helper.addAttachment(attachment_name, file);
//		} catch (MessagingException e) {
//			throw new MailParseException(e);
//		}
//		mailSender.send(message);
//		System.out.println("Mail Send...");
//
//	}
	
	public static String orderIdGenerationForPayment(OrderDetailsForOrderIdGeneration details) {
		
		StringBuffer sb=new StringBuffer("");
		
		details.getOrder_details().entrySet().forEach(order -> {
			sb.append(order.getValue().toString());
		});
		
		String encodedString = (String) Base64.getEncoder().encodeToString((sb.toString()).getBytes());
		
		SplittableRandom splittableRandom = new SplittableRandom();
		int random_number= splittableRandom.nextInt(1, encodedString.length()- details.getLength_without_prefix());
		
		System.out.println("zzzzzzzzzzzz handaler " +encodedString.substring(random_number, random_number+details.getLength_without_prefix()));
		String generated_order_id=details.getPrefix_suffix_word()+"_"+encodedString.substring(random_number, random_number+details.getLength_without_prefix());
		
		return generated_order_id;
		
	}
	
	public static String base64Encoder(HashMap<String,Object> encode_details) {
		
		String encodedString = (String) Base64.getEncoder().encodeToString((encode_details.toString()).getBytes());
		
		return encodedString;
		
	}
	
	public  HashMap<String, Object>  checkAuthorizationToken(String token) {
		HashMap<String, Object> transaction_response = new HashMap<>();
		
		String encodedString ="Basic " +(String) Base64.getEncoder().encodeToString((env.getProperty("payme.merchantkey")+":"+env.getProperty("payme.testkey")).getBytes()); 
		System.out.println("Authorisation Key " + encodedString+" 1 " + env.getProperty("payme.merchantkey")+":"+ env.getProperty("payme.testkey"));
		if(encodedString.equals(token)) {
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", "Valid Authorization");
			transaction_response.put("error", result);
			return transaction_response;
		}else {
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("code", -32504);
			result.put("message", "The RPC request is missing required fields");
			transaction_response.put("error", result);
			return transaction_response;
		}
		
	}
	
	
//	public  HashMap<String,Object> getUpdatesDetailsFromTelegramBot(Integer update_id){
//		
//		HashMap<String,Object> telegram_response=new HashMap<>();
//		 RestTemplate restTemplate = new RestTemplate();
//		 String url="";
//		 if(update_id != 0) {
//			 url +=env.getProperty("telegram.bot.url.getUpdates")+"?offset="+update_id;
//		 }else {
//			 url +=env.getProperty("telegram.bot.url.getUpdates");
//		 }
//		 telegram_response = (HashMap<String, Object>) restTemplate.getForObject(url, HashMap.class);
//
//		    System.out.println("UUUUUUUUUUUUUUUURRRRRRRRRRRRRRRRLLLLLLL " +url);
//		    return telegram_response;
//		
//	}
	
	@SuppressWarnings("unchecked")
	public void sendMessageToUser(String id,String s){
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbooo ");
		
		HashMap<String,Object> response_after_messaging = (HashMap<String, Object>) restTemplate.getForObject(env.getProperty("telegram.bot.Url.sendMessage")+"?chat_id="+id+"&text="+s, HashMap.class);

		    System.out.println("SSSSSSSSSSSSSSEEEEEEEEEEEEEE " +env.getProperty("telegram.bot.Url.sendMessage")+"?chat_id="+id+"&text="+s);
		    
	}
	
	public void sendMailToMultipleWithCC(String[] email,String cc_emails, String contents, String subject) throws IOException {

		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
			helper.setTo(email);
			helper.setCc(cc_emails);
			helper.setSubject(subject);
			helper.setText(contents, true);
		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		mailSender.send(message);

	}
	
	public void sendMailToMultipleWithMultipleCC(String[] email,String[] cc_emails, String contents, String subject) throws IOException {

		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
			helper.setTo(email);
			helper.setCc(cc_emails);
			helper.setSubject(subject);
			helper.setText(contents, true);
		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		mailSender.send(message);

	}

	public static ResponseEntity<Object> generateResponse(Boolean success, HttpStatus status,String message, Object responseObj) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("success", success);
		map.put("status", status.value());
		map.put("message", message);
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}
	
	public static  ResponseEntity<Object> getUserDetailsByToken(String jwtToken) {
		try {
			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
			return new ResponseEntity<Object>(jwtDetails, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}
	
	public static String convertMapToJsonFormat(Map<String, Object> map) throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(map);
		return json;

	}


		public void sendMultipleEmail1(String[] recipients, String content, String subject, String attachmentPath) throws Exception {
		    MimeMessage message = mailSender.createMimeMessage();

		    try {
		        MimeMessageHelper helper = new MimeMessageHelper(message, true);
		        helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
		        helper.setTo(recipients);
		        helper.setSubject(subject);
		        helper.setText(content, true);

		        // Add the attachment
		        if (attachmentPath != null) {
		            File attachment = new File(attachmentPath);
		            if (attachment.exists()) {
		                FileSystemResource file = new FileSystemResource(attachment);
		                helper.addAttachment(attachment.getName(), file);
		            } else {
		                throw new FileNotFoundException("Attachment not found at " + attachmentPath);
		            }
		        }

		    } catch (MessagingException e) {
		        throw new MailParseException(e);
		    }

		    // Send the email
		    mailSender.send(message);
		}

		public static ResponseEntity<Object> generateResponseForIndex1(boolean message, HttpStatus status,
	            Page<Map<String, Object>> paginatedData) {

	    Map<String, Object> map = new LinkedHashMap<>();
	    // Change the type of map1 to match paginatedData
	    Map<String, Page<Map<String, Object>>> map1 = new LinkedHashMap<>();
	    
	    map.put("success", message);
	    map.put("status", status.value());
	    
	    // Use the correct type for paginatedData
	    map1.put("Paginated_data", paginatedData);
	    
	    map.put("data", map1);

	    return new ResponseEntity<Object>(map, status);
	}
	
		public static String getStringTypeTodaysDateDDMMYYYY() {
			
			LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	        String formattedDate = now.format(formatter);
	        System.out.println("Today's date in dd-MM-yyyy format: " + formattedDate);
	        
			return formattedDate;
			
		}

		 public static ResponseEntity<Object> newGenerateResponse(boolean success, HttpStatus status, Object responseBody) {
        // Create a map to hold the response data
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", success);
        response.put("status", status.value());

        // Only include the body as part of the data field
        Map<String, Object> data = new LinkedHashMap<>();
        
        response.put("data", data);
        data.put("body", responseBody);
        return new ResponseEntity<>(response, status);
    }

	public void sendMailToEmployeeWithCC(String email, String ccEmails, String contents, String subject,
			MultipartFile attachment) throws IOException {
		// Create MimeMessage to send email
		MimeMessage message = mailSenderThird.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			// Set the from address
			helper.setFrom("noreply1@acharya.ac.in", "ERP-Info");

			// Set the recipient(s) and CC
			helper.setTo(email);
			helper.setCc(ccEmails);

			// Set the subject and content of the email
			helper.setSubject(subject);
			helper.setText(contents, true); // true means content is HTML

			// Attach the file if it's not null
			if (attachment != null && !attachment.isEmpty()) {
				helper.addAttachment(attachment.getOriginalFilename(), attachment);
			}

		} catch (MessagingException e) {
			throw new MailParseException(e);
		}

		// Send the email using the third mail sender
		mailSenderThird.send(message);
	}

	public void sendMailToVendor(String email, List<String> ccEmails, String contents, String subject, MultipartFile attachment) throws IOException {
		// Create MimeMessage to send email
		MimeMessage message = mailSenderThird.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			// Set the from address
			helper.setFrom("noreply1@acharya.ac.in", "ERP-Info");

			// Set the recipient(s) and CC
			helper.setTo(email);
			if (ccEmails != null && !ccEmails.isEmpty()) {
				helper.setCc(ccEmails.toArray(new String[0]));
			}
			// Set the subject and content of the email
			helper.setSubject(subject);
			helper.setText(contents, true); // true means content is HTML

			// Attach the file if it's not null
			if (attachment != null && !attachment.isEmpty()) {
				helper.addAttachment(Objects.requireNonNull(attachment.getOriginalFilename()), attachment);
			}

		} catch (MessagingException e) {
			throw new MailParseException(e);
		}

		// Send the email using the third mail sender
		mailSenderThird.send(message);
	}

}	
