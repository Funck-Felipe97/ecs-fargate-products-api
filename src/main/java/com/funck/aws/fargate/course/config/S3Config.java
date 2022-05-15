package com.funck.aws.fargate.course.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    private final String awsRegion;

    public S3Config(final @Value("${aws.region}") String awsRegion) {
        this.awsRegion = awsRegion;
    }

    @Bean
    public AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }

}
