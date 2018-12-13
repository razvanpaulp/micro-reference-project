package com.cloud.support;

import java.util.Optional;
import java.util.function.Function;

/**
 * Fetches a property from the Environment, System Properties, then uses a
 * fallback. Useful in Cloud Native (Docker / Kubernetes) deployments.
 *
 */
public interface DeploymentConfiguration {

	Function<String, String> ENV = (key) -> System.getenv().getOrDefault(key, System.getProperty(key));

	static String getProperty(String key, String fallback) {
		String value = ENV.apply(key);
		return Optional.ofNullable( value ).orElse( fallback );
	}
}
