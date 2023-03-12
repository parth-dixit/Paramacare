package com.camohealth.Entity;


/**
 * Container class for OAuth 2.0 to hold authentication & authorization token
 * of users to make subsequent Zoom API requests
 */
public class ZoomOauth {

    private String accessToken;
    private String refreshToken;

    public ZoomOauth() {
    }

    public ZoomOauth(String accessToken, String refreshToken) {

        this.setAccessToken(accessToken);
        this.setRefreshToken(refreshToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}