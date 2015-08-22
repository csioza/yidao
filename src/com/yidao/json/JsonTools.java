package com.yidao.json;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.util.Log;

public class JsonTools {

	public JsonTools() {
		// TODO Auto-generated constructor stub
	}
	
	//登陆用到此接口
	public static String getJsonString(String jsonstring, String flag){
		String result = "null";
		try {
			JSONObject jsonObject = new JSONObject(jsonstring);
			result = jsonObject.getString(flag);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<Map<String,Object>> getListMaps(String key, String JsonString){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			JSONObject jsonobject = new JSONObject(JsonString);
			JSONArray jsonArray = jsonobject.getJSONArray(key);
			for(int i = 0;i<jsonArray.length();i++){
				JSONObject jsonobjecttmp = jsonArray.getJSONObject(i);
				Map<String, Object> map = new HashMap<String,Object>();
				Iterator<String> iterator = jsonobjecttmp.keys();
				while(iterator.hasNext()){
					String json_key = iterator.next();
					Object json_value = jsonobjecttmp.get(json_key);
					if(json_value == null){
						json_value = "";
					}
					map.put(json_key, json_value);
				}
				list.add(map);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	

}
