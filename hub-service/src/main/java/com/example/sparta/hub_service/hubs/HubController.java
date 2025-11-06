package com.example.sparta.hub_service.hubs;

import com.example.sparta.hub_service.hubs.dto.CreateHubCommand;
import com.example.sparta.hub_service.hubs.dto.CreateHubRequest;
import com.example.sparta.hub_service.hubs.dto.HubCreateResponse;
import com.example.sparta.hub_service.hubs.dto.HubDetailResponse;
import com.example.sparta.hub_service.hubs.dto.UpdateHubCommand;
import com.example.sparta.hub_service.hubs.dto.UpdateHubRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    @PostMapping
    public ResponseEntity<HubCreateResponse> createHub(@Valid @RequestBody CreateHubRequest request) {

        CreateHubCommand command = new CreateHubCommand(
            request.code(),
            request.name(),
            request.address(),
            request.latitude(),
            request.longitude()
        );

        UUID hubId = hubService.createHub(command);

        HubCreateResponse response = new HubCreateResponse(
            hubId,
            "허브가 성공적으로 생성되었습니다."
        );

        URI location = URI.create("/hubs/" + hubId);

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("{hubId}")
    public ResponseEntity<HubDetailResponse> getHub(
        @PathVariable UUID hubId
    ) {

        HubDetailResponse response = HubDetailResponse.from(
            hubService.getHub(hubId)
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{hubId}")
    public ResponseEntity<Void> updateHub(
        @PathVariable UUID hubId,
        @RequestBody UpdateHubRequest request
    ) {

        UpdateHubCommand command = new UpdateHubCommand(
            request.code(),
            request.name(),
            request.address(),
            request.status(),
            request.latitude(),
            request.longitude()
        );

        hubService.updateHubService(hubId, command);

        return ResponseEntity.ok().build();

    }

    /// TODO 유저 ID 받아오기
    @DeleteMapping("/{hubId}")
    public ResponseEntity<Void> deleteHub(
        @PathVariable UUID hubId,
        Long userId
    ) {
        userId = 1L;

        hubService.deleteHub(hubId, userId);

        return ResponseEntity.noContent().build();
    }
}
