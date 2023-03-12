package com.camohealth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "zoom.meeting")
public class ZoomMeetingConfig {

    private String meetingBaseURl;
    private String createEndpoint;

    public String getMeetingBaseURl() {
        return meetingBaseURl;
    }

    public void setMeetingBaseURl(String baseURl) {
        this.meetingBaseURl = baseURl;
    }

    public String getCreateEndpoint() {
        return createEndpoint;
    }

    public void setCreateEndpoint(String createEndpoint) {
        this.createEndpoint = createEndpoint;
    }
}