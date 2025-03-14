package com.aleksandrmakarov.springbootcloudstorage.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio")
@NoArgsConstructor
@Getter
@Setter
public class MinioProperties {

    private String bucketName;
    private String endpoint;
    private String secretKey;
    private String accessKey;
}
