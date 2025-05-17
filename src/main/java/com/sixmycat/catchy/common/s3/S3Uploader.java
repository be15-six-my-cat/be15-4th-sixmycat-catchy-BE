package com.sixmycat.catchy.common.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private final String bucket;
    private final String cloudFrontDomain;

    public S3Uploader(
            AmazonS3 amazonS3,
            @Value("${cloud.aws.s3.bucket}") String bucket,
            @Value("${cloud.aws.cloudfront.domain}") String cloudFrontDomain
    ) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
        this.cloudFrontDomain = cloudFrontDomain;
    }

    public String uploadFile(MultipartFile file, String dirName) {
        String fileName = generateFileName(dirName, file.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }

        // CloudFront 기반 URL로 반환
        return "https://" + cloudFrontDomain + "/" + fileName;
    }

    public List<String> uploadFiles(List<MultipartFile> files, String dirName) {
        List<String> uploadedUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            uploadedUrls.add(uploadFile(file, dirName));
        }
        return uploadedUrls;
    }

    private String generateFileName(String dirName, String originalName) {
        return dirName + "/" + UUID.randomUUID() + "-" + originalName;
    }
}