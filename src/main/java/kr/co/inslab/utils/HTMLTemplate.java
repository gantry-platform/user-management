package kr.co.inslab.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Component
public class HTMLTemplate {

    @Value("${mail.inviteUrl}")
    private String mailInviteUrl;

    private final SpringTemplateEngine templateEngine;

    public HTMLTemplate(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String makeInviteHtml(String htmlName,String token,String email){
        Context context = new Context();
        context.setVariable(CommonConstants.MAIL_INVITE_URL,mailInviteUrl);
        context.setVariable(CommonConstants.TOKEN, token);
        context.setVariable(CommonConstants.EMAIL, email);
        return templateEngine.process(htmlName, context);
    }

}
