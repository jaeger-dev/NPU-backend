package com.jaeger.npu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Configuration {

    @Bean
    public S3Client s3CLient() {
        return S3Client.builder()
                .region(Region.EU_CENTRAL_2)
                .build();
    }
}
