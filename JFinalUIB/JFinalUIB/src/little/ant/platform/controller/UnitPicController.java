package little.ant.platform.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import little.ant.platform.annotation.Controller;
import little.ant.platform.model.Unit;
import little.ant.platform.model.UnitPic;
import little.ant.platform.service.UnitPicService;
import little.ant.platform.service.UnitService;
import little.ant.platform.validator.UnitPicValidator;
import little.ant.platform.validator.UnitValidator;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.google.zxing.aztec.encoder.Encoder;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.upload.UploadFile;

/**
 * 重点单位楼层管理
 * @author 鞠玉翔
 */
@SuppressWarnings("unused")
@Controller(controllerKey = "/jf/platform/unitpic")
public class UnitPicController extends BaseController {
	
	private static Logger log = Logger.getLogger(UnitPicController.class);

	public String ids = "";

	public static String queryunitids = ""; // 保存页面的检索条件，以免返回页面时检索条件被清空
	public static String queryunitname = "";
	
	public static BASE64Encoder encoder = new sun.misc.BASE64Encoder();   
    public static BASE64Decoder decoder = new sun.misc.BASE64Decoder();   
    public String binary;
	/**
	 * 列表
	 */
	public void index() {
		UnitPicService.service.list(splitPage);
		render("/platform/unitpic/list.html");
	}
	
	/**
	 * 查询
	 */
	public void query() {
		Map<String, String> map = splitPage.getQueryParam();
		queryunitids = map.get("unitids"); // 记录查询的部门条件
		queryunitname = map.get("unitname");
		
		index();
	}

	/**
	 * 保存
	 */
	@Before(UnitValidator.class)
	public void save() {
		
		UploadFile uf = getFile("unitPic.levelpic","d:/");
		String fileName = uf.getFileName();
		//将file文件转换成二进制存储到数据库中
		File f = new File("d://"+fileName);
		BufferedImage bi;
		try {
			bi = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            binary = encoder.encodeBuffer(bytes).trim();
            
		} catch (IOException e) {
			e.printStackTrace();
		}
		ids = UnitPicService.service.save(getModel(UnitPic.class));
		UnitPic.dao.findById(ids).set("levelpic", binary).update();
		redirect("/jf/platform/unitpic");
		
	}
		
	
	/**
	 * 准备更新
	 */
	public void edit() {
		setAttr("unitpic", UnitPic.dao.findById(getPara()));
		render("/platform/unitpic/update.html");
	}

	/**
	 * 更新
	 */
	@Before(UnitValidator.class)
	public void update() {
		UploadFile uf = getFile("unitPic.levelpic","d:/");
		String fileName = uf.getFileName();
		//将图片转换为二进制数据存储到数据库中
		File f = new File("d://"+fileName);
		BufferedImage bi;
		try {
			bi = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            binary = encoder.encodeBuffer(bytes).trim();
            System.out.println("你好"+binary);
            
		} catch (IOException e) {
			e.printStackTrace();
		}
		UnitPicService.service.update(getModel(UnitPic.class));//如果修改的是图片，那就写一个参数
		Model<UnitPic> unitpic = getModel(UnitPic.class);
		unitpic.set("levelpic", binary).update();
		
		redirect("/jf/platform/unitpic");
		
	}
	
	/**
	 * 删除
	 */
	public void delete() {
		UnitPicService.service.delete(getPara());
		redirect("/jf/platform/unitpic");
	}

	/**
	 * 取楼层图片
	 * 默认平面示意图
	 */
	public void getpic() {
		//获得所要操作的对象
		Model<UnitPic> unitpic = getModel(UnitPic.class);
		
		byte[] pic =  unitpic.get("levelpic");
		System.out.println("这就是"+pic);
		
//		ServletOutputStream sos = null;
//	    try {
//	        //设置头信息,内容处理的方式,attachment以附件的形式打开,就是进行下载,并设置下载文件的命名
////	        response.setHeader("Content-Disposition","attachment;filename="+file.getName());
//	    	HttpServletResponse response = getResponse();
//	        response.setHeader("Pragma", "no-cache");
//	        response.setHeader("Cache-Control", "no-cache");
//	        response.setDateHeader("Expires", 0);
//	        response.setContentType("image/jpeg");
//	        sos=response.getOutputStream();
//	        sos.write(pic);
//
//	    } catch (Exception e) {
//	        log.error("图片render出错:"+e.getLocalizedMessage(),e);
//	        throw new RuntimeException(e);
//	    } finally {
//	        if (sos != null)
//	            try {
//	                sos.close();
//	            } catch (Exception e) {
//	                e.printStackTrace();
//	            }
//	    }
//
//	    System.out.println("这是数据库中拿出来的数据"+pic);
//	    
	    render("/platform/unitpic/pic.html");
	    
	    
	}
}


