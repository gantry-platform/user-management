package kr.co.inslab.bootstrap;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyCloakAdminConfig {
    @Value("${keycloak.serverUrl}")
    private String SERVER_URL;

    @Value("${keycloak.masterRealm}")
    private String MASTER_REALM;

    @Value("${keycloak.targetRealm}")
    private String TARGET_REALM;

    @Value("${keycloak.clientId}")
    private String CLIENT_ID;

    @Value("${keycloak.clientSecret}")
    private String CLIENT_SECRET;

    @Bean
    public Keycloak keycloakAdmin(){
        return KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(MASTER_REALM)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
                .build();
    }
}

