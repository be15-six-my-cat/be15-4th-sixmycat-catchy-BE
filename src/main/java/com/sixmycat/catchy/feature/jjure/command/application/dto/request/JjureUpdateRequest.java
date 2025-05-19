package com.sixmycat.catchy.feature.jjure.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JjureUpdateRequest {

    private String caption;

    @NotBlank(message = "파일 키는 필수입니다.")
    private String fileKey;

    @NotNull(message = "쭈르 ID는 필수입니다.")
    private Long jjureId;
}
