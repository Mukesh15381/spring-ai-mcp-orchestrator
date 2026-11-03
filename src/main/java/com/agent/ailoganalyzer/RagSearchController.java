package com.agent.ailoganalyzer;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rag")
public class RagSearchController {

	private final ChatClient chatClient;

	public RagSearchController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
		this.chatClient = chatClientBuilder
			.defaultAdvisors(
				// QuestionAnswerAdvisor fetches top 2 most relevant matches from PGVector
				QuestionAnswerAdvisor.builder(vectorStore)
					.searchRequest(SearchRequest.builder().topK(2).build())
					.build()
			)
			.build();
	}

	@GetMapping("/analyze")
	public String analyzeWithRag(@RequestParam("query") String query) {
		return this.chatClient.prompt()
			.user(query)
			.call()
			.content();
	}
}
