package little.ant.platform.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import little.ant.platform.annotation.MyTxProxy;
import little.ant.platform.exchange.DepartIdVer;
import little.ant.platform.exchange.StreamObj;
import little.ant.platform.exchange.UnitIdVer;
import little.ant.platform.exchange.UserIdVer;
import little.ant.platform.model.BaseModel;
import little.ant.platform.model.Department;
import little.ant.platform.model.DevPart;
import little.ant.platform.model.Unit;
import little.ant.platform.model.UnitPic;
import little.ant.platform.model.User;
import little.ant.platform.sqLite.dao.SQLiteDao;
import little.ant.platform.tools.ToolUtils;

/**
 * 更新包service
 * @author 李望
 *明月不谙离恨苦,斜光到晓穿朱户.
 */
public class UpdatePackService extends BaseService{
	
	private static Logger log = Logger.getLogger(UnitService.class);

	public static final UpdatePackService service = MyTxProxy.newProxy(UpdatePackService.class);

	/**
	 * 重点单位版本号比较
	 * @param list
	 */
	public List<UnitIdVer> compareUnit(List<UnitIdVer> list,String departIds){
		Map<Long,Long> map=deptPower(departIds);
		List<UnitIdVer> diff=new ArrayList<UnitIdVer>();
		for(UnitIdVer idVer: list){
			if(map.get(idVer.getUnitid())==null){
				continue;
			}
			if( !idVer.getVer().equals(map.get(idVer.getUnitid()))){
				diff.add(new UnitIdVer(idVer.getUnitid(),map.get(idVer.getUnitid())));
			}
//			map.remove(idVer.getUnitid());
		}
		/*添加客户端权限内不存在的单位*/
//		Set<Long> set=map.keySet();
//		if(set!=null && set.size()!=0){
//			for(Long l: set){
//				diff.add(new UnitIdVer(l,map.get(l)));
//			}
//		}
		log.debug("需要更新的unit信息："+diff);
		return diff;
	}
	
	/**
	 * 用户版本号比较
	 */
	public List<User> compareUser(List<UserIdVer> list){
		List<User> users=User.dao.find("select * from pt_user");
		Map<String,Integer> map=new HashMap<String,Integer>();
		for(int i=0;i<users.size();i++){
			map.put(users.get(i).getPKValue(), i);
		}
		List<User> diff=new ArrayList<User>();
		for(UserIdVer idVer: list){
			if(map.get(idVer.getUserids())==null){
				continue;
			}
			User u=users.get(map.get(idVer.getUserids()));
			if( !idVer.getVersion().equals(u.getLong("version"))){
				diff.add(u);
			}
			map.remove(idVer.getUserids());
		}
		Collection<Integer> col=map.values();
		if(col!=null && col.size()!=0){
			for(Integer l: col){
				diff.add(users.get(l));
			}
		}
		log.debug("需要更新的user信息："+diff);
		return diff;
	}
	
	/**
	 * 部门版本号比较
	 */
	public List<Department> comparDepart(List<DepartIdVer> list){
		List<Department> departs=Department.dao.find("select * from pt_department");
		return (List<Department>) compar(departs,list);
	}
	
	/**
	 * 版本号比较
	 */
	public List<? extends BaseModel> compar(List<? extends BaseModel> serv,List clie){
		log.debug("要比较的clie："+clie);
		Map<String,Integer> map=new HashMap<String,Integer>();
		for(int i=0;i<serv.size();i++){
			map.put(((BaseModel) serv.get(i)).getPKValue(), i);
		}
		List<BaseModel> diff=new ArrayList<BaseModel>();
		for(StreamObj idVer: (List<StreamObj>)clie){
			if(map.get(idVer.getIds())==null){
				continue;
			}
			BaseModel b=serv.get(map.get(idVer.getIds()));
			if( !idVer.getVersion().equals(b.getLong("version"))){
				diff.add(b);
			}
			map.remove(idVer.getIds());
		}
		Collection<Integer> col=map.values();
		if(col!=null && col.size()!=0){
			for(Integer l: col){
				diff.add(serv.get(l));
			}
		}
		log.debug("需要更新的信息："+diff);
		return diff;
	}
	
	/**
	 * 通过部门检查权限
	 */
	public Map<Long,Long> deptPower(String departIds){
		if(DepartmentService.service.findOrderIds(departIds)==1){
			return UnitService.service.findVer(null);
		}
		List<String> d=new ArrayList<String>();
		d.add(departIds);
		//权限范围内所有子部门
		List<String> departIdss=DepartmentService.service.findChildDeparts(d);
		departIdss.add(departIds);
		log.debug("该用户权限范围内所有部门ids："+departIdss);
		return UnitService.service.findVer(departIdss);
	}
	
	/**
	 * 复制sqlite模板
	 * @throws IOException 
	 */
	public String copyDatabase(String path,String modelFile) throws IOException{
		String fileName=new SimpleDateFormat("yyyyMMdd").format(new Date())+ToolUtils.getUuidByJdk(true)+".db";
		BufferedInputStream bufis = null;
        BufferedOutputStream bufos = null;
        try {
            bufis = new BufferedInputStream(new FileInputStream(path+modelFile));
            bufos = new BufferedOutputStream(new FileOutputStream(path+fileName));
            int byt = 0;
            while((byt = bufis.read()) != -1) {
                 bufos.write(byt);
            }
        }catch(IOException e){
        	log.error("复制sqlite模板失败！", e);
            throw new IOException("复制sqlite模板失败！");
        }finally{
            try{
              if(bufis != null){
                 bufis.close();
              }
              if(bufos!=null){
            	 bufos.close();
              }
            }catch(IOException e){
            }
        }
		return fileName;
	}
	
	/**
	 * 发送unit更新包
	 * @param unitIds
	 * @return
	 * @throws Exception
	 */
	public String senPack(List<Long> unitIds) throws Exception{
		List<Unit> units=UnitService.service.batchById(unitIds);
		List<String> idss=UnitService.service.batchById2(units);
		List<UnitPic> unitpics=UnitPicService.service.batchByUnit(idss);
		List<DevPart> devparts=DevPartService.service.findByUnitIdss(idss);
		if(units.size()!=unitIds.size()){
			log.error("查询新版本数据不完整！");
			throw new RuntimeException("查询新版本数据与要求数据不一致！");
		}
		String fileName=copyDatabase("F:\\workSpaceToEclipse\\JFinalUIB\\WebContent\\sqlite_db\\","sqlite_model.db");
		String filePath="F:\\workSpaceToEclipse\\JFinalUIB\\WebContent\\sqlite_db\\"+fileName;
		log.debug("sqlite复制后的文件名："+fileName);
		Connection conn=SQLiteDao.getConnection(filePath);
		try{
			SQLiteDao.dao.saveUnit(units, filePath);
			SQLiteDao.dao.saveUnitPic(unitpics, filePath);
			SQLiteDao.dao.saveDevPart(devparts,conn, null, null);
		}catch(Exception e){
			ToolUtils.deleteFile(filePath);
			throw e;
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
		return fileName;
	}
	
	public static void main(String[] args) {
		List a=new ArrayList<>();
		List b=new ArrayList<>();
		System.out.println(b.size());
	}
	
}
