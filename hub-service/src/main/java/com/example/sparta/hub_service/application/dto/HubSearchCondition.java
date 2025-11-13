package com.example.sparta.hub_service.application.dto;

import com.example.sparta.hub_service.domain.vo.HubCode;
import com.example.sparta.hub_service.domain.vo.HubStatus;

public record HubSearchCondition(
    String name,
    HubCode code,
    HubStatus status,
    String address
) {
    public static HubSearchCondition of(
        String name,
        String code,
        String status,
        String address
    ) {
        HubCode hubCode = null;
        if (code != null && !code.trim().isEmpty()) {
            hubCode = HubCode.of(code);
        }

        HubStatus hubStatus = null;
        if (status != null && !status.trim().isEmpty()) {
            hubStatus = HubStatus.from(status);
        }

        return new HubSearchCondition(name, hubCode, hubStatus, address);
    }
}
