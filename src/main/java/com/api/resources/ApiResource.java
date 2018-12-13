package com.api.resources;

import com.api.resources.definition.OpenApiInterface;
import com.async.support.AppExecutors;
import com.model.Message;
import com.service.ApiService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import static com.async.support.Computation.computeAsync;

import java.util.concurrent.ExecutorService;

@RequestScoped
@Path("/")
@Produces({"application/json", "application/xml"})
public class ApiResource implements OpenApiInterface {
	
	@Inject
	private ApiService service;
	@Inject
	private AppExecutors appExecutors;
	
	@Override
	@GET
	@Path("hello/v1")
	public Response sayHello() {
		return Response.ok().entity(new Message("Hello there!")).build();
	}
	
	@Override
	@GET
	@Path("hello/v2")
	public Message sayPojoHello() {
		return new Message("This is a custom hello message.");
	}

	@Override
	@GET
	@Path("hello/v3")
	public void sayParametrizedHello(@QueryParam("name") String name, @Suspended AsyncResponse asyncResponse) {
		ExecutorService executorService = appExecutors.getExecutorService();
		
		computeAsync(() -> service.buildHelloMessage(name), executorService)
				.thenApplyAsync(result -> asyncResponse.resume(Response.ok().entity(result).build()), executorService)
				.exceptionally(error -> asyncResponse.resume(Response.status(400).entity(new Message(error.getMessage())).build()));
	}
}
