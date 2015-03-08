package edu.xautjzd.activityrecognition.predict.svm;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import edu.xautjzd.activityrecognition.predict.decisicion.DecisionTreeServlet;
import edu.xautjzd.activityrecognition.predict.decisicion.PredictAttribute;
import edu.xautjzd.activityrecognition.predict.svm.svm_predict;

@WebServlet("/svm")
public class SVMServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DecisionTreeServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		/*File test_file = new File("src/main/resources/svm_test_set");
		LOG.info(test_file.getAbsolutePath());
		PrintWriter writer = new PrintWriter(test_file, "UTF-8");
		if (writer != null)
			LOG.info("Successfully writed test data!");*/
		
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
		writer.println("svm");
		writer.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.info("Getting request test data...");
		// 获取传过来的待识别特征，将其存到测试集文件中
		BufferedReader reader = request.getReader();
		StringBuilder params = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) 
			params.append(line);
		// 将传过来的json测试数据转换成Object
		Gson gson = new Gson();
		PredictAttribute a = (PredictAttribute)gson.fromJson(params.toString(), PredictAttribute.class);
		if (a != null)
			LOG.info("Successfully get test data!");
		else
			LOG.info("Getting test data failure!");
		
		LOG.info("Writing test data to test file...");
		
		// 将测试数据写入测试文件中,需用绝对路径，且调试与运行时读写文件有问题，但发布后访问正常
		File test_file = new File("D:\\Program Files\\apache-tomcat-7.0.57\\webapps\\realtimepredictserver\\WEB-INF\\classes\\svm_test_set");
		PrintWriter writer = new PrintWriter(test_file, "UTF-8");
		if (writer != null)
			LOG.info("Successfully writed test data!");
		writer.println(
				"1.0" +
				" 1:" + a.getX_Average() +
				" 2:" + a.getY_Average() +
				" 3:" + a.getZ_Average() +
				" 4:" + a.getX_Deviation() +
				" 5:" + a.getY_Deviation() +
				" 6:" + a.getZ_Deviation() +
				" 7:" + a.getXY_Correlation() +
				" 8:" + a.getYZ_Correlation() +
				" 9:" + a.getXZ_Correlation() +
				" 10:" + a.getX_Skewness() + 
				" 11:" + a.getY_Skewness() +
				" 12:" + a.getZ_Skewness() +
				" 13:" + a.getX_Kurtosis() +
				" 14:" + a.getY_Kurtosis() +
				" 15:" + a.getZ_Kurtosis()
 		);
		writer.close();
		
		// svm model
		String modelFile = "svm_train_set.model";       
		// directory of test file, model file, result file
		String[] testArgs = { 
				"svm_test_set", 
				modelFile,
				"svm_result" 
		};        
		
		int predicted =  (int)svm_predict.main(testArgs);
		if (predicted > 0 && predicted < 5 )
			LOG.info("Successfully writed test data!");
		
		String action;
		switch(predicted) {
			case 1:
				action = "上楼";
				break;
			case 2:
				action = "下楼";
				break;
			case 3:
				action = "步行";
				break;
			case 4:
				action = "站立";
				break;
			default:
				action = "站立";
				break;
		}
		LOG.info("Action: " + action);
		response.setHeader("Content-Type", "text/plain;charset=UTF-8");
		writer = response.getWriter();
		writer.println(action);
		writer.close();
	}
}
