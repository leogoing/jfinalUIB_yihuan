package little.ant.platform.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import little.ant.platform.annotation.Controller;
import little.ant.platform.interceptor.UnitPowerInterceptor;
import little.ant.platform.model.Unit;
import little.ant.platform.model.UnitPic;
import little.ant.platform.service.UnitPicService;
import little.ant.platform.service.UnitService;
import little.ant.platform.tools.ToolUtils;
import little.ant.platform.validator.UnitPicValidator;
import little.ant.platform.validator.UnitValidator;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.google.zxing.aztec.encoder.Encoder;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.upload.UploadFile;
import com.mysql.fabric.Response;
import com.mysql.jdbc.Blob;

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
	 * 列表(带权限)
	 */
	@Before(UnitPowerInterceptor.class)
	public void index() {
		UnitPicService.service.list2in(splitPage,(List<Object>)getSessionAttr(getCUserIds()+"4unit"));
//		UnitPicService.service.list(splitPage);
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

		UploadFile uf = getFile("unitPic.levelpic", "d:/");
		String fileName = uf.getFileName();
		// 将file文件转换成二进制存储到数据库中
		File f = new File("d://" + fileName);
		BufferedImage bi;
		byte[] bytes=null;
		try {
			bi = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);
			bytes = baos.toByteArray();
//			binary = encoder.encodeBuffer(bytes).trim();

		} catch (IOException e) {
			e.printStackTrace();
		}

		ids = UnitPicService.service.save(getModel(UnitPic.class));
		UnitPic.dao.findById(ids).set("levelpic", bytes).update();
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
		UploadFile uf = getFile("unitPic.levelpic", "d:/");

		String fileName = uf.getFileName();
		// 将file文件转换成二进制存储到数据库中
		File f = new File("d://" + fileName);
		BufferedImage bi;
		byte[] bytes=null;
		try {
			bi = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);
			bytes = baos.toByteArray();
//			binary = encoder.encodeBuffer(bytes).trim();

		} catch (IOException e) {
			e.printStackTrace();
		}

		UnitPicService.service.update(getModel(UnitPic.class));// 如果修改的是图片，那就写一个参数
		Model<UnitPic> unitpic = getModel(UnitPic.class);
		String ids = unitpic.get("ids");
		UnitPic.dao.findById(ids).set("levelpic", bytes).update();
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
	 * @throws IOException 
	 */
	public void getpic() throws IOException {
		UnitPic unitpic=UnitPic.dao.findById(getPara());
		byte[] bytes = unitpic.getBytes("levelpic");
		System.out.println("pic的参数为："+getPara());
		String picName=new SimpleDateFormat("yyyyMMdd").format(new Date())+ToolUtils.getUuidByJdk(true)+".jpg";
		picName="sqlite_db/"+picName;
		log.debug("生成的picName: "+picName);
		BASE64Decoder decoder = new BASE64Decoder();
		ImageOutputStream ios = ImageIO.createImageOutputStream(new File("F:\\workSpaceToEclipse\\JFinalUIB\\WebContent\\"+picName));
		ios.write(bytes);
		ios.flush();
		ios.close();
		setAttr("picName", picName);
		setAttr("unitpic",unitpic);
		render("/platform/unitpic/pic.html");
	}
	
	//标注存储
	public void getdes(){
		String des = getPara("des");
		String ids  = getPara("ids");
		UnitPic.dao.findById(ids).set("des", des).update();
		//打印信息
		System.out.println("图片描述"+des);
		
		render("/platform/unitpic/pic.html");
	}
}
