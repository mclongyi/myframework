package com.odianyun.search.whale.common.log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.apache.log4j.net.SMTPAppender;
import org.apache.log4j.spi.LoggingEvent;

import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.common.util.NetUtil;

public class TimedSMTPAppender extends SMTPAppender {
	
	private final static Logger log = Logger.getLogger(TimedSMTPAppender.class);

	private Set<String> uniqueEvent = new HashSet<String>();

	private static long sendIntervalMinus;
	private static boolean isSendErrorMail;
	private static String env;
	        
	static {
		try {
			ConfigUtil.loadPropertiesFile("mail.properties");
		} catch (Exception e) {
			log.warn("load mail.properties failed", e);
		}
		sendIntervalMinus = ConfigUtil.getLong("mail.sendIntervalMinus", 10);
		isSendErrorMail = ConfigUtil.getBool("mail.isSendErrorMail", true);
		env = ConfigUtil.get("mail.env");
	}
	
	public TimedSMTPAppender() {
		super();
		
		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(new TimerTask() {
//			public void run() {
//				if(!isSendErrorMail || uniqueEvent.size() == 0)
//					return;
//
//				sendBuffer();
//				uniqueEvent.clear();
//			}
//		}, 1000 * 60 * sendIntervalMinus, 1000 * 60 * sendIntervalMinus);
	}

	@Override
	public void append(LoggingEvent event) {

		if (!checkEntryConditions()) {
			return;
		}

		event.getThreadName();
		event.getNDC();
		event.getMDCCopy();
		event.getRenderedMessage();
		event.getThrowableStrRep();
		
		String eventKey = getEventKey(event);
		if (!uniqueEvent.contains(eventKey)) {
			uniqueEvent.add(eventKey);
		} else {
			return;
		}
		

		if (evaluator.isTriggeringEvent(event)) {
			cb.add(event);
		}
	}
	

	@Override
	public void activateOptions() {
		super.activateOptions();

		try {
			msg.setSubject(msg.getSubject() + ", send from " + NetUtil.getLocalIP());
			if (env != null) {
				msg.setSubject("[" + env + "]" + msg.getSubject());
			}
		} catch (MessagingException e) {
			log.warn("set mail subject failed", e);
		}
	}
	
	private String getEventKey(LoggingEvent event) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(event.getThreadName()).append(event.getNDC())
				.append(event.getRenderedMessage())
				.append(Arrays.toString(event.getThrowableStrRep()));

		return buffer.toString();
	}
}
