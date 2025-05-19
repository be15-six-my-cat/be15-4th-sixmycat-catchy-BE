package com.sixmycat.catchy.feature.member.query.dto.response;

import java.time.LocalDate;

public record CatResponse(
        Long id,
        String name,
        String gender,
        String breed,
        LocalDate birthDay,
        Integer age
) {}
