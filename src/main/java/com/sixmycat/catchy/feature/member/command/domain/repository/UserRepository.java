package com.sixmycat.catchy.feature.member.command.domain.repository;

import com.sixmycat.catchy.feature.member.command.domain.aggregate.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 고양이 정보까지 fetch (cats 컬렉션 fetch join)
    @EntityGraph(attributePaths = "cats")
    Optional<User> findById(Long id);
}

