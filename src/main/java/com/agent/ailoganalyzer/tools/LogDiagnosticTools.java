package com.agent.ailoganalyzer.tools;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class LogDiagnosticTools {

	private static final Logger log = LoggerFactory.getLogger(LogDiagnosticTools.class);

	@Tool(description = "Fetch real-time error logs and recent exception counts for a specific microservice")
	public Map<String, Object> fetchLiveServiceLogs(
		@ToolParam(description = "The exact name of the microservice, e.g. OrderService, AuthService")
		String serviceName) {

		// Never write to stdout: in STDIO mode it carries the MCP JSON-RPC stream
		log.info(">>> [TOOL INVOKED BY LLM] Service: {}", serviceName);

		if ("OrderService".equalsIgnoreCase(serviceName)) {
			return Map.of(
				"service", "OrderService",
				"status", "DEGRADED",
				"errorCountLast5m", 38,
				"lastException", "HikariPool-1: Connection is not available, request timed out after 30000ms",
				"recommendation", "Increase maximumPoolSize or check slow queries on orders table."
			);		}
		return Map.of(
			"service", serviceName,
			"status", "HEALTHY",
			"errorCountLast5m", 0,
			"lastException", "None"
		);
	}
}