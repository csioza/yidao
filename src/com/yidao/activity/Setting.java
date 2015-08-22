package com.yidao.activity;

import java.util.List;
import java.util.Map;

import com.yidao.apply.ApplyUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.PopupWindow; 
import android.widget.TextView;
import android.widget.Toast;
public class Setting extends Activity
{
	private TextView NiCheng,DianHua,MiMa,ChePaiHao,CheXing,ShenFen;
	private Button Nikname,Phone,Password,Actor,Carstyle,Carnumberm,Back ;
	private Button confirmbutton,cancelbutton;
	private RadioButton radiobutton;
	private RadioGroup group;
	private String Infomations;
	private String phone;
	List<Map<String,Object>> list ;   //存放个人信息   
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		Intent intent = getIntent();
        phone = intent.getStringExtra("phonenum");           //获取电话号码
		//phone = "15242044192";
		Nikname=(Button)this.findViewById(R.id.nikname);
        Phone=(Button)this.findViewById(R.id.phonenumber);
        Password=(Button)this.findViewById(R.id.password);
        Actor=(Button)this.findViewById(R.id.actor);
        Carnumberm=(Button)this.findViewById(R.id.carnumber);
        Carstyle=(Button)this.findViewById(R.id.carstyle);
        Back=(Button)this.findViewById(R.id.goback);
        NiCheng=(TextView)this.findViewById(R.id.NiCheng);
        DianHua=(TextView)this.findViewById(R.id.DianHua);
        MiMa=(TextView)this.findViewById(R.id.MiMa);
        ChePaiHao=(TextView)this.findViewById(R.id.ChePaiHao);
        CheXing=(TextView)this.findViewById(R.id.CheXing);
        ShenFen=(TextView)this.findViewById(R.id.ShenFen);
        /*查数据库获取司机或者乘客的注册信息   Infomations=   */
        list = ApplyUtils.ApplySelfInfo( phone);
        
        NiCheng.setText((String)list.get(0).get("name"));//显示信息
        DianHua.setText(phone);
        MiMa.setText((String)list.get(0).get("password"));
        ChePaiHao.setText((String)list.get(0).get("carnum"));
        CheXing.setText((String)list.get(0).get("cartype"));
        if(((Integer)list.get(0).get("type")) == 1 )
        {
        	ShenFen.setText("司   机"); 
        }
        else
        {
        	ShenFen.setText("乘   客"); 
        }
        
 ////////////////////////////////////////////////////////////////////////////
        Nikname.setOnClickListener(          //修改昵称
            new OnClickListener() 
        	{
			
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
				//	initNiknameWindow();	   //初始化修改昵称对话框
					final View contentView =(View)LayoutInflater.from(getApplicationContext()).inflate(R.layout.nikname,null);     //
					AlertDialog.Builder builder = new Builder(Setting.this);
					builder.setTitle("修改昵称").setView(contentView).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						 public void onClick(DialogInterface arg0, int arg1)
						 {
							// TODO Auto-generated method stub
							final EditText NikName;
							NikName=(EditText)contentView.findViewById(R.id.nicheng);
							String Nikname="" ;
							Nikname=NikName.getText().toString();
							if(Nikname==null||Nikname.trim().equals(""))
							{
								Toast.makeText(Setting.this, "请输入昵称", 1).show(); 
							}else
							{
								//标志位，为0时表示修改成功
								int flag = ApplyUtils.ApplySettings(phone, "name", Nikname);
								if(0 == flag)
								{
									NiCheng.setText(Nikname);
									Toast.makeText(Setting.this,"修改成功", 3).show();  
								}
								else
								{
									Toast.makeText(Setting.this,"修改失败", 1).show();  
								}
								//	int flag=1;
								//if(1==flag)修改成功，否则修改失败
							}
						}})
					.setNegativeButton("取消",null)
					.show();

					
				}
			});

/////////////////////////////////////////////////////////////////
        Phone.setOnClickListener
        (          //修改电话
	        new OnClickListener()
	        {  
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//initPhoneNumberWindow();		//初始化修改电话对话框
					
					final View contentView =(View)LayoutInflater.from(getApplicationContext()).inflate(R.layout.phone,null);     //
					AlertDialog.Builder builder = new Builder(Setting.this);
					builder.setTitle("修改电话").setView(contentView).setPositiveButton("确定",
					new DialogInterface.OnClickListener()
					{
						 public void onClick(DialogInterface arg0, int arg1)
						 {
							// TODO Auto-generated method stub
							final EditText MPhone;
						    MPhone=(EditText)contentView.findViewById(R.id.xphone);
							String phone1="" ;
							phone1=MPhone.getText().toString();
							if(Nikname==null||phone.trim().equals(""))
							{
								Toast.makeText(Setting.this, "电话不能为空！", 1).show(); 
							}
							else
							{
								//标志位，为0时表示修改成功
								int flag = ApplyUtils.ApplySettings(phone, "phonenum", phone1);
								if(0 == flag)
								{
									DianHua.setText(phone1);
									phone = phone1;
									Toast.makeText(Setting.this,"修改成功", 3).show();  
								}
								else
								{
									Toast.makeText(Setting.this,"修改失败", 1).show();  
								}
							}
						 }
					 })
					.setNegativeButton("取消",null)
					.show();
					
					
					
				}
			}) ;
//////////////////////////////////////////////////////////////////
        Password.setOnClickListener(               //修改密码
        new OnClickListener()
        {
			
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				//initPasswordWindow() ;				//初始化修改密码对话框
				final View contentView =(View)LayoutInflater.from(getApplicationContext()).inflate(R.layout.password,null);     //
				AlertDialog.Builder builder = new Builder(Setting.this);
				builder.setTitle("修改密码").setView(contentView).setPositiveButton("确定",
				new DialogInterface.OnClickListener()
				{
					 public void onClick(DialogInterface arg0, int arg1)
					 {
						// TODO Auto-generated method stub
						final EditText editText1=(EditText)contentView.findViewById(R.id.Xpassword);
						final EditText editText2=(EditText)contentView.findViewById(R.id.Cpassword);
						String password="" ;
						String Cpassword="";
						password=editText1.getText().toString();
						Cpassword=editText2.getText().toString();
						if(password==null||password.trim().equals("")||Cpassword==null||Cpassword.trim().equals(""))
						{
							Toast.makeText(Setting.this,"密码不能为空！", 1).show(); 
						}else if(!password.equals(Cpassword))
						{
							Toast.makeText(Setting.this,"两次输入的密码不一致！", 1).show(); 
						}else
						{	
							//标志位，为0时表示修改成功
							int flag = ApplyUtils.ApplySettings(phone, "password", Cpassword);
							if(0 == flag)
							{
								MiMa.setText(password);
								Toast.makeText(Setting.this,"修改成功", 3).show();  
							}
							else
							{
								Toast.makeText(Setting.this,"修改失败", 1).show();  
							}
						}
							
					 }
				})
				.setNegativeButton("取消",null)
				.show();
			}
		});
///////////////////////////////////////////////////////////////////
        Carnumberm.setOnClickListener(                  //修改车牌号
        new OnClickListener()
        {
			
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				//initCarnumberWindow();					//初始化修改车牌号对话框
				final View contentView =(View)LayoutInflater.from(getApplicationContext()).inflate(R.layout.carnumber,null);     //
				AlertDialog.Builder builder = new Builder(Setting.this);
				builder.setTitle("修改车牌号").setView(contentView).setPositiveButton("确定",
						new DialogInterface.OnClickListener()
				{
					 public void onClick(DialogInterface arg0, int arg1)
					 {
						// TODO Auto-generated method stub
						EditText MCarnumber;
						MCarnumber=(EditText)contentView.findViewById(R.id.Xcarnumber);
						String carnumber="" ;
						carnumber=MCarnumber.getText().toString();
						if(carnumber==null||carnumber.trim().equals("")) 
						{	//输入的车牌号为空
							Toast.makeText(Setting.this, "修改失败",1).show();
						}
						else
						{
							//标志位，为0时表示修改成功
							int flag = ApplyUtils.ApplySettings(phone, "carnum", carnumber);
							if(0 == flag)
							{
								ChePaiHao.setText(carnumber);
								Toast.makeText(Setting.this,"修改成功", 3).show();  
							}
							else
							{
								Toast.makeText(Setting.this,"修改失败", 1).show();  
							}
						}
					 }
				})
				.setNegativeButton("取消", null)
				.show();
			}
		});
/////////////////////////////////////////////////////////////////////////////////////////////        
        Carstyle.setOnClickListener(					//修改车型
        	new OnClickListener()
        	{
			
				public void onClick(View v) {
					// TODO Auto-generated method stub
				//	initCarstyleWindow();                 //初始化修改车型对话框
					final View contentView =(View)LayoutInflater.from(getApplicationContext()).inflate(R.layout.carstyle,null);     //
					AlertDialog.Builder builder = new Builder(Setting.this);
					builder.setTitle("修改车型").setView(contentView).setPositiveButton("确定",
					new DialogInterface.OnClickListener()
					{
						 public void onClick(DialogInterface arg0, int arg1)
						 {
							 EditText Mcarstyle;
								Mcarstyle=(EditText)contentView.findViewById(R.id.Xcarstyle);
								String carstyle="" ;
								carstyle=Mcarstyle.getText().toString();
								if(carstyle==null||carstyle.trim().equals(""))
								{
									Toast.makeText(Setting.this,"修改失败",1).show();
								}else
								{
									int flag = ApplyUtils.ApplySettings(phone, "cartype", carstyle);
									if(0 == flag)
									{
										CheXing.setText(carstyle);
										Toast.makeText(Setting.this,"修改成功", 3).show();  
									}
									else
									{
										Toast.makeText(Setting.this,"修改失败", 1).show();  
									}
								}
							}
						 })
					.setNegativeButton("取消", null)
					.show();
			}
		});
///////////////////////////////////////////////////////////////////////////////////     
        Actor.setOnClickListener(						//修改身份
        	new OnClickListener()
        	{
			
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
				//initActorWindow();					//初始化修改身份对话框
					final View contentView =(View)LayoutInflater.from(getApplicationContext()).inflate(R.layout.actor,null);     //
					AlertDialog.Builder builder = new Builder(Setting.this);
					builder.setTitle("修改身份").setView(contentView).setPositiveButton("确定",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface arg0, int arg1)
						 {
							int len,i;
							String Actor="";
							group=(RadioGroup)contentView.findViewById(R.id.Act);
							len=group.getChildCount();		            //获得单选按钮的个数
					    	for(i=0;i<len;i++)
							{
								radiobutton=(RadioButton)group.getChildAt(i);
								if(radiobutton.isChecked())
								{
									Actor=radiobutton.getText().toString(); 
									break;
								}
							}
							if(i>=len)                  //判断是否已经选择角色,如果没选择不让提交
							{
								Toast.makeText(Setting.this,"请选择身份",1).show();
							}else
							{
								String flag1 ;//标识身份状态 1为司机0为乘客
								if(Actor.equals("司机"))
								{
									flag1 = "1"; 
								}
								else
								{
									flag1 = "0";
								}
								int flag = ApplyUtils.ApplySettings(phone, "type", flag1);
								if(0 == flag)
								{
									ShenFen.setText(Actor);
									Toast.makeText(Setting.this,"修改成功", 3).show();  
								}
								else
								{
									Toast.makeText(Setting.this,"修改失败", 1).show();  
								}
							}
							
						 }
					 })
					.setNegativeButton("取消", null)
					.show();
				}
        	});
/////////////////////////////////////////////////////////////////////////////////////////////////      
        Back.setOnClickListener(           //返回登陆成功首页
            new	OnClickListener()
        	{
			
        		public void onClick(View v)
        		{
        			if(ShenFen.getText().equals("司机"))
        			{
        				Intent intent=new Intent(Setting.this,Driver.class);      
        				//Intent intent=new Intent(Driver.this, Setting.class);
        		        intent.putExtra("phonenum", phone);
        				startActivity(intent); 
        				Setting.this.finish();
        				//startActivity(intent);
        			}
        			else
        			{
        				Intent intent=new Intent(Setting.this,Passenger.class);       
        				intent.putExtra("phonenum", phone);
        				startActivity(intent);
        				Setting.this.finish();
        			}
        			// TODO Auto-generated method stub
        			
        		}
		});         
	}
	public void onBackPressed() {  
		if(ShenFen.getText().equals("司机"))
		{
			Intent intent=new Intent(Setting.this,Driver.class);       
			//Intent intent=new Intent(Driver.this, Setting.class);
	        intent.putExtra("phonenum", phone);
			startActivity(intent); 
			Setting.this.finish();
			//startActivity(intent);
		}
		else
		{
			Intent intent=new Intent(Setting.this,Passenger.class);       
			intent.putExtra("phonenum", phone);
			startActivity(intent);
			Setting.this.finish();
		}
    	
    }  
}