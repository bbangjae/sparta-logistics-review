package com.example.sparta.hub_route_service.infrastructure.repository;


import com.example.sparta.hub_route_service.application.dto.HubRouteResult;
import com.example.sparta.hub_route_service.domain.vo.HubId;
import java.util.Optional;

public interface HubRouteRepositoryCustom {
    Optional<HubRouteResult> findDetailedRouteBetween(HubId departureHubId, HubId arrivalHubId);
}