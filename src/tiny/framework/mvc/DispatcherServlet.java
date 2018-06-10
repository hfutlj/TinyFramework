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
 * ���Ĭ��servlet���������������ҵ���Ӧ��tinyHandler���д���
 * 
 * @author lijun
 *
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "*.do", loadOnStartup = 3)
public class DispatcherServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		//��ʼ�����
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
		logger.debug("�յ�����{}:{}", curMethodType, curRequestPath);
		// / ת��Homeҳ
		if (curRequestPath.equals("/")) {

		}
		// ȥ����β��/
		if (curRequestPath.endsWith("/")) {
			curRequestPath = curMethodType.substring(0, curRequestPath.length() - 1);
		}
		//TinyHandler tinyHandler = TinyHandlerMapping.getHandler(curMethodType, curRequestPath);
		TinyRequest tinyRequest = new TinyRequest(curMethodType, curRequestPath);
		TinyHandler tinyHandler = ControllerHelper.getActionMap().get(tinyRequest);
		// �Ҳ�����Ӧ�Ĵ����࣬����404ҳ��
		if (tinyHandler == null) {
			logger.debug("�Ҳ�������·����{}:{} ��Ӧ�Ĵ�����", curMethodType, curRequestPath);
			//WebUtil.sendError(404, "�Ҳ���������Դ", curResponse);
		} else {
			TinyHandlerInvoker.invoke(curRequest,curResponse, tinyHandler);
		}
		
		afterService();

	}
	
	/**
	 * ��service����ִ��֮ǰ�Ĳ��������ñ��롢���������ĵ�
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
	 * �˳�service��Ĳ������ͷ���������Դ��
	 */
	private void afterService() {
		// TODO
//		ContextContainer.distory();
	}

}
