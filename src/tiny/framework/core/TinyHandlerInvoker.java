package tiny.framework.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import tiny.framework.mvc.FileHelper;
import tiny.framework.mvc.TinyHandler;
import tiny.framework.tools.ConvertUtil;

/**
 * Handler的实际调用
 * 
 * @author lijun
 *
 */
public class TinyHandlerInvoker {
	private static final Logger logger = LoggerFactory.getLogger(TinyHandlerInvoker.class);

	public static void invoke(HttpServletRequest request, HttpServletResponse response, TinyHandler handler) {
		Object instance = handler.getControllerClass();
		Method method = handler.getActionMethod();
		List<Object> params;
		if (ServletFileUpload.isMultipartContent(request)) {
			// 文件上传请求
			params = FileHelper.getMultipartParamList(request, handler);
		} else {
			params = getParamList(request, handler);
		}
		try {

			logger.debug("调用参数{}", params);
			Object result = method.invoke(instance, params.toArray());
		//	ViewResolver.resolve(request, response, result);
		} catch (Exception e) {
			logger.error("tinyFramework:=====  ,fail to invoke method {} , cause {}" , method.getName() ,e);
			System.out.println(e);
		}
	}

	/**
	 * 获取request的请求参数列表 TODO 仅仅能获取String=String类型的，传入对象类型和put、delete请求还未写
	 */
	public static List<Object> getParams(HttpServletRequest request, TinyHandler handler) {
		List<Object> paramList = new ArrayList<>();

		/*
		 * // TODO 这种方法获得方法的形参名只能在添加了-parameter的编译方式下获得，只有1.8支持 // 否则会报错，待改进 Method
		 * method = handler.getActionMethod(); Enumeration<String> enumeration =
		 * request.getParameterNames(); for(Parameter parameter :
		 * method.getParameters()) { String paramName = parameter.getName(); String
		 * value = request.getParameter(paramName); if(!StringUtils.isEmpty(value)) {
		 * paramList.add(value); } }
		 */

		// 使用javassist获得方法形参
		ClassPool pool = ClassPool.getDefault();
		try {
			CtClass ctClass = pool.get(handler.getControllerClass().getClass().getName());
			CtMethod ctMethod = ctClass.getDeclaredMethod(handler.getActionMethod().getName());
			MethodInfo methodInfo = ctMethod.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);
			// 判断该方法是否为静态方法，静态方法的第一个参数为this，获取参数列表时需要加一
			int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
			for (int i = 0; i < ctMethod.getParameterTypes().length; i++) {
				String key = attr.variableName(i + pos);
				String value = request.getParameter(key);
				if (!StringUtils.isEmpty(value)) {
					paramList.add(value);
				}
			}

		} catch (NotFoundException e) {
			logger.error("tinyFramework:=====  ,fail to get params in class {} ,method {} ",
					handler.getControllerClass().getClass().getName(), handler.getActionMethod().getName());
			System.err.println("javassist方法获取形参失败" + e);
			e.printStackTrace();
		}

		// while(enumeration.hasMoreElements()) {
		// String key = enumeration.nextElement();
		// String value = request.getParameter(key);
		// paramList.add(value);
		// }
		return paramList;
	}

	
	/**
	 * 获取方法参数列表
	 * @param request
	 * @param handler
	 * @return
	 */
	public static List<Object> getParamList(HttpServletRequest request , TinyHandler handler){
		LinkedHashMap<String, Class<?>> nameTypeMap = getNameTypeMap(handler);
		List<Object> paramList = new ArrayList<>();
		// Multipart类型表单，文件上传
		if(ServletFileUpload.isMultipartContent(request)) {
			
		} else {
			// 普通表单参数获取
			for(Entry<String, Class<?>> entry : nameTypeMap.entrySet()) {
				String name = entry.getKey();
				Class<?> type = entry.getValue();
				String[] strValue = request.getParameterValues(name);
				Object objValue = ConvertUtil.convertParam(strValue, type);
				paramList.add(objValue);
			}
		}
		return paramList;
	}
	
	
	/**
	 * 使用javassis方法获取调用的方法的形参-类型 映射关系
	 * @param handler
	 * @return 返回排序Map，顺序与方法定义的参数顺序一致
	 */
	public static LinkedHashMap<String, Class<?>> getNameTypeMap(TinyHandler handler) {
		LinkedHashMap<String, Class<?>> nameTypeMap = new LinkedHashMap<>();
		// 使用javassist获得方法形参
		ClassPool pool = ClassPool.getDefault();
		Method method = handler.getActionMethod();
		try {
			CtClass ctClass = pool.get(handler.getControllerClass().getClass().getName());
			CtMethod ctMethod = ctClass.getDeclaredMethod(method.getName());
			MethodInfo methodInfo = ctMethod.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);
			// 判断该方法是否为静态方法，静态方法的第一个参数为this，获取参数列表时需要加一
			int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
			for (int i = 0; i < ctMethod.getParameterTypes().length; i++) {
				String name = attr.variableName(i + pos);
				Class<?> type = method.getParameterTypes()[i];
				nameTypeMap.put(name, type);
			}

		} catch (NotFoundException e) {
			logger.error("tinyFramework:=====  get method params fail; in {} , method : {}",
					handler.getControllerClass().getClass().getName(), method.getName() );
		//	System.err.println("javassist方法获取形参失败" + e);
			e.printStackTrace();
		}
		return nameTypeMap;
	}
	

	
}
