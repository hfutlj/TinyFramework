package tiny.framework.core;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tiny.framework.annotation.Bean;
import tiny.framework.constant.TinyConstant;


public class ClassHelper {
	private static final Logger logger = LoggerFactory.getLogger(ClassHelper.class);
	
	private static final Set<Class<?>> class_set = new HashSet<>();
	private static Set<Class<?>> bean_set = new HashSet<>();
	
	public static ClassLoader getClassloder() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	/**
	 * 根据包名加载类放置到set中
	 * @param packageName
	 * @return
	 */
	public static Set<Class<?>> getClassSet(String packageName){
		// 加载指定包下的类之前先清空类集合，注意只能有一个类集合
		class_set.clear();
		
		try {
			Enumeration<URL> urls = getClassloder().getResources(packageName.replace(".", "/"));
			logger.debug("tinyFramework:=====  scanner package :{}",packageName);
			while(urls.hasMoreElements()) {
				URL url = urls.nextElement();
				String protocol = url.getProtocol();
				if(protocol.equals("file")) {
					addClass(class_set, url.getPath(), packageName);
				} else if(protocol.equals("jar")){
					// TODO jar处理
				}
			}
		} catch (Exception e) {
			logger.debug("tinyFramework:=====  load class fail , packagename = {} , error :{} ",  packageName , e );
			e.printStackTrace();
		}
		
		// 初始化各类注解的类集合
		initAnnotationSet();
		return class_set;
	}
	
	public static void initAnnotationSet() {
		bean_set = getClassSetByAnnotation(Bean.class);
	}
	
	/**
	 * 加载指定包下的类
	 * @param set
	 * @param packagePath
	 * @param packageName
	 */
	public static void addClass(Set<Class<?>> set, String packagePath, String packageName) {
		File[] files = new File(packagePath).listFiles();
		for(File file : files) {
			String fileName = file.getName();
			if(file.isFile()) {
				fileName = fileName.substring(0 , fileName.lastIndexOf('.'));
				String className = packageName + "." + fileName;
				try {
					Class<?> cls = getClassloder().loadClass(className);
					set.add(cls);
				} catch (ClassNotFoundException e) {
					logger.error("tinyFramework:=====  load class: {} fail , error : {}" , className , e);
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 获取框架中默认包的类
	 */
	public static Set<Class<?>> getDefaultClassSet() {
		return getClassSet(PropUtil.getField(TinyConstant.app_basepackage));
	}
	
	/*
	 * 获取指定类的所有子类
	 */
	public static Set<Class<?>> getClassSetBySuper(Class<?> superClass){
		Set<Class<?>> set = new HashSet<>();
		for(Class<?> cls : class_set) {
			if(superClass.isAssignableFrom(cls)) {
				set.add(cls);
			}
		}
		return set;
	}
	
	/**
	 * 获取带有指定注解的所有类
	 */
	public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotation){
		Set<Class<?>> set = new HashSet<>();
		for(Class<?> cls : class_set) {
			if(cls.isAnnotationPresent(annotation)) {
				set.add(cls);
			}
		}
		return set;
	}

	public static Set<Class<?>> getBean_set() {
		return bean_set;
	}

}

