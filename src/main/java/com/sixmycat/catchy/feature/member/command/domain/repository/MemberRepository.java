package com.sixmycat.catchy.feature.member.command.domain.repository;

import com.sixmycat.catchy.feature.member.command.domain.aggregate.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    // 고양이까지 fetch join 하는 id 조회
    @EntityGraph(attributePaths = "cats")
    Optional<Member> findById(Long id);
}
