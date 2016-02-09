package little.ant.platform.controller;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.jfinal.kit.PathKit;

import little.ant.platform.annotation.Controller;
import little.ant.platform.exchange.DepartIdVer;
import little.ant.platform.exchange.UnitIdVer;
import little.ant.platform.exchange.UserIdVer;
import little.ant.platform.interceptor.ForJsonInterceptor;
import little.ant.platform.service.UpdatePackService;
import little.ant.platform.service.UserService;
import little.ant.platform.sqLite.dao.SQLiteDao;

/**
 * 更新包controller
 * @author 李望10.23
 *槛菊愁烟兰泣露,罗幕轻寒,燕子双飞去.
 */
@Controller(controllerKey="/jf/platform/updatepack")
public class UpdatePackController extends BaseController{
	
	private static Logger log = Logger.getLogger(UpdatePackController.class);
	
	/**
	 * 检查更新及权限
	 */
	@Before(ForJsonInterceptor.class)
	public void checkForUnit(){
		List<UnitIdVer> unitVer=(List<UnitIdVer>)getAttr("unitJson");
log.debug("###########################################checkfor:"+unitVer);
		if(unitVer==null || unitVer.isEmpty()){
			ForJsonInterceptor.handlError(1, "重点单位版本参数为空！", this);
			return;
		}
		String departIds=UserService.service.findDeptIds(getCUserIds());
		log.debug("该用户所属部门："+departIds);
		Map<String,List> map=new HashMap<String,List>();	
		map.put("unit", UpdatePackService.service.compareUnit(unitVer,departIds));
		renderJson("updatelist",map);
	}
	
	/**
	 * 检查用户版本号默认发送用户更新包
	 * @throws Exception 
	 */
	@Before(ForJsonInterceptor.class)
	public void checkForUser() throws Exception{
		List<UserIdVer> userVer=(List<UserIdVer>)getAttr("userJson");
		List<DepartIdVer> departVer=(List<DepartIdVer>)getAttr("departJson");
		log.debug("############################################### "+departVer);
		String fileName=UpdatePackService.service.copyDatabase("F:\\workSpaceToEclipse\\JFinalUIB\\WebContent\\sqlite_db\\","sqlite_model_user.db");
		String filePath="F:\\workSpaceToEclipse\\JFinalUIB\\WebContent\\sqlite_db\\"+fileName;
		setAttr("filePath",filePath);
		SQLiteDao.dao.saveUser(UpdatePackService.service.compareUser(userVer), filePath);
		SQLiteDao.dao.saveDepart(UpdatePackService.service.comparDepart(departVer),SQLiteDao.getConnection(filePath),null,null);
		System.out.println("######################### renderFile #########################");
		renderFile("/sqlite_db/"+fileName);
	}
	
	//完成后可以把业务内容放入service中
	/**
	 * 发送更新包
	 * @throws Exception
	 */
	@Before(ForJsonInterceptor.class)
	public void sendPack() throws Exception{
		List<Long> unitIds=(List<Long>)getAttr("json");
		log.debug("转换后的json对象："+unitIds);
		if(unitIds==null || unitIds.size()==0){
			ForJsonInterceptor.handlError(1, "重点单位id参数为空！", this);
			return;
		}
		String fileName=UpdatePackService.service.senPack(unitIds);
		renderFile("/sqlite_db/"+fileName);
	}
	
	/**
	 * 添加内容
	 */
	public void test(){
//		List<Record> r=Db.use(DictKeys.db_dataSource_assisc).find("select * from TBGROUP");
//		try {
//			Connection co=SQLiteDao.getConnection("WebContent/sqlite_db/bookmarks2Units.db");
//			PreparedStatement p=co.prepareStatement("select level,pic,status,markid from TBLEVELPIC where id>=851 and id<=880");
//			ResultSet rs=p.executeQuery();
//			while(rs.next()){
//				new UnitPic().set("levelname", rs.getString("level")).set("levelpic", rs.getBytes("pic")).set("status",rs.getString("status"))
//				.set("unitids", rs.getString("markid")).set("unitname", "暂无").save();
//			}
//			rs.close();
//			co.close();
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//			throw new RuntimeException();
//		}
			
//		for(Record ge:r){
//			new UnitPic().set("levelname", ge.get("level")).set("levelpic", ge.getBytes("pic")).set("status",ge.get("status"))
//			.set("unitids", "无").set("unitname", "暂无").save();
//			new Group().set("names", ge.get("cgroupname")).save();
//		}
		
		/*List<Unit> us=Unit.dao.find("select * from pt_unit where offsetx=0");
//		List<UnitPic> up=UnitPic.dao.find("select * from pt_unitpic limit 901,1000");
		Map map =new HashMap();
		for(Unit u:us){
			int x=Integer.valueOf(u.get("x").toString());
			int y=Integer.valueOf(u.get("y").toString());
			int z=Integer.valueOf(u.get("z").toString());
			String p=ToolUtils.getLatLon2g(x, y, z);
			log.debug("sql语句中的point："+p);
			Db.use(DictKeys.db_dataSource_main).update("update pt_unit set the_geom="+p+" where ids='"+u.getPKValue()+"'");
		}*/
//		List<Unit> us=Unit.dao.find("select AsText(the_geom) a from pt_unit limit 10");
//		System.out.println(us.get(0).getStr("a"));
		System.out.println("########################### coming ###########################");
		renderJson("result","OK.....");
	}
	
	
	public static void main(String[] args) throws Exception {
		String s="POINT(115.832977294922 28.6821545177775)";
		String a=s.substring(s.indexOf("(")+1,s.indexOf(" "));
		/*获取classes文件夹的绝对路径将"\"换成"/"使能通过URL访问*/
		String path = PathKit.class.getResource("/").toURI().getPath();
		/*返回path上两层文件夹的标准的将符号完全解析的路径*/
		a= new File(path).getParentFile().getParentFile().getCanonicalPath();
		System.out.println(a);
	}
}
