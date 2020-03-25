package kr.co.inslab.util;

import kr.co.inslab.bootstrap.StaticConfig;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Component
public class HTMLTemplate {

    private final SpringTemplateEngine templateEngine;

    public HTMLTemplate(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String makeInviteHtml(String htmlName,String token){
        Context context = new Context();
        context.setVariable(StaticConfig.TOKEN, token);
        String html = templateEngine.process(htmlName, context);
        return html;
    }

}
