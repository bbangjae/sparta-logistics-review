package com.example.sparta.hub_route_service.presentation.request;

import java.util.UUID;

public record HubConnectionRequest(
    UUID departureHubId,
    UUID arrivalHubId,
    Double distanceKm,
    Integer estimatedMinutes
) {}
