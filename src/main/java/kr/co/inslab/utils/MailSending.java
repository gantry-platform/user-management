package kr.co.inslab.utils;

public interface MailSending {
    public void sendHtmlEmail(String from,String to,String subject,String html);
}
