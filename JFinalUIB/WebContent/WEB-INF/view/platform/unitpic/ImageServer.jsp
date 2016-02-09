<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*"%>
<%
//	System.out.print("enter...");
	String filePath = "D:/31437fac1dba472cb64c41ceb81e3ce2.jpg";
	File file = new File(filePath);
	InputStream fis;
	try {
		fis = new FileInputStream(file);
		byte[] buf = new byte[(int) fis.available()];
		fis.read(buf);
		response.setContentType("application/binary;charset=ISO8859_1");
		OutputStream outs = response.getOutputStream();
		outs.write(buf);
		outs.flush();
		out.clear();
		out = pageContext.pushBody();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
%>