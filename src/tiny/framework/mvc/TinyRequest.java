package tiny.framework.mvc;


/**
 * Action类型的方法的参数，请求方法类型（get,post...）和请求路径
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
		this.methodType = methodType.toUpperCase();
		this.path = path;
	}
	
	@Override
	public int hashCode() {
		String hash = methodType + "-" + path;
		return hash.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj instanceof TinyRequest) {
			TinyRequest request = (TinyRequest) obj;
			if(this.methodType.equals(request.methodType) &&
					this.path.equals(request.path)) {
				return true;
			}
		}
		return false;
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

