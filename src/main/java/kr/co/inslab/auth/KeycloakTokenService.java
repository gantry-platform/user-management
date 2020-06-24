package kr.co.inslab.auth;

import kr.co.inslab.model.Token;
import kr.co.inslab.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@Profile("test")
@Service
public class KeycloakTokenService implements OAuthToken{

    @Value("${keycloak.testClientId}")
    private String TEST_CLIENT_ID;

    @Value("${keycloak.testClientSecret}")
    private String TEST_CLIENT_SECRET;

    @Value("${keycloak.tokenEndpoint}")
    private String TOKEN_ENDPOINT;

    @Override
    public Token getToken(String username, String password) throws Exception {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        MultiValueMap<String,String> formMap = new LinkedMultiValueMap<>();
        formMap.add(CommonConstants.CLIENT_ID,TEST_CLIENT_ID);
        formMap.add(CommonConstants.CLIENT_SECRET,TEST_CLIENT_SECRET);
        formMap.add(CommonConstants.USERNAME,username);
        formMap.add(CommonConstants.PASSWORD,password);
        formMap.add(CommonConstants.GRANT_TYPE, CommonConstants.PASSWORD);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(formMap,httpHeaders);

        RestTemplate restTemplate = new RestTemplate();;

        ResponseEntity<Token> res = restTemplate.postForEntity(TOKEN_ENDPOINT,request,Token.class);

        if(!res.hasBody()){
            throw new Exception("There is no body in there");
        }
        return res.getBody();

    }
}
