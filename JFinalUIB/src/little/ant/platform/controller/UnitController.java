package little.ant.platform.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;

import little.ant.platform.annotation.Controller;
import little.ant.platform.interceptor.UnitPowerInterceptor;
import little.ant.platform.model.Unit;
import little.ant.platform.service.UnitService;
import little.ant.platform.validator.UnitValidator;

/**
 * 重点单位管理
 * @author 鞠玉翔
 */
@SuppressWarnings("unused")
@Controller(controllerKey = "/jf/platform/unit")
public class UnitController extends BaseController {
	
	private static Logger log = Logger.getLogger(UnitController.class);

	public static String querydeptids = ""; // 保存页面的检索条件，以免返回页面时检索条件被清空
	public static String querydeptname = "";
	
	/**
	 * 列表(带权限)
	 */
	@Before(UnitPowerInterceptor.class)
	public void index() {
		/*List<Object> deptIds=getAttr("departIdss");
		if(deptIds!=null){
			UnitService.service.list2in(splitPage,"deptids",deptIds, getResponse(), getRequest());
		}else{
			UnitService.service.list2in(splitPage,"ids",(List<Object>)getSessionAttr(getCUserIds()+"4unit"), getResponse(), getRequest());
		}*/
//		UnitService.service.list(splitPage, getResponse(), getRequest());
		UnitService.service.list2in(splitPage,(List<Object>)getSessionAttr(getCUserIds()+"4unit"), getResponse(), getRequest());
		render("/platform/unit/list.html");
	}
	
	/**
	 * 发布单个新版本
	 */
	public void addVer2One(){
		UnitService.service.updateVer("ids", getPara());
		redirect("/jf/platform/unit");
	}
	
	/**
	 * 发布部门新版本
	 */
	public void addVer2Dept(){
		UnitService.service.updateVer("deptids", getPara());
		redirect("/jf/platform/unit");
	}
	
	public void query() {
		Map<String, String> map = splitPage.getQueryParam();
		querydeptids = map.get("deptids"); // 记录查询的部门条件
		querydeptname = map.get("deptname");
		
		index();
	}

	/**
	 * 保存
	 */
	@Before(UnitValidator.class)
	public void save() {
		ids = UnitService.service.save(getModel(Unit.class));
		redirect("/jf/platform/unit");
	}

	/**
	 * 准备更新
	 */
	public void edit() {
		setAttr("unit", Unit.dao.findById(getPara()));
		setAttr("levelList", UnitService.service.getPics(getPara()));
		render("/platform/unit/update.html");
	}
	
	/**
	 * 地图上兴趣点被点击，准备更新
	 */
	public void mapedit() {
		setAttr("unit", Unit.dao.findById(getPara()));
		render("/platform/unit/mapupdate.html");
	}

	/**
	 * 更新
	 * @throws 
	 */
	@Before(UnitValidator.class)
	public void update() {
		UnitService.service.update(getModel(Unit.class));
		redirect("/jf/platform/unit");
	}
	
	public void updatemap() {
		UnitService.service.update(getModel(Unit.class));
	}
	
	public void updatepos() {
		UnitService.service.updatepos(getPara(1), getPara(2), getPara(2));
	}
	
	/**
	 * 删除
	 */
	public void delete() {
		UnitService.service.delete(getPara());
		redirect("/jf/platform/unit");
	}
	
	/**
	 * tree节点数据
	 */
	public void treeData() {
		String jsonText = UnitService.service.childNodeData(ids);
		renderJson(jsonText);
	}
	
	public static void main(String[] args) {
		long[] a={1};
//		List b=new ArrayList();
//		b.add(a);
//		long s=((Long)b.get(0)).longValue();
		System.out.println("dd");
	}

}


