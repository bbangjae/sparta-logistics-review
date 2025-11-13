package com.example.sparta.hub_route_service.application;

import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import com.example.sparta.hub_route_service.application.command.HubConnectionCommand;
import com.example.sparta.hub_route_service.application.command.UpdateHubConnectionCommand;
import com.example.sparta.hub_route_service.application.dto.HubConnectionResult;
import com.example.sparta.hub_route_service.domain.entity.HubConnection;
import com.example.sparta.hub_route_service.domain.service.HubValidator;
import com.example.sparta.hub_route_service.domain.vo.Distance;
import com.example.sparta.hub_route_service.domain.vo.Duration;
import com.example.sparta.hub_route_service.domain.vo.HubId;
import com.example.sparta.hub_route_service.infrastructure.repository.HubConnectionRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HubConnectionService {

    private final HubValidator hubValidator;
    private final HubConnectionRepository hubConnectionRepository;

    @Transactional
    @Caching(
        evict = {
            @CacheEvict(cacheNames = "hubConnections", allEntries = true),
            @CacheEvict(cacheNames = "hubRoute", allEntries = true), // 경로 캐시도 무효화
        }
    )
    public UUID createConnection(HubConnectionCommand command) {
        hubValidator.validateExists(command.departureHubId());
        hubValidator.validateExists(command.arrivalHubId());

        HubConnection connection = HubConnection.create(
            HubId.of(command.departureHubId()),
            HubId.of(command.arrivalHubId()),
            Distance.of(command.distanceKm()),
            Duration.of(command.estimatedMinutes())
        );

        HubConnection savedHubConnection = hubConnectionRepository.save(
            connection
        );

        return savedHubConnection.getId();
    }

    @Cacheable(cacheNames = "hubConnections", key = "methodName")
    public List<HubConnectionResult> getConnections() {
        return hubConnectionRepository
            .findAllByDeletedAtIsNull()
            .stream()
            .map(HubConnectionResult::from)
            .toList();
    }

    @Cacheable(cacheNames = "hubConnection", key = "#hubConnectionId")
    public HubConnectionResult getConnection(UUID hubConnectionId) {
        return HubConnectionResult.from(getHubConnectionById(hubConnectionId));
    }

    @Transactional
    @Caching(
        evict = {
            @CacheEvict(cacheNames = "hubConnection", key = "#hubConnectionId"),
            @CacheEvict(cacheNames = "hubConnections", allEntries = true),
            @CacheEvict(cacheNames = "hubRoute", allEntries = true),
        }
    )
    public void updateConnection(
        UUID hubConnectionId,
        UpdateHubConnectionCommand command
    ) {
        HubConnection hubConnection = getHubConnectionById(hubConnectionId);

        hubValidator.validateExists(command.departureHubId());
        hubValidator.validateExists(command.arrivalHubId());

        hubConnection.update(
            HubId.of(command.departureHubId()),
            HubId.of(command.arrivalHubId()),
            Distance.of(command.distanceKm()),
            Duration.of(command.estimatedMinutes())
        );
    }

    @Transactional
    @Caching(
        evict = {
            @CacheEvict(cacheNames = "hubConnection", key = "#hubConnectionId"),
            @CacheEvict(cacheNames = "hubConnections", allEntries = true),
            @CacheEvict(cacheNames = "hubRoute", allEntries = true),
        }
    )

    public void deleteHubConnection(UUID hubConnectionId, Long userId) {
        HubConnection hubConnection = getHubConnectionById(hubConnectionId);

        hubConnection.delete(userId);
    }

    private HubConnection getHubConnectionById(UUID hubConnectionId) {
        return hubConnectionRepository
            .findById(hubConnectionId)
            .orElseThrow(() ->
                new BusinessException(ErrorCode.HUB_CONNECTION_NOT_FOUND)
            );
    }
}
