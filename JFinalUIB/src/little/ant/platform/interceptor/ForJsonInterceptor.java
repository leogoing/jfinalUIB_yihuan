package little.ant.platform.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

import little.ant.platform.exchange.DepartIdVer;
import little.ant.platform.exchange.UnitIdVer;
import little.ant.platform.exchange.UserIdVer;
import net.sf.json.JSONArray;

/**
 * json转换
 * @author 李望10.23
 * 愁愁愁，白了少年头
 */
public class ForJsonInterceptor implements Interceptor{

	private static Logger log = Logger.getLogger(ForJsonInterceptor.class);
	
	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller=ai.getController();
		String url=controller.getRequest().getServletPath();
		log.debug(("请求路径为："+url));
		String lastUrl=url.substring(url.lastIndexOf("/"));
		if("/checkForUnit".equals(lastUrl)){
			if(conversion(controller,"unitJson",UnitIdVer.class)){
				ai.invoke();
			}	
		}else if("/checkForUser".equals(lastUrl)){
			if(conversion(controller,"userJson",UserIdVer.class) && conversion(controller,"departJson",DepartIdVer.class)){
				log.debug("url:checkFor,用户版本json对象转换完毕！");
				ai.invoke();
			}
		}else if("/sendPack".equals(lastUrl)){
			if(conversion(controller,"json",Long.class)){
				ai.invoke();
			}
		}
		
	}
	
	/**
	 * 转换json字符串为java对象
	 * @param controller
	 * @param param
	 * @param clazz
	 * @return
	 */
	public boolean conversion(Controller controller,String param,Class clazz){
		String jsonStr=controller.getPara(param);
		log.debug("获得请求参数"+param+"字符串："+jsonStr);
		if(jsonStr==null || "".equals(jsonStr)){
			handlError(2,param+"参数为空！",controller);
			return false;
		}else{
			List jsonList=null;
			String servletPath=controller.getRequest().getServletPath();
			try {
				JSONArray json=JSONArray.fromObject(jsonStr);
				jsonList=JSONArray.toList(json,clazz);
			} catch (Exception e) {
				log.error(param+"转换异常！", e);
				handlError(3,param+"转换异常！",controller);
				return false;
			}
			controller.setAttr(param, jsonList);
			return true;
		}
	}
	
	public static void handlError(int status,String mes,Controller controller){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("message", mes);
		map.put("status", status);
		controller.renderJson("result",map);
	}
	
	public static void main(String[] args) {
		String a="akhjhdfdnbmx";
		
		System.out.println(1==new Long(1l));
	}
	
}
