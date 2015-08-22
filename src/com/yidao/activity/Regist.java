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
	private Button submit=null;					//�ύ��ť
	private RadioGroup group=null;               //�洢��ݱ���
	private RadioButton driverRadioButton=null;
	private RadioButton passengerRadioButton=null;
	private String string="";                //������ʾ��Ϣ��
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
	    nikname=(EditText)this.findViewById(R.id.nickName);            //�ǳ�
	    phonenumber=(EditText)this.findViewById(R.id.phone);			//�绰����
	    password=(EditText)this.findViewById(R.id.password);			//����
	    confirmpassword=(EditText)this.findViewById(R.id.Confirm_password);		//ȷ������
	    carnumber=(EditText)this.findViewById(R.id.car_number);					//���ƺ�
	    carstyle=(EditText)this.findViewById(R.id.car_style);					//����
	    submit=(Button)this.findViewById(R.id.submit);							//�ύ��ť
	    group=(RadioGroup)this.findViewById(R.id.actor);
	    driverRadioButton=(RadioButton)this.findViewById(R.id.driver);
	    passengerRadioButton=(RadioButton)this.findViewById(R.id.passenger); 
	    
	    driverRadioButton.setChecked(true);                     //Ĭ�����������˾��
	    Actor_flag=1;            //��ʶΪ˾�� 
	    group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {        //����ѡ������    
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{ 
				if(checkedId==passengerRadioButton.getId())       //ѡ���˳˿�
				{
					Actor_flag=0;
					carnumber.setEnabled(false);     //��ֹ���복�ƺźͳ���
					carstyle.setEnabled(false);
					   
				}
				else if(checkedId==driverRadioButton.getId())   //ѡ����˾��
				{
					Actor_flag=1;
					carnumber.setEnabled(true);     //���복Ʊ�źͳ���
					carstyle.setEnabled(true);
				}			
			}
			});
	    submit.setOnClickListener(new View.OnClickListener()
	    {	
		     public void onClick(View v) 
			{
				// TODO Auto-generated method stub
		    	//�鿴�����������
				ConnectivityManager manager = (ConnectivityManager) Regist.this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE); 
		        if (manager == null) {  
		            //return false;  
		        }  	        
		        NetworkInfo networkinfo = manager.getActiveNetworkInfo();  
		        if (networkinfo == null || !networkinfo.isAvailable()) {  
		            Toast.makeText(Regist.this, "�ף������������磡����", 3).show();
		            return;  
		        }
		            
				Nikname_value=nikname.getText().toString();
				if(Nikname_value==null||Nikname_value.trim().equals(""))            //�ж��ǳ��Ƿ�Ϊ��
				{
					string="�������ǳƣ�";
					nikname.setError(Warn(string));          //��ʾ�û��ǳ�
					return ;	
				}
				Phone_value=phonenumber.getText().toString();
				if(Phone_value==null||Phone_value.trim().equals(""))               //�жϵ绰�Ƿ�Ϊ��
				{
					string="������绰��";
					phonenumber.setError(Warn(string));
					return ;	
				}
				Password_value=password.getText().toString();
				if(Password_value==null||Password_value.trim().equals(""))             //�ж������Ƿ�Ϊ��
				{
					string="���������룡";
					password.setError(Warn(string));
					return ;	
				}
				Confirmpassword_value=confirmpassword.getText().toString();
				if(Confirmpassword_value==null||Confirmpassword_value.trim().equals(""))       //�ж�ȷ�������Ƿ�Ϊ��
				{	
					string="������ȷ�����룡";
					confirmpassword.setError(Warn(string));
					return ;	
				}
				if(!(Password_value.equals(Confirmpassword_value)))                  //�ж���������������Ƿ�һ��
				{	
					string="�������벻һ�£�";
					password.setError(Warn(string));
					return ;
				}
				if(1==Actor_flag)               //��˾�������복�ƺźͳ��ͣ�
				{
					Carnumber_value=carnumber.getText().toString();
					if(Carnumber_value==null||Carnumber_value.trim().equals(""))       //�жϳ��ƺ��Ƿ�Ϊ��
					{
						string="�����복�ƺ�";
						carnumber.setError(Warn(string));
						return ;	
					}
					Carstyle_value=carstyle.getText().toString();
					if(Carstyle_value==null||Carstyle_value.trim().equals(""))       //�жϳ����Ƿ�Ϊ��
					{
						string="�����복��";
						carstyle.setError(Warn(string));
						return ;	
					}
				}else if(-1==Actor_flag)
				{
					passengerRadioButton.setError(Warn("��ѡ�����"));
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
				//	System.out.println("��ȡ�ļ�Ϊһ���ڴ��ַ���ʧ�ܣ�ʧ��ԭ����ʹ���˲�֧�ֵ��ַ�����"); 
				//	e.printStackTrace();
				//}
				
				//Toast.makeText(Regist.this, Carnumber_value, 3).show();
				//ע��ɹ�Info_flag=1������Info_flag=0
				Info_flag= ApplyUtils.ApplySignin(Phone_value, Password_value, Nikname_value, Actor_flag, Carnumber_value, Carstyle_value);
				if(1==Info_flag)
				{
					Dialog dialog = new AlertDialog.Builder(Regist.this).setIcon(  

							    android.R.drawable.btn_star).
							    setTitle("��ʾ").setMessage("��ϲ����ע��ɹ���").
							    setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
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
						    setTitle("��ʾ").setMessage("�Բ���,ע��ʧ�ܣ�").
						    /*setPositiveButton("ȡ��",new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface arg0, int arg1)
								 {
									 Intent intent=new Intent(Regist.this,Regist.class);
									 startActivity(intent);
									 Regist.this.finish();
								}}).show();*/
						    setNegativeButton("ȡ��", null).show();
						    
				}
				
				//ע������ӿ�
				//����1��ʾ�ɹ�������0��ʾʧ��
				//Info_flag= ApplyUtils.ApplySignin(Phone_value,Password_value, Nikname_value, Actor,
				//		Carnumber_value, Carstyle_value);
				//String Msg="";
			//	Msg=Nikname_value+","+Phone_value+","+Password_value+","+Actor_flag+","+Carnumber_value+
				//	","+Carstyle_value;    								//ע����Ϣ
				//Toast.makeText(Regist.this, Msg, 10).show();
										
			 }/* end*/
						
		    });	//�ύ��ť�����¼�����
	}
	
	private SpannableString Warn(String Inf)    //��ʾ��ʾ��Ϣ�ӿں���    
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