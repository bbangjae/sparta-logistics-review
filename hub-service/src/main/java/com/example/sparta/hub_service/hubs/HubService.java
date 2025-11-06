package com.example.sparta.hub_service.hubs;

import com.example.sparta.common.exception.BusinessException;
import com.example.sparta.common.exception.ErrorCode;
import com.example.sparta.hub_service.core.domain.Hub;
import com.example.sparta.hub_service.core.enums.HubCode;
import com.example.sparta.hub_service.core.enums.HubStatus;
import com.example.sparta.hub_service.core.vo.HubAddress;
import com.example.sparta.hub_service.core.vo.Location;
import com.example.sparta.hub_service.hubs.dto.CreateHubCommand;
import com.example.sparta.hub_service.hubs.dto.UpdateHubCommand;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubService {

    private final HubRepository hubRepository;


    @Transactional
    public UUID createHub(CreateHubCommand command) {

        HubCode hubCode = HubCode.of(command.code());

        if (hubRepository.existsByCode(hubCode)) {
            throw new IllegalStateException("이미 존재하는 허브 코드입니다");
        }

        HubAddress hubAddress = HubAddress.of(
            command.address()
        );

        Location hubLocation = Location.of(
            command.latitude(),
            command.longitude()
        );

        Hub hub = Hub.create(
            hubCode,
            command.name(),
            hubAddress,
            hubLocation
        );

        Hub savedHub = hubRepository.save(hub);

        return savedHub.getId();
    }

    public HubResult getHub(UUID hubId) {
        Hub hub = getHubById(hubId);

        return HubResult.from(hub);
    }

    @Transactional
    public void updateHubService(UUID hubId, UpdateHubCommand command) {
        Hub hub = getHubById(hubId);

        HubCode hubCode = HubCode.of(command.code());

        HubAddress hubAddress = HubAddress.of(
            command.address()
        );

        HubStatus hubStatus = HubStatus.from(command.status());

        Location hubLocation = Location.of(
            command.latitude(),
            command.longitude()
        );

        hub.update(
            hubCode,
            command.name(),
            hubStatus,
            hubAddress,
            hubLocation
        );

    }

    @Transactional
    public void deleteHub(UUID hubId, Long userId) {
        Hub hub = getHubById(hubId);

        hub.delete(userId);
    }

    private Hub getHubById(UUID hubId) {
        return hubRepository.findById(hubId)
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));
    }
}
