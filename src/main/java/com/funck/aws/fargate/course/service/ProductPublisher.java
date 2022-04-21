package com.funck.aws.fargate.course.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.Topic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.funck.aws.fargate.course.model.Envelope;
import com.funck.aws.fargate.course.model.EventType;
import com.funck.aws.fargate.course.model.Product;
import com.funck.aws.fargate.course.model.ProductEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductPublisher {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired(required = false)
    private AmazonSNS amazonSNS;

    @Qualifier("product-events-topic")
    @Autowired(required = false)
    private Topic topic;

    public void publishProductEvent(Product product, EventType type) {
        try {
            var event = new ProductEvent(product.getId(), product.getCode());

            var envelope = new Envelope(type, MAPPER.writeValueAsString(event));

            amazonSNS.publish(topic.getTopicArn(), MAPPER.writeValueAsString(envelope));
        } catch (Exception e) {
            log.error("Error on publish topic event {} {} ", product, type, e);
        }
    }

}
