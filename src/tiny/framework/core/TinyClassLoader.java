package tiny.framework.core;

import tiny.framework.ioc.IOCEngine;

/**
 * �涨��ܳ�ʼ��˳��
 * @author lee
 *
 */
public class TinyClassLoader {
	
	public static void init() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Class<?> cls[] = {
				ClassHelper.class,
				BeanHelper.class,
//				AopHelper.class,
				IOCEngine.class,
//				ControllerHelper.class
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
