package org.dbp.hackaton.hackaton1.config.ai;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureAIConfig {
    @Value("${azure.ai.endpoint}")
    private String endpoint;

    @Value("${azure.ai.key}")
    private String key;

    @Bean
    public ChatCompletionsClient chatCompletionsClient() {
        return new ChatCompletionsClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(key))
                .buildClient();
    }
}
