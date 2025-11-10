package com.sparta.user_service.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.user_service.domain.entity.QUserEntity;
import com.sparta.user_service.domain.entity.UserEntity;
import com.example.sparta.common.enums.UserRoleEnum;
import com.sparta.user_service.domain.enums.UserStatusEnum;
import com.sparta.user_service.domain.repository.UserRepository;
import com.sparta.user_service.presentation.response.UserSearchResponse;
import lombok.RequiredArgsConstructor;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final QUserEntity user = QUserEntity.userEntity;

    @Override
    public UserEntity save(UserEntity user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public Optional<UserEntity> findById(UUID userid) {
        return userJpaRepository.findById(userid);
    }

    @Override
    public Page<UserSearchResponse> searchUsers(
            String name,
            String slackId,
            UserRoleEnum role,
            UserStatusEnum status,
            Pageable pageable
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        // Soft Delete 조건
        builder.and(user.deletedAt.isNull());

        if (StringUtils.hasText(name)) {
            builder.and(user.name.containsIgnoreCase(name));
        }

        if (StringUtils.hasText(slackId)) {
            builder.and(user.slackId.containsIgnoreCase(slackId));
        }

        if (role != null) {
            builder.and(user.role.eq(role));
        }

        if (status != null) {
            builder.and(user.status.eq(status));
        }

        // 조회 쿼리
        List<UserEntity> entities  = queryFactory
                .selectFrom(user)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getSortOrderSpecifiers(pageable))
                .fetch();

        List<UserSearchResponse> content = entities.stream()
                .map(UserSearchResponse::of)
                .toList();


        // 전체 건수 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(user.count())
                .from(user)
                .where(builder);

        Long total = countQuery.fetchOne();
        if (total == null) total = 0L;

        return new PageImpl<>(content, pageable, total);
    }

    private OrderSpecifier<?>[] getSortOrderSpecifiers(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return new OrderSpecifier[]{user.createdAt.desc(), user.updatedAt.desc()};
        }

        return pageable.getSort().stream()
                .map(s -> {
                    com.querydsl.core.types.Order direction = s.isAscending() ?
                            com.querydsl.core.types.Order.ASC :
                            com.querydsl.core.types.Order.DESC;

                    return switch (s.getProperty()) {
                        case "name" -> new OrderSpecifier<>(direction, user.name);
                        case "slackId" -> new OrderSpecifier<>(direction, user.slackId);
                        case "role" -> new OrderSpecifier<>(direction, user.role);
                        case "status" -> new OrderSpecifier<>(direction, user.status);
                        case "updatedAt" -> new OrderSpecifier<>(direction, user.updatedAt);
                        default -> new OrderSpecifier<>(direction, user.createdAt);
                    };
                })
                .toArray(OrderSpecifier[]::new);
    }
}
