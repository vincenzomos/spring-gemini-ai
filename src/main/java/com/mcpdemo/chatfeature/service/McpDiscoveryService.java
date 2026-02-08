package com.mcpdemo.chatfeature.service;

import io.modelcontextprotocol.client.McpSyncClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
@Slf4j
public class McpDiscoveryService {

    private final ToolCallbackProvider toolCallbackProvider;

    public McpDiscoveryService(ToolCallbackProvider toolCallbackProvider) {
        this.toolCallbackProvider = toolCallbackProvider;
    }

    @Tool
    public Mono<String> listAvailableTools() {

        log.info(" TESTLOG   triggered listAvailableTools via MCP callback. Retrieving tool list...");
       // Get all available tools
        ToolCallback[] tools = toolCallbackProvider.getToolCallbacks();

        if (tools == null || tools.length == 0) {
            return Mono.just("No MCP tools are currently configured in the system.");
        }

        String toolList = Arrays.stream(tools)
                .map(tool -> {
                    try {
                        var def = tool.getToolDefinition();
                        if (def != null) {
                            String name = def.name() == null ? "<unknown>" : def.name();
                            String desc = def.description() == null ? "(no description)" : def.description();
                            return String.format("- %s: %s", name, desc);
                        }
                    }
                    catch (Exception e) {
                        // fallback to toString()
                    }
                    return "- " + tool.toString();
                })
                .collect(Collectors.joining("\n"));

        String result = "The following tools are available:\n" + toolList;
        return Mono.just(result);
    }
}