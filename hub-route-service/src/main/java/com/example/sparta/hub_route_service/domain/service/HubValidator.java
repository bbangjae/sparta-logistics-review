package com.example.sparta.hub_route_service.domain.service;

import java.util.UUID;

public interface HubValidator {
    boolean exists(UUID hubId);
    void validateExists(UUID hubId);
}
