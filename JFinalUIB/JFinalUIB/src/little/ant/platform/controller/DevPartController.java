package little.ant.platform.controller;

import java.util.List;
import java.util.Map;

import little.ant.platform.annotation.Controller;
import little.ant.platform.model.Unit;
import little.ant.platform.model.DevPart;
import little.ant.platform.service.UnitPicService;
import little.ant.platform.service.DevPartService;
import little.ant.platform.validator.UnitPicValidator;
import little.ant.platform.validator.UnitValidator;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;

/**
 * 部件管理
 * @author 鞠玉翔
 */
@SuppressWarnings("unused")
@Controller(controllerKey = "/jf/platform/devpart")
public class DevPartController extends BaseController {
	
	private static Logger log = Logger.getLogger(DevPartController.class);

	public static String queryunitids = ""; // 保存页面的检索条件，以免返回页面时检索条件被清空
	public static String queryunitname = "";
	public static String querylevelids = "";
	public static String querylevelname = "";
	
	/**
	 * 列表
	 */
	public void index() {
		DevPartService.service.list(splitPage);
		render("/platform/devpart/list.html");
	}
	
	/**
	 * 查询
	 */
	public void query() {
		Map<String, String> map = splitPage.getQueryParam();
		queryunitids = map.get("unitids"); // 记录查询的部门条件
		queryunitname = map.get("unitname");
		querylevelids = map.get("levelids");
		querylevelname = map.get("levelname");
		
		index();
	}

	/**
	 * 保存
	 */
	@Before(UnitValidator.class)
	public void save() {
		ids = DevPartService.service.save(getModel(DevPart.class));
		redirect("/jf/platform/devpart");
	}

	/**
	 * 准备更新
	 */
	public void edit() {
		setAttr("devpart", DevPart.dao.findById(getPara()));
		render("/platform/devpart/update.html");
	}

	/**
	 * 更新
	 */
	@Before(UnitValidator.class)
	public void update() {
		DevPartService.service.update(getModel(DevPart.class));
		redirect("/jf/platform/devpart");
	}
	
	/**
	 * 删除
	 */
	public void delete() {
		DevPartService.service.delete(getPara());
		redirect("/jf/platform/devpart");
	}

}


