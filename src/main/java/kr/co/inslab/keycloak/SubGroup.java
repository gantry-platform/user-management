package kr.co.inslab.keycloak;

public enum SubGroup {
    ADMIN("admin"),
    OPS("ops"),
    DEV("dev");

    private String value;

    SubGroup(String value){
        this.value = value;
    }

    public String toString(){
        return String.valueOf(this.value);
        }
}

