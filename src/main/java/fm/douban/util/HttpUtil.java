package fm.douban.util;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    private static final OkHttpClient OKHTTPCLIENT = new OkHttpClient();

    public Map<String, String> buildHeaderData(String referer, String host) {
        if (!StringUtils.hasText(referer) || !StringUtils.hasText(host)){
            return null;
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Referer", referer);
        headers.put("Host", host);
        return headers;
    }

    public String getContent(String url, Map<String, String>headers) {

        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4128.3 Safari/537.36")
                .addHeader("Referer",headers.get("Referer"))
                .addHeader("Host",headers.get("Host"))
                .build();
        Call call = OKHTTPCLIENT.newCall(request);

        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            System.out.println("request " + url + " error . ");
            e.printStackTrace();
        }
        return null;
    }

}
