package com.yidao.activity;

import com.yidao.apply.ApplyUtils;
import com.yidao.activity.Driver;
import com.yidao.activity.Login;
import com.yidao.activity.Passenger;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends Activity {
	
	EditText username,password;       //用户名和密码
	Button login,Register,dialogButton;			//登陆和注册按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //openGPSSettings();
        username=(EditText)this.findViewById(R.id.phonenumber);                
        password=(EditText)this.findViewById(R.id.Password);
        login=(Button)this.findViewById(R.id.logining);
        Register=(Button)this.findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//查看gps连接状态
				boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER );
				if(!gpsEnabled){
					Toast.makeText(Login.this, "亲，请先开启GPS！！！", 3).show();
					return;
				}
				//查看网络连接情况
				ConnectivityManager manager = (ConnectivityManager) Login.this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE); 
	            if (manager == null) {  
	                //return false;  
	            }  	        
	            NetworkInfo networkinfo = manager.getActiveNetworkInfo();  
	            if (networkinfo == null || !networkinfo.isAvailable()) {  
	                Toast.makeText(Login.this, "亲，请先连接网络！！！", 3).show();
	                return;  
	            }
	            
	            
			    String username_value=username.getText().toString();   //获取用户名信息
			
		/*	    if(username_value==null||username_value.trim().equals(""))       //判断用户名是否为空
			    {
					String string="用户名不能为空";
					SpannableString warn=new SpannableString(string);
					warn.setSpan(new AbsoluteSizeSpan(15), 0, string.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					warn.setSpan(new BackgroundColorSpan(Color.WHITE), 0, string.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					warn.setSpan(new ForegroundColorSpan(Color.RED), 0, string.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					username.setError(warn);
					return ;	
			    }
				String password_value=password.getText().toString();             //获取密码信息
				if(password_value==null||password_value.trim().equals(""))      //判断密码是否为空
				{
					String string="密码不能为空";
					SpannableString warn=new SpannableString(string);
					warn.setSpan(new AbsoluteSizeSpan(20), 0, string.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					warn.setSpan(new BackgroundColorSpan(Color.WHITE), 0, string.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					warn.setSpan(new ForegroundColorSpan(Color.RED), 0, string.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					password.setError(warn);
					return ;
				}
		*/		
				//flag=0  失败，1是乘客，2是司机
				int flag = 1;//ApplyUtils.ApplyLogin(username_value, password_value);       //flag=调用接口函数
				switch(flag)
				{
					case 0:
						dialogButton=new Button(Login.this);      //提示的登陆失败
						dialogButton.setText("关闭");
						dialogButton.setHeight(2);
						dialogButton.setWidth(3);
						final Dialog dialog=new Dialog(Login.this);
						dialog.setTitle("密码或者用户名输入错误!");
						dialog.setContentView(dialogButton);
						dialog.show();
						
						dialogButton.setOnClickListener(new OnClickListener()
						{
							public void onClick(View v)
							{
								dialog.dismiss();
							}
						});
							
							break;
					case 1:
							Intent intent_Passenger=new Intent(Login.this, Passenger.class);
							intent_Passenger.putExtra("phonenum",username_value);
							startActivity(intent_Passenger);
							Login.this.finish();
							break;
					case 2:
							Intent intent_Driver=new Intent(Login.this, Driver.class);
							intent_Driver.putExtra("phonenum",username_value);
							startActivity(intent_Driver);
							Login.this.finish();
							break;
				}
			}
		});
        //跳转到注册界面
        Register.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent=new Intent(Login.this,Regist.class);
				startActivity(intent);
				Login.this.finish();
			}
		});
        
        
    }
    
    public void onBackPressed() {  
        //Intent intent=new Intent(Regist.this, Login.class);
		//startActivity(intent); 
    	Builder dialog = new AlertDialog.Builder(Login.this);   
		dialog.setTitle("提示") ; 
        dialog.setMessage("您确定退出吗？");
        dialog.setPositiveButton("确定", 
	            new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
                        Login.this.finish(); 
					}
				});
        dialog.setNegativeButton("取消", null);
        dialog.show();
    } 
    
    /*private void openGPSSettings() {       
        //获取GPS现在的状态（打开或是关闭状态）
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER );
        if(gpsEnabled){
             //关闭GPS
             Settings.Secure.setLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER, false);
        }else{
             //打开GPS  
             Settings.Secure.setLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER, true);
      }
    }*/

}
