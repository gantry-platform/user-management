package kr.co.inslab.service;

import org.thymeleaf.context.Context;

public interface MailSendingService {

    public void sendHtmlEmail(String from,String to,String subject,String html);
}
