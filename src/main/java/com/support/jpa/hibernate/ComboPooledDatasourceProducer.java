package com.support.jpa.hibernate;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.support.jpa.DataSourceProducer;

import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import static com.support.cloud.DeploymentConfiguration.getProperty;

@ApplicationScoped 
public class ComboPooledDatasourceProducer implements DataSourceProducer {

	@Override
	public DataSource createDataSource() {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setJdbcUrl( getProperty("jdbc.url", "jdbc:hsqldb:mem://localhost/test"));
		cpds.setUser("test");
		cpds.setPassword("test");
		cpds.setMaxPoolSize(3);

		return cpds;
	}
}
