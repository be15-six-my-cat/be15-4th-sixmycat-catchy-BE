package com.sixmycat.catchy.common.s3.dto;

public record PresignedUrlResponse(
        String fileName,
        String presignedUrl
) {}