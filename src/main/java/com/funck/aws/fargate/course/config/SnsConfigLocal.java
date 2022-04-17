package com.funck.aws.fargate.course.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile("test")
public class SnsConfigLocal {

    private final String awsRegion;
    private final String productEventsTopic;

    public SnsConfigLocal(@Value("${aws.region}") String awsRegion, @Value("${aws.sns.topic.products.events}") String productEventsTopic) {
        this.awsRegion = awsRegion;
        this.productEventsTopic = productEventsTopic;
    }

    @Bean
    public AmazonSNS snsClient() {
        log.info("Configuring AmazonSNS in local profile");

        return AmazonSNSClient.builder()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                "http://localhost:4566",
                                awsRegion))
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }

    @Bean("product-events-topic")
    public Topic snsProductsEventsTopic(AmazonSNS amazonSNS) {
        log.info("Configuring Topic in local profile");

        var createTopicRequest = new CreateTopicRequest(productEventsTopic);

        var topicArn = amazonSNS.createTopic(createTopicRequest).getTopicArn();

        return new Topic().withTopicArn(topicArn);
    }

}
