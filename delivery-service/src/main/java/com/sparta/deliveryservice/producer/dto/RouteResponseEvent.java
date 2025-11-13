package com.sparta.deliveryservice.producer.dto;

import com.sparta.deliveryservice.client.dto.RouteInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Value
@Getter
@Builder
@AllArgsConstructor
public class RouteResponseEvent {

    UUID routeId;
    UUID departureHubId;
    UUID arrivalHubId;
    double totalDistanceKm;
    int totalDurationMinutes;
    List<SegmentResponse> segments;

}

