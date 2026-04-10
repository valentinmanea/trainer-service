package com.trainerservice;

import com.trainerservice.model.CallRecord;
import com.trainerservice.repository.CallRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TrainerServiceApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CallRecordRepository callRecordRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void h2DatabaseIsReachableAndCallRecordCanBeSaved() {
        CallRecord record = new CallRecord("+1234567890", "test-call-id", "queued", "2024-01-01T00:00:00Z");
        CallRecord saved = callRecordRepository.save(record);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPhoneNumber()).isEqualTo("+1234567890");
        assertThat(saved.getCallId()).isEqualTo("test-call-id");
    }

    @Test
    void getCallRecordsEndpointReturns200() {
        var response = restTemplate.getForEntity("/api/bland/records", List.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
