package lrk.tools.miraiutils;

import com.google.gson.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class GlotCodeHelper {
    private static final HashMap<String, URI> supportLanguages = new HashMap<>();
    private static final HashMap<String, String> fileSuffix = new HashMap<>() {{
        put("assembly", "asm");
        put("ats", "dats");
        put("bash", "sh");
        put("clisp", "lsp");
        put("cobol", "cob");
        put("coffeescript", "coffee");
        put("crystal", "cr");
        put("csharp", "cs");
        put("elixir", "ex");
        put("erlang", "erl");
        put("fsharp", "fs");
        put("guile", "scm");
        put("hare", "ha");
        put("haskell", "hs");
        put("idris", "idr");
        put("julia", "jl");
        put("kotlin", "kt");
        put("mercury", "m");
        put("ocaml", "ml");
        put("pascal", "pp");
        put("perl", "pl");
        put("python", "py");
        put("ruby", "rb");
        put("rust", "rs");
        put("typescript", "ts");
    }};
    private static final String token = "c577d9c5-578f-4c1a-ab96-f4234c3b2977";
    private static final Gson gson = new Gson();

    public static String getSuggestedFileName(String languageName) {
        String suffix = fileSuffix.get(languageName);
        return ("Main.%s".formatted(suffix == null ? languageName : suffix)).intern();
    }

    public static HashMap<String, URI> getSupportLanguages() throws URISyntaxException, IOException {
        if (supportLanguages.isEmpty()) {
            URI uri = new URI("https://glot.io/api/run");
            JsonArray result = JsonParser.parseString(new String(uri.toURL().openConnection().getInputStream().readAllBytes())).getAsJsonArray();
            for (JsonElement jsonElement : result) {
                JsonObject entry = jsonElement.getAsJsonObject();
                supportLanguages.put(entry.get("name").getAsString(), new URI("%s/latest".formatted(entry.get("url").getAsString())));
            }
        }
        return supportLanguages;
    }

    public static HashMap<String, String> runCode(String languageName, String fileNameInDocker, String code) throws URISyntaxException, IOException {
        URI uri = getSupportLanguages().get(languageName);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", String.format("Token %s", token));
        connection.setRequestProperty("Content-type", "application/json");
        connection.setDoOutput(true);
        JsonObject data = new JsonObject();
        JsonArray files = new JsonArray();
        JsonObject file = new JsonObject();
        file.addProperty("name", fileNameInDocker);
        file.addProperty("content", code);
        files.add(file);
        data.add("files", files);
        connection.getOutputStream().write(data.toString().getBytes(StandardCharsets.UTF_8));

        String responseJson = new String(connection.getInputStream().readAllBytes());
        Response response = gson.fromJson(responseJson, Response.class);
        HashMap<String, String> result = new HashMap<>();
        result.put("stdout", response.stdout);
        result.put("stderr", response.stderr);
        result.put("error", response.error);
        return result;
    }

    static class Response {
        String stdout;
        String stderr;
        String error;
    }
}
