package little.ant.platform.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import little.ant.platform.annotation.MyTxProxy;
import little.ant.platform.common.DictKeys;
import little.ant.platform.common.SplitPage;
import little.ant.platform.model.Unit;
import little.ant.platform.model.UnitPic;

import org.apache.log4j.Logger;

public class UnitPicService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(UnitPicService.class);

	public static final UnitPicService service = MyTxProxy.newProxy(UnitPicService.class);
	
	/**
	 * 保存
	 * @param unit
	 * @return
	 */
	public String save(UnitPic unitpic) {
		// 保存
		unitpic.save();
		return unitpic.getPKValue();
	}

	/**
	 * 更新
	 * @param unit
	 */
	public void update(UnitPic unitpic){
		// 更新
		unitpic.update();
		
		// 缓存
		UnitPic.dao.cacheAdd(unitpic.getPKValue());
	}

	/**
	 * 删除
	 * @param unit
	 */
	public void delete(String unitPicIds){
		// 缓存
		UnitPic.dao.cacheRemove(unitPicIds);
		
		// 删除
		UnitPic.dao.deleteById(unitPicIds);
	}
	
	/**
	 * 取楼层图
	 * @param unit
	 */
	public byte[] getpic(String unitPicIds){
		
		return UnitPic.dao.get("levelpic");
	}

	/**
	 * 分页
	 * @param splitPage
	 */
	public void list(SplitPage splitPage){
		String select = " select * ";
		splitPageBase(DictKeys.db_dataSource_main, splitPage, select, "platform.unitpic.splitPage");
	}
	
}
