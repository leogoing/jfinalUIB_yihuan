package little.ant.platform.controller;

import java.util.List;
import java.util.Map;

import little.ant.platform.annotation.Controller;
import little.ant.platform.model.Unit;
import little.ant.platform.model.DevType;
import little.ant.platform.service.StationService;
import little.ant.platform.service.UnitPicService;
import little.ant.platform.service.DevTypeService;
import little.ant.platform.validator.UnitPicValidator;
import little.ant.platform.validator.UnitValidator;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;

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
	public void index() {
		DevTypeService.service.list(splitPage);
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


