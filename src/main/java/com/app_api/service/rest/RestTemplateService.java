package com.app_api.service.rest;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RestTemplateService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String baseUrl = "https://apis.fedex.com";

    private final RestTemplate restTemplate;

    @Value("${grant_type}")
    private String grant_type;
    @Value("${client_id}")
    private String client_id;
    @Value("${client_secret}")
    private String client_secret;

    private String access_token;

    private boolean statusFlag = true;

    private void authFedex() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grant_type);
        params.add("client_id", client_id);
        params.add("client_secret", client_secret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/oauth/token", request, String.class);

        extractAccessToken(response.getBody());
    }

    public HashMap<String, String> track(List<String> trackingNumbers) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(access_token);

        HttpEntity<String> request = new HttpEntity<>(generateTrackingRequestBody(trackingNumbers), headers);

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(baseUrl + "/track/v1/trackingnumbers", request, String.class);
        } catch (HttpStatusCodeException ex) {
            if (statusFlag && ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                statusFlag = false;
                authFedex();
                return track(trackingNumbers);
            }
        }

        if(response == null) {
            return new HashMap<>();
        }

        return extractFTI(response.getBody());
    }

    private String generateTrackingRequestBody(List<String> trackingNumbers) {
        JSONObject request = new JSONObject();
        request.put("includeDetailedScans", false);

        JSONArray trackingInfoArr = new JSONArray();

        for (String trackingNumber : trackingNumbers) {
            JSONObject trackingNumberInfo = new JSONObject();
            trackingNumberInfo.put("trackingNumber", trackingNumber);

            JSONObject trackingInfoObj = new JSONObject();
            trackingInfoObj.put("trackingNumberInfo", trackingNumberInfo);

            trackingInfoArr.put(trackingInfoObj);
        }

        request.put("trackingInfo", trackingInfoArr);

        return request.toString();
    }

    private HashMap<String, String> extractFTI(String json) {
        HashMap<String, String> result = new HashMap<>();
        try {
            JSONArray completeTrackResults = new JSONObject(json)
                    .getJSONObject("output")
                    .getJSONArray("completeTrackResults");
            for (int i = 0; i < completeTrackResults.length(); i++) {
                String trackingNumber = completeTrackResults.getJSONObject(i)
                        .getString("trackingNumber");

                String description = completeTrackResults.getJSONObject(i)
                        .getJSONArray("trackResults")
                        .getJSONObject(0)
                        .getJSONObject("latestStatusDetail")
                        .getString("description");

                result.put(trackingNumber, description);
            }
        } catch (JSONException e) {
            logger.error("Invalid JSON - extractFTI : " + e.getMessage());
        }
        return result;
    }

    private void extractAccessToken(String json) {
        try {
            access_token = new JSONObject(json).getString("access_token");
            statusFlag = true;
        } catch (JSONException e) {
            logger.error("Invalid JSON - extractAccessToken : " + e.getMessage());
        }
    }

}
