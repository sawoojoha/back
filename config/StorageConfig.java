package com.exameple.mytube.config;

import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Bean
    public com.google.cloud.storage.Storage storage() {
        return StorageOptions.getDefaultInstance().getService();
    }
}
