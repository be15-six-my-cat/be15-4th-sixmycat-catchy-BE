package com.sixmycat.catchy.common.s3.service;

import com.sixmycat.catchy.common.s3.dto.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3PresignedUrlService {

    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public PresignedUrlResponse generatePresignedUrl(String fileName) {
        // 1. 업로드 객체 요청 정의
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        // 2. Presigned URL 생성 요청 정의
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(
                PutObjectPresignRequest.builder()
                        .putObjectRequest(objectRequest)
                        .signatureDuration(Duration.ofMinutes(10)) // 유효 시간 설정
                        .build()
        );

        // 3. URL 반환
        return new PresignedUrlResponse(fileName, presignedRequest.url().toString());
    }
}
