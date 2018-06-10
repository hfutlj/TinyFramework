package tiny.framework.mvc;


import java.lang.reflect.Method;

/**
 * request的处理类，对应的方法，是否以json格式返回
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
