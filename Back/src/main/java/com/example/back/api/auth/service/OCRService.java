package com.example.back.api.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OCRService {

    @Value("${clova.url}")
    private String serviceUrl;

    @Value("${clova.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate;

    public Map<String, String> extractTextFromImage(MultipartFile diagnosis) throws IOException, JSONException {

        String base64Image = Base64.encodeBase64String(diagnosis.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-OCR-SECRET", secretKey);
        headers.set("Content-Type", "application/json");

        JSONObject body = new JSONObject();
        JSONArray imagesArray = new JSONArray();
        JSONObject imageObject = new JSONObject();
        imageObject.put("format", diagnosis.getContentType().split("/")[1]); // jpg 또는 png
        imageObject.put("name", "medium");
        imageObject.put("data", base64Image);
        imagesArray.put(imageObject);

        body.put("images", imagesArray);
        body.put("lang", "ko");
        body.put("requestId", "string");
        body.put("resultType", "string");
        body.put("timestamp", System.currentTimeMillis());
        body.put("version", "V2");

        HttpEntity<String> requestEntity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(serviceUrl, HttpMethod.POST, requestEntity, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        return parseTextFromResponse(jsonResponse);
    }

    private Map<String, String> parseTextFromResponse(JSONObject jsonResponse) throws JSONException {
        Map<String, String> extractedData = new HashMap<>();
        JSONArray images = jsonResponse.optJSONArray("images");

        if (images != null && images.length() > 0) {
            JSONObject firstImage = images.getJSONObject(0);
            JSONArray fields = firstImage.optJSONArray("fields");

            StringBuilder confirmationDate = new StringBuilder();
            boolean confirmationDateFound = false;

            if (fields != null) {
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject field = fields.getJSONObject(i);
                    String inferText = field.optString("inferText", "");

                    // inferText 값을 로그로 출력하여 확인
                    if (inferText.startsWith("연번호")) {
                        extractedData.put("registrationID", getNextInferText(fields, i + 1));
                    }
                    if (inferText.startsWith("국제질병분류번호:")) {
                        extractedData.put("diseaseInfoId", getNextInferText(fields, i + 1).replace(")", ""));
                        extractedData.put("diseaseInfoName", getPreviousInferText(fields, i - 1));
                    }
                    if (inferText.startsWith("발행일")) {
                        confirmationDateFound = true;
                    }
                    if (confirmationDateFound && inferText.matches("\\d{4}년")) {
                        confirmationDate.append(inferText);
                    }
                    if (confirmationDateFound && inferText.matches("\\d{1,2}월")) {
                        confirmationDate.append(inferText);
                    }
                    if (confirmationDateFound && inferText.matches("\\d{1,2}일")) {
                        confirmationDate.append(inferText);
                        confirmationDateFound = false;  // reset after collecting the full date
                    }
                    if (inferText.startsWith("의료기관")) {
                        extractedData.put("hospitalInfo", getNextInferText(fields, i + 1));
                    }
                }
            }
            extractedData.put("confirmationDate", confirmationDate.toString());
        } else {
            log.error("이미지가 없거나 공백입니다.");
        }
        return extractedData;
    }

    private String getNextInferText(JSONArray fields, int index) throws JSONException {
        if (index < fields.length()) {
            return fields.getJSONObject(index).optString("inferText", "").trim();
        }
        return "";
    }

    private String getPreviousInferText(JSONArray fields, int index) throws JSONException {
        if (index >= 0) {
            return fields.getJSONObject(index).optString("inferText", "").trim();
        }
        return "";
    }
}
