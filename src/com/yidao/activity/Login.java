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
	
	EditText username,password;       //�û���������
	Button login,Register,dialogButton;			//��½��ע�ᰴť
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
				//�鿴gps����״̬
				boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER );
				if(!gpsEnabled){
					Toast.makeText(Login.this, "�ף����ȿ���GPS������", 3).show();
					return;
				}
				//�鿴�����������
				ConnectivityManager manager = (ConnectivityManager) Login.this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE); 
	            if (manager == null) {  
	                //return false;  
	            }  	        
	            NetworkInfo networkinfo = manager.getActiveNetworkInfo();  
	            if (networkinfo == null || !networkinfo.isAvailable()) {  
	                Toast.makeText(Login.this, "�ף������������磡����", 3).show();
	                return;  
	            }
	            
	            
			    String username_value=username.getText().toString();   //��ȡ�û�����Ϣ
			
		/*	    if(username_value==null||username_value.trim().equals(""))       //�ж��û����Ƿ�Ϊ��
			    {
					String string="�û�������Ϊ��";
					SpannableString warn=new SpannableString(string);
					warn.setSpan(new AbsoluteSizeSpan(15), 0, string.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					warn.setSpan(new BackgroundColorSpan(Color.WHITE), 0, string.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					warn.setSpan(new ForegroundColorSpan(Color.RED), 0, string.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					username.setError(warn);
					return ;	
			    }
				String password_value=password.getText().toString();             //��ȡ������Ϣ
				if(password_value==null||password_value.trim().equals(""))      //�ж������Ƿ�Ϊ��
				{
					String string="���벻��Ϊ��";
					SpannableString warn=new SpannableString(string);
					warn.setSpan(new AbsoluteSizeSpan(20), 0, string.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					warn.setSpan(new BackgroundColorSpan(Color.WHITE), 0, string.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					warn.setSpan(new ForegroundColorSpan(Color.RED), 0, string.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					password.setError(warn);
					return ;
				}
		*/		
				//flag=0  ʧ�ܣ�1�ǳ˿ͣ�2��˾��
				int flag = 1;//ApplyUtils.ApplyLogin(username_value, password_value);       //flag=���ýӿں���
				switch(flag)
				{
					case 0:
						dialogButton=new Button(Login.this);      //��ʾ�ĵ�½ʧ��
						dialogButton.setText("�ر�");
						dialogButton.setHeight(2);
						dialogButton.setWidth(3);
						final Dialog dialog=new Dialog(Login.this);
						dialog.setTitle("��������û����������!");
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
        //��ת��ע�����
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
		dialog.setTitle("��ʾ") ; 
        dialog.setMessage("��ȷ���˳���");
        dialog.setPositiveButton("ȷ��", 
	            new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
                        Login.this.finish(); 
					}
				});
        dialog.setNegativeButton("ȡ��", null);
        dialog.show();
    } 
    
    /*private void openGPSSettings() {       
        //��ȡGPS���ڵ�״̬���򿪻��ǹر�״̬��
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER );
        if(gpsEnabled){
             //�ر�GPS
             Settings.Secure.setLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER, false);
        }else{
             //��GPS  
             Settings.Secure.setLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER, true);
      }
    }*/

}
