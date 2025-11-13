package com.example.sparta.hub_route_service.presentation.request;

import java.util.UUID;

public record UpdateHubConnectionRequest(
    UUID departureHubId,
    UUID arrivalHubId,
    Double distanceKm,
    Integer estimatedMinutes
) {}
