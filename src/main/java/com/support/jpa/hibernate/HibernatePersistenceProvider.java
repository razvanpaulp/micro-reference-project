package com.support.jpa.hibernate;

import com.google.common.collect.ImmutableMap;
import com.support.jpa.AppPersistenceProvider;
import com.support.jpa.DataSourceProducer;
import com.support.jpa.DatabaseInit;
import com.support.jpa.NamingInitialize;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static com.support.cloud.DeploymentConfiguration.getProperty;
import static org.hibernate.cfg.AvailableSettings.*;

@ApplicationScoped
public class HibernatePersistenceProvider implements AppPersistenceProvider {
	
	private EntityManagerFactory entityManagerFactory;
	
	@Inject
	private DataSourceProducer dataSourceProducer;
	@Inject
	private NamingInitialize namingInitialize;
	@Inject
	private DatabaseInit databaseInit;
	
	private void init(@Observes @Initialized(ApplicationScoped.class) Object d)
			throws Exception {
		DataSource dataSource = dataSourceProducer.createDataSource();
		namingInitialize.initialize(dataSource);
		entityManagerFactory = createEntityManagerFactory();
		databaseInit.init();
	}

	@Override
	public EntityManagerFactory createEntityManagerFactory() {
		return new org.hibernate.jpa.HibernatePersistenceProvider()
				.createContainerEntityManagerFactory(
						persistenceUnitInfo(),
						ImmutableMap.<String, Object>builder()
								.put(JTA_PLATFORM, getProperty("hibernate.transaction.jta.platform", "JBossTS"))
								.put(JPA_JDBC_DRIVER, "com.mysql.jdbc.Driver")
								.put(DIALECT,  getProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect"))
								.put(SHOW_SQL, getProperty("hibernate.show_sql", "false"))
								.put(STATEMENT_BATCH_SIZE, getProperty("hibernate.jdbc.batch_size", "20"))
								.build());
	}

	@Override
	public PersistenceUnitInfo persistenceUnitInfo() {
		return new PersistenceUnitInfo() {
			@Override
			public String getPersistenceUnitName() {
				return "app-unit";
			}

			@Override
			public String getPersistenceProviderClassName() {
				return getProperty("hibernate.persistence.provider","org.hibernate.jpa.HibernatePersistenceProvider");
			}

			@Override
			public PersistenceUnitTransactionType getTransactionType() {
				return PersistenceUnitTransactionType.JTA;
			}

			@Override
			public DataSource getJtaDataSource() {
				try {
					return (DataSource) new InitialContext().lookup(DataSourceProducer.DATASOURCE_JNDI);
				} catch (NamingException e) {
					return null;
				}
			}

			@Override
			public DataSource getNonJtaDataSource() {
				return null;
			}

			@Override
			public List<String> getMappingFileNames() {
				return Collections.emptyList();
			}

			@Override
			public List<java.net.URL> getJarFileUrls() {
				try {
					return Collections.list(this.getClass()
							.getClassLoader()
							.getResources(""));
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}

			@Override
			public URL getPersistenceUnitRootUrl() {
				return null;
			}

			@Override
			public List<String> getManagedClassNames() {
				return Collections.emptyList();
			}

			@Override
			public boolean excludeUnlistedClasses() {
				return false;
			}

			@Override
			public SharedCacheMode getSharedCacheMode() {
				return null;
			}

			@Override
			public ValidationMode getValidationMode() {
				return null;
			}

			@Override
			public Properties getProperties() {
				return new Properties();
			}

			@Override
			public String getPersistenceXMLSchemaVersion() {
				return null;
			}

			@Override
			public ClassLoader getClassLoader() {
				return null;
			}

			@Override
			public void addTransformer(ClassTransformer transformer) {

			}

			@Override
			public ClassLoader getNewTempClassLoader() {
				return null;
			}
		};
	}

	@Override public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

}
