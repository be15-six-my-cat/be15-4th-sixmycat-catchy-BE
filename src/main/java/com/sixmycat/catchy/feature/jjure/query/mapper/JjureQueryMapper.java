package com.sixmycat.catchy.feature.jjure.query.mapper;

import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureCommentResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JjureQueryMapper {

    List<JjureCommentResponse> findCommentsByJjureId(@Param("jjureId") Long jjureId);

    boolean existsByJjureId(@Param("jjureId") Long jjureId);
}
