package com.support.jpa;

import javax.sql.DataSource;

public interface DataSourceProducer {

	String DATASOURCE_JNDI = "java:/dataSource";
	
	DataSource createDataSource();
}
