package tiny.framework.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tiny.framework.annotation.Action;
import tiny.framework.annotation.Controller;
import tiny.framework.annotation.JsonResponse;
import tiny.framework.core.BeanHelper;

public class ControllerHelper {
	
	private static final Map<TinyRequest, TinyHandler> Action_Map = new HashMap<>();
	private static final Logger logger = LoggerFactory.getLogger(ControllerHelper.class);

	static {
		Map<Class<?>, Object> bean_map = BeanHelper.getBeanMap();
		
		for(Entry<Class<?>,Object> entry : bean_map.entrySet()) {
			// ��ȡ���е�controller��
			Class<?> cls = entry.getKey();
			Object instance = entry.getValue();
			if(cls.isAnnotationPresent(Controller.class)) {
				// ��ȡ���еķ������ҳ�����action�ķ���
				Method[] methods = cls.getDeclaredMethods();
				for(Method method :methods) {
					if(method.isAnnotationPresent(Action.class)) {
						Action action = method.getAnnotation(Action.class);
						String mapping = action.value();
						TinyRequest request = new TinyRequest();
						TinyHandler handler = new TinyHandler(instance , method);
						if (method.isAnnotationPresent(JsonResponse.class)
								|| cls.isAnnotationPresent(JsonResponse.class)) {
							handler.setJsonResponse(true);
						}
						// Action����д����Ϊ�� @Action("GET:/login")
						if(mapping.contains(":")) {
							String args[] = mapping.split(":");
							if(args.length != 2) {
								logger.error("tinyFramework:=====  : illegal mapping {} , in class {} , method {}" , mapping , cls.getName() , method.getName());
								throw new RuntimeException("illegal mapping : " + mapping);
							}
							request.setMethodType(args[0]);
							request.setPath(args[1]);
						}else {
							// Ĭ��ʹ��GET����
							request = new TinyRequest("GET" , mapping);
							handler = new TinyHandler(instance, method);
						}
						Action_Map.put(request, handler);
						logger.debug("tinyFramework:=====  : add action : {} " , mapping);
					}
				}
			}
		}
//		for(TinyRequest tinyRequest : Action_Map.keySet()) {
//			System.out.println(tinyRequest);
//		}
	}
	
	public static Map<TinyRequest, TinyHandler> getActionMap() {
		return Action_Map;
	}
	

}
