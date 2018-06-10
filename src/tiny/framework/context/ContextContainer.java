package tiny.framework.context;

/**
 * 上下文容器，为每一次请求保存一次上下文，线程安全
 * @author lee
 */
public class ContextContainer {
	
	public static final ThreadLocal<TinyContext> CONTEXTS = new ThreadLocal<>();
	
	
	public static void addTinyContext(TinyContext tinyContext) {
		CONTEXTS.set(tinyContext);
	}
	
	public static TinyContext geTinyContext() {
		return CONTEXTS.get();
	}
	
	/**
	 * 删除当前线程的上下文对象，释放资源
	 */
	public static void distory() {
		CONTEXTS.remove();
	}

}
