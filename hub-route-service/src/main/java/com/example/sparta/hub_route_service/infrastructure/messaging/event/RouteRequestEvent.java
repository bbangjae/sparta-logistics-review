package com.example.sparta.hub_route_service.infrastructure.messaging.event;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RouteRequestEvent implements Serializable {

    private UUID departureHubId;
    private UUID arrivalHubId;
}
