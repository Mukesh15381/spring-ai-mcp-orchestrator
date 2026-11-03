package com.agent.mcpclient.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseQueryController {

	private final ChatClient chatClient;

	public DatabaseQueryController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@GetMapping("/api/v1/db/ask")
	public String queryDatabaseInPlainEnglish(@RequestParam("prompt") String prompt) {
		return this.chatClient.prompt()
			.user(prompt)
			.call()
			.content();
	}
}