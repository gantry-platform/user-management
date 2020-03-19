package kr.co.inslab.keycloak;

public enum Role {
    ADMIN("admin"),
    OPS("ops"),
    DEV("dev");

    private String value;

    Role(String value){
        this.value = value;
    }

    public String toString(){
        return String.valueOf(this.value);
    }
}
