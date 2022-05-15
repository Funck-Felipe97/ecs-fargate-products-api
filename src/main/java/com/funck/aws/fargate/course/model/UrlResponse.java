package com.funck.aws.fargate.course.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlResponse {

    private String url;
    private long expirationTime;

}
