package com.funck.aws.fargate.course.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SnsMessage {

    private final String message;
    private final String type;
    private final String topicArn;
    private final String timestamp;
    private final String messageId;

    public SnsMessage(
            @JsonProperty("Message") String message,
            @JsonProperty("Type") String type,
            @JsonProperty("TopicArn") String topicArn,
            @JsonProperty("Timestamp") String timestamp,
            @JsonProperty("MessageId") String messageId) {
        this.message = message;
        this.type = type;
        this.topicArn = topicArn;
        this.timestamp = timestamp;
        this.messageId = messageId;
    }
}
