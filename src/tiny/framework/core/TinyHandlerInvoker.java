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
 * Handler��ʵ�ʵ���
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
			// �ļ��ϴ�����
			params = FileHelper.getMultipartParamList(request, handler);
		} else {
			params = getParamList(request, handler);
		}
		try {

			logger.debug("���ò���{}", params);
			Object result = method.invoke(instance, params.toArray());
		//	ViewResolver.resolve(request, response, result);
		} catch (Exception e) {
			logger.error("tinyFramework:=====  ,fail to invoke method {} , cause {}" , method.getName() ,e);
			System.out.println(e);
		}
	}

	/**
	 * ��ȡrequest����������б� TODO �����ܻ�ȡString=String���͵ģ�����������ͺ�put��delete����δд
	 */
	public static List<Object> getParams(HttpServletRequest request, TinyHandler handler) {
		List<Object> paramList = new ArrayList<>();

		/*
		 * // TODO ���ַ�����÷������β���ֻ���������-parameter�ı��뷽ʽ�»�ã�ֻ��1.8֧�� // ����ᱨ�����Ľ� Method
		 * method = handler.getActionMethod(); Enumeration<String> enumeration =
		 * request.getParameterNames(); for(Parameter parameter :
		 * method.getParameters()) { String paramName = parameter.getName(); String
		 * value = request.getParameter(paramName); if(!StringUtils.isEmpty(value)) {
		 * paramList.add(value); } }
		 */

		// ʹ��javassist��÷����β�
		ClassPool pool = ClassPool.getDefault();
		try {
			CtClass ctClass = pool.get(handler.getControllerClass().getClass().getName());
			CtMethod ctMethod = ctClass.getDeclaredMethod(handler.getActionMethod().getName());
			MethodInfo methodInfo = ctMethod.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);
			// �жϸ÷����Ƿ�Ϊ��̬��������̬�����ĵ�һ������Ϊthis����ȡ�����б�ʱ��Ҫ��һ
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
			System.err.println("javassist������ȡ�β�ʧ��" + e);
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
	 * ��ȡ���������б�
	 * @param request
	 * @param handler
	 * @return
	 */
	public static List<Object> getParamList(HttpServletRequest request , TinyHandler handler){
		LinkedHashMap<String, Class<?>> nameTypeMap = getNameTypeMap(handler);
		List<Object> paramList = new ArrayList<>();
		// Multipart���ͱ����ļ��ϴ�
		if(ServletFileUpload.isMultipartContent(request)) {
			
		} else {
			// ��ͨ��������ȡ
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
	 * ʹ��javassis������ȡ���õķ������β�-���� ӳ���ϵ
	 * @param handler
	 * @return ��������Map��˳���뷽������Ĳ���˳��һ��
	 */
	public static LinkedHashMap<String, Class<?>> getNameTypeMap(TinyHandler handler) {
		LinkedHashMap<String, Class<?>> nameTypeMap = new LinkedHashMap<>();
		// ʹ��javassist��÷����β�
		ClassPool pool = ClassPool.getDefault();
		Method method = handler.getActionMethod();
		try {
			CtClass ctClass = pool.get(handler.getControllerClass().getClass().getName());
			CtMethod ctMethod = ctClass.getDeclaredMethod(method.getName());
			MethodInfo methodInfo = ctMethod.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);
			// �жϸ÷����Ƿ�Ϊ��̬��������̬�����ĵ�һ������Ϊthis����ȡ�����б�ʱ��Ҫ��һ
			int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
			for (int i = 0; i < ctMethod.getParameterTypes().length; i++) {
				String name = attr.variableName(i + pos);
				Class<?> type = method.getParameterTypes()[i];
				nameTypeMap.put(name, type);
			}

		} catch (NotFoundException e) {
			logger.error("tinyFramework:=====  get method params fail; in {} , method : {}",
					handler.getControllerClass().getClass().getName(), method.getName() );
		//	System.err.println("javassist������ȡ�β�ʧ��" + e);
			e.printStackTrace();
		}
		return nameTypeMap;
	}
	

	
}
