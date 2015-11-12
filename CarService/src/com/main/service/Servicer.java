package com.main.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jdbc.dbutils.JdbcUtils;
import com.json.tools.JsonTools;

public class Servicer extends HttpServlet {

	private JdbcUtils jdbcutils;
	private Connection connection;
	/**
	 * Constructor of the object.
	 */
	public Servicer() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		List<Object> params = new ArrayList<Object>();
		//String jsonString = JsonTools.createJsonString("listmap", service.getListMaps());
		
		String jsonString = "";
		String action_flag = request.getParameter("action_flag");
		
		
		
		//乘客修改自己坐标，和获取司机的坐标
		if (action_flag.equals("position_p")){
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			String phonenum = request.getParameter("phonenum");
			int latitude = Integer.parseInt(request.getParameter("latitude"));//int i=Integer.parseInt(String);
			int longitude = Integer.parseInt(request.getParameter("longitude"));
			//System.out.println(latitude+"  <=======>  "+longitude);
			String sql = "update userinfo set latitude = ?, longitude = ? where phonenum = ?";
			//String sql = "insert into userinfo(phonenum,latitude,longitude) values(?,?,?)";
			params.clear();
			params.add(latitude);
			params.add(longitude);
			params.add(phonenum);
			
			try {
				String flag = jdbcutils.updateByPreparedStatement(sql, params);
				//System.out.println("=====================>"+flag+"==================>"+phonenum);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			sql = "select phonenum_p,phonenum_c,success from trade where phonenum_p = ?";
			params.clear();
			params.add(phonenum);
			
			try {
				list = jdbcutils.findMoreResult(sql, params);
				if(list.isEmpty()){
					sql = "select phonenum,name,latitude,longitude,carnum,appraisescore,destination,sit from userinfo where phonenum != ? and online = ? and type = ? and success = ?";
					params.clear();
					params.add(phonenum);
					params.add(1);//在线
					params.add(1);//司机
					params.add("0");
					try {
						jsonString = JsonTools.createJsonString("listmap", jdbcutils.findMoreResult(sql, params));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					String phonenum_c = "";
					String success = "";
					for(int i = 0; i<list.size();i++){
						Map<String,Object> maps = list.get(i);
						Set<String> key = maps.keySet();
						Iterator<String> it = key.iterator();
						
						phonenum_c = (String)maps.get("phonenum_c");
						success = (String)maps.get("success");
					}
					
					if(success.equals("1")){
						sql = "select phonenum,name,latitude,longitude,carnum,appraisescore,destination,sit from userinfo where phonenum = ?";
						params.clear();
						params.add(phonenum_c);
						
						try {
							jsonString = JsonTools.createJsonString("driver_position", jdbcutils.findMoreResult(sql, params));//driver_position
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if(success.equals("0")){
						sql = "select phonenum_p,phonenum_c,success from trade where phonenum_c = ? and success = ?";
						params.clear();
						params.add(phonenum_c);
						params.add("1");
						list = jdbcutils.findMoreResult(sql, params);
						if(list.isEmpty()){
							sql = "select phonenum,name,latitude,longitude,carnum,appraisescore,destination,sit from userinfo where phonenum = ?";
							params.clear();
							params.add(phonenum_c);
							
							try {
								jsonString = JsonTools.createJsonString("listmap", jdbcutils.findMoreResult(sql, params));
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							sql = "delete from trade where phonenum_p = ?";
				        	params.clear();
				        	params.add(phonenum);
				        	jdbcutils.updateByPreparedStatement(sql, params);
				        	
							sql = "select phonenum,name,latitude,longitude,carnum,appraisescore,destination,sit from userinfo where phonenum != ? and online = ? and type = ? and success = ?";
							params.clear();
							params.add(phonenum);
							params.add(1);//在线
							params.add(1);//司机
							params.add("0");
							jsonString = JsonTools.createJsonString("listmap-c", jdbcutils.findMoreResult(sql, params));
						}
					}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		//司机修改自己坐标，和获取乘客的坐标
		if (action_flag.equals("position_c")){
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			String phonenum = request.getParameter("phonenum");
			int latitude = Integer.parseInt(request.getParameter("latitude"));//int i=Integer.parseInt(String);
			int longitude = Integer.parseInt(request.getParameter("longitude"));
			System.out.println(latitude+"  <=======>  "+longitude);
			String sql = "update userinfo set latitude = ?, longitude = ? where phonenum = ?";
			//String sql = "insert into userinfo(phonenum,latitude,longitude) values(?,?,?)";
			params.clear();
			params.add(latitude);
			params.add(longitude);
			params.add(phonenum);
			
			try {
				String flag = jdbcutils.updateByPreparedStatement(sql, params);
				System.out.println("=====================>"+flag+"==================>"+phonenum);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			sql = "select phonenum_p,phonenum_c,success from trade where phonenum_c = ? and success = ?";
			params.clear();
			params.add(phonenum);
			params.add("1");
			
			try {
				list = jdbcutils.findMoreResult(sql, params);
				if(list.isEmpty()){
					sql = "select phonenum,name,latitude,longitude,appraisescore,destination from userinfo,trade where phonenum_c = ? and phonenum = phonenum_p";
					params.clear();
					params.add(phonenum);
					
					list = jdbcutils.findMoreResult(sql, params);
					if(!list.isEmpty()){
						jsonString = JsonTools.createJsonString("apply_passagers", jdbcutils.findMoreResult(sql, params));
					}else{
						sql = "select phonenum,name,latitude,longitude,appraisescore,destination from userinfo where phonenum != ? and online = ? and type = ? and success = ?";
						params.clear();
						params.add(phonenum);
						params.add(1);//在线
						params.add(0);//乘客
						params.add("0");
						jsonString = JsonTools.createJsonString("listmap", jdbcutils.findMoreResult(sql, params));
					}
				}else{
					String phonenum_p = "";
					for(int i = 0; i<list.size();i++){
						Map<String,Object> maps = list.get(i);
						Set<String> key = maps.keySet();
						Iterator<String> it = key.iterator();
			
						phonenum_p = (String)maps.get("phonenum_p");
						sql = "select phonenum,name,latitude,longitude,appraisescore,destination from userinfo where phonenum = ?";
						params.clear();
						params.add(phonenum_p);
						
						try {
							jsonString = JsonTools.createJsonString("passager_position", jdbcutils.findMoreResult(sql, params));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		//处理查询拼友
		if ( action_flag.equals("position_pf")){
			//List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			String phonenum = request.getParameter("phonenum");
			
			String sql = "select phonenum,name,latitude,longitude,appraisescore,destination from userinfo where phonenum != ? and online = ? and type = ? and success = ?";
			params.clear();
			params.add(phonenum);
			params.add(1);//在线
			params.add(0);//乘客
			params.add("0");
			try {
				jsonString = JsonTools.createJsonString("listmap_pf", jdbcutils.findMoreResult(sql, params));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//处理登陆请求
		if(action_flag.equals("login")){
			String result;
			//String test = "true";
			String phonenum = request.getParameter("phonenum");
			String password = request.getParameter("password");
			String sql = "select * from userinfo where phonenum = ? and password = ?";
			params.clear();
			params.add(phonenum);
			params.add(password);
			//params.clear();
			try {
				result = jdbcutils.loginFind(sql, params);
				//System.out.println("result==========>"+result);
				if(result.equals("passenger")||result.equals("driver")){
					sql = "update userinfo set online = ? where phonenum = ?";
					params.clear();
					params.add(1);
					params.add(phonenum);
					String flag = jdbcutils.updateByPreparedStatement(sql, params);
				}
				jsonString = JsonTools.createJsonString("log", result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//处理注册请求
        if(action_flag.equals("signin")){
        	String result = "";
        	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			String phonenum = request.getParameter("phonenum");
			String password = request.getParameter("password");
			
			String name = request.getParameter("name");
			//name = new String(name.getBytes("gbk"),"UTF-8");//转码
			
			int type = Integer.parseInt(request.getParameter("type"));
			
			String carnum = request.getParameter("carnum");
			//carnum = new String(carnum.getBytes("UTF-16"),"UTF-8");
			
			String cartype = request.getParameter("cartype");
			//cartype = new String(cartype.getBytes("gbk"),"UTF-8");
			
			String sql = "select * from userinfo where phonenum = ?";
			params.clear();
			params.add(phonenum);
			try {
				list = jdbcutils.findMoreResult(sql, params);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		if(list.isEmpty()){
    			if(type == 1){
	    			sql = "insert into userinfo(phonenum,password,name,online,type,longitude,latitude,carnum,cartype,appraisetimes,appraisescore,success) values(?,?,?,?,?,?,?,?,?,?,?,?)";
	            	params.clear();
	            	params.add(phonenum);
	            	params.add(password);
	            	params.add(name);
	            	params.add(0);
	            	params.add(1);
	            	params.add(0);
	            	params.add(0);
	            	params.add(carnum);
	            	params.add(cartype);
	            	params.add(0);
	            	params.add(0);
	            	params.add("0");
    			}else{
    				sql = "insert into userinfo(phonenum,password,name,online,type,longitude,latitude,appraisetimes,appraisescore,success) values(?,?,?,?,?,?,?,?,?,?)";
	            	params.clear();
	            	params.add(phonenum);
	            	params.add(password);
	            	params.add(name);
	            	params.add(0);
	            	params.add(0);
	            	params.add(0);
	            	params.add(0);
	            	params.add(0);
	            	params.add(0);
	            	params.add("0");
    			}
    			try {
					result = jdbcutils.updateByPreparedStatement(sql, params);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}else{
    			result = "existed";
    		}
    		jsonString = JsonTools.createJsonString("sign", result);
		}
        
        //处理乘客打车请求
        if(action_flag.equals("call")){
        	String phonenum_p = request.getParameter("phonenum_p");
        	String phonenum_c = request.getParameter("phonenum_c");
        	String sql = "insert into trade(phonenum_p,phonenum_c,success) values(?,?,?)";
        	params.clear();
        	params.add(phonenum_p);
        	params.add(phonenum_c);
        	params.add("0");
        	try {
				jsonString = JsonTools.createJsonString("call", jdbcutils.updateByPreparedStatement(sql, params));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
        //处理乘客取消原有打车请求
        if(action_flag.equals("cancelcall")){
        	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        	String result = "false";
        	String phonenum = request.getParameter("phonenum");
        	String sql = "select phonenum_c from trade where phonenum_p = ? and success = ?";
			params.clear();
        	params.add(phonenum);
        	params.add("1");
        	try {
        		list = jdbcutils.findMoreResult(sql, params);
        		if(!list.isEmpty()){
    				Map<String,Object> maps = list.get(0);
    				String phonenum_c = "";
    				phonenum_c = (String) maps.get("phonenum_c");
    				sql = "update userinfo set success = ? where phonenum = ? or phonenum = ?";
            	    params.clear();
            	    params.add("0");
            	    params.add(phonenum_c);
            	    params.add(phonenum);
				    result = jdbcutils.updateByPreparedStatement(sql, params);
				    
				    sql = "delete from trade where phonenum_p = ?";
			        params.clear();
			        params.add(phonenum);
			        result = jdbcutils.updateByPreparedStatement(sql, params);
					
            	}else{
            		sql = "select phonenum_c from trade where phonenum_p = ? and success = ?";
        			params.clear();
                	params.add(phonenum);
                	params.add("0");
                	list = jdbcutils.findMoreResult(sql, params);
                	if(!list.isEmpty()){
                		sql = "delete from trade where phonenum_p = ?";
    			        params.clear();
    			        params.add(phonenum);
    			        result = jdbcutils.updateByPreparedStatement(sql, params);
                	}else{
            		   result = "true";
                	}
            	}
        		
        		jsonString = JsonTools.createJsonString("cancelcall",result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
      //处理司机载客请求
        if(action_flag.equals("pick")){
        	String result = "false";
        	String phonenum_p = request.getParameter("phonenum_p");
        	String phonenum_c = request.getParameter("phonenum_c");
        	String sql = "update trade set success = ? where phonenum_p = ? and phonenum_c = ?";
        	params.clear();
        	params.add("1");
        	params.add(phonenum_p);
        	params.add(phonenum_c);
        	try {
				result = jdbcutils.updateByPreparedStatement(sql, params);
				//System.out.println("+++++++++++++>>>>>>>>>>>>>>>>>>"+result);
				if(result.equals("true")){
					sql = "update userinfo set success = ? where phonenum = ? or phonenum = ?";
                	params.clear();
                	params.add("1");
                	params.add(phonenum_p);
                	params.add(phonenum_c);
					result = jdbcutils.updateByPreparedStatement(sql, params);
				}
				jsonString = JsonTools.createJsonString("pick", result);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
        
        //处理司机取消原有载客请求
        if(action_flag.equals("cancelpick")){
        	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        	String phonenum = request.getParameter("phonenum");
        	String result = "false";
        	String sql = "select phonenum_p from trade where phonenum_c = ? and success = ?";
			params.clear();
        	params.add(phonenum);
        	params.add("1");
        	try {
        		list = jdbcutils.findMoreResult(sql, params);
        		if(!list.isEmpty()){
    				Map<String,Object> maps = list.get(0);
    				String phonenum_p = "";
    				phonenum_p = (String) maps.get("phonenum_p");
    				sql = "update userinfo set success = ? where phonenum = ? or phonenum = ?";
            	    params.clear();
            	    params.add("0");
            	    params.add(phonenum_p);
            	    params.add(phonenum);
				    result = jdbcutils.updateByPreparedStatement(sql, params);
				    if(result.equals("true")){
	        			sql = "update trade set success = ? where phonenum_c = ? and success = ?";
			        	params.clear();
			        	params.add("0");
			        	params.add(phonenum);
			        	params.add("1");
			        	result = jdbcutils.updateByPreparedStatement(sql, params);
					}
            	}else{
            		result = "true";
            	}
        		
        		jsonString = JsonTools.createJsonString("cancelpick",result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
      //处理评价请求
		if(action_flag.equals("appraise")){
			String result = "false";
			int times = 0;
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
			String phonenum = request.getParameter("phonenum");
			double score = Double.parseDouble(request.getParameter("score"));
			
			String sql = "select * from userinfo where phonenum = ?";
			params.clear();
			params.add(phonenum);
			try {
				list = jdbcutils.findMoreResult(sql, params);
				if(!list.isEmpty()){
    				Map<String,Object> maps = list.get(0);
    				int appraisetimes = (Integer)(maps.get("appraisetimes"));
    				double appraisescore = (Double)(maps.get("appraisescore"));
				
    				times = appraisetimes + 1;
    				score = (appraisescore * appraisetimes + score ) / times;
					sql = "update userinfo set appraisetimes = ?, appraisescore = ? where phonenum = ?";
					params.clear();
					params.add(times);
					params.add(score);
					params.add(phonenum);
					result = jdbcutils.updateByPreparedStatement(sql, params);
				}
				jsonString = JsonTools.createJsonString("appraise", result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//处理乘客修改目的地请求
		if(action_flag.equals("passenger_setting")){
			String phonenum = request.getParameter("phonenum");
			String destination = request.getParameter("destination");
			
			String sql = "update userinfo set destination = ? where phonenum = ?";
			params.clear();
			params.add(destination);
			params.add(phonenum);
			try {
				jsonString = JsonTools.createJsonString("psngsetting", jdbcutils.updateByPreparedStatement(sql, params));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//处理司机修改乘客数和目的地请求
		if(action_flag.equals("driver_setting")){
			String phonenum = request.getParameter("phonenum");
			String sit = request.getParameter("sit");
			String destination = request.getParameter("destination");
			
			String sql = "update userinfo set destination = ?, sit = ? where phonenum = ?";
			params.clear();
			params.add(destination);
			params.add(sit);
			params.add(phonenum);
			try {
				jsonString = JsonTools.createJsonString("drvrsetting", jdbcutils.updateByPreparedStatement(sql, params));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//处理用户设置请求
		if(action_flag.equals("settings")){
			String phonenum = request.getParameter("phonenum");
			String key = request.getParameter("key");
			String value = request.getParameter("value");
			
			String sql = "update userinfo set "+key+" = ? where phonenum = ?";
			params.clear();
			params.add(value);
			//params.add(sit);
			params.add(phonenum);
			try {
				jsonString = JsonTools.createJsonString("settings", jdbcutils.updateByPreparedStatement(sql, params));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//处理列表请求
		if(action_flag.equals("carlist")){
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			//String result;
			//String test = "true";
			String phonenum = request.getParameter("phonenum");
			//System.out.println("===============================> "+phonenum);
			//String password = request.getParameter("password");
			String sql = "select phonenum,name,latitude,longitude,carnum,cartype,appraisescore from userinfo where online = ? and type = ? and success = ?";
			params.clear();
			//params.add(phonenum);
			params.add(1);//在线
			params.add(1);//司机
			params.add("0");
			try {
				list = jdbcutils.findMoreResult(sql, params);
				//System.out.println("result==========>"+result);
				if(list != null && !list.isEmpty()){
					//System.out.println("===============================>list "+"bukong");
					jsonString = JsonTools.createJsonString("car_list", list);
				}else{
					list = null;
					jsonString = JsonTools.createJsonString("car_list", list);
				}
				
				//jsonString = JsonTools.createJsonString("car_list", result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//处理个人信息请求
		if(action_flag.equals("selfinfo")){
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			//String result;
			//String test = "true";
			String phonenum = request.getParameter("phonenum");
			//System.out.println("===============================> "+phonenum);
			//String password = request.getParameter("password");
			String sql = "select phonenum,password,type,name,carnum,cartype from userinfo where phonenum = ?";
			params.clear();
			params.add(phonenum);
			//params.add(1);//在线
			//params.add(1);//司机
			//params.add("0");
			try {
				list = jdbcutils.findMoreResult(sql, params);
				//System.out.println("result==========>"+result);
				if(list != null && !list.isEmpty()){
					//System.out.println("===============================>list "+"bukong");
					jsonString = JsonTools.createJsonString("selfinfo", list);
				}else{
					list = null;
					jsonString = JsonTools.createJsonString("selfinfo", list);
				}
				
				//jsonString = JsonTools.createJsonString("car_list", result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		out.println(jsonString);
		out.flush();
		out.close();
		
		
		/*if (connection != null){
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		//jdbcutils.releaseConn();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		jdbcutils = new JdbcUtils();
		connection = jdbcutils.getConnection();
	}

}
