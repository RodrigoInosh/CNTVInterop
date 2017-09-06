package cl.subtel.interop.cntv.util;

import java.io.UnsupportedEncodingException;

//File Name SendEmail.java

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Mail {

	public static void sendMail(String subject, String message_body) {

		String from = "rinostroza@subtel.gob.cl";
		String host = "jupiter.subtel.cl";
		
		ArrayList<String> destinataries = new ArrayList<String>();
		destinataries.add("r.inostroza.inosh@gmail.com");
		destinataries.add("rinostroza@subtel.gob.cl");
		destinataries.add("vsanchez@subtel.gob.cl");

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);

		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);

			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.setFrom(new InternetAddress(from, "DDT"));
			
			for(String dest : destinataries) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(dest));
			}

			message.setSubject(subject);
			message.setContent(message_body, "text/html; charset=UTF-8");

			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getBody(Long numero_op, String response_code, String response_message) {
		String body = "";
		
		body = "<b>Inserción de Número OP</b>: " + numero_op + "<br>";
		body += "Cod <b>" + response_code + "</b><br>" + response_message;
		return body;
	}
}