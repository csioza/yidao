package com.yidao.activity;

import java.io.UnsupportedEncodingException;

import com.yidao.apply.ApplyUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class Regist extends Activity {
	
	private EditText nikname,phonenumber,password,confirmpassword,carnumber,carstyle;
	private Button submit=null;					//提交按钮
	private RadioGroup group=null;               //存储身份变量
	private RadioButton driverRadioButton=null;
	private RadioButton passengerRadioButton=null;
	private String string="";                //错误提示信息变
	private String Nikname_value="";
	private String Phone_value="";
	private String Password_value="";
	private String Confirmpassword_value="";
	private String Carnumber_value="";
	private String Carstyle_value="";
	private int Actor_flag=-1;
	private int Info_flag;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_regist);
	    nikname=(EditText)this.findViewById(R.id.nickName);            //昵称
	    phonenumber=(EditText)this.findViewById(R.id.phone);			//电话号码
	    password=(EditText)this.findViewById(R.id.password);			//密码
	    confirmpassword=(EditText)this.findViewById(R.id.Confirm_password);		//确认密码
	    carnumber=(EditText)this.findViewById(R.id.car_number);					//车牌号
	    carstyle=(EditText)this.findViewById(R.id.car_style);					//车型
	    submit=(Button)this.findViewById(R.id.submit);							//提交按钮
	    group=(RadioGroup)this.findViewById(R.id.actor);
	    driverRadioButton=(RadioButton)this.findViewById(R.id.driver);
	    passengerRadioButton=(RadioButton)this.findViewById(R.id.passenger); 
	    
	    driverRadioButton.setChecked(true);                     //默认设置身份是司机
	    Actor_flag=1;            //标识为司机 
	    group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {        //监听选择的身份    
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{ 
				if(checkedId==passengerRadioButton.getId())       //选择了乘客
				{
					Actor_flag=0;
					carnumber.setEnabled(false);     //禁止输入车牌号和车型
					carstyle.setEnabled(false);
					   
				}
				else if(checkedId==driverRadioButton.getId())   //选择了司机
				{
					Actor_flag=1;
					carnumber.setEnabled(true);     //输入车票号和车型
					carstyle.setEnabled(true);
				}			
			}
			});
	    submit.setOnClickListener(new View.OnClickListener()
	    {	
		     public void onClick(View v) 
			{
				// TODO Auto-generated method stub
		    	//查看网络连接情况
				ConnectivityManager manager = (ConnectivityManager) Regist.this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE); 
		        if (manager == null) {  
		            //return false;  
		        }  	        
		        NetworkInfo networkinfo = manager.getActiveNetworkInfo();  
		        if (networkinfo == null || !networkinfo.isAvailable()) {  
		            Toast.makeText(Regist.this, "亲，请先连接网络！！！", 3).show();
		            return;  
		        }
		            
				Nikname_value=nikname.getText().toString();
				if(Nikname_value==null||Nikname_value.trim().equals(""))            //判断昵称是否为空
				{
					string="请输入昵称！";
					nikname.setError(Warn(string));          //提示用户昵称
					return ;	
				}
				Phone_value=phonenumber.getText().toString();
				if(Phone_value==null||Phone_value.trim().equals(""))               //判断电话是否为空
				{
					string="请输入电话！";
					phonenumber.setError(Warn(string));
					return ;	
				}
				Password_value=password.getText().toString();
				if(Password_value==null||Password_value.trim().equals(""))             //判断密码是否为空
				{
					string="请输入密码！";
					password.setError(Warn(string));
					return ;	
				}
				Confirmpassword_value=confirmpassword.getText().toString();
				if(Confirmpassword_value==null||Confirmpassword_value.trim().equals(""))       //判断确认密码是否为空
				{	
					string="请输入确认密码！";
					confirmpassword.setError(Warn(string));
					return ;	
				}
				if(!(Password_value.equals(Confirmpassword_value)))                  //判断两次输入的密码是否一致
				{	
					string="两次密码不一致！";
					password.setError(Warn(string));
					return ;
				}
				if(1==Actor_flag)               //是司机则输入车牌号和车型，
				{
					Carnumber_value=carnumber.getText().toString();
					if(Carnumber_value==null||Carnumber_value.trim().equals(""))       //判断车牌号是否为空
					{
						string="请输入车牌号";
						carnumber.setError(Warn(string));
						return ;	
					}
					Carstyle_value=carstyle.getText().toString();
					if(Carstyle_value==null||Carstyle_value.trim().equals(""))       //判断车型是否为空
					{
						string="请输入车型";
						carstyle.setError(Warn(string));
						return ;	
					}
				}else if(-1==Actor_flag)
				{
					passengerRadioButton.setError(Warn("请选择身份"));
					return ;
				}
				
				String name = "";
				String carnum = "";
				String cartype = "";
				//try {
					//name=java.net.URLEncoder.encode(Nikname_value, "UTF-8");
					//name = new String(Nikname_value.getBytes(), "UTF-16");
					//name = new String(name.getBytes("UTF-8"), "UTF-8");
					//carnum = new String(Carnumber_value.getBytes(),"UTF-16");
					
					
				//} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
				//	System.out.println("读取文件为一个内存字符串失败，失败原因是使用了不支持的字符编码"); 
				//	e.printStackTrace();
				//}
				
				//Toast.makeText(Regist.this, Carnumber_value, 3).show();
				//注册成功Info_flag=1，否则Info_flag=0
				Info_flag= ApplyUtils.ApplySignin(Phone_value, Password_value, Nikname_value, Actor_flag, Carnumber_value, Carstyle_value);
				if(1==Info_flag)
				{
					Dialog dialog = new AlertDialog.Builder(Regist.this).setIcon(  

							    android.R.drawable.btn_star).
							    setTitle("提示").setMessage("恭喜您！注册成功！").
							    setPositiveButton("确定",new DialogInterface.OnClickListener() {
									 public void onClick(DialogInterface arg0, int arg1)
									 {
										 Intent intent=new Intent(Regist.this,Login.class);
										 startActivity(intent);
										 Regist.this.finish();
										 
									}}).show();
				}else
				{
					Dialog dialog = new AlertDialog.Builder(Regist.this).setIcon(  

						    android.R.drawable.btn_star).
						    setTitle("提示").setMessage("对不起,注册失败！").
						    /*setPositiveButton("取消",new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface arg0, int arg1)
								 {
									 Intent intent=new Intent(Regist.this,Regist.class);
									 startActivity(intent);
									 Regist.this.finish();
								}}).show();*/
						    setNegativeButton("取消", null).show();
						    
				}
				
				//注册申请接口
				//返回1表示成功，返回0表示失败
				//Info_flag= ApplyUtils.ApplySignin(Phone_value,Password_value, Nikname_value, Actor,
				//		Carnumber_value, Carstyle_value);
				//String Msg="";
			//	Msg=Nikname_value+","+Phone_value+","+Password_value+","+Actor_flag+","+Carnumber_value+
				//	","+Carstyle_value;    								//注册信息
				//Toast.makeText(Regist.this, Msg, 10).show();
										
			 }/* end*/
						
		    });	//提交按钮监听事件结束
	}
	
	private SpannableString Warn(String Inf)    //显示提示信息接口函数    
	{
		SpannableString warn=new SpannableString(Inf);
		warn.setSpan(new AbsoluteSizeSpan(25), 0, Inf.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		warn.setSpan(new BackgroundColorSpan(Color.WHITE), 0, Inf.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		warn.setSpan(new ForegroundColorSpan(Color.RED), 0, Inf.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return warn;
	}
	public void onBackPressed() {  
        Intent intent=new Intent(Regist.this, Login.class);
		startActivity(intent); 
        Regist.this.finish(); 
    }  
}