package com.trainerservice.service;

import com.trainerservice.client.BlandAIClient;
import com.trainerservice.model.CallRecord;
import com.trainerservice.model.CallRequest;
import com.trainerservice.repository.CallRecordRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class BlandAIService {

    private static final Logger log = LoggerFactory.getLogger(BlandAIService.class);

    private final BlandAIClient blandAIClient;
    private final CallRecordRepository callRecordRepository;

    public BlandAIService(BlandAIClient blandAIClient,
                          CallRecordRepository callRecordRepository) {
        this.blandAIClient = blandAIClient;
        this.callRecordRepository = callRecordRepository;
    }

    /**
     * Initiates a phone call via the Bland AI API.
     *
     * @param request the call parameters
     * @return the API response body as a map
     */
    public Map<String, Object> initiateCall(CallRequest request) {
        Map<String, Object> callResponse;
        try {
            callResponse = blandAIClient.initiateCall(request);
        } catch (FeignException e) {
            throw new BlandAIException("Failed to initiate call via Bland AI: " + e.getMessage(), e);
        }

        if (callResponse != null) {
            Object rawCallId = callResponse.get("call_id");
            if (rawCallId == null) {
                log.warn("Bland AI response for phoneNumber={} did not contain a call_id; skipping record save",
                        request.getPhoneNumber());
            } else {
                String callId = String.valueOf(rawCallId);
                String status = String.valueOf(callResponse.getOrDefault("status", "queued"));
                CallRecord record = new CallRecord(
                        request.getPhoneNumber(),
                        callId,
                        status,
                        Instant.now().toString()
                );
                callRecordRepository.save(record);
            }
        }

        return callResponse;
    }

    /**
     * Retrieves details for a specific call from Bland AI.
     *
     * @param callId the Bland AI call identifier
     * @return the API response body as a map
     */
    public Map<String, Object> getCall(String callId) {
        try {
            return blandAIClient.getCall(callId);
        } catch (FeignException e) {
            throw new BlandAIException("Failed to retrieve call " + callId + " from Bland AI: " + e.getMessage(), e);
        }
    }

    /**
     * Returns all call records stored in the local database.
     */
    public List<CallRecord> getAllCallRecords() {
        return callRecordRepository.findAll();
    }
}
