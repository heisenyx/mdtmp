package dev.heisen.enhancement.service;

import dev.heisen.enhancement.dto.EnhancementRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnhancementService {

    private static final String ENHANCE_PROMPT = """
        You’re a skilled editor. Given a title and Markdown content, polish for clarity, engagement, and style:

        1. Rewrite the title to be catchy and SEO‑friendly.
        2. Add/adjust headings (`##`, `###`), intro (1–2 lines) and conclusion.
        3. Improve grammar, flow, and use active voice.
        4. Use bullets or lists where helpful.
        5. Maintain a friendly, professional tone and address “you.”
        6. Output only the final Markdown content.

        Title: {{Title}}

        Content:
        {{Content}}
        """.stripIndent();

    private final AiService aiService;

    public String enhance(EnhancementRequest request) {
        String prompt = ENHANCE_PROMPT
                .replace("{{Title}}", request.title())
                .replace("{{Content}}", request.content());
        return aiService.chat(prompt);
    }
}