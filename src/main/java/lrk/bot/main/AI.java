package lrk.bot.main;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class AI extends WebSocketListener {
    // 地址与鉴权信息
    public static final String hostUrl = "https://spark-api.xf-yun.com/v1.1/chat";
    public static final String app_id;
    public static final String apiSecret;
    public static final String apiKey;

    static {
        DataBridge.initRobotProp("AI_app_id", "请输入讯飞星火AI应用的app_id: ", "未设置");
        DataBridge.initRobotProp("AI_apiSecret", "请输入讯飞星火AI应用的apiSecret: ", "未设置");
        DataBridge.initRobotProp("AI_apiKey", "请输入讯飞星火AI应用的apiKey: ", "未设置");
        app_id = DataBridge.getRobotProp("AI_app_id");
        apiSecret = DataBridge.getRobotProp("AI_apiSecret");
        apiKey = DataBridge.getRobotProp("AI_apiKey");
    }

    private static final Type history_type = new TypeToken<ArrayList<Text>>() {
    }.getType();
    public static final HashMap<Long, JsonArray> all_history = new HashMap<>();
    public static final Gson gson = new Gson();
    private ArrayList<Text> history = new ArrayList<>();
    private final StringBuffer answerBuffer = new StringBuffer();
    private final String userId;
    private String question;
    private BiConsumer<JsonArray, String> processor;
    private final OkHttpClient client;
    private final Request request;

    boolean finished = false;

    // 构造函数
    public AI(String userId) throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException {
        this.userId = userId;
        String authUrl = getAuthUrl();
        client = new OkHttpClient.Builder().build();
        String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
        request = new Request.Builder().url(url).build();
    }

    public void askSync(String question, BiConsumer<JsonArray, String> processor) throws InterruptedException {
        this.finished = false;
        this.question = question;
        this.processor = processor;
        answerBuffer.delete(0, answerBuffer.length());
        answerBuffer.trimToSize();
        client.newWebSocket(request, this);
        while (!finished) Thread.sleep(100);
    }

    public void askSync(JsonArray content, String question, BiConsumer<JsonArray, String> processor) throws InterruptedException {
        this.finished = false;
        this.question = question;
        this.processor = processor;
        this.history = gson.fromJson(content.toString(), history_type);
        answerBuffer.delete(0, answerBuffer.length());
        answerBuffer.trimToSize();
        client.newWebSocket(request, this);
        while (!finished) Thread.sleep(100);
    }

    public void askASync(String question, BiConsumer<JsonArray, String> processor) {
        this.finished = false;
        this.question = question;
        this.processor = processor;
        answerBuffer.delete(0, answerBuffer.length());
        answerBuffer.trimToSize();
        client.newWebSocket(request, this);
    }

    public void askASync(JsonArray content, String question, BiConsumer<JsonArray, String> processor) {
        this.finished = false;
        this.question = question;
        this.processor = processor;
        this.history = gson.fromJson(content.toString(), history_type);
        answerBuffer.delete(0, answerBuffer.length());
        answerBuffer.trimToSize();
        client.newWebSocket(request, this);
    }

    public void waitToFinish() throws InterruptedException {
        while (!finished) Thread.sleep(100);
    }

    public static void cleanHistory(long qq) {
        all_history.remove(qq);
    }

    // 鉴权方法
    private String getAuthUrl() throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException {
        URL url = new URL(AI.hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(AI.apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", AI.apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();
        return httpUrl.toString();
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        new Thread(() -> {
            JsonObject json = new JsonObject();
            JsonObject header = new JsonObject();
            JsonObject parameter = new JsonObject(), chat = new JsonObject();
            JsonObject payload = new JsonObject(), message = new JsonObject();

            header.addProperty("app_id", app_id);
            header.addProperty("uid", userId);

            chat.addProperty("domain", "general");
            chat.addProperty("temperature", 0.5);
            chat.addProperty("max_tokens", 1024);
            parameter.add("chat", chat);

            Text t = new Text("user", question);
            history.add(t);

            message.add("text", gson.toJsonTree(history).getAsJsonArray());
            payload.add("message", message);

            json.add("header", header);
            json.add("parameter", parameter);
            json.add("payload", payload);

            try {
                webSocket.send(json.toString());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        JsonParse myJsonParse = gson.fromJson(text, JsonParse.class);
        if (myJsonParse.header.code != 0) {
            System.out.println("发生错误，错误码为：" + myJsonParse.header.code);
            System.out.println("本次请求的sid为：" + myJsonParse.header.sid);
            webSocket.close(1000, "");
        }
        List<Text> textList = myJsonParse.payload.choices.text;
        for (Text temp : textList) {
            answerBuffer.append(temp.content);
        }
        if (myJsonParse.header.status == 2) {
            history.add(new Text("assistant", answerBuffer.toString()));
            processor.accept(gson.toJsonTree(history).getAsJsonArray(), answerBuffer.toString());
            finished = true;
            webSocket.close(1000, "ok");
        }

    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        try {
            if (null != response) {
                int code = response.code();
                System.out.println("onFailure code:" + code);
                if (response.body() != null) {
                    System.out.println("onFailure body:" + response.body().string());
                }
                if (101 != code) {
                    System.out.println("connection failed");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //返回的json结果拆解
    static class JsonParse {
        Header header;
        Payload payload;
    }

    static class Header {
        int code;
        int status;
        String sid;
    }

    static class Payload {
        Choices choices;
    }

    static class Choices {
        List<Text> text;
    }

    static class Text {
        String role;
        String content;

        public Text(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
