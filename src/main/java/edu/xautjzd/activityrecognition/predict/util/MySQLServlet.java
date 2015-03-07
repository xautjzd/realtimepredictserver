package edu.xautjzd.activityrecognition.predict.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;

@WebServlet("/getActions")
public class MySQLServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MySQLServlet.class);

	/*public static void main(String args[]) throws ServletException, IOException {
		MySQLServlet servlet = new MySQLServlet();
		servlet.doGet(null, null);
	}*/
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		LOG.info("Connecting MySQL...Creating AttributeDao Instance...");
		ApplicationContext appContext = 
		    	  new ClassPathXmlApplicationContext("applicationContext.xml");
		AttributeDao attributeDao = (AttributeDao)appContext.getBean("attributeDao");
		if (attributeDao != null)
			LOG.info("AttributeDao instance created successfully!");
		
		List <String> actions = attributeDao.getActions();
		Gson gson = new Gson();
		String json = gson.toJson(actions);
		// 解決中文亂碼問題
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		writer.print(json);
		writer.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}
