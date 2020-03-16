package kr.co.inslab.bootstrap;

import kr.co.inslab.interceptor.RequestLoggingInterceptor;
import kr.co.inslab.service.LoggingService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {

    private final LoggingService loggingService;

    public WebConfig(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestLoggingInterceptor(this.loggingService));
    }
}
