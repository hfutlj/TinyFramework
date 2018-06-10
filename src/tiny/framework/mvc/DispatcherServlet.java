package tiny.framework.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tiny.framework.core.TinyClassLoader;
import tiny.framework.core.TinyHandlerInvoker;
/**
 * 框架默认servlet，拦截所有请求找到对应的tinyHandler进行处理
 * 
 * @author lijun
 *
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "*.do", loadOnStartup = 3)
public class DispatcherServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		//初始化框架
		TinyClassLoader.init();
		
	}

	private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

	@Override
	protected void service(HttpServletRequest curRequest, HttpServletResponse curResponse)
			throws ServletException, IOException {
		beforeService(curRequest, curResponse);
		
		String curMethodType = curRequest.getMethod().toLowerCase();
		String curRequestPath = curRequest.getServletPath();
		String pathInfo = curRequest.getPathInfo();
		if (!StringUtils.isEmpty(pathInfo)) {
			curRequestPath = curRequestPath + pathInfo;
		}
		logger.debug("收到请求{}:{}", curMethodType, curRequestPath);
		// / 转到Home页
		if (curRequestPath.equals("/")) {

		}
		// 去掉结尾的/
		if (curRequestPath.endsWith("/")) {
			curRequestPath = curMethodType.substring(0, curRequestPath.length() - 1);
		}
		//TinyHandler tinyHandler = TinyHandlerMapping.getHandler(curMethodType, curRequestPath);
		TinyRequest tinyRequest = new TinyRequest(curMethodType, curRequestPath);
		TinyHandler tinyHandler = ControllerHelper.getActionMap().get(tinyRequest);
		// 找不到对应的处理类，跳到404页面
		if (tinyHandler == null) {
			logger.debug("找不到请求路径：{}:{} 对应的处理类", curMethodType, curRequestPath);
			//WebUtil.sendError(404, "找不到请求资源", curResponse);
		} else {
			TinyHandlerInvoker.invoke(curRequest,curResponse, tinyHandler);
		}
		
		afterService();

	}
	
	/**
	 * 在service方法执行之前的操作，设置编码、创建上下文等
	 */
	private void beforeService(HttpServletRequest request, HttpServletResponse response) {
		// TODO 
//		try {
//			request.setCharacterEncoding(PropUtil.getField(TinyConstant.DEF_ENCODING , TinyConstant.DEF_ENCODING));
//			response.setCharacterEncoding(TinyConstant.RESPONSE_ENCODE);
//			TinyContext tinyContext = new TinyContext(request, response);
//			ContextContainer.addTinyContext(tinyContext);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 退出service后的操作，释放上下文资源等
	 */
	private void afterService() {
		// TODO
//		ContextContainer.distory();
	}

}
