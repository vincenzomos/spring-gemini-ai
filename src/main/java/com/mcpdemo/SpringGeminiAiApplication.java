package com.mcpdemo;

import com.mcpdemo.chatfeature.service.McpDiscoveryService;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@SpringBootApplication
public class SpringGeminiAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringGeminiAiApplication.class, args);
	}


    @Bean
    public ToolCallback mcpDiscoveryTools(@Autowired McpDiscoveryService mcpDiscoveryService) {
        Method method = ReflectionUtils.findMethod(McpDiscoveryService.class, "listAvailableTools");
        return MethodToolCallback.builder()
                .toolDefinition(ToolDefinitions.builder(method)
                        .description("Get all mcp tools and list them in a human readable format")
                        .build())
                .toolMethod(method)
                .toolObject(mcpDiscoveryService)
                .build();
    }
}
