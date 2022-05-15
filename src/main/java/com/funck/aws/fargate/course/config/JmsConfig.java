package com.funck.aws.fargate.course.config;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.Session;

@Profile("!local")
@EnableJms
@Configuration
public class JmsConfig {

    private final String awsRegion;

    public JmsConfig(@Value("${aws.region}") String awsRegion) {
        this.awsRegion = awsRegion;
    }

    @Bean
    public SQSConnectionFactory connectionFactory() {
        return new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.standard()
                        .withRegion(awsRegion)
                        .withCredentials(new DefaultAWSCredentialsProviderChain())
                        .build());
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(SQSConnectionFactory connectionFactory) {
        var jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();

        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
        jmsListenerContainerFactory.setDestinationResolver(new DynamicDestinationResolver());
        jmsListenerContainerFactory.setConcurrency("2");
        jmsListenerContainerFactory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);

        return jmsListenerContainerFactory;
    }

    @Bean
    public JmsTemplate defaultJmsTemplate(SQSConnectionFactory connectionFactory){
        return new JmsTemplate(connectionFactory);
    }

}