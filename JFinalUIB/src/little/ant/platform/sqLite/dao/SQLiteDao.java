package little.ant.platform.sqLite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;

import little.ant.platform.common.DictKeys;
import little.ant.platform.model.Department;
import little.ant.platform.model.DevPart;
import little.ant.platform.model.Operator;
import little.ant.platform.model.Unit;
import little.ant.platform.model.UnitPic;
import little.ant.platform.model.User;
import little.ant.platform.tools.ToolContext;

/**
 * sqlite连接数据库
 * @author 李望10.22
 *欲寄彩笺无尺素,山长水远知何处
 */
public class SQLiteDao {
	
	private static Logger log = Logger.getLogger(SQLiteDao.class);
	
	public static final SQLiteDao dao=new SQLiteDao();
	
	public boolean saveDepart(List<Department> models,Connection conn,String sql,String columns){
		if(models==null || models.size()==0){
			log.info("要插入的SQLite数据库得Department表内容为空！");
			return false;
		}
		columns="ids,version,allchildnodeids,departmentlevel,depttype,description,images,isparent,"
				+ "names,orderids,url,parentdepartmentids,principaluserids";
		sql="insert into pt_department ("+columns+") values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		log.debug("存储要更新的department："+models);
		Db.use(DictKeys.db_dataSource_assisc).batch(sql, columns, models, 20,conn);
		return true;
	}
	
	public boolean saveDevPart(List<DevPart> models,Connection conn,String sql,String column){
		if(models==null || models.size()==0){
			log.info("要插入的SQLite数据库得DevPart表内容为空！");
			return false;
		}
		column="ids,offx,offy,unitpicids,parttypename,partvendorname,unitids,name,deviceid,loopid,"
				+ "addr,desc,unitname,unitpicname,parttypeids,partvendorids";
		sql="insert into pt_devpart ("+column+") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		log.debug("存储要更新的devPart："+models);
		Db.use(DictKeys.db_dataSource_assisc).batch(sql,column,models,20,conn);
		return true;
	}
	
	public boolean saveUnit(List<Unit> models,String fileName) throws Exception{
		if(models==null || models.size()==0){
			log.info("要插入的SQLite数据库得Unit表内容为空！");
			return false;
		}
		Connection conn=null;
		PreparedStatement unitPrep=null;
		try {
			conn=getConnection(fileName);
			unitPrep=conn.prepareStatement("insert into pt_unit (ids,unitname,unitdesc,offsetx,offsety,markoffsetx,markoffsety,ver"
					+ ",x,y,z,levels,unitid,status,lat,lon,deptids,deptname,the_geom) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			int unitI=0;
			for(int i=0;i<models.size();i++){
					Unit unit=models.get(i);
					unitPrep.setString(1, unit.getStr("ids"));
					unitPrep.setString(2, unit.getStr("unitname"));
					if(unit.get("unitdesc")==null)
						unitPrep.setNull(3,Types.VARCHAR);
					else
						unitPrep.setObject(3, unit.getStr("unitdesc"));
					if(unit.get("offsetx")==null)
						unitPrep.setObject(4, unit.get("offsetx"));
					else
						unitPrep.setObject(4, unit.getInt("offsetx"));
					if(unit.get("offsety")==null)
						unitPrep.setNull(5, Types.INTEGER);
					else
						unitPrep.setObject(5, unit.getInt("offsety"));
					if(unit.get("markoffsetx")==null)
						unitPrep.setNull(6, Types.INTEGER);
					else
						unitPrep.setObject(6, unit.get("markoffsetx"));
					if(unit.get("markoffsety")==null)
						unitPrep.setNull(7, Types.INTEGER);
					else
						unitPrep.setObject(7, unit.get("markoffsety"));
					if(unit.get("ver")==null)
						unitPrep.setNull(8, Types.INTEGER);
					else
						unitPrep.setObject(8, unit.get("ver"));
					if(unit.get("x")==null)
						unitPrep.setNull(9, Types.INTEGER);
					else
						unitPrep.setObject(9, unit.get("x"));
					if(unit.get("y")==null)
						unitPrep.setNull(10, Types.INTEGER);
					else
						unitPrep.setObject(10, unit.get("y"));
					if(unit.get("z")==null)
						unitPrep.setNull(11, Types.INTEGER);
					else
						unitPrep.setObject(11, unit.get("z"));
					if(unit.get("levels")==null)
						unitPrep.setNull(12, Types.INTEGER);
					else
						unitPrep.setObject(12, unit.get("levels"));
					if(unit.get("unitid")==null)
						unitPrep.setNull(13, Types.INTEGER);
					else
						unitPrep.setObject(13, unit.get("unitid"));
					if(unit.get("status")!=null)
						unitPrep.setNull(14, Types.INTEGER);
					else
						unitPrep.setObject(14, unit.get("status"));
					String point=unit.getStr("point");
					String lon=point.substring(point.indexOf("(")+1, point.indexOf(" "));
					String lat=point.substring(point.indexOf(" ")+1, point.indexOf(")"));
					unitPrep.setObject(15, Double.valueOf(lat));
					unitPrep.setObject(16, Double.valueOf(lon));
					unitPrep.setObject(17, unit.getStr("deptids"));
					unitPrep.setObject(18, unit.getStr("deptname"));
					unitPrep.addBatch();
					unitI++;
					if(unitI !=0 && unitI%20==0){
						unitPrep.executeBatch();
						unitPrep.clearBatch();
					}
			}
			if(unitPrep!=null){
				unitPrep.executeBatch();
				unitPrep.close();
			}
		} catch (ClassNotFoundException | SQLException e) {
			log.error("sqlite插入操作失败！", e);
			throw e;
		}finally {
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return true;
	}
	
	public boolean saveUnitPic(List<UnitPic> models,String fileName) throws Exception{
		if(models==null || models.size()==0){
			log.info("要插入的SQLite数据库得UnitPic表内容为空！");
			return false;
		}
		Connection conn=null;
		PreparedStatement unitPicPrep=null;
		try {
			conn=getConnection(fileName);
			unitPicPrep=conn.prepareStatement("insert into pt_unitpic (ids,unitids,unitname,levelname,levelpic,status) values (?,?,?,?,?,?)");
			int unitpicI=0;
			for(int i=0;i<models.size();i++){
					UnitPic unitpic=models.get(i);
					unitPicPrep.setObject(1, unitpic.get("ids"));
					unitPicPrep.setObject(2, unitpic.get("unitids"));
					unitPicPrep.setObject(3, unitpic.get("unitname"));
					unitPicPrep.setObject(4, unitpic.get("levelname"));
					unitPicPrep.setBytes(5, unitpic.getBytes("levelpic"));
					unitPicPrep.setObject(6, unitpic.get("status"));
					unitPicPrep.addBatch();
					unitpicI++;
					if(unitpicI !=0 && unitpicI%20==0){
						unitPicPrep.executeBatch();
						unitPicPrep.clearBatch();
					}
				}
			if(unitPicPrep!=null){
				unitPicPrep.executeBatch();
				unitPicPrep.close();
			}
		} catch (ClassNotFoundException | SQLException e) {
			log.error("sqlite插入操作失败！", e);
			throw e;
		}finally {
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return true;
	}
	
	public boolean saveUser(List<User> models,String fileName) throws Exception{
		Connection conn=null;
		PreparedStatement userPrep=null;
		try {
			conn=getConnection(fileName);
			userPrep=conn.prepareStatement("insert into pt_user (ids,version,errorcount,orderids,password,salt,status,stopdate,username,"
					+ "departmentids,userinfoids,stationids,deptids,userids,groupids,departmentnames,stationnames,deptnames,usernames,groupnames,"
					+ "waterpower,elecpower,commpower) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			int userI=0;
			for(int i=0;i<models.size();i++){
				User user=models.get(i);
				/*检查用户权限*/
				boolean power=ToolContext.hasPrivilegeOperator(Operator.dao.cacheGet("url"),user);
					userPrep.setObject(1,user.get("ids"));
					userPrep.setObject(2,user.get("version"));
					userPrep.setObject(3,user.get("errorcount"));
					userPrep.setObject(4,user.get("oderids"));
					userPrep.setBytes(5,user.getBytes("password"));
					userPrep.setBytes(6,user.getBytes("salt"));
					userPrep.setObject(7,user.get("status"));
					userPrep.setTimestamp(8, user.getTimestamp("stopdate"));
					userPrep.setObject(9, user.get("username"));
					userPrep.setObject(10,user.get("departmentids"));
					userPrep.setObject(11,user.get("userinfoids"));
					userPrep.setObject(12,user.get("stationids"));
					userPrep.setObject(13,user.get("deptids"));
					userPrep.setObject(14,user.get("userids"));
					userPrep.setObject(15,user.get("groupids"));
					userPrep.setObject(16,user.get("departmentnames"));
					userPrep.setObject(17,user.get("stationnames"));
					userPrep.setObject(18,user.get("deptnames"));
					userPrep.setObject(19,user.get("usernames"));
					userPrep.setObject(20,user.get("groupnames"));
					userPrep.setObject(21,user.get("waterpower"));
					userPrep.setObject(22,user.get("elecpower"));
					userPrep.setObject(23,power?1:0);
					userPrep.addBatch();
					userI++;
					if(userI !=0 && userI%20==0){
						userPrep.executeBatch();
						userPrep.clearBatch();
					}
				}
			if(userPrep!=null){
				userPrep.executeBatch();
				userPrep.close();
			}
		} catch (ClassNotFoundException | SQLException e) {
			log.error("sqlite插入操作失败！", e);
			throw e;
		}finally {
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return true;
	}
	
	public static Connection getConnection(String fileName) throws ClassNotFoundException,SQLException{
		Connection conn=null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+fileName);
		} catch (ClassNotFoundException e) {
			log.error("装载SQLiteJDBC失败！", e);
			throw e;
		}catch (SQLException e) {
			log.error("新建 "+fileName+" 的数据库连接失败！", e);
			throw e;
		}
		return conn;
	}
	
}
