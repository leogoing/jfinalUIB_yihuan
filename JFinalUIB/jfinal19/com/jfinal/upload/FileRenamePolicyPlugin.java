package com.jfinal.upload;

import java.io.File;

import little.ant.platform.tools.ToolUtils;

import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * 文件重命名
 * @author Administrator
 *
 */
public class FileRenamePolicyPlugin implements FileRenamePolicy {

	/**
	 * 使用uuid重命名文件
	 */
	@Override
	public File rename(File file) {
        String ext = "";   
        int pot = file.getName().lastIndexOf(".");   
        if(pot != -1){   
        	ext = file.getName().substring(pot);   
        }else{   
        	ext = "";   
        }   
        String newName = ToolUtils.getUuidByJdk(true) + ext;   
        file = new File(file.getParent(),newName);   
        return file;
	}

}
