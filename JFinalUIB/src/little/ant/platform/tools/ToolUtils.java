package little.ant.platform.tools;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.log4j.Logger;

/**
 * 公共工具类
 * @author 董华健  2012-9-7 下午2:20:06
 * @author 李望<br>
 * 昨夜西风凋碧树,独上高楼,望尽天涯路.
 */
public class ToolUtils {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(ToolUtils.class);
	
	/**
	 * double精度调整
	 * @param doubleValue 需要调整的值123.454
	 * @param format 目标样式".##"
	 * @return
	 */
	public static String decimalFormatToString(double doubleValue, String format){
		DecimalFormat myFormatter = new DecimalFormat(format);  
		String formatValue = myFormatter.format(doubleValue);
		return formatValue;
	}
	
	/**
	 * 获取UUID by jdk
	 * @author 董华健    2012-9-7 下午2:22:18
	 * @return
	 */
	public static String getUuidByJdk(boolean is32bit){
		String uuid = UUID.randomUUID().toString();
		if(is32bit){
			return uuid.toString().replace("-", ""); 
		}
		return uuid;
	}
	
	/**
	 * 删除文件
	 * @param args
	 * @author 李望
	 */
	public static boolean deleteFile(String filePath){
		File file=new File(filePath);
		if(file.isFile()){
			if(file.delete()){
				log.debug("成功删除文件："+filePath);
				return true;
			}
		}
		log.error("删除文件失败："+filePath);
		return false;
	}
	
	/**
	 * 批量删除文件
	 * @param args
	 * @author 李望
	 */
	public static int deleteFiles(List<String> dirPaths,String filesNamespace){
		int index=0;
		for(String dir:dirPaths){
			File d=new File(dir);
			if(d.isDirectory()){
				log.debug("扫描要删除文件的目录："+d);
				File[] files=d.listFiles();
				for(int i=0;i<files.length;i++){
					String fileName=files[i].getName();
					if(files[i].isFile()){
						if(fileName.length()>8){
						}
						if(!"".equals(filesNamespace) && fileName.matches("^"+filesNamespace+"\\w*")){
							try {
								files[i].delete();
								index++;
							} catch (Exception e) {
								log.error("删除文件 "+fileName+"失败！",e);
							}
						}else if(fileName.length()>8 && fileName.substring(0, 8).matches("^\\d+$") && 
									Integer.parseInt(fileName.substring(0, 8)) <= 
									Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()))){
							log.debug("要删除的文件名："+fileName);
								try {
									if(files[i].delete()){
										index++;
									}else{
										throw new RuntimeException();
									}
								} catch (Exception e) {
									log.error("删除文件 "+fileName+"失败！",e);
								}
							}
					}
				}
			}else{
				log.error("此路径不是文件夹："+dir);
			}
		}
		return index;
	}
	
	/**
	 * 开启定时器删除db文件
	 * @author 李望
	 */
	public static boolean startDelTimer(int interval,int delay,List<String> dirPaths,String nameSpace){
		final List<String> dirs=dirPaths;
		final String space=nameSpace;
		new Timer().schedule(new TimerTask(){ 
            public void run() {
            	log.debug("定时器开始执行删除临时db文件");
                int i=deleteFiles(dirs,space);
                log.debug("已完成删除临时文件数："+i);
            }
        },delay,interval);
		return true;
	}
	
	/**
	 * 拼接sql批量in语句后部分
	 * @return
	 * @author 李望
	 */
	public static String appendSql(List<?> params){
		if(params==null || params.isEmpty()){
			log.error("要拼接的sql参数为空");
			throw new RuntimeException("要拼接的sql参数为空");
		}
		StringBuilder sql=new StringBuilder();
		for(int i=0;i<params.size();i++){
			Object par=params.get(i);
			if(i!=0){
				sql.append(",");
			}
			if(par instanceof String){
				sql.append("'"+par+"'");
			}else{
				sql.append(par);
			}
		}
		return sql.append(")").toString();
	}
	
	/**
	 * 拼接预处理sql批量in语句后部分
	 * @param size
	 * @return
	 */
	public static String appendPrepSql(int size){
		StringBuilder sql=new StringBuilder();
		if(size==0){
			log.error("要拼接的sql参数为空");
			throw new RuntimeException("要拼接的sql参数为空");
		}
		for(int i=0;i<size;i++){
			if(i!=0){
				sql.append(",");
			}
			sql.append("?");
		}
		return sql.append(")").toString();
	}
	
	/**
	 * 将unit表中x和y和z转换为the_geom能用的point和插入字符串
	 */
	public static String getLatLon2g(int x, int y, int zoom) {
		double lon = -180; // x
		double lonWidth = 360; // width 360

		// double lat = -90; // y
		// double latHeight = 180; // height 180
		double lat = -1;
		double latHeight = 2;

		int tilesAtThisZoom = 1 << (17 - zoom);
		lonWidth = 360.0 / tilesAtThisZoom;
		lon = -180 + (x * lonWidth);
		latHeight = -2.0 / tilesAtThisZoom;
		lat = 1 + (y * latHeight);

		// convert lat and latHeight to degrees in a transverse mercator projection
		// note that in fact the coordinates go from about -85 to +85 not -90 to 90!
		latHeight += lat;
		latHeight = (2 * Math.atan(Math.exp(Math.PI * latHeight))) - (Math.PI / 2);
		latHeight *= (180 / Math.PI);

		lat = (2 * Math.atan(Math.exp(Math.PI * lat))) - (Math.PI / 2);
		lat *= (180 / Math.PI);

		latHeight -= lat;

		if (lonWidth < 0) {
			lon = lon + lonWidth;
			lonWidth = -lonWidth;
		}

		if (latHeight < 0) {
			lat = lat + latHeight;
			latHeight = -latHeight;
		}

		String point="GEOMFROMTEXT('POINT("+lon+" "+lat+")')";
//		String point=lat+","+lon;
		return point;
	}
	
	public static void main(String[] args){
		boolean b=new File("F:\\workSpaceToEclipse\\JFinalUIB\\WebContent\\sqlite_db\\201510281e8404792b0142d0a61f5e7aaf6b00f1.db").delete();
		System.out.println(b);
	}
	
}
