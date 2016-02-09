package little.ant.platform.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import little.ant.platform.annotation.MyTxProxy;
import little.ant.platform.common.DictKeys;
import little.ant.platform.common.SplitPage;
import little.ant.platform.model.DevType;
import little.ant.platform.model.Unit;
import little.ant.platform.tools.ToolUtils;
import little.ant.platform.model.DevVendor;

import org.apache.log4j.Logger;

public class DevVendorService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(DevVendorService.class);

	public static final DevVendorService service = MyTxProxy.newProxy(DevVendorService.class);
	
	/**
	 * 权限分页查询
	 * @param splitPage
	 * @param column
	 * @param paras
	 * @author 李望
	 */
	public void list2in(SplitPage splitPage,String column,List<Object> paras){
		String select =" select * ";
		String sqlExceptSelect=" from pt_devvendor where "+column+" in ("+ToolUtils.appendPrepSql(paras.size());
		splitPageBase4In(DictKeys.db_dataSource_main, splitPage, select, sqlExceptSelect, paras.toArray());
	}
	
	/**
	 * 保存
	 * @param unit
	 * @return
	 */
	public String save(DevVendor devvendor) {
		// 保存
		devvendor.save();
		return devvendor.getPKValue();
	}

	/**
	 * 更新
	 * @param unit
	 */
	public void update(DevVendor devvendor){
		// 更新
		devvendor.update();
		
		// 缓存
		devvendor.dao.cacheAdd(devvendor.getPKValue());
	}

	/**
	 * 删除
	 * @param unit
	 */
	public void delete(String devvendorIds){
		// 缓存
		DevVendor.dao.cacheRemove(devvendorIds);
		
		// 删除
		DevVendor.dao.deleteById(devvendorIds);
	}

	/**
	 * 分页
	 * @param splitPage
	 */
	public void list(SplitPage splitPage){
		String select = " select * ";
		splitPageBase(DictKeys.db_dataSource_main, splitPage, select, "platform.devvendor.splitPage");
	}
	
	/**
	 * 获取子节点数据
	 * @param parentIds
	 * @return
	 */
	public String childNodeData(String parentIds){
		List<DevVendor> list = null;
		if(null != parentIds){
			//String sql = getSql("platform.devtype.child");
			//list = Station.dao.find(sql, parentIds);
			
			return ""; // 类型只有一级
		}else{
			String sql = getSql("platform.devvendor.root");
			list = DevVendor.dao.find(sql);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		int size = list.size() - 1;
		for (DevVendor station : list) {
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
