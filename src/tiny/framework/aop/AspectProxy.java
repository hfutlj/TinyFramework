package tiny.framework.aop;

import java.lang.reflect.Method;

public abstract class AspectProxy implements Proxy{
	
	@Override
	public final Object doProxy(ProxyChain proxyChain)  {
		Object result = null;
		Class<?> targetClass = proxyChain.getTargetClass();
		Method targetMethod = proxyChain.getTargetMethod();
		Object[] params = proxyChain.getPrarms();
		
		try {
			begin(targetClass, targetMethod, params);
			result = proxyChain.doProxyChain();
			after(targetClass, targetMethod, params, result);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	public Object begin(Class<?> cls , Method method , Object[] params) {
		return null;
	}
	
	public Object after(Class<?> cls , Method method , Object[] params , Object result) {
		return null;
	}

}
