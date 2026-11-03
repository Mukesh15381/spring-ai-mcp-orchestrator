package com.agent.ailoganalyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class VectorDataLoaderService implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(VectorDataLoaderService.class);

	private final VectorStore vectorStore;

	public VectorDataLoaderService(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}

	@Override
	public void run(String... args) {
		// Sample internal knowledge base documents/logs
		List<Document> documents = List.of(
			new Document(
				"Error 500 in OrderService: HikariCP connection pool exhausted. Root cause: Missing @Transactional timeout on slow queries.",
				Map.of("service", "OrderService", "type", "Database")
			),
			new Document(
				"Error 401 in AuthService: JWT signature verification failed. Root cause: Clock skew between microservices exceeding 3000ms.",
				Map.of("service", "AuthService", "type", "Security")
			)
		);

		// Generates embeddings via Ollama and saves them to PGVector PostgreSQL
		// Disabled: this ran on every startup and inserted the same two documents again each time.
		// vectorStore.add(documents);
		log.info(">>> Successfully ingested knowledge base documents into PGVector!");
	}
}
