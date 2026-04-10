package com.trainerservice.repository;

import com.trainerservice.model.CallRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallRecordRepository extends JpaRepository<CallRecord, Long> {

    List<CallRecord> findByPhoneNumber(String phoneNumber);

    List<CallRecord> findByStatus(String status);
}
