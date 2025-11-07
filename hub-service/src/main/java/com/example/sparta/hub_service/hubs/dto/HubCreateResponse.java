package com.example.sparta.hub_service.hubs.dto;

import java.util.UUID;

public record HubCreateResponse(
    UUID hubId,
    String message
) {}
