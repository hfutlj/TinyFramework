package tiny.framework.mvc;


import java.lang.reflect.Method;

/**
 * request�Ĵ����࣬��Ӧ�ķ������Ƿ���json��ʽ����
 * @author lijun
 *
 */
public class TinyHandler {
	
	private Object controllerClass;
	
	private Method actionMethod;
	
	private boolean jsonResponse;

	public TinyHandler() {
		super();
	}
	
	public TinyHandler(Object controllerClass, Method actionMethod) {
		this.controllerClass = controllerClass;
		this.actionMethod = actionMethod;
		this.jsonResponse = false;
	}

	public Object getControllerClass() {
		return controllerClass;
	}

	public void setControllerClass(Object controllerClass) {
		this.controllerClass = controllerClass;
	}

	public Method getActionMethod() {
		return actionMethod;
	}

	public void setActionMethod(Method actionMethod) {
		this.actionMethod = actionMethod;
	}

	public boolean isJsonResponse() {
		return jsonResponse;
	}

	public void setJsonResponse(boolean jsonResponse) {
		this.jsonResponse = jsonResponse;
	}
	
	

}
