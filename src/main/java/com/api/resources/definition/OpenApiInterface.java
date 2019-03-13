package com.api.resources.definition;

import com.model.Message;
import com.web.json.JsonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

/**
 * Interface extending ApiInterface and decorating methods with OpenApi documentation.
 */
public interface OpenApiInterface extends ApiInterface {

	@Override
	@Operation(summary = "Returns a greetings message.", tags = { "Hello Api", },
			responses = {
					@ApiResponse(responseCode = "200", description = "Returns a \"Hello there!\" message.",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = Message.class)))})
	Response sayHello();
	
	@Override
	@Operation(summary = "Returns a Java Object containg a greetings message.", tags = { "Hello Api", },
			responses = {
					@ApiResponse(responseCode = "200", description = "Returns a greetings message.",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = Message.class)))})
	Message sayPojoHello();

	@Override
	@Operation(summary = "Returns a parametrized greetings message.", tags = { "Async response", },
			responses = {
					@ApiResponse(responseCode = "200", description = "Creates a parametrized greetings message by using"
							+ " the provided name parameter.",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = Message.class))),
					@ApiResponse(responseCode = "400", description = "Parameter supplied must not be null."),})
	void sayParametrizedHello(@Parameter(in = ParameterIn.QUERY , description = "The name used for creating the custom " 
			+ "hello message.",
			schema = @Schema(type = "String", example = "John"), required = true) 
			String name, AsyncResponse asyncResponse);
	
	
	@Override
	@Operation(summary = "Returns a json message.", tags = { "Json response", },
			responses = {
					@ApiResponse(responseCode = "200", description = "Returns a json.",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = JsonResponse.class)))})
	JsonResponse sayJson();

	@Override
	@Operation(summary = "Returns a Java Object from underlying storage.", tags = { "Hello Api", },
			responses = {
					@ApiResponse(responseCode = "200", description = "Returns a greetings message.",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = Message.class)))})
	void getMessageFromStorage(long id, AsyncResponse asyncResponse);
}
