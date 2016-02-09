package little.ant.platform.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import little.ant.platform.annotation.MyTxProxy;
import little.ant.platform.common.DictKeys;
import little.ant.platform.common.SplitPage;
import little.ant.platform.model.Unit;
import little.ant.platform.tools.ToolUtils;
import little.ant.platform.model.DevPart;

import org.apache.log4j.Logger;

public class DevPartService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(DevPartService.class);

	public static final DevPartService service = MyTxProxy.newProxy(DevPartService.class);
	
	/**
	 * 批量通过unitIds查找厂家ids
	 * @return
	 * @author 李望
	 */
	public List<DevPart> findVendorsByUnitIdss(List<String> unitIdss){
		String sql="select a.partvendorids from pt_devpart as a,(select ids from pt_unit where deptids in ("+
				ToolUtils.appendSql(unitIdss)+") as b where a.unitids = b.ids";
		return DevPart.dao.find(sql);
	}
	
	/**
	 * 批量通过unitIds查找类型
	 * @param unitIdss
	 * @return
	 * @author 李望
	 */
	public List<DevPart> findPartTypeByUnitIdss(List<String> unitIdss){
		String sql="select a.parttypeids from pt_devpart as a,(select ids from pt_unit where deptids in ("+
				ToolUtils.appendSql(unitIdss)+") as b where a.unitids = b.ids";
		return DevPart.dao.find(sql);
	}
	
	/**
	 * 批量通过unitIds查找所有
	 * @param unitIdss
	 * @return
	 * @author 李望
	 */
	public List<DevPart> findByUnitIdss(List<String> unitIdss){
		String sql="select * from pt_devpart where unitids in ("+ToolUtils.appendSql(unitIdss);
		return DevPart.dao.find(sql);
	}
	
	/**
	 * 保存
	 * @param unit
	 * @return
	 */
	public String save(DevPart devpart) {
		// 保存
		devpart.save();
		return devpart.getPKValue();
	}

	/**
	 * 更新
	 * @param unit
	 */
	public void update(DevPart devpart){
		// 更新
		devpart.update();
		
		// 缓存
		DevPart.dao.cacheAdd(devpart.getPKValue());
	}

	/**
	 * 删除
	 * @param unit
	 */
	public void delete(String devPartIds){
		// 缓存
		DevPart.dao.cacheRemove(devPartIds);
		
		// 删除
		DevPart.dao.deleteById(devPartIds);
	}

	/**
	 * 分页
	 * @param splitPage
	 */
	public void list(SplitPage splitPage){
		String select = " select * ";
		splitPageBase(DictKeys.db_dataSource_main, splitPage, select, "platform.devpart.splitPage");
	}
	
	/**
	 * 权限分页
	 * @param splitPage
	 * @param paras
	 * @author 李望
	 */
	public void list2in(SplitPage splitPage,List<Object> paras){
		String select =" select a.* ";
		String sqlExceptSelect=" from pt_devpart as a,(select ids from pt_unit where deptids in("
				+ToolUtils.appendPrepSql(paras.size())+")as b where a.unitids = b.ids";
		splitPageBase4In(DictKeys.db_dataSource_main, splitPage, select, sqlExceptSelect, paras.toArray());
	}
	
}
