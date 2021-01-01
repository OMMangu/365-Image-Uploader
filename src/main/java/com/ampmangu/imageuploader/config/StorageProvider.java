package com.ampmangu.imageuploader.config;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class StorageProvider {
    @Value("${gcp.project}")
    private String projectId;

    @Autowired
    private Environment environment;

    @Bean
    public Storage getGcpStorage() throws IOException {
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(environment.getProperty("GOOGLE_APPLICATION_CREDENTIALS")));
        return StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();
    }
}
