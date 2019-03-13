package com.support.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

public interface AppPersistenceProvider {
	
	EntityManagerFactory createEntityManagerFactory();

	PersistenceUnitInfo persistenceUnitInfo();

	EntityManagerFactory getEntityManagerFactory();
}
