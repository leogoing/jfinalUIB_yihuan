package little.ant.platform.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;

import little.ant.platform.annotation.Controller;
import little.ant.platform.interceptor.UnitPowerInterceptor;
import little.ant.platform.model.DevPart;
import little.ant.platform.model.DevType;
import little.ant.platform.service.DevPartService;
import little.ant.platform.service.DevTypeService;
import little.ant.platform.validator.UnitValidator;

/**
 * 部件管理
 * @author 鞠玉翔
 */
@SuppressWarnings("unused")
@Controller(controllerKey = "/jf/platform/devtype")
public class DevTypeController extends BaseController {
	
	private static Logger log = Logger.getLogger(DevTypeController.class);

	/**
	 * 列表
	 */
	@Before(UnitPowerInterceptor.class)
	public void index() {
		List<DevPart> devParts=
				DevPartService.service.findPartTypeByUnitIdss((List<String>)getSessionAttr(getCUserIds()+"4unit"));
		List<Object> idss=new ArrayList<Object>();
		for(DevPart devpart : devParts){
			idss.add(devpart.getStr("parttypeids"));
		}
		DevTypeService.service.list2in(splitPage, "ids", idss);
//		DevTypeService.service.list(splitPage);
		render("/platform/devtype/list.html");
	}
	
	/**
	 * 保存
	 */
	@Before(UnitValidator.class)
	public void save() {
		ids = DevTypeService.service.save(getModel(DevType.class));
		redirect("/jf/platform/devtype");
	}

	/**
	 * 准备更新
	 */
	public void edit() {
		setAttr("devtype", DevType.dao.findById(getPara()));
		render("/platform/devtype/update.html");
	}

	/**
	 * 更新
	 */
	@Before(UnitValidator.class)
	public void update() {
		DevTypeService.service.update(getModel(DevType.class));
		redirect("/jf/platform/devtype");
	}
	
	/**
	 * 删除
	 */
	public void delete() {
		DevTypeService.service.delete(getPara());
		redirect("/jf/platform/devtype");
	}

	/**
	 * 类型treeData
	 */
	public void treeData()  {
		String jsonText = DevTypeService.service.childNodeData(ids);
		renderJson(jsonText);
	}
}


