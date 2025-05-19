package com.sixmycat.catchy.feature.member.command.domain.repository;

import com.sixmycat.catchy.feature.member.command.domain.aggregate.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndDeletedAtIsNull(String email);

    boolean existsByNicknameAndDeletedAtIsNull(String nickname);

    @Query("SELECT m.profileImage FROM Member m WHERE m.id = :id AND m.deletedAt IS NULL")
    Optional<String> findProfileImageByIdAndDeletedAtIsNull(@Param("id") Long id);
}
