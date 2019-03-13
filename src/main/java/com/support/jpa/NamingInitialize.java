package com.support.jpa;

import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
import com.arjuna.ats.arjuna.recovery.RecoveryManager;
import com.arjuna.ats.jta.utils.JNDIManager;
import com.arjuna.common.internal.util.propertyservice.BeanPopulator;
import org.jnp.server.NamingBeanImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.sql.DataSource;

@ApplicationScoped 
public class NamingInitialize {
	/**
	 * JNDI server.
	 */
	private static final NamingBeanImpl NAMING_BEAN = new NamingBeanImpl();
	private static final String storeClassName = com.arjuna.ats.internal.arjuna.objectstore.VolatileStore.class
			.getName();

	static void shutdown() {
		try {
			// Stop recovery manager
			RecoveryManager.manager().terminate();
		} catch (Throwable t) {
		}

		try {
			// Stop naming server
			NAMING_BEAN.stop();
		} catch (Throwable t) {
		}
	}

	public void initialize(DataSource dataSource)
			throws Exception {
		// Start JNDI server
		NAMING_BEAN.start();

		// Bind JTA implementation with default names
		JNDIManager.bindJTAImplementation();

		//set volatile recovery object store
		BeanPopulator.getDefaultInstance(ObjectStoreEnvironmentBean.class).setObjectStoreDir(storeClassName);
		BeanPopulator.getNamedInstance(ObjectStoreEnvironmentBean.class, "defaultStore").setObjectStoreType(storeClassName);
		BeanPopulator.getNamedInstance(ObjectStoreEnvironmentBean.class, "communicationStore")
				.setObjectStoreType(storeClassName);
		// Bind datasource
		new InitialContext().bind(DataSourceProducer.DATASOURCE_JNDI, dataSource);

	}
}
