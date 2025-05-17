package com.sixmycat.catchy.common.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken implements Serializable {
    private String token;
}
