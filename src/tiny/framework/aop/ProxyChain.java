package tiny.framework.aop;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.MethodProxy;

public class ProxyChain {
	private Class<?> targetClass;
	private Object targetObject;
	private Method targetMethod;
	private MethodProxy methodProxy;
	private Object[] prarms;
	
	private List<Proxy> list = new ArrayList<>();
	private int index = 0;
	
	public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy,
			Object[] prarms, List<Proxy> proxyList) {
		super();
		this.targetClass = targetClass;
		this.targetObject = targetObject;
		this.targetMethod = targetMethod;
		this.methodProxy = methodProxy;
		this.prarms = prarms;
		this.list = proxyList;
	}
	
	public Object doProxyChain() throws Throwable {
		Object result;
		if(index<list.size()) {
			result = list.get(index++).doProxy(this);
		}else {
			result = methodProxy.invokeSuper(targetObject, prarms);
		}
		return result;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public Method getTargetMethod() {
		return targetMethod;
	}

	public void setTargetMethod(Method targetMethod) {
		this.targetMethod = targetMethod;
	}

	public Object[] getPrarms() {
		return prarms;
	}

	public void setPrarms(Object[] prarms) {
		this.prarms = prarms;
	}
	
	
	
	
}
