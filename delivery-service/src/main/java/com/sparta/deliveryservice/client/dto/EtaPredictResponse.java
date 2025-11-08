package com.sparta.deliveryservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EtaPredictResponse {

    private LocalDateTime estimatedArrivalTime;
}
