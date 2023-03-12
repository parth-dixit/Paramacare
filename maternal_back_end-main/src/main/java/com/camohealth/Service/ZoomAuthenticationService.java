package com.camohealth.Service;

import com.camohealth.Entity.ZoomOauth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.json.JSONObject;


public class ZoomAuthenticationService {

    @Autowired
    ZoomOauth zoomoauth;
    private static final Logger LOG = LoggerFactory.getLogger(ZoomAuthenticationService.class);

    public ZoomOauth accessToken(String clientID, String clientSecret, String code, String redirectURI)
            throws IOException, InterruptedException{

        String keys = clientID + ":" + clientSecret;

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "authorization_code");
        parameters.put("code", code);
        parameters.put("redirect_uri", redirectURI);
        String form = parameters.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String encoding = Base64.getEncoder().encodeToString(keys.getBytes());
        HttpClient client = HttpClient.newHttpClient();
        // Update URI according to specified endpoint
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://zoom.us/oauth/token"))
                .headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Basic " + encoding)
                .POST(BodyPublishers.ofString(form)).build();
        HttpResponse<?> response = client.send(request, BodyHandlers.ofString());
        System.out.println("ACCESS TOKEN RESPONSE: " + response.body().toString());

        JSONObject jsonObj = new JSONObject(response.body().toString());

        String access_token = jsonObj.getString("access_token");
        String refresh_token = jsonObj.getString("refresh_token");
        zoomoauth.setAccessToken(access_token);
        zoomoauth.setRefreshToken(refresh_token);
        return zoomoauth;
    }

    public ZoomOauth refreshAccessToken(String clientID, String clientSecret) throws IOException, InterruptedException {

        String keys = clientID + ":" + clientSecret;

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "refresh_token");
        parameters.put("refresh_token", zoomoauth.getRefreshToken());
        String form = parameters.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String encoding = Base64.getEncoder().encodeToString(keys.getBytes());
        HttpClient client = HttpClient.newHttpClient();
        // Update URI according to specified endpoint
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://zoom.us/oauth/token"))
                .headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Basic " + encoding)
                .POST(BodyPublishers.ofString(form)).build();
        HttpResponse<?> response = client.send(request, BodyHandlers.ofString());
        System.out.println("REFRESH ACCESS TOKEN RESPONSE: " + response.body().toString());

        JSONObject jsonObj = new JSONObject(response.body().toString());

        String access_token = jsonObj.getString("access_token");
        String refresh_token = jsonObj.getString("refresh_token");
        zoomoauth.setAccessToken(access_token);
        zoomoauth.setRefreshToken(refresh_token);

        return zoomoauth;
    }
}
