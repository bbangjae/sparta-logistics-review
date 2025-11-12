package com.example.sparta.hub_service.hub_routes.dto;

import com.example.sparta.hub_service.core.domain.HubConnection;
import java.util.UUID;

public record HubConnectionResult(
    UUID hubConnectionId,
    UUID departureHubId,
    UUID arrivalHubId,
    Double distanceKm,
    Integer estimatedMinutes
) implements java.io.Serializable {
    public static HubConnectionResult from(HubConnection HubConnection) {
        return new HubConnectionResult(
            HubConnection.getId(),
            HubConnection.getDepartureHubId().getId(),
            HubConnection.getArrivalHubId().getId(),
            HubConnection.getDistanceKm().getDistance(),
            HubConnection.getEstimatedMinutes().getDuration()
        );
    }
}
