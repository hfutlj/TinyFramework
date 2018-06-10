package tiny.framework.core;


import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tiny.framework.annotation.Bean;
import tiny.framework.annotation.Controller;
import tiny.framework.annotation.Inject;
import tiny.framework.annotation.Service;
import tiny.framework.annotation.Aspect;

public class BeanHelper {

	private static final Logger logger = LoggerFactory.getLogger(BeanHelper.class);

	private static final Set<Class<?>> class_set = ClassHelper.getDefaultClassSet();
	private static final Map<Class<?>, Object> bean_map = new HashMap<>();

	/**
	 * 实例化bean
	 */
	static {
		try {
			// Map<Class<?>, Object> map = new HashMap<>();
			//logger.debug("classSet大小为：{}", class_set.size());
			for (Class<?> cls : class_set) {
				if (cls.isAnnotationPresent(Service.class) 
						|| cls.isAnnotationPresent(Bean.class)
					//	|| cls.isAnnotationPresent(Inject.class) 
						|| cls.isAnnotationPresent(Controller.class)
						|| cls.isAnnotationPresent(Aspect.class)) {
					Object obj = cls.newInstance();
					bean_map.put(cls, obj);
				}
			}
		} catch (Exception e) {
			logger.error("tinyFramework:=====  , get instance fail {}" , e);
			throw new RuntimeException("获取类实例失败", e);
		}
	}

	// 获取指定注解下的所有类
	public static Set<Class<?>> getAnnotationSet(Class<? extends Annotation> annotationClass) {
		Set<Class<?>> set = new HashSet<>();
		for (Class<?> cls : class_set) {
			if (cls.isAnnotationPresent(annotationClass)) {
				set.add(cls);
			}
		}
		return set;
	}

	// 获取service层的类
	public static Set<Class<?>> getServiceSet() {
		Set<Class<?>> set = new HashSet<>();
		for (Class<?> cls : class_set) {
			if (cls.isAnnotationPresent(Service.class)) {
				set.add(cls);
			}
		}
		return set;
	}

	// 获取controller层的类
	public static Set<Class<?>> getControllerSet() {
		Set<Class<?>> set = new HashSet<>();
		for (Class<?> cls : class_set) {
			if (cls.isAnnotationPresent(Controller.class)) {
				set.add(cls);
			}
		}
		return set;
	}

	public static Map<Class<?>, Object> getBeanMap() {
		//System.out.println("bean map" + bean_map);
		return bean_map;
	}

	public static Object getBeanInstance(Class<?> cls) {
		return bean_map.get(cls);
	}

	// 将指定的class的实例替换
	public static void setInstance(Class<?> cls, Object instance) {
		bean_map.put(cls, instance);
	}
}
