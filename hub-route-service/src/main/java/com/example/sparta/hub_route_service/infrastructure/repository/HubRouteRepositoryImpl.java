package com.example.sparta.hub_route_service.infrastructure.repository;

import static com.example.sparta.hub_route_service.domain.entity.QHubRoute.hubRoute;
import static com.example.sparta.hub_route_service.domain.entity.QHubRouteSegment.hubRouteSegment;

import com.example.sparta.hub_route_service.application.dto.HubRouteResult;
import com.example.sparta.hub_route_service.domain.vo.HubId;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HubRouteRepositoryImpl implements HubRouteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<HubRouteResult> findDetailedRouteBetween(
        HubId departureHubId,
        HubId arrivalHubId
    ) {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(hubRoute).distinct()
                .leftJoin(hubRoute.hubRouteSegments, hubRouteSegment).fetchJoin()
                .leftJoin(hubRouteSegment.hubConnection).fetchJoin()
                .where(
                    hubRoute.departureHubId.eq(departureHubId),
                    hubRoute.arrivalHubId.eq(arrivalHubId),
                    hubRoute.deletedAt.isNull()
                )
                .fetchOne())
            .map(HubRouteResult::from);
    }
}
