package com.yidao.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.CoordinateConvert;
import com.baidu.mapapi.Projection;
import com.baidu.mapapi.RouteOverlay;
import com.yidao.apply.ApplyUtils;
import com.yidao.json.JsonTools;
import com.yidao.http.HttpUtils;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Driver extends MapActivity {
    /** Called when the activity is first created. */
	private MapView mapView;
	private BMapManager bMapManager;//���ص�ͼ����
	private MKSearch mkSearch;//���ڼ�������
	private String keystring = "125C5C47491C0E734F6A9344CF7DDC8E0FD6DB09";
	//��Ӱٶȵ�ͼһЩ�ؼ�
	private MapController mapController;
	//GPS
	public LocationManager locationManager;
	Location location;
	private int latitude;
	private int longitude;
	private int baidulat;
	private int baidulng;
	List<Overlay> overlayList;
	private SentMessage sentMessage;
	private Freshmap freshmap;
	List<Map<String,Object>> list;
	private int listtux;
	private String phonenum;
	String phonenum_p;
	int select;
	boolean isCall;
	boolean isPick;
	boolean isFirst;
	//������ʼ����յ�
	private MKPlanNode start;
	private MKPlanNode end;
	MKDrivingRouteResult mkDrivingRouteResult;
	//private boolean listmux;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        Intent intent = getIntent();
        phonenum = intent.getStringExtra("phonenum");
        System.out.println("====================>"+phonenum+"<====================");
        mapView = (MapView)this.findViewById(R.id.bmapView);
        bMapManager = new BMapManager(Driver.this);
        //���ذٶ�key
        bMapManager.init(keystring, new MKGeneralListener(){

			public void onGetNetworkState(int arg0) {
				// TODO Auto-generated method stub
				if (arg0==300){
					Toast.makeText(Driver.this, "key����", 1).show();
				}
			}

			public void onGetPermissionState(int arg0) {
				// TODO Auto-generated method stub
				
			}
        });
        
        this.initMapActivity(bMapManager);
        mapView.setBuiltInZoomControls(true);//�����������Ź���
        mapController = mapView.getController();
        
        //ʵ������ͼ��ѯ��
        mkSearch = new MKSearch();
        mkSearch.init(bMapManager, new MySearchListener());
        start = new MKPlanNode();
        end = new MKPlanNode();
        mkSearch.setDrivingPolicy(MKSearch.EBUS_TIME_FIRST);//����ʱ�����Ȳ���
        
        
        //phonenum = "15242044195";//�Լ��ĵ绰���룬ͨ����ͼ�л����
        phonenum_p = "";//�򳵵ĳ˿͵绰����ĳ�ʼ��
        select = 3;//����
        isCall = false;
        isPick = false;
        isFirst = true;
        //GPS
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        //���÷�����
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);
        
        //��������
        updateWithNewLocation(location);
        
        //�������б�ʵ����
        overlayList = mapView.getOverlays();
        
        //ע��һ�������Եĸ��£�3000ms����һ��
        //locationListener����������λ��Ϣ�ĸı�
        locationManager.requestLocationUpdates(provider, 3000, 0, gpslocationListener);
        
        GeoPoint gp1 = new GeoPoint(latitude,longitude);
		GeoPoint gp2 = CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(gp1));
		baidulat = gp2.getLatitudeE6();
		baidulng = gp2.getLongitudeE6();
		
		list = null;
		listtux = 1;//list���ʿ���
		        
        sentMessage = new SentMessage();//���ͺͻ�ȡ��Ϣ�߳�
        sentMessage.start();
        
        freshmap = new Freshmap();//ˢ�µ�ͼ�߳�
        freshmap.start();
                            
    }
    
    public class MySearchListener implements MKSearchListener{

		public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			mkDrivingRouteResult = arg0;
		}

		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		public void onGetRGCShareUrlResult(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    //������������Լ������꣬���ܷ�����������������Ϣ����ˢ��ͼ����߳�
    private class SentMessage extends Thread{
		public void run(){
			while(true){
				
				String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=position_c&phonenum="+phonenum+"&latitude=" + baidulat + "&longitude=" + baidulng;
				String jsonstring = HttpUtils.getJsonContent(path);
				System.out.println("==========================>"+phonenum);
				while(listtux == 0){
					try {
						sleep(800);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				listtux = 0;
				list = JsonTools.getListMaps("listmap", jsonstring);
				if(list.isEmpty()){
					list = JsonTools.getListMaps("apply_passagers", jsonstring);
					if(!list.isEmpty()){
						isCall = true;
					}else{
						list = JsonTools.getListMaps("passager_position", jsonstring);
						if(isFirst&&!list.isEmpty()){
							Map<String,Object> maps = list.get(0);
						    int lat = (Integer)maps.get("latitude");
						    int lng = (Integer)maps.get("longitude");
						    start.pt = new GeoPoint(baidulat,baidulng);
						    end.pt = new GeoPoint(lat,lng);
						    mkSearch.drivingSearch(null, start, null, end);
						    isFirst = false;
						}
					}
				}else{
					isCall = false;
				}
				//Log.i("Driver", result);
				listtux = 1;
				
				try {
					sleep(5000);
			    } catch (InterruptedException e) {}    
			}
		}
	}
    
    //ˢ��ͼ���̣߳�ÿ4��ˢ��һ��
    private class Freshmap extends Thread{
    	
		public void run()
		{   
			GeoPoint gp1 = new GeoPoint(baidulat,baidulng);
			Log.i("Driver",baidulat+"  "+baidulng);
	        mapController.setCenter(gp1);//�������ĵ�
	        mapController.setZoom(12);//�������ż���Ϊ12
	        
			while(true){
				overlayList.clear();
		        //������ͼƬ
		        Drawable drawableself = getResources().getDrawable(R.drawable.self);
		        overlayList.add(new SelfOverLayItem(drawableself));
		        if ( list !=null && !list.isEmpty()) {
					Drawable drawableothers = getResources().getDrawable(R.drawable.passenger);
					overlayList.add(new OthersOverLayItem(drawableothers));
				}
		        if(mkDrivingRouteResult != null){
		            RouteOverlay routeOverlay = new RouteOverlay(Driver.this, mapView);
		            routeOverlay.setData(mkDrivingRouteResult.getPlan(0).getRoute(0));
		            overlayList.add(routeOverlay);
		        }
				//ˢ��MapView
				mapView.postInvalidate();
				try {
					sleep(6000);
			    } catch (InterruptedException e) {}
			    
		    }
			
		}
	}
    //������ͼ��--�Լ�
    public class SelfOverLayItem extends ItemizedOverlay<OverlayItem>{

    	private List<OverlayItem> listitems = new ArrayList<OverlayItem>();
    	
		public SelfOverLayItem(Drawable arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
			
			GeoPoint geoPoint = new GeoPoint(baidulat, baidulng);
			//Projection projection = mapView.getProjection(); //mapviewΪMapView����  
			//Point point = new Point();
			//projection.toPixels(geoPoint, point);
			//point.x = point.x-10;
			//point.y = point.y-10;
			//geoPoint = projection.fromPixels(point.x, point.y);
			listitems.add(new OverlayItem(geoPoint,"�Լ�","������"));
			populate();
		}

		@Override
		protected OverlayItem createItem(int arg0) {
			// TODO Auto-generated method stub
			return listitems.get(arg0);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return listitems.size();
		}
		@Override
    	public boolean onTap(int i) {
			Toast.makeText(Driver.this, listitems.get(i).getTitle(), 3).show();
    		return true;
    	}
    	
    }
    //������ͼ��---�������ˣ���˾����˿�
    public class OthersOverLayItem extends ItemizedOverlay<OverlayItem>{

    	private List<OverlayItem> listitems = new ArrayList<OverlayItem>();
    	
		public OthersOverLayItem(Drawable arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
			
			if ( listtux == 1){
				//listmux = false;
				listtux = 0;
				for(int i = 0; i<list.size();i++){
					Map<String,Object> maps = list.get(i);
					
					Set<String> key = maps.keySet();
					Iterator<String> it = key.iterator();
					
					String phonenum = (String)maps.get("phonenum");
					String name = (String)maps.get("name");
					int lat = (Integer)maps.get("latitude");
					int lng = (Integer)maps.get("longitude");
					double score = Double.parseDouble(maps.get("appraisescore").toString());
					String destination = (String)maps.get("destination");
					
					GeoPoint geoPoint = new GeoPoint(lat,lng);
					//Projection projection = mapView.getProjection(); //mapviewΪMapView����  
					//Point point = new Point();
					//projection.toPixels(geoPoint, point);
					//point.x = point.x-15;
					//point.y = point.y-15;
					//geoPoint = projection.fromPixels(point.x, point.y);
					listitems.add(new OverlayItem(geoPoint,phonenum,name+"\n��  ��  �� :    "+score+"��" +"\nĿ  ��  �أ�   "+destination));
					//maps.get(i);
				}
				listtux = 1;
				populate();
			}
			populate();
		}

		@Override
		protected OverlayItem createItem(int arg0) {
			// TODO Auto-generated method stub
			return listitems.get(arg0);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return listitems.size();
		}
		@Override
    	public boolean onTap(int i) {
			//Toast.makeText(Driver.this, listitems.get(i).getTitle(), 3).show();
			
			phonenum_p = listitems.get(i).getTitle();
			String information = listitems.get(i).getSnippet();
			Builder dialog = new AlertDialog.Builder(Driver.this) ;   
			dialog.setTitle("������Ϣ") ; 
	        dialog.setMessage("�绰���룺   "+phonenum_p+"\n��       �ƣ�   "+information);
			if((!isPick)&&isCall){
				dialog.setPositiveButton("�ؿ�", 
	        		             new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
	                                    int flag = ApplyUtils.ApplyPick(phonenum_p, phonenum);
	                                    //System.out.println("==============================="+flag);
	                                    isPick = true;
									} 
                                 });
            }
			dialog.setNeutralButton("����",
			         new DialogInterface.OnClickListener() {
		                 public void onClick(DialogInterface arg0, int arg1) {
		              	    // TODO Auto-generated method stub
		                	//������� parse������������
		                     Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenum_p));
		                     //֪ͨactivtity�������call����
		                     Driver.this.startActivity(intent);
		                 } 
                    });
			dialog.setNegativeButton("ȡ��", null);
	        dialog.show();
    		return true;
    	}
    	
    }
    
    
    //����GPS��ȡ��γ��
    protected void updateWithNewLocation(Location location){
        //String resultString = "��λʧ�ܣ�";
    	if (location!=null){
    		double geolat = location.getLatitude()*1E6;
    		latitude = (int)geolat;
    		double geolng = location.getLongitude()*1E6;
    		longitude = (int)geolng;
    		GeoPoint gp1 = new GeoPoint(latitude,longitude);
			GeoPoint gp2 = CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(gp1));
			baidulat = gp2.getLatitudeE6();
			baidulng = gp2.getLongitudeE6();
    		//Toast.makeText(Driver.this, "γ�ȣ�"+latitude+" ���ȣ�"+longitude, 1).show();
    	}
    	else{
    		//Toast.makeText(Driver.this, resultString, 1).show();
    	}
    }
    //loactionListener�������� ��ʵʱ��������
    private final LocationListener gpslocationListener = new LocationListener(){

    	//�������ʱ�����˺���
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			updateWithNewLocation(location);
		}

		//Provider����ʱ�����˺���������GPS���ر�
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			updateWithNewLocation(null);
		}

		//Provider����ʱ�����˺���������GPS����
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		//Provider��״̬�ڿ��á���ʱ�����ú��޷�������״ֱ̬���л�ʱ�����˺���
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
    	
    };
    
    
	
        
	//MENU���¼�����    
       
	private final int MENU_SELFLOCATION = Menu.FIRST;
	private final int MENU_APPLY = Menu.FIRST + 1;
	private final int MENU_SETTING = Menu.FIRST + 2;
	private final int MENU_SELFINFO = Menu.FIRST + 3;
	//private final int MENU_SEND = Menu.FIRST + 4;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		menuItemSelected(item);
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		createMenu(menu);
		return super.onPrepareOptionsMenu(menu);
	}
   
    /**
	 * �����˵�
	 * 
	 * @param menu
	 */
	private void createMenu(Menu menu) {
		menu.clear();
		// �Լ���λ
		MenuItem selfItem = menu.add(0, MENU_SELFLOCATION, 0, "�Լ���λ");
		//selfItem.setVisible(false);//���ɼ�
		//settingItem.setEnabled(bluetoothM.isSearch());//�����ã���ɫ
		// ����򳵻��ؿ�
		MenuItem applyItem = menu.add(0, MENU_APPLY, 0,"�ؿ�");
		// ����
		MenuItem settingItem = menu.add(0, MENU_SETTING, 0,"����");
		MenuItem selfinfoItem = menu.add(0, MENU_SELFINFO, 0,"������Ϣ");
		
	}

	/**
	 * �˵�����
	 * 
	 * @param item
	 */
	private void menuItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		// ��λ�Լ�
		case MENU_SELFLOCATION:
			GeoPoint gp1 = new GeoPoint(baidulat,baidulng);
	        mapController.setCenter(gp1);//�������ĵ�  
			break;
		// ����򳵻��ؿ�
		case MENU_APPLY:
			//TODO:�����������trade���е�����
			Builder dialog = new AlertDialog.Builder(Driver.this) ;   
			dialog.setTitle("��ʾ") ; 
	        dialog.setMessage("ȷ�������ؿͣ�");
			
		    dialog.setPositiveButton("ȷ��", 
	        		                 new DialogInterface.OnClickListener() {
									    public void onClick(DialogInterface arg0, int arg1) {
									    	// TODO Auto-generated method stub
									    	int flag = ApplyUtils.ApplyCancelPick(phonenum);
									    	if(flag == 0) {
										    	isCall = false; //����򳵱�־λ
										    	isPick = false;
										    	isFirst = true;
										        //mkDrivingRouteResult = null;
										        if(mkDrivingRouteResult!=null){
													mkDrivingRouteResult = null;
													Builder dialog2 = new AlertDialog.Builder(Driver.this);
													dialog2.setTitle("����") ; 
											        dialog2.setMessage("�Ƿ�������ۣ�");
											        dialog2.setPositiveButton("����", 
								        		             new DialogInterface.OnClickListener() {
																public void onClick(DialogInterface arg0, int arg1) {
																	// TODO Auto-generated method stub
																	
																	new AlertDialog.Builder(Driver.this)  
																		.setTitle("��ѡ��")  
																		.setIcon(android.R.drawable.ic_dialog_info)                  
																		.setSingleChoiceItems(new String[] {"1��","2��","3��","4��","5��"}, 2,   
																		    new DialogInterface.OnClickListener() {  
																		                              
																		        public void onClick(DialogInterface dialog, int which) {  
																		              
																		        	//Toast.makeText(Driver.this, "which = "+which, 2).show();
																		        	select = which + 1;
																		        	
																		        	//dialog.dismiss();
																	            }  
																		    }  
																		)  
																		.setPositiveButton("ȷ��", 
	        		                                                           new DialogInterface.OnClickListener() {
									                                               public void onClick(DialogInterface arg0, int arg1) {
									                                            	// TODO Auto-generated method stub
									                                            	   int result = ApplyUtils.ApplyAppraise(phonenum_p, select);
									                                               }
																		       })
																	    .setNegativeButton("ȡ��", null) 
																	    .show(); 

																}
											                 });
											        dialog2.setNegativeButton("ȡ��", null);
											        dialog2.show();
												}
										    }
									    } 
                                     });
			dialog.setNegativeButton("ȡ��", null);
	        dialog.show();
			break;
		// �����ò˵�
		case MENU_SETTING:
			final View contentView =(View)LayoutInflater.from(getApplicationContext()).inflate(R.layout.setting_dialog_driver,null);     //
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("����").setView(contentView).setPositiveButton("ȷ��",
			new DialogInterface.OnClickListener()
			{
				 public void onClick(DialogInterface arg0, int arg1)
				 {
					// TODO Auto-generated method stub
					final EditText Pasenger_number=(EditText)contentView.findViewById(R.id.pasenger_number);
					final EditText Driver_destination=(EditText)contentView.findViewById(R.id.driver_destination);
					String psng_number_value ="" ;                       
					String destination_value ="";
					psng_number_value = Pasenger_number.getText().toString();
					destination_value = Driver_destination.getText().toString();
					if(psng_number_value==null||psng_number_value.trim().equals(""))
					{
						Toast.makeText(Driver.this,"�˿�������Ϊ�գ�", 1).show(); 
					}else if(destination_value==null||destination_value.trim().equals(""))
					{
						Toast.makeText(Driver.this,"Ŀ�ĵز���Ϊ�գ�", 1).show(); 
					}else
					{	
						Toast.makeText(Driver.this, psng_number_value+"    "+destination_value, 1).show();    // �˴����ַ����������ݿ�		
						//int flag=0;��־λ��Ϊ0ʱ��ʾ�޸ĳɹ�
						//�����޸�ʧ��
						int flag = ApplyUtils.ApplyDrvrSetting(phonenum, psng_number_value, destination_value );
						System.out.println("========================>flag+"+flag);
						if(flag == 0){
							Toast.makeText(Driver.this,"�޸ĳɹ�", 3).show();
						}else{
							Toast.makeText(Driver.this,"�޸�ʧ��", 3).show();
						}
						
					}
						
				 }
			})
			.setNegativeButton("ȡ��",null)
			.show();
			break;
		case MENU_SELFINFO:
			Intent intent=new Intent(Driver.this, Setting.class);
	        intent.putExtra("phonenum", phonenum);
			startActivity(intent); 
			Driver.this.finish();
			break;
		//case MENU_SEND:
		//	break;
		}
	}
	
	
	public void onBackPressed() {  
        //Intent intent=new Intent(Regist.this, Login.class);
		//startActivity(intent); 
    	Builder dialog = new AlertDialog.Builder(Driver.this);   
		dialog.setTitle("��ʾ") ; 
        dialog.setMessage("��ȷ���˳���");
        dialog.setPositiveButton("ȷ��", 
	            new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Driver.this.finish(); 
					}
				});
        dialog.setNegativeButton("ȡ��", null);
        dialog.show();
    }
	
	//�����������ڿ���
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	if (bMapManager!=null){
    		bMapManager.destroy();
    		bMapManager = null;
    	}
    }
    @Override
    protected void onResume(){
    	super.onResume();
    	if (bMapManager!=null){
    		bMapManager.start();
    	}
    }
    @Override
    protected void onPause(){
    	super.onPause();
    	if (bMapManager!=null){
    		bMapManager.stop();
    	}
    }
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}