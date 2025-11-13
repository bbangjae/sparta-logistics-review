package com.example.sparta.hub_route_service.application.command;

import com.example.sparta.hub_route_service.presentation.request.UpdateHubConnectionRequest;
import java.util.UUID;

public record UpdateHubConnectionCommand(
    UUID departureHubId,
    UUID arrivalHubId,
    Double distanceKm,
    Integer estimatedMinutes
) {
    public static UpdateHubConnectionCommand from(UpdateHubConnectionRequest request) {
        return new UpdateHubConnectionCommand(
            request.departureHubId(),
            request.arrivalHubId(),
            request.distanceKm(),
            request.estimatedMinutes()
        );
    }
}
