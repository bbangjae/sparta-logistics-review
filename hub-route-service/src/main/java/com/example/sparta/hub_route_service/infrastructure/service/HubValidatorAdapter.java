package com.example.sparta.hub_route_service.infrastructure.service;

import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import com.example.sparta.hub_route_service.domain.service.HubValidator;
import com.example.sparta.hub_route_service.infrastructure.client.HubClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubValidatorAdapter implements HubValidator {

    private final HubClient hubClient;

    @Override
    public boolean exists(UUID hubId) {
        return hubClient.existsHub(hubId);
    }

    @Override
    public void validateExists(UUID hubId) {
        if (!exists(hubId)) {
            throw new BusinessException(ErrorCode.HUB_NOT_FOUND);
        }
    }
}
