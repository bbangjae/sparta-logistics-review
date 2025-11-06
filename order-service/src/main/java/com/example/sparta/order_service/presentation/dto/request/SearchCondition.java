package com.example.sparta.order_service.presentation.dto.request;

public record SearchCondition(
        String state,
        String startDate,
        String endDate,
        String searchType,
        String keyword
) {
}
