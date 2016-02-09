package little.ant.platform.model;

import little.ant.platform.annotation.Table;
import little.ant.platform.common.DictKeys;
import little.ant.platform.thread.ThreadParamInit;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 重点单位model
 * @author 鞠玉翔
 */
@SuppressWarnings("unused")
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_unit")
public class Unit extends BaseModel<Unit> {

	private static final long serialVersionUID = 6761767368352810428L;

	private static Logger log = Logger.getLogger(Unit.class);
	
	public static final Unit dao = new Unit();

	/**
	 * 添加或者更新缓存
	 */
	public void cacheAdd(String ids){
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_unit + ids, Unit.dao.findById(ids));
	}

	/**
	 * 删除缓存
	 */
	public void cacheRemove(String ids){
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_unit + ids);
	}

	/**
	 * 获取缓存
	 * @param key
	 * @return
	 */
	public Unit cacheGet(String key){
		Unit unit = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_unit + key);
		return unit;
	}
}
