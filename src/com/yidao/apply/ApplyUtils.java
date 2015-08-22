package com.yidao.apply;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.yidao.http.HttpUtils;
import com.yidao.json.JsonTools;

public class ApplyUtils {

	public ApplyUtils() {
		// TODO Auto-generated constructor stub
	}

	//��½����ӿ�
	public static int ApplyLogin(String phonenum, String password){
		String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=login&phonenum=" + phonenum + "&password=" + password;
		String jsonstring = HttpUtils.getJsonContent(path);
		//System.out.println("===============>"+jsonstring);
		String result =  JsonTools.getJsonString(jsonstring, "log");
		//System.out.println("===================>"+result);
		if(result.equals("passenger")){
			return 1;    //��ʾ��½�ɹ�
		}
		if(result.equals("driver")){
			return 2;
		}
		return 0;        //��ʾ��¼ʧ��
	}
	
	//��������ӿ�
	public static int ApplyAppraise(String phonenum, int score){
		String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=appraise&phonenum=" + phonenum + "&score=" + score;
		String jsonstring = HttpUtils.getJsonContent(path);
		String result =  JsonTools.getJsonString(jsonstring, "appraise");
		if(result.equals("true")){
			return 1;    //��ʾ���۳ɹ�
		}
		return 0;        //��ʾ����ʧ��
	}
	
	//ע������ӿ�
	public static int ApplySignin(String phonenum, String password, String name, int type, String carnumber, String cartype){
		String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=signin&phonenum=" + phonenum + "&password=" + password + "&name=" + name + "&type=" + type + "&carnum=" + carnumber + "&cartype=" + cartype;
		
		String jsonstring = HttpUtils.getJsonContent(path);
		//System.out.println("===============>"+phonenum+"<===============");
		//System.out.println("===============>"+password+"<===============");
		//System.out.println("===============>"+name+"<===============");
		//System.out.println("===============>"+type+"<===============");
		//System.out.println("===============>"+carnumber+"<===============");
		//System.out.println("===============>"+cartype+"<===============");
		String result =  JsonTools.getJsonString(jsonstring, "sign");
		//System.out.println("===============>"+phonenum+"<===============");
		if(result.equals("true")){
			return 1;    //��ʾע��ɹ�
		}
		return 0;        //��ʾע��ʧ��
	}
	//��������ӿ�
	public static int ApplyCall(String phonenum_p, String phonenum_c){
		String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=call&phonenum_p=" + phonenum_p + "&phonenum_c=" + phonenum_c;
		String JsonString = HttpUtils.getJsonContent(path);
		String success="";
		try {
			JSONObject jsonobject = new JSONObject(JsonString);
			success = (String)jsonobject.get("call");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(success.equals("true")){
			return 0;    //��ʾ������ɹ�
		}
		return 1;        //��ʾ������ʧ�� 
	}
	
	//ȡ��ԭ�������¼�����ӿ�
	public static int ApplyCancelCall(String phonenum){
		String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=cancelcall&phonenum=" + phonenum;
		String JsonString = HttpUtils.getJsonContent(path);
		String flag="";
		try {
			JSONObject jsonobject = new JSONObject(JsonString);
			flag = (String)jsonobject.get("cancelcall");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(flag.equals("true")){
			return 0;    //��ʾȡ��������ɹ�
		}
		return 1;        //��ʾȡ��������ʧ�� 
	}
	
	//��������ӿ�
	public static int ApplyPick(String phonenum_p, String phonenum_c){
		String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=pick&phonenum_p=" + phonenum_p + "&phonenum_c=" + phonenum_c;
		String JsonString = HttpUtils.getJsonContent(path);
		String success="";
		try {
			JSONObject jsonobject = new JSONObject(JsonString);
			success = (String)jsonobject.get("pick");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(success.equals("true")){
			return 0;    //��ʾ�ؿ�����ɹ�
		}
		return 1;        //��ʾ�ؿ�����ʧ�� 
	}
	
	//ȡ��ԭ�������¼�����ӿ�
	public static int ApplyCancelPick(String phonenum){
		String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=cancelpick&phonenum=" + phonenum;
		String JsonString = HttpUtils.getJsonContent(path);
		String flag="";
		try {
			JSONObject jsonobject = new JSONObject(JsonString);
			flag = (String)jsonobject.get("cancelpick");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(flag.equals("true")){
			return 0;    //��ʾȡ���ؿ�����ɹ�
		}
		return 1;        //��ʾȡ���ؿ�����ʧ�� 
	}
	
	//�˿��޸�Ŀ�ĵ������¼�����ӿ�
	public static int ApplyPsngSetting(String phonenum, String destination){
		String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=passenger_setting&phonenum=" + phonenum + "&destination=" + destination;
		String JsonString = HttpUtils.getJsonContent(path);
		String flag="";
		try {
			JSONObject jsonobject = new JSONObject(JsonString);
			flag = (String)jsonobject.get("psngsetting");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(flag.equals("true")){
			return 0;    //��ʾ�޸�����ɹ�
		}
		return 1;        //��ʾ�޸�����ʧ�� 
	}
	
	//�ֻ��޸ĳ˿�����Ŀ�ĵ������¼�����ӿ�
	public static int ApplyDrvrSetting(String phonenum, String sit, String destination){
		String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=driver_setting&phonenum=" + phonenum + "&sit=" + sit + "&destination=" + destination;
		String JsonString = HttpUtils.getJsonContent(path);
		String flag="";
		try {
			JSONObject jsonobject = new JSONObject(JsonString);
			flag = (String)jsonobject.get("drvrsetting");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(flag.equals("true")){
			return 0;    //��ʾ�޸�����ɹ�
		}
		return 1;        //��ʾ�޸�����ʧ�� 
	}
	
	//�б������¼�����ӿ�
	public static List<Map<String, Object>> ApplyCarList(String phonenum){
		List<Map<String, Object>> list = null;
		String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=carlist&phonenum=" + phonenum;
		String JsonString = HttpUtils.getJsonContent(path);
		//String flag="";
		try {
			//JSONObject jsonobject = new JSONObject(JsonString);
			list = JsonTools.getListMaps("car_list", JsonString);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list; 
	}
	
	//��ѯ�Լ���Ϣ�¼�����ӿ�
	public static List<Map<String, Object>> ApplySelfInfo(String phonenum){
		List<Map<String, Object>> list = null;
		String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=selfinfo&phonenum=" + phonenum;
		String JsonString = HttpUtils.getJsonContent(path);
		//String flag="";
		try {
			//JSONObject jsonobject = new JSONObject(JsonString);
			list = JsonTools.getListMaps("selfinfo", JsonString);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list; 
	}
	
	//�����޸����¼�����ӿ�
	public static int ApplySettings(String phonenum, String key, String value){
		String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=settings&phonenum=" + phonenum + "&key=" + key + "&value=" + value;
		String JsonString = HttpUtils.getJsonContent(path);
		String flag="";
		try {
			JSONObject jsonobject = new JSONObject(JsonString);
			flag = (String)jsonobject.get("settings");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(flag.equals("true")){
			return 0;    //��ʾ�޸�����ɹ�
		}
		return 1;        //��ʾ�޸�����ʧ�� 
	}
}
