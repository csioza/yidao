package com.jdbc.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcUtils {

	//定义数据库用户名
	private final String USERNAME = "root";
	//定义数据库密码
	private final String PASSWORD = "123456";
	//定义数据库驱动
	private final String DRIVER = "com.mysql.jdbc.Driver";
	//定义访问数据库的地址
	private final String URL = "jdbc:mysql://localhost:3306/carservice?useUnicode=true&characterEncoding=utf8";
	//定义数据库的链接
	private Connection connection;
	//定义sql语句执行的对象
	private PreparedStatement pstmt;
	//定义查询返回的结果集合
	private ResultSet resultSet;
	public JdbcUtils() {
		// TODO Auto-generated constructor stub
		try {
			Class.forName(DRIVER);
			System.out.println("注册驱动成功！！！");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//定义获得数据库的链接
	public Connection getConnection(){
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	//定义完成对数据库添加删除和修改的操作
	public String updateByPreparedStatement(String sql, List<Object> params) throws SQLException{
				
		String flag = "false";
		//表示当用户执行添加删除和修改的操作时所影响数据库的行数
		int result = -1;
		pstmt = connection.prepareStatement(sql);
		
		int index = 1;
		if (params != null && !params.isEmpty()){
			for (int i = 0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		
		result = pstmt.executeUpdate();
		flag = result > 0 ? "true" : "false";  //成功是true，失败是false
		releaseRes();
		return flag;
	}
	
	//查询返回单条记录
	public Map<String, Object> findSimpleResult(String sql, List<Object> params)throws SQLException{
		Map<String, Object> map = new HashMap<String, Object>();
		int index = 1;
		pstmt = connection.prepareStatement(sql);
		if (params != null && !params.isEmpty()){
			for (int i = 0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();//返回查询结果
		ResultSetMetaData metadata = resultSet.getMetaData();
		int col_len = metadata.getColumnCount();//获得查询结果列数
		while (resultSet.next()){
			for (int i = 0; i < col_len; i++){
				String col_name = metadata.getColumnName(i+1);
				Object col_value = resultSet.getObject(col_name);
				if (col_value == null){
					col_value = "";
				}
				map.put(col_name, col_value);
			}
		}
		releaseRes();
		return map;
	}
	
	//登陆查询返回单条记录
	public String loginFind(String sql, List<Object> params)throws SQLException{
		String result;
		Map<String, Object> map = new HashMap<String, Object>();
		int index = 1;
		pstmt = connection.prepareStatement(sql);
		if (params != null && !params.isEmpty()){
			for (int i = 0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();//返回查询结果
		ResultSetMetaData metadata = resultSet.getMetaData();
		int col_len = metadata.getColumnCount();//获得查询结果列数

		if(resultSet.next()){
			for (int i = 0; i < col_len; i++){
				String col_name = metadata.getColumnName(i+1);
				Object col_value = resultSet.getObject(col_name);
				if (col_value == null){
					col_value = "";
				}
				map.put(col_name, col_value);
			}
			
			if((Integer)map.get("type") == 0){
				result = "passenger";
			}else{
				result = "driver";
			}
			releaseRes();
			return result;
		}else{
			result = "false";
			releaseRes();
			return result;
		}
	}
	
	
	//查询返回多条记录
	public List<Map<String, Object>> findMoreResult(String sql, List<Object> params)throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int index = 1;
		pstmt = connection.prepareStatement(sql);
		if (params != null && !params.isEmpty()){
			for (int i = 0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();//返回查询结果
		ResultSetMetaData metadata = resultSet.getMetaData();
		int col_len = metadata.getColumnCount();//获得查询结果列数
		while (resultSet.next()){
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < col_len; i++){
				String col_name = metadata.getColumnName(i+1);
				Object col_value = resultSet.getObject(col_name);
				if (col_value == null){
					col_value = "";
				}
				map.put(col_name, col_value);
			}
			list.add(map);
		}
		releaseRes();
		return list;
	}
	
	public void releaseRes(){
		if (resultSet != null){
			try {
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (pstmt != null){
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*if (connection != null){
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}
}
