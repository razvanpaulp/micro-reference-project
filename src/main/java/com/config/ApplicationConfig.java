package com.config;


import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.api.resources.ApiResource;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * JAX-RS application configuration class.
 */
@ApplicationPath("/api")
public class ApplicationConfig extends ResourceConfig {

	public ApplicationConfig() {
		register(ApiResource.class);
		register(JacksonJaxbJsonProvider.class);

		register(OpenApiResource.class);
		register(AcceptHeaderOpenApiResource.class);
	}	
	
}
