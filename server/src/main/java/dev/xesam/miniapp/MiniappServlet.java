package dev.xesam.miniapp;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;
import java.util.Base64;

public class MiniappServlet extends HttpServlet {

    private static String appid;
    private static String appsecret;

    private static OkHttpClient client = new OkHttpClient();

    private Jscode2session jscode2session;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appid = config.getServletContext().getInitParameter("appid");
        appsecret = config.getServletContext().getInitParameter("appsecret");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
        String action = req.getParameter("action");
        PrintWriter writer = resp.getWriter();
        if ("jscode2session".equals(action)) {
            String code = req.getParameter("code");
            try {
                String body = jscode2session(code);
                jscode2session = new Gson().fromJson(body, Jscode2session.class);
                System.out.println(jscode2session);
                writer.println(body);
            } catch (Exception e) {
                writer.println("error");
            }
        } else if ("getUserInfo".equals(action)) {
            String encryptedData = URLDecoder.decode(req.getParameter("encryptedData"), StandardCharsets.UTF_8.name());
            String iv = URLDecoder.decode(req.getParameter("iv"), StandardCharsets.UTF_8.name());
            String decoded = decode(encryptedData, iv, jscode2session.session_key);
            writer.println(decoded);
        } else if ("getPhone".equals(action)) {
            String encryptedData = URLDecoder.decode(req.getParameter("encryptedData"), StandardCharsets.UTF_8.name());
            String iv = URLDecoder.decode(req.getParameter("iv"), StandardCharsets.UTF_8.name());
            String decoded = decode(encryptedData, iv, jscode2session.session_key);
            writer.println(decoded);
        } else {
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private static String jscode2session(String code) throws IOException {
        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appid,
                appsecret,
                code);
        Response response = client.newCall(new Request.Builder()
                .url(url)
                .build()).execute();
        return response.body().string();
    }

    private static String decode(String encryptedData, String iv, String sessionKey) {
        // 被加密的数据
        byte[] dataByte = Base64.getDecoder().decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.getDecoder().decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.getDecoder().decode(iv);
        try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key key = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("AES");
            algorithmParameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, key, algorithmParameters);
            byte[] bytes = cipher.doFinal(dataByte);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
