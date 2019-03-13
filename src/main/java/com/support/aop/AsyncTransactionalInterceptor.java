package com.support.aop;

import javax.enterprise.context.control.RequestContextController;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@AsyncTransactional
@Interceptor
public class AsyncTransactionalInterceptor {
	
	@Inject
	private RequestContextController requestContextController;
	
	@AroundInvoke
	public Object processActiveContext(InvocationContext invocationContext)
			throws Exception {

		Object returnedVal = null;
		
		requestContextController.activate();	
	
		//execute intercepted method
		 returnedVal = invocationContext.proceed();
		
		return returnedVal;
	}
}
