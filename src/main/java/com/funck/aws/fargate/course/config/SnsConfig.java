package com.funck.aws.fargate.course.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.Topic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prd")
public class SnsConfig {

    private final String awsRegion;
    private final String productEventsTopic;

    public SnsConfig(final @Value("${aws.region}") String awsRegion, final @Value("${aws.sns.topic.products.events}") String productEventsTopic) {
        this.awsRegion = awsRegion;
        this.productEventsTopic = productEventsTopic;
    }

    @Bean
    public AmazonSNS snsClient() {
        return AmazonSNSClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }

    @Bean("product-events-topic")
    public Topic snsProductsEventsTopic() {
        return new Topic().withTopicArn(productEventsTopic);
    }

}
