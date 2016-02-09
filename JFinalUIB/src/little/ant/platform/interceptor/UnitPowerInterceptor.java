package little.ant.platform.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

import little.ant.platform.model.Unit;
import little.ant.platform.service.DepartmentService;
import little.ant.platform.service.UserService;
import little.ant.platform.tools.ToolSqlXml;
import little.ant.platform.tools.ToolUtils;

/**
 * 用户权限拦截
 * @author 李望
 *
 */
public class UnitPowerInterceptor implements Interceptor{
	
	private static Logger log = Logger.getLogger(UnitPowerInterceptor.class);
	
	/**
	 * 方式一
	 * 检查重点单位session没有则查询创建
	 * @param controller
	 */
	public void checkSession(Controller controller,String actionKey,ActionInvocation invocation){
		String userIds=controller.getAttrForStr("cUserIds");
		List<String> unitIds=controller.getSessionAttr(userIds+"4unit");
		log.debug("已保存在session中的unitIds："+unitIds);
		if(unitIds==null){
			/*通过用户id查找重点单位并存到session*/
			String departIds=UserService.service.findDeptIds(userIds);
			List<String> d=new ArrayList<String>();
			d.add(departIds);
			List<String> departIdss=DepartmentService.service.findChildDeparts(d);
			departIdss.add(departIds);
			log.debug("查找当前用户权限内的部门departIdss:"+departIdss);
			if("/unit".equals(actionKey)){
				controller.setAttr("departIdss", departIdss);
				invocation.invoke();
			}
			String sql=ToolSqlXml.getSql("platform.unit.all")+" where deptids in ("+ToolUtils.appendSql(departIdss);
			List<Unit> units=Unit.dao.find(sql);
			List<String> unitIdss=new ArrayList<String>();
			for(Unit unit:units){
				unitIdss.add(unit.getPKValue());
			}
			log.debug("保存当前用户权限内的unitIdss到session："+unitIdss);
			controller.setSessionAttr(userIds+"4unit", unitIdss);
			if(!"/unit".equals(actionKey)){
				invocation.invoke();
			}
		}else{
			invocation.invoke();
		}
	}
	
	/**
	 * 方式二
	 * 检查部门session没有则查询创建
	 * @param controller
	 * @param actionKey
	 * @param invocation
	 */
	public void checkSession2(Controller controller,String actionKey,ActionInvocation invocation){
		String userIds=controller.getAttrForStr("cUserIds");
		List<String> deptIds=controller.getSessionAttr(userIds+"4unit");
		log.debug("已保存在session中的部门Ids："+deptIds);
		if(deptIds==null){
			/*通过用户id查找部门并存到session*/
			String departIds=UserService.service.findDeptIds(userIds);
			List<String> d=new ArrayList<String>();
			d.add(departIds);
			List<String> departIdss=DepartmentService.service.findChildDeparts(d);
			departIdss.add(departIds);
			log.debug("查找当前用户权限内的部门departIdss:"+departIdss);
			controller.setSessionAttr(userIds+"4unit", departIdss);
		}
		invocation.invoke();
	}

	@Override
	public void intercept(ActionInvocation invocation) {
		String actionKey=invocation.getActionKey();
		Controller controller=invocation.getController();
//		checkSession(controller,actionKey,invocation);
		checkSession2(controller,actionKey,invocation);
	}
	
}
