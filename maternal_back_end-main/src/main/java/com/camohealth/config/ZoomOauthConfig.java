package com.camohealth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "zoom.oauth")
public class ZoomOauthConfig {

	private String clientId;
	private String clientSecret;
	private String baseURl;
	private String tokenEndpoint;
	private String authEndpoint;
	private String redirectUri;

	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getBaseURl() {
		return baseURl;
	}
	public void setBaseURl(String baseURl) {
		this.baseURl = baseURl;
	}
	public String getTokenEndpoint() {
		return tokenEndpoint;
	}
	public void setTokenEndpoint(String tokenEndpoint) {
		this.tokenEndpoint = tokenEndpoint;
	}
	public String getAuthEndpoint() {
		return authEndpoint;
	}
	public void setAuthEndpoint(String authEndpoint) {
		this.authEndpoint = authEndpoint;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

}