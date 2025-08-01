package dev.heisen.enhancement.service;

import dev.heisen.enhancement.dto.EnhancementRequest;
import dev.heisen.enhancement.exception.EnhanceFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnhancementService {

    private static final String SYSTEM_INSTRUCTION = """
        You are a skilled editor. Your task is to polish the given Markdown content for clarity, engagement, and style.
        Output only the final, enhanced Markdown content! Without any titles, metadata, prefixes, or conversational text.
        """;

    private static final String USER_TEMPLATE = """
        Please enhance the following content. Follow these rules:
        
        1. Add or adjust headings (`##`, `###`), write a short 1-2 line introduction, and a conclusion.
        2. Improve grammar, sentence flow, and use the active voice where possible.
        3. Use bullet points or numbered lists if it improves readability.
        4. Maintain a friendly yet professional tone, addressing the reader as "you."

        Content to enhance:
        {{Content}}
        """.stripIndent();

    private final AiService aiService;

    public String enhance(EnhancementRequest request) {
        SystemMessage systemMessage = SystemMessage.builder()
                .text(SYSTEM_INSTRUCTION)
                .build();

        UserMessage userMessage = UserMessage.builder()
                .text(USER_TEMPLATE.replace("{{Content}}", request.content()))
                .build();

        Prompt prompt = Prompt.builder()
                .messages(systemMessage, userMessage)
                .build();

        try {
            return aiService.chat(prompt);
        } catch (EnhanceFailedException e) {
            return request.content();
        }
    }
}