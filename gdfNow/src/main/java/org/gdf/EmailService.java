package org.gdf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.gdf.model.Business;
import org.gdf.model.Deeder;
import org.gdf.model.EmailMessage;
import org.gdf.model.EmailTemplateType;
import org.gdf.model.Government;
import org.gdf.model.Ngo;
import org.gdf.model.User;
import org.gdf.repository.EmailMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(EmailService.class);
	
	
	@Autowired
	EmailMessageRepository emailMessageRepository;
	
	@Autowired
	private JavaMailSender mailSender;
	
	private String protocol="http://";
	private String webURI="localhost:8080";
	private String accessConfirmURI="/accessconfirm?id=";
	private String sender="admin@gdfnow.org";
	
	public List<EmailMessage> getEmailMessageTemplates(){
		return emailMessageRepository.findAll();
	}
	
	public void sendUserRegConfirmEmail(User user) {
        List<EmailMessage> regMessages=emailMessageRepository.findByTemplate(EmailTemplateType.USER_REGISTER.name());
        StringBuilder sb=new StringBuilder(user.getEmail()+",\n");
            Map<String, String> map=new HashMap<String, String>();
            for (EmailMessage msg:regMessages){
                map.put(msg.getMessageTitle(), msg.getText());
            }
            //IN the email we need values in the following order
            //registrationUser, successfullyReg, setPassword, createAccess
            sb.append(map.get("registrationUser")).append("\n");
            sb.append(map.get("successfullyReg")).append("\n");
            sb.append(map.get("setPassword")).append("\n");
            sb.append(protocol).append(webURI).append(accessConfirmURI).append(user.getEmail());
            /*SimpleMailMessage message = new SimpleMailMessage();
        	message.setFrom(sender);
        	message.setTo(user.getEmail());
        	message.setSubject("This is a plain text email");
        	message.setText("Hello guys! This is a plain text email.");*/
        	
        	MimeMessage message = mailSender.createMimeMessage();
    		MimeMessageHelper helper = new MimeMessageHelper(message);
    		
    		try {
    			helper.setSubject(map.get("subject"));
				helper.setFrom(sender);
				helper.setTo(user.getEmail());
				boolean html = false;
	    		helper.setText(sb.toString(), html);
	    		mailSender.send(message);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
	
	
	public void sendDeederRegConfirmEmail(Deeder deeder) {
        List<EmailMessage> regMessages=emailMessageRepository.findByTemplate(EmailTemplateType.DEEDER_REGISTER.name());
        StringBuilder sb=new StringBuilder(deeder.getEmail()+",\n");
            Map<String, String> map=new HashMap<String, String>();
            for (EmailMessage msg:regMessages){
                map.put(msg.getMessageTitle(), msg.getText());
            }
            //IN the email we need values in the following order
            //registrationDeeder, successfullyReg, setPassword, createAccess
            sb.append(map.get("registrationDeeder")).append("\n");
            sb.append(map.get("successfullyReg")).append("\n");
            sb.append(map.get("setPassword")).append("\n");
            sb.append(protocol).append(webURI).append(accessConfirmURI).append(deeder.getEmail());
            MimeMessage message = mailSender.createMimeMessage();
    		MimeMessageHelper helper = new MimeMessageHelper(message);
    		try {
    			helper.setSubject(map.get("subject"));
				helper.setFrom(sender);
				helper.setTo(deeder.getEmail());
				boolean html = false;
	    		helper.setText(sb.toString(), html);
	    		mailSender.send(message);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
	
	public void sendAccessConfirmEmail(String email) {
        List<EmailMessage> accessMessages=emailMessageRepository.findByTemplate(EmailTemplateType.ACCESS_CONFIRM.name());
        Map<String, String> map=new HashMap<String, String>();
        for (EmailMessage msg:accessMessages){
            map.put(msg.getMessageTitle(), msg.getText());
        }
        StringBuilder sb=new StringBuilder();
        sb.append(email).append("\n");
        sb.append(map.get("thankYou")).append("\n");
        sb.append(map.get("welcome")).append("\n");
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(map.get("subject"));
            message.setContent(sb.toString(), "text/plain; charset=utf-8");
            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
	
	public void sendBusinessRegConfirmEmail(Business business) {
		List<EmailMessage> accessMessages=emailMessageRepository.findByTemplate(EmailTemplateType.BUSINESS_REGISTER.name());
		StringBuilder sb=new StringBuilder(business.getEmail()+"\n");
		Map<String, String> map=new HashMap<String, String>();
        for (EmailMessage msg:accessMessages){
            map.put(msg.getMessageTitle(), msg.getText());
        }
        
        sb.append(map.get("registrationBusiness")).append("\n");
        sb.append(map.get("successfullyReg")).append("\n");
        sb.append(map.get("setPassword")).append("\n");
        sb.append(protocol).append(webURI).append(accessConfirmURI).append(business.getEmail());
        MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setSubject(map.get("subject"));
			helper.setFrom(sender);
			helper.setTo(business.getEmail());
			boolean html = false;
    		helper.setText(sb.toString(), html);
    		mailSender.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void sendGovernmentRegConfirmEmail(Government government) {
		List<EmailMessage> accessMessages=emailMessageRepository.findByTemplate(EmailTemplateType.GOVERNMENT_REGISTER.name());
		StringBuilder sb=new StringBuilder(government.getEmail()+"\n");
		Map<String, String> map=new HashMap<String, String>();
        for (EmailMessage msg:accessMessages){
            map.put(msg.getMessageTitle(), msg.getText());
        }
        
        sb.append(map.get("registrationGovernment")).append("\n");
        sb.append(map.get("successfullyReg")).append("\n");
        sb.append(map.get("setPassword")).append("\n");
        sb.append(protocol).append(webURI).append(accessConfirmURI).append(government.getEmail());
        MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setSubject(map.get("subject"));
			helper.setFrom(sender);
			helper.setTo(government.getEmail());
			boolean html = false;
    		helper.setText(sb.toString(), html);
    		mailSender.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void sendNgoRegConfirmEmail(Ngo ngo) {
		List<EmailMessage> accessMessages=emailMessageRepository.findByTemplate(EmailTemplateType.NGO_REGISTER.name());
		StringBuilder sb=new StringBuilder(ngo.getEmail()+"\n");
		Map<String, String> map=new HashMap<String, String>();
        for (EmailMessage msg:accessMessages){
            map.put(msg.getMessageTitle(), msg.getText());
        }
        
        sb.append(map.get("registrationNgo")).append("\n");
        sb.append(map.get("successfullyReg")).append("\n");
        sb.append(map.get("setPassword")).append("\n");
        sb.append(protocol).append(webURI).append(accessConfirmURI).append(ngo.getEmail());
        MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setSubject(map.get("subject"));
			helper.setFrom(sender);
			helper.setTo(ngo.getEmail());
			boolean html = false;
    		helper.setText(sb.toString(), html);
    		mailSender.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}

