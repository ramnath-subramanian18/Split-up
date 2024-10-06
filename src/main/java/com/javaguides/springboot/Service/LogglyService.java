package com.javaguides.springboot.Service;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class LogglyService {
    @Value("${api.token}")
    private String apiToken;
    private String logglyUrl;

//    private static final String LOGGLY_URL = "https://logs-01.loggly.com/inputs/3ae2bb93-2347-44ff-a4c9-1f8ee6fc5273/tag/http/";
//init is automatically called and Postconstructor is used to load the value

    @PostConstruct
    public void init() {
        logglyUrl = "https://logs-01.loggly.com/inputs/" + apiToken + "/tag/http/";
    }

    public void sendLog(String jsonPayload) {
        try {
            URL url = new URL(logglyUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Log sent successfully.");
            } else {
                System.out.println("Failed to send log. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
