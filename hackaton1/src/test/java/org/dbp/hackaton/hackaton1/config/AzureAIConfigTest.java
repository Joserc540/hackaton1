package org.dbp.hackaton.hackaton1.config;

import com.azure.ai.inference.ChatCompletionsClient;
import org.dbp.hackaton.hackaton1.config.ai.AzureAIConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = AzureAIConfig.class)
@TestPropertySource(properties = {
        "azure.ai.endpoint=https://test-endpoint/",
        "azure.ai.key=test-key"
})

public class AzureAIConfigTest {
    @Autowired
    private ChatCompletionsClient chatCompletionsClient;

    @Autowired
    private AzureAIConfig azureAIConfig;

    @Test
    void chatCompletionsClientBeanLoads() {
        assertNotNull(chatCompletionsClient, "El bean ChatCompletionsClient debe cargarse en el contexto");
    }

    @Test
    void propertiesAreInjected() {
        String endpointValue = (String) ReflectionTestUtils.getField(azureAIConfig, "endpoint");
        String keyValue = (String) ReflectionTestUtils.getField(azureAIConfig, "key");

        assertEquals("https://test-endpoint/", endpointValue,
                "Debe inyectarse correctamente la propiedad azure.ai.endpoint");
        assertEquals("test-key", keyValue,
                "Debe inyectarse correctamente la propiedad azure.ai.key");
    }
}
