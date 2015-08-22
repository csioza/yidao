package com.yidao.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {

	public HttpUtils() {
		// TODO Auto-generated constructor stub
	
	}
	
	public static String getJsonContent(String url_path){
		HttpURLConnection connection;
		try {
			URL url = new URL(url_path);
			try {
				connection = (HttpURLConnection)url.openConnection();
				connection.setConnectTimeout(3000);
				connection.setRequestMethod("POST");
				connection.setDoInput(true);
				int code = connection.getResponseCode();
				if (code == 200) {
					return changeInputStream(connection.getInputStream());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}

	private static String changeInputStream(InputStream inputStream) {
		// TODO Auto-generated method stub
		String JsonString = "";
		ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
		int len = 0;
		byte[] data = new byte[512];
		try {
			while((len = inputStream.read(data)) != -1){
				outputstream.write(data,0,len);	
			}
			JsonString = new String(outputstream.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JsonString;
	}

}
