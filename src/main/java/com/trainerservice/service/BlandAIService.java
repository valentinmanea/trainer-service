package com.trainerservice.service;

import com.trainerservice.config.BlandAIConfig;
import com.trainerservice.model.CallRecord;
import com.trainerservice.model.CallRequest;
import com.trainerservice.repository.CallRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class BlandAIService {

    private static final Logger log = LoggerFactory.getLogger(BlandAIService.class);

    private final RestTemplate restTemplate;
    private final BlandAIConfig blandAIConfig;
    private final CallRecordRepository callRecordRepository;

    public BlandAIService(RestTemplate restTemplate,
                          BlandAIConfig blandAIConfig,
                          CallRecordRepository callRecordRepository) {
        this.restTemplate = restTemplate;
        this.blandAIConfig = blandAIConfig;
        this.callRecordRepository = callRecordRepository;
    }

    /**
     * Initiates a phone call via the Bland AI API.
     *
     * @param request the call parameters
     * @return the API response body as a map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> initiateCall(CallRequest request) {
        HttpHeaders headers = buildHeaders();
        HttpEntity<CallRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response;
        try {
            response = restTemplate.exchange(
                    blandAIConfig.getBaseUrl() + "/v1/calls",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );
        } catch (RestClientException e) {
            throw new BlandAIException("Failed to initiate call via Bland AI: " + e.getMessage(), e);
        }

        Map<String, Object> body = response.getBody();

        if (body != null) {
            Object rawCallId = body.get("call_id");
            if (rawCallId == null) {
                log.warn("Bland AI response for phoneNumber={} did not contain a call_id; skipping record save",
                        request.getPhoneNumber());
            } else {
                String callId = String.valueOf(rawCallId);
                String status = String.valueOf(body.getOrDefault("status", "queued"));
                CallRecord record = new CallRecord(
                        request.getPhoneNumber(),
                        callId,
                        status,
                        Instant.now().toString()
                );
                callRecordRepository.save(record);
            }
        }

        return body;
    }

    /**
     * Retrieves details for a specific call from Bland AI.
     *
     * @param callId the Bland AI call identifier
     * @return the API response body as a map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getCall(String callId) {
        HttpHeaders headers = buildHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response;
        try {
            response = restTemplate.exchange(
                    blandAIConfig.getBaseUrl() + "/v1/calls/" + callId,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );
        } catch (RestClientException e) {
            throw new BlandAIException("Failed to retrieve call " + callId + " from Bland AI: " + e.getMessage(), e);
        }

        return response.getBody();
    }

    /**
     * Returns all call records stored in the local database.
     */
    public List<CallRecord> getAllCallRecords() {
        return callRecordRepository.findAll();
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", blandAIConfig.getApiKey());
        return headers;
    }
}
