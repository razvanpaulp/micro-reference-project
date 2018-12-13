package com.server;

import com.config.ApplicationConfig;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jboss.weld.environment.servlet.Listener;

import javax.servlet.ServletException;

import static com.cloud.support.DeploymentConfiguration.getProperty;
import static io.undertow.servlet.Servlets.*;

public class Server {

	public static void main(String[] args)
			throws ServletException {

		PathHandler path = Handlers.path();
		
		Integer port1 = Integer.valueOf(getProperty("port", "8080"));
		String host = getProperty("host", "localhost");
		
		final Undertow server = Undertow.builder()
				.addHttpListener(port1, host)
				.setHandler(path)
				.build();

		server.start();

		DeploymentInfo servletBuilder = deployment()
				.setClassLoader(Server.class.getClassLoader())
				.setContextPath("/")
				.addListeners(listener(Listener.class))
				.setResourceManager(new ClassPathResourceManager(Server.class.getClassLoader()))
				.addServlets(
						servlet("jerseyServlet", ServletContainer.class).setLoadOnStartup(1)
								.addInitParam("javax.ws.rs.Application", ApplicationConfig.class.getName())
								.addMapping("/api/*").setAsyncSupported(true))
				.setDeploymentName("micro-reference-project.war");

		DeploymentManager manager = defaultContainer().addDeployment(servletBuilder);
		manager.deploy();

		path.addPrefixPath("/", manager.start());
	}

}
