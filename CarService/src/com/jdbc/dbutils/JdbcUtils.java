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

	//�������ݿ��û���
	private final String USERNAME = "root";
	//�������ݿ�����
	private final String PASSWORD = "123456";
	//�������ݿ�����
	private final String DRIVER = "com.mysql.jdbc.Driver";
	//����������ݿ�ĵ�ַ
	private final String URL = "jdbc:mysql://localhost:3306/carservice?useUnicode=true&characterEncoding=utf8";
	//�������ݿ������
	private Connection connection;
	//����sql���ִ�еĶ���
	private PreparedStatement pstmt;
	//�����ѯ���صĽ������
	private ResultSet resultSet;
	public JdbcUtils() {
		// TODO Auto-generated constructor stub
		try {
			Class.forName(DRIVER);
			System.out.println("ע�������ɹ�������");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//���������ݿ������
	public Connection getConnection(){
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	//������ɶ����ݿ����ɾ�����޸ĵĲ���
	public String updateByPreparedStatement(String sql, List<Object> params) throws SQLException{
				
		String flag = "false";
		//��ʾ���û�ִ�����ɾ�����޸ĵĲ���ʱ��Ӱ�����ݿ������
		int result = -1;
		pstmt = connection.prepareStatement(sql);
		
		int index = 1;
		if (params != null && !params.isEmpty()){
			for (int i = 0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		
		result = pstmt.executeUpdate();
		flag = result > 0 ? "true" : "false";  //�ɹ���true��ʧ����false
		releaseRes();
		return flag;
	}
	
	//��ѯ���ص�����¼
	public Map<String, Object> findSimpleResult(String sql, List<Object> params)throws SQLException{
		Map<String, Object> map = new HashMap<String, Object>();
		int index = 1;
		pstmt = connection.prepareStatement(sql);
		if (params != null && !params.isEmpty()){
			for (int i = 0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();//���ز�ѯ���
		ResultSetMetaData metadata = resultSet.getMetaData();
		int col_len = metadata.getColumnCount();//��ò�ѯ�������
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
	
	//��½��ѯ���ص�����¼
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
		resultSet = pstmt.executeQuery();//���ز�ѯ���
		ResultSetMetaData metadata = resultSet.getMetaData();
		int col_len = metadata.getColumnCount();//��ò�ѯ�������

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
	
	
	//��ѯ���ض�����¼
	public List<Map<String, Object>> findMoreResult(String sql, List<Object> params)throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int index = 1;
		pstmt = connection.prepareStatement(sql);
		if (params != null && !params.isEmpty()){
			for (int i = 0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();//���ز�ѯ���
		ResultSetMetaData metadata = resultSet.getMetaData();
		int col_len = metadata.getColumnCount();//��ò�ѯ�������
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
