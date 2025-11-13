package com.example.sparta.hub_route_service.infrastructure.messaging.event;

import com.example.sparta.hub_route_service.application.dto.HubRouteResult;
import com.example.sparta.hub_route_service.presentation.response.SegmentResponse;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponseEvent implements Serializable {

    private UUID routeId;
    private UUID departureHubId;
    private UUID arrivalHubId;
    private double totalDistanceKm;
    private int totalDurationMinutes;
    private List<SegmentResponse> segments;

    public static RouteResponseEvent from(HubRouteResult result) {
        return new RouteResponseEvent(
            result.routeId(),
            result.departureHubId(),
            result.arrivalHubId(),
            result.totalDistanceKm(),
            result.totalDurationMinutes(),
            result.segments()
        );
    }
}
