package com.yidao.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.yidao.apply.ApplyUtils;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DriverList extends Activity {
 
	private ListView item_list = null;                    //�б���ͼ
	private TextView nikname = null,phonenum = null,carnumber = null,trust = null,carstyle = null;
	ArrayList<TreeMap<String,Object>>dialog_info = new ArrayList<TreeMap<String,Object>>();				//��ŵ����Ի������Ϣ	
	List<Map<String,Object>> list = null;// ����list= new ArrayList<Map<String,Object>>(); 				//��Ŵ����ݿ��е�����˾����Ϣ
	private String carNum=null;							//��Ŵ�list�л�ȡ�ĳ��ƺ�
	private String phonenumber=null;
	private String phone;
	
	private int lat_a, lng_a; 			//����û��ľ�γ��
	//private double lat_b=123.1234566,lngt_b=80.123455;
	//private double trus=4.3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver_list);
		Intent intent = getIntent();
        phone = intent.getStringExtra("phonenum");
        lat_a = intent.getIntExtra("latitude",0);
        lng_a = intent.getIntExtra("longitude",0);
		item_list = (ListView)this.findViewById(R.id.MyListView);
		
		list = ApplyUtils.ApplyCarList("123456");
		if(list == null){
			Toast.makeText(DriverList.this, "null", 3).show();
		}
		if(list.isEmpty()){
			Toast.makeText(DriverList.this, "��", 3).show();
		}
		//װ��item���ص����� ����������Ϊ���ƺź;���
		ArrayList<TreeMap<String, Object>> mylist = new ArrayList<TreeMap<String, Object>>();
		
		
		int flag = 0;
		while(list!= null)
		{
			for(int i=0;i<list.size();i++)
			{	
				TreeMap<String,Object>map = new TreeMap<String,Object>();
				carNum = (String)list.get(i).get("carnum");
				double lat_b,lng_b;
				//int lat = (Integer)maps.get("latitude");
			    //int lng = (Integer)maps.get("longitude");
				lat_b=((Integer)list.get(i).get("latitude"))/(1000000.0);    //��ȡγ��
				lng_b=((Integer)list.get(i).get("longitude"))/(1000000.0);   //��ȡ����
				int distance = (int)Comput_distance(lat_a/1000000, lng_a/1000000, lat_b,  lng_b);    //�������
				map.put("carnumber", carNum);
				map.put("distance", String.valueOf(distance));
				mylist.add(map);       //���б������������
				
				map.put("phonenumber",(String)list.get(i).get("phonenum"));
				map.put("nikname", (String)list.get(i).get("name"));
				map.put("carstyle",(String)list.get(i).get("cartype"));
				map.put("carnumber", (String)list.get(i).get("carnum"));
				double appraise= Double.parseDouble(list.get(i).get("appraisescore").toString());//��ȡ��������
				map.put("trust",String.valueOf(appraise)+"��");
				dialog_info.add(map);          
			}
			flag = 1;
			break;
		} 
		if(1 == flag)
		{
			 SimpleAdapter ListAdapter_info = new SimpleAdapter(  
					   this, mylist,//������Դ    
				        R.layout.my_listitem,//ListItem��XMLʵ��      
				          //��̬������ListItem��Ӧ������           
				        new String[] {"carnumber", "distance"},   
				           //ListItem��XML�ļ����������TextView ID   
				        new int[] {R.id.carNumber,R.id.distance});  
				    //��Ӳ�����ʾ ���ƺź;���  
			   
			item_list.setAdapter(ListAdapter_info);  //����˾���б���Ϣ
			  //����ÿ��Item����¼�
			item_list.setOnItemClickListener(new OnItemClickListener() {   
					public void onItemClick(AdapterView<?> parent, View arg1,
							int position, long id){
					 phonenumber = dialog_info.get(position).get("phonenumber").toString().trim();     //��ȡ˾���绰����
		
					final View contentView = (View)LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_information,null);    			
					//�󶨰�ť
					nikname = (TextView)contentView.findViewById(R.id.nicheng1);
					phonenum = (TextView)contentView.findViewById(R.id.phoneNumber1);
					carnumber = (TextView)contentView.findViewById(R.id.carnumber1);
					carstyle = (TextView)contentView.findViewById(R.id.carstyle1);
					trust = (TextView)contentView.findViewById(R.id.trust1);
					
					/*����Ϣ���ص�dialog�Ի�����*/
					 String info = "";
					 info = dialog_info.get(position).get("nikname").toString().trim();    
					 nikname.setText(info);
					 
					 info = dialog_info.get(position).get("phonenumber").toString().trim();
					// Toast.makeText(MainActivity.this, info, 3).show();
					 phonenum.setText(info);
					 
					 info = dialog_info.get(position).get("trust").toString().trim();
					 trust.setText(info);
					 //Toast.makeText(MainActivity.this, info, 3).show();
					 
					 info = dialog_info.get(position).get("carnumber").toString().trim();
					 carnumber.setText(info);
					// Toast.makeText(MainActivity.this, info, 3).show();
					 
					 info = dialog_info.get(position).get("carstyle").toString().trim();
					 carstyle.setText(info);
					 
					AlertDialog.Builder builder = new Builder(DriverList.this);
					builder.setTitle("˾����Ϣ").setView(contentView).setNeutralButton("��    ��",
					new DialogInterface.OnClickListener()
					{
						 public void onClick(DialogInterface arg0, int arg1)
						 {
							// TODO Auto-generated method stub
							
							 Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenumber));
			                 //֪ͨactivtity�������call����        
							 DriverList.this.startActivity(intent);
						 }
					})
					.setNegativeButton("ȡ    ��", null)
					.show();
			}});
		}
			
	} 
	/*���ݾ�γ�ȼ���˿ͺ�˾���ľ��룬���ص�λ����*/
	private final double EARTH_RADIUS = 6378137.0;  
	private double Comput_distance(int lat_a, int lng_a, double lat_b, double lng_b)
	 {
	       double radLat1 = (lat_a * Math.PI / 180.0);
	       double radLat2 = (lat_b * Math.PI / 180.0);
	       double a = radLat1 - radLat2;
	       double b = (lng_a - lng_b) * Math.PI / 180.0;
	       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
	              + Math.cos(radLat1) * Math.cos(radLat2)
	              * Math.pow(Math.sin(b / 2), 2)));
	       s = s * EARTH_RADIUS;
	       s = Math.round(s * 10000) / 10000;
	       return s;
	    }
	
	public void onBackPressed() {  
        Intent intent=new Intent(DriverList.this, Passenger.class);
        intent.putExtra("phonenum", phone);
		startActivity(intent); 
		DriverList.this.finish();
    	
    }  
}	   
