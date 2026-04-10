package com.trainerservice.client;

import com.trainerservice.model.CallRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "bland-ai", url = "${bland.ai.api.base-url}")
public interface BlandAIClient {

    @PostMapping("/v1/calls")
    Map<String, Object> initiateCall(@RequestBody CallRequest request);

    @GetMapping("/v1/calls/{callId}")
    Map<String, Object> getCall(@PathVariable("callId") String callId);
}
