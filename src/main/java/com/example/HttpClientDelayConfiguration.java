package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "app.http")
@Data
public class HttpClientDelayConfiguration {

    private String host = "localhost";
    private Integer port = 8080;
    private String path;
    private Integer delayMillis = 10 * 1000;
    private String json;
}