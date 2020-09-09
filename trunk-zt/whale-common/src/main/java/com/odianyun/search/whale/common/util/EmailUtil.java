package com.odianyun.search.whale.common.util;

import java.util.Date;
import java.util.Properties;

//import javax.mail.Address;
//import javax.mail.BodyPart;
//import javax.mail.Message;
//import javax.mail.Multipart;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class EmailUtil {
	private final static Logger log = Logger.getLogger(EmailUtil.class);

	private static Properties mailProp = new Properties();
	protected static String localIP = null;
	private static final String DEFAULT_MSGTO = "search@odianyun.com";
	
	private static String host = "mail.odianyun.com.cn";
	private static String msgFrom = "odysearch@odianyun.com.cn";

	private static String name = "odysearch@odianyun.com.cn";
	private static String password = "Ody,123";
	
	private static String env;

//	static {
//		localIP = NetUtil.getLocalIP();
//		try {
//			ConfigUtil.loadPropertiesFile("mail.properties");
//		} catch (Exception e) {
//			log.warn("load mail.properties failed", e);
//		}
//
//		if(ConfigUtil.get("mail.smtp.host") != null)
//			host = ConfigUtil.get("mail.smtp.host");
//		if(ConfigUtil.get("mail.msgFrom") != null)
//			msgFrom = ConfigUtil.get("mail.msgFrom");
//
//		if(ConfigUtil.get("mail.name") != null)
//			name = ConfigUtil.get("mail.name");
//		if(ConfigUtil.get("mail.password") != null)
//			password = ConfigUtil.get("mail.password");
//
//		env = ConfigUtil.get("mail.env");
//	}

	@Deprecated
	public static void sendMail(String title, Object message) {
		log.error("邮件服务已经关闭,发送内容为:"+title+","+message);
		sendMail(title, DEFAULT_MSGTO, message);
	}

	@Deprecated
	public static void sendMail(String title, String msgTo, Object message) {
//		String sender = "send from: " + localIP;
//		if(env != null) {
//			sender += ", env is " + env;
//			title = "[" + env + "]" + title;
//		}
//		String content = GsonUtil.getGson().toJson(message) + "</br>" + sender;
//
//		try {
//			mailProp.put("mail.smtp.auth", "true");
//			mailProp.put("mail.smtp.host", host);
//			mailProp.put("mail.msgFrom", msgFrom);
//			mailProp.put("mail.name", name);
//			mailProp.put("mail.password", password);
//			Session session = Session.getInstance(mailProp);
//
//			Message msg = new MimeMessage(session);
//
//			msgTo = msgTo.replaceAll(",", ";");
//			String[] msgTos = msgTo.split(";");
//			Address[] addresses = new Address[msgTos.length];
//			for (int i = 0; i < msgTos.length; i++) {
//				addresses[i] = new InternetAddress(msgTos[i], false);
//			}
//			msg.setFrom(new InternetAddress(msgFrom));
//			msg.setRecipients(Message.RecipientType.TO, addresses);
//			msg.setSentDate(new Date());
//			msg.setSubject(title);
//
//			Multipart mm = new MimeMultipart();
//
//			BodyPart mdp = new MimeBodyPart();
//			mdp.setContent(content, "text/html;charset=utf-8");
//			mm.addBodyPart(mdp);
//			msg.setContent(mm);
//			Transport tran = session.getTransport("smtp");
//			tran.connect(host, name, password);
//			tran.sendMessage(msg, msg.getAllRecipients());
//			tran.close();
//			log.info("send email success ");
//		} catch (Exception e) {
//			log.error("send mail exception!", e);
//		}
	}
}
