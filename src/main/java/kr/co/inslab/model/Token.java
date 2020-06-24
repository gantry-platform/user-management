package kr.co.inslab.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * Token
 */
@Profile("test")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-06-18T11:49:22.012+09:00[Asia/Seoul]")
public class Token   {
    @JsonProperty("access_token")
    private String accessToken = null;

    @JsonProperty("refresh_token")
    private String refreshToken = null;

    @JsonProperty("id_token")
    private String idToken = null;

    @JsonProperty("expires_in")
    private String expiresIn = null;

    @JsonProperty("scope")
    private String scope = null;

    public Token accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    /**
     * Get accessToken
     * @return accessToken
     **/
    @ApiModelProperty(value = "")

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Token refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    /**
     * Get refreshToken
     * @return refreshToken
     **/
    @ApiModelProperty(value = "")

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Token idToken(String idToken) {
        this.idToken = idToken;
        return this;
    }

    /**
     * Get idToken
     * @return idToken
     **/
    @ApiModelProperty(value = "")

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public Token expiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    /**
     * Get expiresIn
     * @return expiresIn
     **/
    @ApiModelProperty(value = "")

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Token scope(String scope) {
        this.scope = scope;
        return this;
    }

    /**
     * Get scope
     * @return scope
     **/
    @ApiModelProperty(value = "")

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Token token = (Token) o;
        return Objects.equals(this.accessToken, token.accessToken) &&
                Objects.equals(this.refreshToken, token.refreshToken) &&
                Objects.equals(this.idToken, token.idToken) &&
                Objects.equals(this.expiresIn, token.expiresIn) &&
                Objects.equals(this.scope, token.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, refreshToken, idToken, expiresIn, scope);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Token {\n");

        sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
        sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n");
        sb.append("    idToken: ").append(toIndentedString(idToken)).append("\n");
        sb.append("    expiresIn: ").append(toIndentedString(expiresIn)).append("\n");
        sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
