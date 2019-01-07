package com.undertow.standalone;

import static io.undertow.servlet.Servlets.defaultContainer;
import static io.undertow.servlet.Servlets.deployment;
import static io.undertow.servlet.Servlets.listener;
import static io.undertow.servlet.Servlets.servlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletException;

import org.glassfish.jersey.servlet.ServletContainer;
import org.jboss.weld.environment.servlet.Listener;

import com.config.ApplicationConfig;
import com.server.Server;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.AuthenticationMode;
import io.undertow.security.handlers.AuthenticationCallHandler;
import io.undertow.security.handlers.AuthenticationConstraintHandler;
import io.undertow.security.handlers.AuthenticationMechanismsHandler;
import io.undertow.security.handlers.SecurityInitialHandler;
import io.undertow.security.idm.DigestAlgorithm;
import io.undertow.security.idm.IdentityManager;
import io.undertow.security.impl.BasicAuthenticationMechanism;
import io.undertow.security.impl.DigestAuthenticationMechanism;
import io.undertow.security.impl.DigestQop;
import io.undertow.security.impl.SimpleNonceManager;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.GracefulShutdownHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.StuckThreadDetectionHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.WebSocketProtocolHandshakeHandler;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;
import static com.support.cloud.DeploymentConfiguration.getProperty;
import static io.undertow.Handlers.websocket;

public final class UndertowServer {

	public final Lock LOCK = new ReentrantLock();

	private final String host;
	private final int port;
	private final String deploymentName;

	private volatile Undertow server;

	public UndertowServer(String host, int port, String deploymentName){
		this.host = host;
		this.port = port;
		this.deploymentName = deploymentName;
	}

	private HttpHandler bootstrap() throws ServletException {
		final DeploymentInfo servletBuilder = deployment()
				.setClassLoader(Server.class.getClassLoader())
				.setContextPath("/")
				.addListeners(listener(Listener.class))
				.setResourceManager(new ClassPathResourceManager(Server.class.getClassLoader(), "webapp/resources"))
				.addWelcomePage("index.html")
				.setDeploymentName(deploymentName)
				.addServlets(
						servlet("jerseyServlet", ServletContainer.class)
						.addInitParam("javax.ws.rs.Application", ApplicationConfig.class.getName())
						.addMapping("/api/*")
						.setLoadOnStartup(1)
						.setAsyncSupported(true));

		final DeploymentManager manager = defaultContainer().addDeployment(servletBuilder);
		manager.deploy();

		//Servlet handler
		final HttpHandler servletHandler = manager.start();

		//Open API resource handler
		final ResourceHandler resourceHandler = new ResourceHandler(new ClassPathResourceManager(Server.class.getClassLoader(), "apidoc"))
				.addWelcomeFiles("index.html")
				.setDirectoryListingEnabled(false);

		//Websocket handler
		final WebSocketProtocolHandshakeHandler chatHandler = websocket(new WebSocketConnectionCallback() {

			@Override
			public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
				channel.getReceiveSetter().set(new AbstractReceiveListener() {

					@Override
					protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
						final String messageData = message.getData();
						for (WebSocketChannel session : channel.getPeerConnections()) {
							WebSockets.sendText(messageData, session, null);
						}
					}
				});
				channel.resumeReceives();
			}
		});

		final PathHandler pathHandler = Handlers.path()
				.addPrefixPath("/", servletHandler)
				.addPrefixPath("apidoc", resourceHandler)
				.addPrefixPath("/chat", chatHandler);

		return pathHandler;


	}


	public void start() throws ServletException{

		final HttpHandler httpHandler = bootstrap();
		final StuckThreadDetectionHandler stuck = new StuckThreadDetectionHandler(getProperty("thread.execution.time", 100), httpHandler);
		final GracefulShutdownHandler shutdown = Handlers.gracefulShutdown(stuck);



		final Map<String, char[]> users = new HashMap<>(2);
		users.put("userOne", "passwordOne".toCharArray());
		users.put("userTwo", "passwordTwo".toCharArray());

		final IdentityManager identityManager = new MapIdentityManager(users);
		HttpHandler handler = shutdown;
		handler = new AuthenticationCallHandler(handler);
		handler = new AuthenticationConstraintHandler(handler);
		List<DigestAlgorithm> digestAlgorithms = Collections.singletonList(DigestAlgorithm.MD5);
        List<DigestQop> digestQops = Collections.emptyList();
   
		final List<AuthenticationMechanism> mechanisms = Collections.<AuthenticationMechanism>singletonList(new BasicAuthenticationMechanism( "My Realm"));
		
		//final List<AuthenticationMechanism> mechanisms = Collections.<AuthenticationMechanism>singletonList(new DigestAuthenticationMechanism(digestAlgorithms, digestQops, "My Realm", "/",
                //new SimpleNonceManager()));
		handler = new AuthenticationMechanismsHandler(handler, mechanisms);
		handler = new SecurityInitialHandler(AuthenticationMode.PRO_ACTIVE, identityManager, handler);
		LOCK.lock();

		server = Undertow.builder()
				.addHttpListener(port, host)
				.setHandler(handler)
				.setServerOption(UndertowOptions.ENABLE_HTTP2, true)
				.build();

		server.start();
	}



	public void stop() {

		server.stop();
		LOCK.unlock();
	}
}
