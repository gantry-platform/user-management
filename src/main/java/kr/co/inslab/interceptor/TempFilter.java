package kr.co.inslab.interceptor;

import org.keycloak.TokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//임시필터임
@Component
@Order(1)
public class TempFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        MutableHttpServletRequest mutableHttpServletRequest = new MutableHttpServletRequest(req);

        try {
            String token = req.getHeader("Authorization");
            if(token !=null && !token.isEmpty()){
                String [] splitToken = token.split(" ");
                AccessToken accessToken = TokenVerifier.create(splitToken[1],AccessToken.class).getToken();
                mutableHttpServletRequest.putHeader("X-User-Id",accessToken.getId());
            }
        } catch (VerificationException e) {
            e.printStackTrace();
           throw new ServletException("임시토큰 Error");
        }
        chain.doFilter(mutableHttpServletRequest,response);
    }
}
