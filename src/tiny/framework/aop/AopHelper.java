package tiny.framework.aop;


import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tiny.framework.annotation.Aspect;
import tiny.framework.core.BeanHelper;
import tiny.framework.core.ClassHelper;

public class AopHelper {

	private static final Logger logger = LoggerFactory.getLogger(AopHelper.class);

	static {
		try {
			Map<Class<?>, List<Proxy>> targetMap = getTargetMap();
			//System.out.println(targetMap);
			for (Entry<Class<?>, List<Proxy>> entry : targetMap.entrySet()) {
				Class<?> targetClass = entry.getKey();
				List<Proxy> proxyList = entry.getValue();
				Object proxy = ProxyManager.createProxy(targetClass, proxyList);
				BeanHelper.setInstance(targetClass, proxy);
			}
		} catch (Exception e) {
			logger.error("tinyFramework:=====  ,fail to init Aop framework , cause {}" , e);
			//logger.error("AOP 加载失败{}" , e);
			e.printStackTrace();
		}
	}

	/*
	 * 获取Aspect注解中标记的目标类集合
	 */
	public static Set<Class<?>> getTargetClassSet(Aspect aspect) {
		Class<? extends Annotation> targetClass = aspect.value();
		Set<Class<?>> set = new HashSet<>();
		if (targetClass != null && !targetClass.equals(Aspect.class)) {
			set = ClassHelper.getClassSetByAnnotation(targetClass);
		} else {
			logger.error("tinyFramework:=====  :");
			logger.debug("获取切入的目标类集合失败，该注解不为Aspect注解：{}", targetClass);
		}
		return set;
	}

	/*
	 * 获取代理类和目标类集合之间的映射
	 * 先获取普通代理Aspect与目标的关系，再获取事务代理之间的关系
	 */
	public static Map<Class<?>, Set<Class<?>>> getProxyMap() {
		Map<Class<?>, Set<Class<?>>> map = new HashMap<>();
		// 获取所有AspectProxy的子类，即代理类
		Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
		// 获取所有代理类的目标类
		for (Class<?> proxyClass : proxyClassSet) {
			// 获取代理类的Aspect注解
			Aspect aspect = proxyClass.getAnnotation(Aspect.class);
			// 根据Aspect注解找到目标类集合
			Set<Class<?>> targetClassSet = getTargetClassSet(aspect);
			map.put(proxyClass, targetClassSet);
		}
		
		// 添加事务代理关系
//		map.putAll(getTransactionMap());
		return map;
	}
	
//	public static Map<Class<?>, Set<Class<?>>> getTransactionMap(){
//		Map<Class<?>, Set<Class<?>>> map = new HashMap<>();
//		Set<Class<?>> set = ClassHelper.getClassSetByAnnotation(Service.class);
//		map.put(TransactionProxy.class, set);
//		return map;
//	}

	/*
	 * 获取目标类与代理对象之间的关系
	 */
	public static Map<Class<?>, List<Proxy>> getTargetMap() throws Exception {
		Map<Class<?>, List<Proxy>> map = new HashMap<>();
		Map<Class<?>, Set<Class<?>>> proxyMap = getProxyMap();
		
		for (Entry<Class<?>, Set<Class<?>>> entry : proxyMap.entrySet()) {
			// 代理类
			Class<?> proxy = entry.getKey();
			// 目标类集合
			Set<Class<?>> targetClassSet = entry.getValue();
			// 为所有目标类都创建代理类实例
			for (Class<?> targetClass : targetClassSet) {
				Proxy proxyInstance = (Proxy) proxy.newInstance();
				if (map.containsKey(proxy)) {
					map.get(targetClass).add(proxyInstance);
				} else {
					List<Proxy> proxyInstanceList = new ArrayList<>();
					proxyInstanceList.add(proxyInstance);
					map.put(targetClass, proxyInstanceList);
				}
			}

		}
		return map;
	}

}
