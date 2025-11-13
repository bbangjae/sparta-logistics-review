package com.example.sparta.hub_route_service.infrastructure.repository;

import com.example.sparta.hub_route_service.domain.entity.HubRoute;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubRouteRepository extends JpaRepository<HubRoute, UUID>,
    HubRouteRepositoryCustom {}
