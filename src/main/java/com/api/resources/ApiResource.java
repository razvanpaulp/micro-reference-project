package com.api.resources;

import com.api.resources.definition.OpenApiInterface;
import com.model.Message;
import com.service.ApiService;
import com.support.async.AppExecutors;
import com.web.json.JsonResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import static com.support.async.Computation.computeAsync;

@RequestScoped
@Path("/")
@Produces({"application/json", "application/xml"})
public class ApiResource implements OpenApiInterface {

	@Inject
	private ApiService apiService;
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
		return new Message("Hello again!.");
	}


	@Override
	@GET
	@Path("hello/v3")
	public void sayParametrizedHello(@QueryParam("name") String name, @Suspended AsyncResponse asyncResponse) {
		ExecutorService executorService = appExecutors.getExecutorService();

		computeAsync(() -> apiService.buildHelloMessage(name), executorService)
		.thenApplyAsync(result -> asyncResponse.resume(Response.ok().entity(result).build()), executorService)
		.exceptionally(error -> asyncResponse.resume(Response.status(400).entity(new Message(error.getMessage())).build()));
	}

	@Override
	@GET
	@Path("json")
	public JsonResponse sayJson() {
		HashMap<String, String> player = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;{
				put("Name", "Djokovic");
				put("Surname","Novak");

			}};
			return new JsonResponse()
					.with("Competition", "ATP")
					.with("Rank", 1)
					.with("Player", player)
					.done();
	}

	@Override
	@GET
	@Path("getMessageFromStorage")
	public void getMessageFromStorage(@QueryParam("id") long id, @Suspended AsyncResponse asyncResponse) {
		ExecutorService executorService = appExecutors.getExecutorService();

		computeAsync(() -> apiService.getMessageFromStorage(id), executorService)
				.thenApplyAsync(result -> asyncResponse.resume(Response.ok().entity(result).build()), executorService)
				.exceptionally(error -> asyncResponse.resume(Response.status(400).entity(new Message(error.getMessage())).build()));
	}

	@GET
	@Path("getMessageWithCounter")
	public void getMessageWithCounter(@QueryParam("id") long id, @Suspended AsyncResponse asyncResponse) {
		ExecutorService executorService = appExecutors.getExecutorService();

		Message result = apiService.getMessageWithCounter(id);
		asyncResponse.resume(Response.ok().entity(result).build());
	}

}
