package com.api.resources.definition;

import com.model.Message;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

/**
 * Interface that defines the general contract of the API resource.
 */
public interface ApiInterface {

	Response sayHello();

	Message sayPojoHello();

	void sayParametrizedHello(String name, AsyncResponse asyncResponse);
}
