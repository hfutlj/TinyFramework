package tiny.framework.ioc;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tiny.framework.annotation.Inject;
import tiny.framework.core.BeanHelper;
import tiny.framework.core.ClassHelper;

/**
 * IOC引擎，注入带Inject注解的属性的Bean
 * @author lee
 *
 */
public class IOCEngine {
	
	private static final Logger logger = LoggerFactory.getLogger(IOCEngine.class);
	
	static {
		Map<Class<?>, Object> map = BeanHelper.getBeanMap();
		for (Entry<Class<?>, Object> entry : map.entrySet()) {
			Class<?> cls = entry.getKey();
			Object instance = entry.getValue();
			// 获取所有的声明的属性
			Field[] fields = cls.getDeclaredFields();
			// 找出带有注解的属性
			for (Field field : fields) {
				if (field.isAnnotationPresent(Inject.class)) {
					// 获取注入类的类型
					Class<?> interfaceType = field.getType();
					// 查看是否存在实现类
					Object implementClass = map.get(interfaceType);
					if (implementClass == null) {
						logger.error("tinyFramework:=====  inject field :" + field.getName() + "Fail , can't find implment class ");
						throw new RuntimeException(
								"注入属性" + field.getName() + "失败，未能找到实现类： " + interfaceType.getName());
					} else {
						field.setAccessible(true);
						try {
							field.set(instance, implementClass);
						} catch (IllegalArgumentException | IllegalAccessException e) {
							logger.error("tinyFramework:===== inject field:"+field.getName()+" fail :" + e);
							e.printStackTrace();
						} 
					}

				}
			}
		}
		logger.debug("tinyFramework:=====  ioc map " + map);
	}
	
	

}
