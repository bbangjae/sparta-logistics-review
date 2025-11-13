package com.sparta.deliveryservice.repository;

import com.sparta.deliveryservice.domain.DlqEvent;
import com.sparta.deliveryservice.domain.DlqQueueLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DlqQueueLogRepository extends JpaRepository<DlqQueueLog, Long> {
}
