package com.sixmycat.catchy.feature.member.command.domain.repository;

import com.sixmycat.catchy.feature.member.command.domain.aggregate.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 나를 팔로우한 사람 수
    int countByFollowing_Id(Long userId);

    // 내가 팔로우한 사람 수
    int countByFollower_Id(Long userId);
}

