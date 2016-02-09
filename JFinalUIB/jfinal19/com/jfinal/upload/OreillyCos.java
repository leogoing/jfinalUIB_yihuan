/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.upload;

/**
 * 上传组件
 * OreillyCos.
 */
public class OreillyCos {
	
	private static Boolean isMultipartSupported = null;
	
	/**
	 * 判断OreillyCos的jar组件是否存在
	 * @return
	 */
	public static boolean isMultipartSupported() {
		if (isMultipartSupported == null) {
			detectOreillyCos();
		}
		return isMultipartSupported;
	}
	
	/**
	 * 初始化上传组件
	 * @param saveDirectory 保存文件夹路径
	 * @param maxPostSize 最大上传容量
	 * @param encoding 编码
	 */
	public static void init(String saveDirectory, int maxPostSize, String encoding) {
		if (isMultipartSupported()) {
			MultipartRequest.init(saveDirectory, maxPostSize, encoding);
		}
	}
	
	/**
	 * 用反射搜索OreillyCos的jar包是否存在
	 */
	private static void detectOreillyCos() {
		try {
			Class.forName("com.oreilly.servlet.MultipartRequest");
			isMultipartSupported = true;
		} catch (ClassNotFoundException e) {
			isMultipartSupported = false;
		}
	}
}
