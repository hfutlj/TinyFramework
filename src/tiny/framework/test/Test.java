
package tiny.framework.test;

import java.util.Set;

import tiny.framework.constant.TinyConstant;
import tiny.framework.core.BeanHelper;
import tiny.framework.core.ClassHelper;
import tiny.framework.core.PropUtil;
import tiny.framework.core.TinyClassLoader;
import tiny.framework.ioc.IOCEngine;

public class Test {
	
	@org.junit.Test
	public void testProp() {
		System.out.println(PropUtil.getField(TinyConstant.app_basepackage));
	}
	
	@org.junit.Test
	public void testBeanload() {
		Set<Class<?>> set = ClassHelper.getDefaultClassSet();
		for(Class<?> cls : set) {
			System.out.println(cls.getName());
		}
	}
	
	@org.junit.Test
	public void testIoc() throws ClassNotFoundException {
		TinyClassLoader.init();
		TestBean testBean = (TestBean) BeanHelper.getBeanMap().get(Class.forName("tiny.framework.test.TestBean"));
		testBean.show();
	}
	
}
