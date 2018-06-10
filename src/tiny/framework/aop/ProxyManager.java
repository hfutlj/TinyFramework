package tiny.framework.aop;


import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

// 由此类生成代理链
public class ProxyManager {
	
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(Class<?> targetClass , List<Proxy> proxyList ) {
		return (T) Enhancer.create(targetClass, new MethodInterceptor() {
			@Override
			public Object intercept(Object targetObject, Method targetMethod, Object[] prarms, MethodProxy methodProxy) throws Throwable {
				return new ProxyChain(targetClass, targetObject, targetMethod, methodProxy, prarms, proxyList).doProxyChain();
			}
		});
	}

}
