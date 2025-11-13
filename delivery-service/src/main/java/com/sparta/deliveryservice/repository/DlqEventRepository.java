package com.sparta.deliveryservice.repository;

import com.sparta.deliveryservice.domain.DlqEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DlqEventRepository extends JpaRepository<DlqEvent, Long> {
    List<DlqEvent> findByRetryCountLessThan(int max);
}
