package little.ant.platform.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import little.ant.platform.annotation.MyTxProxy;
import little.ant.platform.common.DictKeys;
import little.ant.platform.common.SplitPage;
import little.ant.platform.model.Station;
import little.ant.platform.model.Unit;
import little.ant.platform.tools.ToolUtils;
import little.ant.platform.model.DevType;

import org.apache.log4j.Logger;

public class DevTypeService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(DevTypeService.class);

	public static final DevTypeService service = MyTxProxy.newProxy(DevTypeService.class);
	
	/**
	 * 权限分页
	 * @param splitPage
	 * @param paras
	 * @author 李望
	 */
	public void list2in(SplitPage splitPage,String column,List<Object> paras){
		String select =" select * ";
		String sqlExceptSelect=" from pt_devtype where "+column+" in ("+ToolUtils.appendPrepSql(paras.size());
		splitPageBase4In(DictKeys.db_dataSource_main, splitPage, select, sqlExceptSelect, paras.toArray());
	}
	
	/**
	 * 保存
	 * @param unit
	 * @return
	 */
	public String save(DevType devtype) {
		// 保存
		devtype.save();
		return devtype.getPKValue();
	}

	/**
	 * 更新
	 * @param unit
	 */
	public void update(DevType devtype){
		// 更新
		devtype.update();
		
		// 缓存
		devtype.dao.cacheAdd(devtype.getPKValue());
	}

	/**
	 * 删除
	 * @param unit
	 */
	public void delete(String devtypeIds){
		// 缓存
		DevType.dao.cacheRemove(devtypeIds);
		
		// 删除
		DevType.dao.deleteById(devtypeIds);
	}

	/**
	 * 分页
	 * @param splitPage
	 */
	public void list(SplitPage splitPage){
		String select = " select * ";
		splitPageBase(DictKeys.db_dataSource_main, splitPage, select, "platform.devtype.splitPage");
	}
	
	
	/**
	 * 获取子节点数据
	 * @param parentIds
	 * @return
	 */
	public String childNodeData(String parentIds){
		List<DevType> list = null;
		if(null != parentIds){
			//String sql = getSql("platform.devtype.child");
			//list = Station.dao.find(sql, parentIds);
			
			return ""; // 类型只有一级
		}else{
			String sql = getSql("platform.devtype.root");
			list = DevType.dao.find(sql);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		int size = list.size() - 1;
		for (DevType station : list) {
			sb.append(" { ");
			sb.append(" id : '").append(station.getPKValue()).append("', ");
			sb.append(" name : '").append(station.getStr("names")).append("', ");
			sb.append(" isParent : false, ");
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" icon : '").append("/jsFile/zTree/css/zTreeStyle/img/diy/2.png").append("' ");
			sb.append(" }");
			if(list.indexOf(station) < size){
				sb.append(", ");
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}
}
