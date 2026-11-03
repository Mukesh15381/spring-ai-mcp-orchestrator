package com.agent.ailoganalyzer;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agent.ailoganalyzer.tools.LogDiagnosticTools;

@RestController
public class AgentDiagnosticController {

	private final ChatClient chatClient;

	public AgentDiagnosticController(ChatClient.Builder chatClientBuilder, LogDiagnosticTools logDiagnosticTools) {
		this.chatClient = chatClientBuilder.defaultTools(logDiagnosticTools).build();
	}

	@GetMapping("/api/v1/agent/diagnose")
	public String diagnoseLiveService(@RequestParam("query") String query) {
		return this.chatClient.prompt()
			.user(query)
			.call()
			.content();
	}
}
