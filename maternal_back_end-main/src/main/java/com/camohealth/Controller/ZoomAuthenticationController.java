package com.camohealth.Controller;

import com.camohealth.Entity.ZoomEntity;
import com.camohealth.config.ZoomMeetingConfig;
import com.camohealth.config.ZoomOauthConfig;
import com.google.gson.Gson;
import com.salesboxai.zoom.ZoomAccessToken;
import okhttp3.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.camohealth.model.Clients;

import lombok.SneakyThrows;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping(value = "/api/v1")
@Controller
public class ZoomAuthenticationController {

    private static final Logger LOG = LoggerFactory.getLogger(ZoomAuthenticationController.class);

    @Autowired
    private ZoomOauthConfig zoomconfig;

    @Autowired
    private ZoomMeetingConfig zoomMeetingConfig;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Autowired
    private ZoomEntity zoomEntity;

    /*TODO: if token is being saved in application, then before everything first check if the token exists and
       hasnt expired, if it is still active bypass new token call and use the existing token for the user to schedule
       the meeting otherwise 2cases:
    case 1: if token doesnt exist at all for the user, use the web auth and get token and then schedule the meeting
    case 2: if token exists and has expired, use refresh token api call to refresh the existing token, update it in DB
    and then proceed to schedule meeting call*/
    @GetMapping("/{client}/token")
    private RedirectView getToken(@PathVariable Clients client,Model model) { //model not working as of now
        RedirectView redirectView = new RedirectView();
//        model.addAttribute("zoomEntity",zoomEntity);
//        redirectAttributes.addFlashAttribute(zoomEntity.getEmail());

        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(zoomconfig.getBaseURl() + zoomconfig.getAuthEndpoint()).newBuilder()
                .addQueryParameter("response_type", "code")
                .addQueryParameter("client_id", zoomconfig.getClientId())
                .addQueryParameter("redirect_uri", zoomconfig.getRedirectUri());

        redirectView.setUrl(urlBuilder.build().toString());
        LOG.info(String.valueOf(redirectView));
        return redirectView;
    }

    @GetMapping("/redirect")
    public String getOauthToken(Model model, @RequestParam Map<String, String> queryParameters) throws Exception {
        String code="";
        LOG.info("inside getOauthToken 68");
        if(queryParameters.containsKey("code")){
            code=queryParameters.get("code");
        } else {
            throw new Exception("Something went wrong while fetching oauth token!");
        }

        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("code", code)
                .add("grant_type", "authorization_code")
                .add("redirect_uri", zoomconfig.getRedirectUri())
                .build();

        Map<String,String> mapHeaders= new HashMap<>();
        mapHeaders.put("Authorization", "Basic "+ Base64.getEncoder().encodeToString(
                (zoomconfig.getClientId()+":"+zoomconfig.getClientSecret()).getBytes())
        );

        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(zoomconfig.getBaseURl() + zoomconfig.getTokenEndpoint()).newBuilder();

        Request req = new Request.Builder()
                .url(urlBuilder.build().toString())
                .headers(Headers.of(mapHeaders))
                .post(requestBody)
                .build();

        Call call = httpClient.newCall(req);
        Response resp = call.execute();
        ResponseBody responseBody = resp.body();

        Gson gson = new Gson();
        ZoomAccessToken accessToken = gson.fromJson(responseBody.string(), ZoomAccessToken.class);
        //TODO: saveAccessToken(accessToken); save logic goes here if token needs to be saved
        //TODO: remove log in the next line added only for debugging purpose
        LOG.info("Step access token"+accessToken.access_token);
        //save access token against that user and create meeting
        //if else to check what type of zoom api i need to call
        //static value from map
        createZoomMeeting(model,accessToken);

        return "index"; //add the path to redirection page
    }

    @SneakyThrows
    private void createZoomMeeting(Model model,ZoomAccessToken accessToken) {
        OkHttpClient httpClient = new OkHttpClient();
        LOG.info("inside createZoomMeeting 114");
        Map<String,String> mapHeaders= new HashMap<>();
        mapHeaders.put("Authorization", "Bearer "+ accessToken.access_token);
        String json ="{\n" +
                "    \"topic\": \"Consultation\",\n" +
                "    \"type\": 2,\n" +
                "    \"start_time\": \"2022-03-14T10:35:01\",\n" +
                "    \"duration\": 60,\n" +
                "    \"timezone\": \"America/New_York\",\n" +
                "    \"password\": \"password\",\n" +
                "    \"agenda\": \"Virtual Consultation Session\",\n" +
                "    \"recurrence\": {\n" +
                "        \"type\": 1,\n" +
                "        \"repeat_interval\": 1,\n" +
                "        \"weekly_days\": \"1,2,3,4,5,6\",\n" +
                "        \"end_times\": 50\n" +
                "    },\n" +
                "    \"settings\": {\n" +
                "        \"host_video\": true,\n" +
                "        \"participant_video\": true,\n" +
                "        \"cn_meeting\": true,\n" +
                "        \"in_meeting\": false,\n" +
                "        \"join_before_host\": true,\n" +
                "        \"mute_upon_entry\": false,\n" +
                "        \"watermark\": false,\n" +
                "        \"use_pmi\": false,\n" +
                "        \"approval_type\": 2,\n" +
                "        \"registration_type\": 3,\n" +
                "        \"audio\": \"both\",\n" +
                "        \"auto_recording\": \"cloud\",\n" +
                "        \"enforce_login\": false,\n" +
                "        \"alternative_hosts\": \"\"\n" +
                "        \n" +
                "    }\n" +
                "}";

        RequestBody body = RequestBody.create(json, JSON);

        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(zoomMeetingConfig.getMeetingBaseURl() + zoomMeetingConfig.getCreateEndpoint())
                .newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .headers(Headers.of(mapHeaders))
                .post(body)
                .build();

        Call call = httpClient.newCall(request);
        Response resp = call.execute();
        ResponseBody responseBody = resp.body();
        //Gson gson = new Gson();
        LOG.info("Meeting schedule response: " + responseBody.string());
        if(resp.isSuccessful()){
            model.addAttribute("meetingDetails",responseBody);
        }
        LOG.info(model.toString());
    }

    @SneakyThrows
    @PostMapping("/zoom/tokens")
    public String getTokens(){
        return null;
    }


}
