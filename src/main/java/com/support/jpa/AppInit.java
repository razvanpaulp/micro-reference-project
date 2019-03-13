package com.support.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class AppInit {

	@Inject
	private DatabaseInit databaseInit;
	
	private void initDatabase(@Observes @Initialized(ApplicationScoped.class) Object init) {
	//	databaseInit.createTable();
	//	databaseInit.insertData();
	}

	
}
