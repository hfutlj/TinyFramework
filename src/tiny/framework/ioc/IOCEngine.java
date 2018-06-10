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
 * IOC���棬ע���Injectע������Ե�Bean
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
			// ��ȡ���е�����������
			Field[] fields = cls.getDeclaredFields();
			// �ҳ�����ע�������
			for (Field field : fields) {
				if (field.isAnnotationPresent(Inject.class)) {
					// ��ȡע���������
					Class<?> interfaceType = field.getType();
					// �鿴�Ƿ����ʵ����
					Object implementClass = map.get(interfaceType);
					if (implementClass == null) {
						logger.error("tinyFramework:=====  inject field :" + field.getName() + "Fail , can't find implment class ");
						throw new RuntimeException(
								"ע������" + field.getName() + "ʧ�ܣ�δ���ҵ�ʵ���ࣺ " + interfaceType.getName());
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
