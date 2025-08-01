package dev.heisen.enhancement.service;

import dev.heisen.enhancement.exception.EnhanceFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private static final Logger log = LoggerFactory.getLogger(AiService.class);

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    public String chat(Prompt prompt) {
        try {
            return chatClient.prompt(prompt)
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("Enhancement failed: {}", e.getMessage());
            throw new EnhanceFailedException("Enhancement failed", e);
        }
    }
}