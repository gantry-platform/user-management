package kr.co.inslab.service;

public interface MailSendingService {
    public void sendHtmlEmail(String from,String to,String subject,String html);
}
