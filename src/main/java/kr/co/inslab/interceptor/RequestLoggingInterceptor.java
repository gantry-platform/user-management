package kr.co.inslab.interceptor;

import kr.co.inslab.utils.HttpLogging;
import org.springframework.boot.web.servlet.DispatcherType;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private final HttpLogging httpLogging;

    public RequestLoggingInterceptor(HttpLogging httpLogging) {
        this.httpLogging = httpLogging;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name()) && request.getMethod().equals(HttpMethod.GET.name())){
            httpLogging.logRequest(request,null);
        }
        return true;
    }
}

