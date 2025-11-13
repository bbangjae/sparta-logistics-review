package com.example.sparta.hub_route_service.infrastructure.repository;

import com.example.sparta.hub_route_service.domain.entity.HubConnection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubConnectionRepository extends JpaRepository<HubConnection, UUID> {

    List<HubConnection> findAllByDeletedAtIsNull();
}
