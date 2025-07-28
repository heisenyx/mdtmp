package dev.heisen.enhancement.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    public String chat(Prompt prompt) {
        return chatClient.prompt(prompt)
                .call()
                .content();
    }
}