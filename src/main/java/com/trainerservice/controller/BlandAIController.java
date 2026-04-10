package com.trainerservice.controller;

import com.trainerservice.model.CallRecord;
import com.trainerservice.model.CallRequest;
import com.trainerservice.service.BlandAIException;
import com.trainerservice.service.BlandAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bland")
public class BlandAIController {

    private final BlandAIService blandAIService;

    public BlandAIController(BlandAIService blandAIService) {
        this.blandAIService = blandAIService;
    }

    /**
     * POST /api/bland/calls
     * Initiates a phone call via Bland AI.
     */
    @PostMapping("/calls")
    public ResponseEntity<Map<String, Object>> initiateCall(@RequestBody CallRequest request) {
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Map<String, Object> response = blandAIService.initiateCall(request);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(BlandAIException.class)
    public ResponseEntity<Map<String, String>> handleBlandAIException(BlandAIException ex) {
        return ResponseEntity.status(502).body(Map.of("error", ex.getMessage()));
    }

    /**
     * GET /api/bland/calls/{callId}
     * Retrieves details for a specific call from Bland AI.
     */
    @GetMapping("/calls/{callId}")
    public ResponseEntity<Map<String, Object>> getCall(@PathVariable String callId) {
        Map<String, Object> response = blandAIService.getCall(callId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/bland/records
     * Lists all call records stored in the local database.
     */
    @GetMapping("/records")
    public ResponseEntity<List<CallRecord>> getCallRecords() {
        return ResponseEntity.ok(blandAIService.getAllCallRecords());
    }
}
