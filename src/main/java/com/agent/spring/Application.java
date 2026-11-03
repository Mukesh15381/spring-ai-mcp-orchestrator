package com.agent.spring;

import java.util.List;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.agent.ailoganalyzer.tools.LogDiagnosticTools;

@SpringBootApplication
@ComponentScan(basePackages = {"com.agent.spring", "com.agent.ailoganalyzer", "com.agent.mcpclient"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public List<ToolCallback> diagnosticTools(LogDiagnosticTools diagnosticService) {
		// Scans the service for @Tool annotations and converts them to MCP Tool specifications
		return List.of(ToolCallbacks.from(diagnosticService));
	}
}
