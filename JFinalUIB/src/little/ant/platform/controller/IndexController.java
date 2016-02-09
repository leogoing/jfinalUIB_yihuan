package little.ant.platform.controller;

import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;

import little.ant.platform.annotation.Controller;
import little.ant.platform.interceptor.UnitPowerInterceptor;
import little.ant.platform.model.Department;
import little.ant.platform.model.Menu;
import little.ant.platform.model.Systems;
import little.ant.platform.model.User;
import little.ant.platform.service.IndexService;
import little.ant.platform.service.ResourcesService;
import little.ant.platform.tools.ToolContext;
import little.ant.platform.tools.ToolSqlXml;

/**
 * 首页处理
 */
@SuppressWarnings("unused")
@Controller(controllerKey = {"/jf/platform/", "/jf/platform/index"})
public class IndexController extends BaseController {

	private static Logger log = Logger.getLogger(IndexController.class);
	
	private List<Systems> systemsList; // 系统列表
	private List<Menu> menuList; // 菜单列表
	
	/**
	 * 首页
	 */
	public void index() {
		log.debug("############################# 进入首页  ##############################");
		User user = ToolContext.getCurrentUser(getRequest(), true); // cookie认证自动登陆处理
		if(null != user){//后台
			String sql = ToolSqlXml.getSql("platform.systems.all");
			systemsList = Systems.dao.find(sql);
			if(null == ids || ids.isEmpty()){ // 默认系统
				ids = "8a40c0353fa828a6013fa898d4ac0020";
			}
			menuList = IndexService.service.menu(ids, user, getI18nPram());
			log.debug("########################## render_index #############################");
			render("/platform/index.html");
		}else{
			render("/platform/login.html");
		}
	}
	
	/**
	 * 首页content 调用地图
	 */
	public void content(){
		log.debug("############################# 进入content  ##############################");
		/*获取系统浏览量和负载*/
		setAttrs(ResourcesService.service.pv());
		setAttrs(ResourcesService.service.getResources());
		/*判断用户是否数据维护人员来决定开启地图操作服务*/
		String departids=User.dao.findById(getCUserIds()).getStr("departmentids");
		String departtype=Department.dao.getStr("depttype");
		if(0==0 || departtype!=null && "some".equals(departtype)){
			render("/platform/contentmap.html"); // 修改用于在iFrame中显示地图
		}else{
			/*可添加其他组件*/
		}
	}
	
	public void map(){
		log.debug("############################# 进入map  ##############################");
		render("/platform/map.html"); // 修改用于在iFrame中显示地图
	}
}
