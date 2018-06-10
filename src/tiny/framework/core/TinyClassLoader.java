package tiny.framework.core;

import tiny.framework.aop.AopHelper;
import tiny.framework.ioc.IOCEngine;
import tiny.framework.mvc.ControllerHelper;

/**
 * 规定框架初始化顺序
 * @author lee
 *
 */
public class TinyClassLoader {
	
	public static void init() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Class<?> cls[] = {
				ClassHelper.class,
				BeanHelper.class,
				AopHelper.class,
				IOCEngine.class,
				ControllerHelper.class
		};
		for(int i=0;i<cls.length;i++) {
			try {
				Class.forName(cls[i].getName(), true, classLoader);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
