package com.sixmycat.catchy.command.like.domain.repository;

import com.sixmycat.catchy.command.user.domain.aggregate.Entity_;
import com.sixmycat.catchy.command.user.domain.repository.EntityRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEntityRepository extends EntityRepository, JpaRepository<Entity_, Long> {
}
