package com.example.sparta.order_service.presentation.dto.request;

public record SearchCondition(
        int page,
        String sort,
        String state,
        String startDate,
        String endDate,
        String searchType,
        String keyword
) {
}
