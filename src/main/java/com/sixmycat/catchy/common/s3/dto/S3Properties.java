package com.sixmycat.catchy.common.s3.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloud.aws.s3")
@Data
public class S3Properties {
    private String bucket;
    private String accessKey;
    private String secretKey;
    private String region;
}

