package tiny.framework.mvc;


/**
 * Action���͵ķ����Ĳ��������󷽷����ͣ�get,post...��������·��
 * 
 * @author lijun
 *
 */
public class TinyRequest {
	
	private String methodType;
	private String path;
	
	public TinyRequest() {
		super();
	}
	
	public TinyRequest(String methodType, String path) {
		super();
		this.methodType = methodType;
		this.path = path;
	}
	
	public String getMethodType() {
		return methodType;
	}
	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return "{" +this.methodType + ":" + this.path + "}";
	}
	

}

