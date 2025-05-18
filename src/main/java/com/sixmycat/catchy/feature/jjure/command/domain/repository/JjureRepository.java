package com.sixmycat.catchy.feature.jjure.command.domain.repository;

import com.sixmycat.catchy.feature.jjure.command.domain.aggregate.Jjure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JjureRepository extends JpaRepository<Jjure, Long> {
}

