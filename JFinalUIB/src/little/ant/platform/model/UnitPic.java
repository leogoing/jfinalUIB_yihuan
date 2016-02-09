package little.ant.platform.model;

import little.ant.platform.annotation.Table;
import little.ant.platform.common.DictKeys;
import little.ant.platform.thread.ThreadParamInit;

import org.apache.log4j.Logger;

import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 单位楼层model
 * @author 鞠玉翔
 */
@SuppressWarnings("unused")
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_unitpic")
public class UnitPic extends BaseModel<UnitPic> {

	private static final long serialVersionUID = 6761767368352810428L;

	private static Logger log = Logger.getLogger(UnitPic.class);
	
	public static final UnitPic dao = new UnitPic();

	/**
	 * 添加或者更新缓存
	 */
	public void cacheAdd(String ids){
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_unitpic + ids, UnitPic.dao.findById(ids));
	}

	/**
	 * 删除缓存
	 */
	public void cacheRemove(String ids){
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_unitpic + ids);
	}

	/**
	 * 获取缓存
	 * @param key
	 * @return
	 */
	public UnitPic cacheGet(String key){
		UnitPic unit = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_unitpic + key);
		return unit;
	}
}
