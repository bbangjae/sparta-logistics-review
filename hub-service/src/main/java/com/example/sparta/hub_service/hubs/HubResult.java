package com.example.sparta.hub_service.hubs;

import com.example.sparta.hub_service.core.domain.Hub;
import com.example.sparta.hub_service.core.enums.HubCode;
import com.example.sparta.hub_service.core.enums.HubStatus;
import com.example.sparta.hub_service.core.vo.HubAddress;
import java.math.BigDecimal;
import java.util.UUID;

public record HubResult(
    UUID hubId,
    HubCode code,
    String name,
    HubAddress address,
    HubStatus status,
    BigDecimal latitude,
    BigDecimal longitude
) {
    public static HubResult from(Hub hub) {
        return new HubResult(
            hub.getId(),
            hub.getCode(),
            hub.getName(),
            hub.getAddress(),
            hub.getStatus(),
            hub.getHubLocation().getLatitude(),
            hub.getHubLocation().getLatitude()
        );
    }
}
