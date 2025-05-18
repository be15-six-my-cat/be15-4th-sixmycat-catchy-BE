package com.sixmycat.catchy.feature.jjure.command.application.controller;

import com.sixmycat.catchy.common.s3.dto.PresignedUrlResponse;
import com.sixmycat.catchy.common.s3.service.S3PresignedUrlService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jjure/upload")
@Tag(name = "", description = "")
public class JjureCommandController {
    private final S3PresignedUrlService presignedUrlService;

    @GetMapping("/presigned-url")
    public ResponseEntity<PresignedUrlResponse> getPresignedUrl(@RequestParam String fileName) {
        return ResponseEntity.ok(presignedUrlService.generatePresignedUrl(fileName));
    }

}
