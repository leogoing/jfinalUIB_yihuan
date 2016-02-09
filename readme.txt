消防二期项目——李望的部分：

需要注意的一点是有些有路径的地方写死了，以后换环境了可能需要修改的！
还有一点是因为要对安卓端的测试所以把AuthenticationInterceptor的权限认证给屏蔽了：74行的   ‘ if(0==1 && operator.get("privilegess").equals("1")){// 是否需要权限验证 ’   把‘0==1 &&’去掉就行
主要内容：更新下载功能；
	  新增的类：UpdatePackController 
		    ForJsonInterceptor 转换json为java对象用
		    little.ant.platform.exchange包下的类用来配合ForJsonInterceptor使用
	   	    UploadPackService
		    SQLiteDao  进行sqlite数据库连接操作sqlite数据库
	主要大的添加就是这些 然后还别的一些的内容添加如unitservice类的等
	sqlite数据库文件的存放地在WebContent下的sqlite_db文件夹下
	删除db文件及缓存楼层图片的定时器在jfinalConfig类的afterJFinalStart方法里


	企业用户的权限管理；
	 新增的类：unitPowerInterceptor 权限拦截
	 主要在unitController、unitpicController、devPartController、devtypeController、devvenderController中的index方法改变
	 及以上的service中添加了list2in方法并在baseService类中添加splitPageBase4In方法改变以前的分页查询部分
	 

	楼层图片编辑：
	 主要改动在unitpic下的pic.html和isfile下的jquery下的jquery.iPictire.js文件jquery.ipicture.css也有少许改动 以及unitpicController类的完善
	 取消了base64编码使用的是缓存数据库中的图片到服务器再传到页面，有一点需要完善：每查询一次都会产生新的缓存图片不管文件夹里有没有一样的图片，所以需要把文件夹里的图片进行
	  记录，防止重复向数据库获取已经存在的图片
	 画线的js部分在同文件夹下的draw.html中，只实现了画点、线、多边形方法 还差结合着鼠标的点击事件赋值调用了。