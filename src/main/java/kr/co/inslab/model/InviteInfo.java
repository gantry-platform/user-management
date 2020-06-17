package kr.co.inslab.model;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("InviteInfo")
public class InviteInfo implements Serializable {
    private String email;
    private String code;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "InviteInfo{" +
                "email='" + email + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
