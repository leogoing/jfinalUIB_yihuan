package little.ant.platform.beetl.func;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.beetl.core.Context;
import org.beetl.core.Function;

// 用于解码cookie中的中文
public class Decode implements Function {

	private static Logger log = Logger.getLogger(Decode.class);
	
	/**
	 * 过滤xml文档函数实现
	 */
	@Override
	public Object call(Object[] arg, Context context) {
		if(arg.length != 1 || null == arg[0] || !(arg[0] instanceof String)){
			return "";
		}
		String content = null;// 
		try {
			content = (String) arg[0];
		} catch (Exception e) {
			return "";
		}

		log.debug("解码 content=" + content);
		try {
			return java.net.URLDecoder.decode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
