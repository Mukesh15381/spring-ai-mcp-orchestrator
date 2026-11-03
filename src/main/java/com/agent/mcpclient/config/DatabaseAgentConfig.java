package com.agent.mcpclient.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseAgentConfig {

	@Bean
	public ChatClient chatClient(
		ChatClient.Builder builder,
		SyncMcpToolCallbackProvider mcpToolCallbackProvider) {

		return builder
			.defaultSystem("""
                        You are an expert PostgreSQL Data Analyst assistant.
                        You have access to a live PostgreSQL database via an MCP server tool.
                        
                        Guidelines:
                        1. When asked a data question, use the `query` tool to execute a standard PostgreSQL SELECT statement.
                        2. The MCP tool enforces READ-ONLY transactions; do not attempt INSERT, UPDATE, or DELETE statements.
                        3. Always write valid PostgreSQL syntax and use reasonable LIMIT clauses.
                        4. Interpret the returned rows and present the final answer clearly in plain English.
                        """)
			// Registers the provider itself; its tools (discovered from the Postgres MCP server)
			// are resolved lazily on each request. Spring AI 2.0 defaultTools() accepts a
			// ToolCallbackProvider directly; the defaultToolCallbacks() overloads are deprecated.
			.defaultTools(mcpToolCallbackProvider)
			.build();
	}
}
