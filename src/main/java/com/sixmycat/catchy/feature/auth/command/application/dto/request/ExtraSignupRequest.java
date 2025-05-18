package com.sixmycat.catchy.feature.auth.command.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtraSignupRequest {
    private String email;
    private String contactNumber;
    private String nickname;
    private String social;
    private String name;
}
