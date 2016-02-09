package little.ant.platform.controller;

import java.util.List;
import java.util.Map;

import little.ant.platform.annotation.Controller;
import little.ant.platform.model.Unit;
import little.ant.platform.model.DevVendor;
import little.ant.platform.service.DevTypeService;
import little.ant.platform.service.UnitPicService;
import little.ant.platform.service.DevVendorService;
import little.ant.platform.validator.UnitPicValidator;
import little.ant.platform.validator.UnitValidator;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;

/**
 * 部件管理
 * @author 鞠玉翔
 */
@SuppressWarnings("unused")
@Controller(controllerKey = "/jf/platform/devvendor")
public class DevVendorController extends BaseController {
	
	private static Logger log = Logger.getLogger(DevVendorController.class);

	/**
	 * 列表
	 */
	public void index() {
		DevVendorService.service.list(splitPage);
		render("/platform/devvendor/list.html");
	}
	
	/**
	 * 保存
	 */
	@Before(UnitValidator.class)
	public void save() {
		ids = DevVendorService.service.save(getModel(DevVendor.class));
		redirect("/jf/platform/devvendor");
	}

	/**
	 * 准备更新
	 */
	public void edit() {
		setAttr("devvendor", DevVendor.dao.findById(getPara()));
		render("/platform/devvendor/update.html");
	}

	/**
	 * 更新
	 */
	@Before(UnitValidator.class)
	public void update() {
		DevVendorService.service.update(getModel(DevVendor.class));
		redirect("/jf/platform/devvendor");
	}
	
	/**
	 * 删除
	 */
	public void delete() {
		DevVendorService.service.delete(getPara());
		redirect("/jf/platform/devvendor");
	}

	/**
	 * 类型treeData
	 */
	public void treeData()  {
		String jsonText = DevVendorService.service.childNodeData(ids);
		renderJson(jsonText);
	}
}


