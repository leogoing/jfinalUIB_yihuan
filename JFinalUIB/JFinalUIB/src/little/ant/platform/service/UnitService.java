package little.ant.platform.service;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import little.ant.platform.annotation.MyTxProxy;
import little.ant.platform.common.DictKeys;
import little.ant.platform.common.SplitPage;
import little.ant.platform.model.Department;
import little.ant.platform.model.Menu;
import little.ant.platform.model.Unit;
import little.ant.platform.model.UnitPic;
import little.ant.platform.model.User;
import little.ant.platform.model.UserInfo;
import little.ant.platform.tools.ToolWeb;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;
import org.apache.commons.lang.StringEscapeUtils;

public class UnitService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(UnitService.class);

	public static final UnitService service = MyTxProxy.newProxy(UnitService.class);

	/**
	 * 保存
	 * @param unit
	 * @return
	 */
	public String save(Unit unit) {
		// 保存
		unit.save();
		return unit.getPKValue();
	}

	/**
	 * 更新
	 * @param unit
	 */
	public void update(Unit unit){
		// 更新
		unit.update();
		
		// 缓存
		Unit.dao.cacheAdd(unit.getPKValue());
	}
	
	public void updatepos(String unitIds, String x, String y){

		Unit unit = Unit.dao.get(unitIds);
		unit.set("the_geom", "GEOMFROMTEXT('POINT(" + x + " " + y + ")')");
		unit.update();
	}

	/**
	 * 删除
	 * @param unit
	 */
	public void delete(String unitIds){
		// 缓存
		Unit.dao.cacheRemove(unitIds);
		
		// 删除
		Unit.dao.deleteById(unitIds);
	}

	/**
	 * 分页
	 * @param splitPage
	 */
	public void list(SplitPage splitPage, HttpServletResponse response, HttpServletRequest request){
		String select = " select * ";
		splitPageBase(DictKeys.db_dataSource_main, splitPage, select, "platform.unit.splitPage");
		
		// 将搜索条件中的部门记录到cookie
		/*Map<String, String> map = splitPage.getQueryParam();
		if(map.get("deptids") != null && !map.get("deptids").equals("")) {
			ToolWeb.addCookie(response, "", "/", true, "deptids", map.get("deptids"), 24*60*60);
		}
		else {
			ToolWeb.addCookie(response, "", "/", true, "deptids", "", -1);
		}
		
		if(map.get("deptname") != null && !map.get("deptname").equals("")) {
			String strName;
			try {
				strName = java.net.URLEncoder.encode(map.get("deptname"), "UTF-8");
				ToolWeb.addCookie(response, "", "/", true, "deptname", strName, 24*60*60);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			ToolWeb.addCookie(response, "", "/", true, "deptname", "", -1);
		}*/
	}
	
	// 取楼层列表, 参数为重点单位ids
	public List<UnitPic> getPics(String unitIds) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("column", "unitids");
		List<UnitPic> list = UnitPic.dao.find(getSql("platform.unitpic.column", param), unitIds);
		return list;
	}
	
	/**
	 * 获取子节点数据
	 * @param parentIds
	 * @return
	 */
	public String childNodeData(String parentIds) {
		List<Department> list = null;
		if(null != parentIds) { // 查找此父节点的所有子节点
			String sql = getSql("platform.department.childNode");
			list = Department.dao.find(sql, parentIds);
		} else { // 没有父节点的是根节点
			String sql = getSql("platform.department.rootNode");
			list = Department.dao.find(sql);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		int size = list.size() - 1;
		if(size >= 0 ) { // 有下级部门的，没有重点单位
			for (Department dept : list) {
				sb.append(" { ");
				sb.append(" id : '").append(dept.getPKValue()).append("', ");
				sb.append(" name : '").append(dept.getStr("names")).append("', ");
				sb.append(" isParent : true, ");
				sb.append(" nocheck : true, ");
				sb.append(" font : {'font-weight':'bold'}, ");
				sb.append(" icon : '").append("/jsFile/zTree/css/zTreeStyle/img/diy/").append(dept.getStr("images")).append("' ");
				sb.append(" }");
				if(list.indexOf(dept) < size){
					sb.append(", ");
				}
			}
		} else { // 没有下级部门的，查找其重点单位
			String sql2 = getSql("platform.unit.deptunit");
			List<Unit> listUnit = Unit.dao.find(sql2, parentIds);
			int unitSize = listUnit.size() - 1;
			for (Unit unit : listUnit) {
				sb.append(" { ");
				sb.append(" id : '").append(unit.getPKValue()).append("', ");
				sb.append(" name : '").append(unit.getStr("unitname")).append("', ");
				sb.append(" isParent : false, ");
				sb.append(" font : {'font-weight':'bold'}, ");
				sb.append(" icon : '").append("/jsFile/zTree/css/zTreeStyle/img/diy/2.png").append("' ");
				sb.append(" }");
				if(list.indexOf(unit) < unitSize){
					sb.append(", ");
				}
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}
	
}
