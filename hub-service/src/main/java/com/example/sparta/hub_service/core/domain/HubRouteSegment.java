package com.example.sparta.hub_service.core.domain;

import com.example.sparta.common.model.BaseEntity;
import com.example.sparta.hub_service.core.vo.HubConnectionId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_hub_route_segments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubRouteSegment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "route_segment_id", nullable = false, updatable = false)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "hub_connection_id", nullable = false))
    private HubConnectionId hubConnectionId;

    @Column(name = "segment_sequence", nullable = false)
    private Integer segmentSequence;

    public static HubRouteSegment create(
        HubConnectionId hubConnectionId,
        Integer segmentSequence
    ) {
        HubRouteSegment segment = new HubRouteSegment();
        segment.hubConnectionId = hubConnectionId;
        segment.segmentSequence = segmentSequence;
        return segment;
    }
}
