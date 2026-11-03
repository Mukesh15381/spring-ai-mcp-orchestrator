package com.agent.ailoganalyzer.model;

import java.util.List;

public record LogAnalysisResult(
	String errorType,
	String rootCause,
	String severity, // e.g., HIGH, MEDIUM, LOW
	List<String> suggestedFixes,
	boolean requiresImmediateAction
) {}
