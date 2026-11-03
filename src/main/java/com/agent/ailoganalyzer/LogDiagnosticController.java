package com.agent.ailoganalyzer;

import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agent.ailoganalyzer.model.LogAnalysisResult;

@RestController
public class LogDiagnosticController {

	private final ChatClient chatClient;

	// Spring AI automatically provides ChatClient.Builder
	public LogDiagnosticController(ChatClient.Builder chatClientBuilder) {
		var chatMemory = MessageWindowChatMemory.builder().maxMessages(20).build();

		this.chatClient = chatClientBuilder
			// 1. SYSTEM PROMPT: Sets global persona, rules, and guardrails
			.defaultSystem("""
                        You are a Principal Java Backend Architect specializing in Spring Boot microservices.
                        Always keep answers concise, technical, and formatted cleanly.
                        If you are unsure of a root cause, explicitly state the missing information needed.
                        """)
			// 2. CONVERSATION MEMORY: Automatically records and injects chat history
			.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
			.build();
	}

	@GetMapping("/chat")
	public String chatWithMemory(
		@RequestParam(value = "conversationId", defaultValue = "user-session-1") String conversationId,
		@RequestParam("message") String message) {

		return this.chatClient.prompt()
			.user(message)
			// Link request to the conversation session
			.advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
			.call()
			.content();
	}

	@GetMapping("/api/v1/analyze-quick")
	public LogAnalysisResult analyzeLogSnippet(@RequestParam(value = "log", defaultValue = "NullPointerException at line 42") String log) {
		String prompt = """
                You are a Senior Java Backend SDE. 
                Analyze this log message briefly and suggest a potential root cause:
                Log: %s
                """.formatted(log);

		return this.chatClient.prompt()
			.user(prompt)
			.advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "analyze-quick"))
			// Send the JSON schema to Ollama as `format` so the model is forced to emit valid JSON
			.advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
			.call()
			.entity(LogAnalysisResult.class);
	}
}