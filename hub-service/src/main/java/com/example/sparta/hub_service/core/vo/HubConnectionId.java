package com.example.sparta.hub_service.core.vo;

import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubConnectionId {

    private UUID id;

    private HubConnectionId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException(
                "유효하지 않은 HubConnectionID 입니다"
            );
        }
        this.id = id;
    }

    public static HubConnectionId of(UUID id) {
        return new HubConnectionId(id);
    }
}
