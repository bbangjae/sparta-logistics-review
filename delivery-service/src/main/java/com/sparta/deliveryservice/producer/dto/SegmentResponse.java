package com.sparta.deliveryservice.producer.dto;


import com.sparta.deliveryservice.client.dto.RouteInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import javax.swing.text.Segment;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;


@Value
@Getter
@Builder
@AllArgsConstructor
public class SegmentResponse {

    int sequence;
    UUID hubConnectionId;
    UUID departureHubId;
    UUID arrivalHubId;
    double distanceKm;
    int durationMinutes;

    public static RouteInfoResponse toRouteInfoResponse(SegmentResponse segmentResponse) {
        return new RouteInfoResponse(
                segmentResponse.departureHubId,
                segmentResponse.arrivalHubId,
                segmentResponse.distanceKm,
                segmentResponse.durationMinutes
        );
    }
}
