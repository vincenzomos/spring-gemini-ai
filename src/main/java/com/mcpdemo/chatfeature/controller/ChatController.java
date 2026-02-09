package com.mcpdemo.chatfeature.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

@RestController
@Slf4j
public class ChatController {

    @Autowired
    private ChatMemoryRepository chatMemoryRepository;

    private ToolCallbackProvider toolCallbackProvider;
    private ToolCallback mcpDiscoveryTools;

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools, ToolCallback mcpDiscoveryTools) {

        this.toolCallbackProvider = tools;
        this.mcpDiscoveryTools = mcpDiscoveryTools;
        Arrays.stream(tools.getToolCallbacks()).forEach(tool -> {
            log.info("Tool Callback registered: {}", tool.getToolDefinition());
        });

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(10)
                .build();

        ToolCallback[] toolCallbacks = getAllTools();

        this.chatClient = chatClientBuilder
                .defaultSystem("""
                You are a versatile AI assistant.
                - For questions about timesheets, projects or PDF documents, use the available tools.
                - For all other general questions, use your own knowledge as the language model.
                - If you use a tool and find nothing, indicate that and avoid hallucinating.
                """)
                .defaultOptions(
                        VertexAiGeminiChatOptions.builder()
                                .temperature(0.7)
                                .build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultToolCallbacks(toolCallbacks)
                .build();
    }

    private ToolCallback[] getAllTools() {
        ToolCallback[] tools = new ToolCallback[toolCallbackProvider.getToolCallbacks().length + 1];
        log.info("Total number of tool callbacks registered: {}", tools.length);
        for (int i = 0; i < toolCallbackProvider.getToolCallbacks().length; i++) {
            tools[i] = toolCallbackProvider.getToolCallbacks()[i];
            log.info("Tool {}: {}", i, tools[i].getToolDefinition());
        }
        tools[tools.length - 1] = mcpDiscoveryTools;
        return tools;
    }

    @PostMapping("/chat")
    public String chat(@RequestParam String message) {
        return  chatClient
                .prompt(message)
                .call()
                .content();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithStream(@RequestParam String message) {
        // We create an advisor that provides context but keeps the model free
//        var qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
//                .protectFromBlocking(true)
//                .build();

        return chatClient
                .prompt()
//                .advisors(qaAdvisor)
                .user(message)
                .stream()
                .content()
                .delayElements(Duration.ofMillis(50))
                .doOnNext(chunk -> log.info("Emitting chunk: {}", chunk))
                .concatWith(Flux.just("[DONE]"));// verify server emits
    }

    @GetMapping(value = "/test-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> testStream() {
        return Flux.interval(Duration.ofMillis(200))
                .take(10)
                .map(i -> "Chunk " + i + " at " + System.currentTimeMillis())
                .concatWith(Flux.just("[DONE]"));
    }

}
