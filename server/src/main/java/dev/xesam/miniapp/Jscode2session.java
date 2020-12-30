package dev.xesam.miniapp;

public class Jscode2session {
    public String session_key;
    public String openid;
    public String unionid;

    @Override
    public String toString() {
        return "Jscode2session{" +
                "session_key='" + session_key + '\'' +
                ", openid='" + openid + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
