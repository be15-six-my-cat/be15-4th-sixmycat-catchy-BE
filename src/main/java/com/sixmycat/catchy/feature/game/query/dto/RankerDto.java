package com.sixmycat.catchy.feature.game.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankerDto {
    private final int rank;
    private final String nickname;
    private final int score;
}
