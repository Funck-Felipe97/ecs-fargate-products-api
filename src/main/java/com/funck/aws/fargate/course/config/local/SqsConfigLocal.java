package com.funck.aws.fargate.course.config.local;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.Topic;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile("local")
public class SqsConfigLocal {

    private final String awsRegion;
    private final AmazonSNS amazonSNS;
    private final Topic topic;

    public SqsConfigLocal(@Value("${aws.region}") String awsRegion, AmazonSNS amazonSNS, @Qualifier("product-events-topic") Topic topic) {
        this.amazonSNS = amazonSNS;
        this.topic = topic;
        this.awsRegion = awsRegion;

        configureQueueSubscription();
    }

    private void configureQueueSubscription() {
        var amazonSQS = AmazonSQSClient.builder()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                "http://localhost:4566",
                                awsRegion))
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();

        var createTopicRequest = new CreateQueueRequest("products-events");

        var queueUrl = amazonSQS.createQueue(createTopicRequest).getQueueUrl();

        Topics.subscribeQueue(amazonSNS, amazonSQS, topic.getTopicArn(), queueUrl);
    }

}
