package little.ant.platform.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import little.ant.platform.annotation.MyTxProxy;
import little.ant.platform.common.DictKeys;
import little.ant.platform.common.SplitPage;
import little.ant.platform.model.Unit;
import little.ant.platform.model.DevPart;

import org.apache.log4j.Logger;

public class DevPartService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(DevPartService.class);

	public static final DevPartService service = MyTxProxy.newProxy(DevPartService.class);
	
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
	
}
