package little.ant.platform.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;

import little.ant.platform.annotation.Controller;
import little.ant.platform.interceptor.UnitPowerInterceptor;
import little.ant.platform.model.DevPart;
import little.ant.platform.model.DevVendor;
import little.ant.platform.service.DevPartService;
import little.ant.platform.service.DevVendorService;
import little.ant.platform.validator.UnitValidator;

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
	@Before(UnitPowerInterceptor.class)
	public void index() {
		List<DevPart> devPartIdss=
				DevPartService.service.findVendorsByUnitIdss((List<String>)getSessionAttr(getCUserIds()+"4unit"));
		List<Object> idss=new ArrayList<Object>();
		for(DevPart devp :devPartIdss){
			idss.add(devp.getStr("partvendorids"));
		}
		DevVendorService.service.list2in(splitPage, "ids", idss);
//		DevVendorService.service.list(splitPage);
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


