package edu.xautjzd.activityrecognition.predict.decisicion;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import edu.xautjzd.activityrecognition.predict.util.AttributeDao;
import quickml.data.AttributesMap;
import quickml.supervised.classifier.randomForest.RandomForest;

@WebServlet("/DecisionTreeServlet")
public class DecisionTreeServlet extends HttpServlet {
	
	/*public static void main(String []args) {
		DecisionTreeServlet servlet = new DecisionTreeServlet();	
		try {
			servlet.doPost(null, null);
			
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DecisionTreeServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		
		/*ApplicationContext appContext = 
		    	  new ClassPathXmlApplicationContext("ApplicationContext.xml");
		AttributeDao attributeDao = (AttributeDao)appContext.getBean("attributeDao");
		List <String> actions = attributeDao.getActions();*/

		//doPost(null, null);
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
		writer.println("test");
		writer.close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {
			// 获取训练好的DecisionTree模型		
			DataInputStream in = new DataInputStream(DecisionTreeServlet.class.getResourceAsStream("/decisiontree_model.txt"));
			ObjectInputStream is = new ObjectInputStream(in);			
			RandomForest randomForest1 = (RandomForest)is.readObject();
			
			@SuppressWarnings("resource")
			ApplicationContext appContext = 
			    	  new ClassPathXmlApplicationContext("applicationContext.xml");
			AttributeDao attributeDao = (AttributeDao)appContext.getBean("attributeDao");
			// 获取采样数据中所有的已知动作集合
			List <String> actions = attributeDao.getActions();
			
			try {
				// 获取传过来的待识别特征，封装成DecisionTree预测模型所需的特定格式AttributesMap
				BufferedReader reader = request.getReader();
				StringBuilder params = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) 
					params.append(line);
				Gson gson = new Gson();
			//	String text = "{X_Average:8.23366394042969,Y_Average:-6.31057891845703,Z_Average:-0.774726,X_Deviation:0.82977,Y_Deviation:0.5929037,Z_Deviation:0.142005,XY_Correlation:-0.39356,YZ_Correlation:0.3101246,XZ_Correlation:-0.242845,X_Skewness:0.41335687,Y_Skewness:-1.32150137768,Z_Skewness:1.00981784,X_Kurtosis:-1.592978,Y_Kurtosis:1.176373,Z_Kurtosis:0.032665}";
				PredictAttribute a = (PredictAttribute)gson.fromJson(params.toString(), PredictAttribute.class);
				
				AttributesMap map = new AttributesMap();
				map.put("X_Average", a.getX_Average());
				map.put("Y_Average", a.getY_Average());
				map.put("Z_Average", a.getZ_Average());
				
				map.put("X_Deviation", a.getX_Deviation());
				map.put("Y_Deviation", a.getY_Deviation());			
				map.put("Z_Deviation", a.getZ_Deviation());
				
				map.put("XY_Correlation", a.getXY_Correlation());
				map.put("YZ_Correlation", a.getYZ_Correlation());
				map.put("XZ_Correlation", a.getXZ_Correlation());
				
				map.put("X_Skewness", a.getX_Skewness());
				map.put("Y_Skewness", a.getY_Skewness());				
				map.put("Z_Skewness", a.getZ_Skewness());
				
				map.put("X_Kurtosis", a.getX_Kurtosis());
				map.put("Y_Kurtosis", a.getY_Kurtosis());
				map.put("Z_Kurtosis", a.getZ_Kurtosis());
				
				// 獲取各動作對應的概率（Key为动作， Value为对应的概率）
				HashMap<String,Double> pair=new HashMap<String, Double>();
				for (String action: actions) {
					pair.put(action, randomForest1.getProbability(map, action));
				}
				// 獲取最大概率的動作（Key为动作， Value为最大概率值）
				Map.Entry<String, Double> maxEntry = null;
				for (Map.Entry<String, Double> entry: pair.entrySet()) {
					if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
				        maxEntry = entry;
				    }
				}	
				LOG.debug(maxEntry.getKey());
				
				response.setHeader("Content-Type", "text/plain;charset=UTF-8");
				PrintWriter writer = response.getWriter();
				writer.println(maxEntry.getKey());
				writer.close();
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}
			is.close();
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}
}
