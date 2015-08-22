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


//import android.R;
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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class Passenger extends MapActivity {
    /** Called when the activity is first created. */
	private MapView mapView;
	private BMapManager bMapManager;//���ص�ͼ����
	private MKSearch mkSearch;//���ڼ�������
	//private EditText pasenger_destination=null;
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
	private PsngFindPsng psngFindPsng;
	private Freshmap freshmap;
	List<Map<String,Object>> list;
	List<Map<String,Object>> list_pf;
	private int listtux, listtux_pf;
	private String phonenum;
	String phonenum_c;
	int select;
	boolean iscall;
	boolean isFirst;
	//������ʼ����յ�
	private MKPlanNode start;
	private MKPlanNode end;
	MKDrivingRouteResult mkDrivingRouteResult;
	//private boolean listmux;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        Intent intent = getIntent();
        phonenum = intent.getStringExtra("phonenum");
        System.out.println("====================>"+phonenum+"<====================");
        mapView = (MapView)this.findViewById(R.id.bmapView);
        bMapManager = new BMapManager(Passenger.this);
        //���ذٶ�key
        bMapManager.init(keystring, new MKGeneralListener(){

			public void onGetNetworkState(int arg0) {
				// TODO Auto-generated method stub
				if (arg0==300){
					Toast.makeText(Passenger.this, "key����", 1).show();
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
        
        
        //phonenum = "15242044192";//�Լ��ĵ绰���룬ͨ����ͼ�л����
        phonenum_c = "";//Ҫ�򳵵�˾���绰����ĳ�ʼ��
        select = 3;//����
        iscall = false;
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
		//listmux = false;
		list_pf = null;        
		listtux_pf = 1;
		
        //sentMessage = new SentMessage();
        //sentMessage.start();
        
        //psngFindPsng = new PsngFindPsng();
        //psngFindPsng.start();
        
        freshmap = new Freshmap();
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
				
				String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=position_p&phonenum="+phonenum+"&latitude=" + baidulat + "&longitude=" + baidulng;
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
					list = JsonTools.getListMaps("listmap-c", jsonstring);
					if(!list.isEmpty()){
						iscall = false;
					}else{
						list = JsonTools.getListMaps("driver_position", jsonstring);
						if(isFirst&&!list.isEmpty()){
							Map<String,Object> maps = list.get(0);
						    int lat = (Integer)maps.get("latitude");
						    int lng = (Integer)maps.get("longitude");
						    start.pt = new GeoPoint(lat,lng);
						    end.pt = new GeoPoint(baidulat,baidulng);
						    mkSearch.drivingSearch(null, start, null, end);
						    isFirst = false;
						}
					}
				}
				//Log.i("Passenger", result);
				listtux = 1;
				
				try {
					sleep(5000);
			    } catch (InterruptedException e) {}    
			}
		}
	}
    
    
    //ƴ���߳�
    private class PsngFindPsng extends Thread{
    	
		public void run()
		{   
			while(true){
				
				String path = "http://192.168.1.103:8080/CarService/servlet/Servicer?action_flag=position_pf&phonenum="+phonenum;
				String jsonstring = HttpUtils.getJsonContent(path);
				
				while(listtux_pf == 0){ 
					try {
						sleep(800);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				listtux_pf = 0;
				list_pf = JsonTools.getListMaps("listmap_pf", jsonstring);
				//Log.i("Passenger", result);
				listtux_pf = 1;
				
				try {
					sleep(5000);
			    } catch (InterruptedException e) {}    
			}
		}
	}
    
    
    //ˢ��ͼ���̣߳�ÿ2��ˢ��һ��
    private class Freshmap extends Thread{
    	
		public void run()
		{   
			GeoPoint gp1 = new GeoPoint(baidulat,baidulng);
			Log.i("Passenger",baidulat+"  "+baidulng);
	        mapController.setCenter(gp1);//�������ĵ�
	        mapController.setZoom(12);//�������ż���Ϊ12
	        
			while(true){
				overlayList.clear();
		        //������ͼƬ
		        Drawable drawableself = getResources().getDrawable(R.drawable.self);
		        overlayList.add(new SelfOverLayItem(drawableself));
		        
		   /*     if ( list !=null && !list.isEmpty()) {
					Drawable drawableothers = getResources().getDrawable(R.drawable.driver);
					overlayList.add(new OthersOverLayItem(drawableothers));
				}
		        
		        if ( list_pf != null && !list_pf.isEmpty()) {
		        	Drawable drawableothers = getResources().getDrawable(R.drawable.passenger);
					overlayList.add(new PFOverLayItem(drawableothers));
		        }
		        
		        if(mkDrivingRouteResult != null){
		            RouteOverlay routeOverlay = new RouteOverlay(Passenger.this, mapView);
		            routeOverlay.setData(mkDrivingRouteResult.getPlan(0).getRoute(0));
		            overlayList.add(routeOverlay);
		        }
		    */    
				//ˢ��MapView
				mapView.postInvalidate();
				try {
					sleep(2000);
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
			//projection.fromPixels(point.x, point.y);
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
			Toast.makeText(Passenger.this, listitems.get(i).getTitle(), 3).show();
    		return true;
    	}
    	
    }
    
    
    //������ͼ��---˾��
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
					//while(it.hasNext()) {
			        //    String map_key = (String) it.next();
			        //    maps.get(map_key);
			        //}
					//maps.get("id");
					String phonenum = (String)maps.get("phonenum");
					String name = (String)maps.get("name");
					int lat = (Integer)maps.get("latitude");
					int lng = (Integer)maps.get("longitude");
					String carnum = (String)maps.get("carnum");
					String destination = (String)maps.get("destination");
					String sit = (String)maps.get("sit");
					double score = Double.parseDouble(maps.get("appraisescore").toString());
					
					GeoPoint geoPoint = new GeoPoint(lat,lng);
					//Projection projection = mapView.getProjection(); //mapviewΪMapView����  
					//Point point = new Point();
					//projection.toPixels(geoPoint, point);
					//point.x = point.x-15;
					//point.y = point.y-15;
					//geoPoint = projection.fromPixels(point.x, point.y);
					listitems.add(new OverlayItem(geoPoint,phonenum,name+"\n��  ��  �ȣ�   "+score+"��"+"\n��  ��  �ţ�   "+carnum+"\n��  ��  ����   "+sit+"\nĿ  ��  �أ�   "+destination));
					//maps.get(i);
				}
				listtux = 1;
				//GeoPoint geoPoint = new GeoPoint(latitude, longitude);
				//listitems.add(new OverlayItem(geoPoint,"GPS","��ʵGPS������"));
				populate();
			}
			
			//for test
			//GeoPoint geoPoint = new GeoPoint(latitude, longitude);
			//listitems.add(new OverlayItem(geoPoint,"GPS","��ʵGPS������"));
			
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
			//Toast.makeText(Passenger.this, listitems.get(i).getTitle(), 3).show();
			
			phonenum_c = listitems.get(i).getTitle();
			String information = listitems.get(i).getSnippet();
			Builder dialog = new AlertDialog.Builder(Passenger.this) ;   
			dialog.setTitle("������Ϣ") ; 
	        dialog.setMessage("�绰���룺   "+phonenum_c+"\n��       �ƣ�   "+information);
			if(!iscall){
				dialog.setPositiveButton("��", 
	        		             new DialogInterface.OnClickListener() {
									 public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
	                                    int flag = ApplyUtils.ApplyCall(phonenum, phonenum_c);
	                                    iscall = true;
									} 
                                 });
            }
			dialog.setNeutralButton("����",
			         new DialogInterface.OnClickListener() {
		                 public void onClick(DialogInterface arg0, int arg1) {
		              	    // TODO Auto-generated method stub
		                	//������� parse������������
		                     Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenum_c));
		                     //֪ͨactivtity�������call����
		                     Passenger.this.startActivity(intent);
		                 } 
                    });
			dialog.setNegativeButton("ȡ��", null);
	        dialog.show();
    		return true;
    	}
    	
    }
    
    //������ͼ��---ƴ��
    public class PFOverLayItem extends ItemizedOverlay<OverlayItem>{

    	private List<OverlayItem> listitems = new ArrayList<OverlayItem>();
    	String phonenum_py;
    	
		public PFOverLayItem(Drawable arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
			
			if ( listtux_pf == 1){
				//listmux = false;
				listtux_pf = 0;
				for(int i = 0; i<list_pf.size();i++){
					Map<String,Object> maps = list_pf.get(i);
					Set<String> key = maps.keySet();
					Iterator<String> it = key.iterator();
					
					String phonenum = (String)maps.get("phonenum");
					String name = (String)maps.get("name");
					int lat = (Integer)maps.get("latitude");
					int lng = (Integer)maps.get("longitude");
					String destination = (String)maps.get("destination");
					double score = Double.parseDouble(maps.get("appraisescore").toString());
					
					GeoPoint geoPoint = new GeoPoint(lat,lng);
					
					listitems.add(new OverlayItem(geoPoint,phonenum,name+"\n��  ��  �ȣ�   "+score+"��"+"\nĿ  ��  �أ�   " + destination));
					
				}
				listtux_pf = 1;
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
			//Toast.makeText(Passenger.this, listitems.get(i).getTitle(), 3).show();
			
			phonenum_py = listitems.get(i).getTitle();
			String information = listitems.get(i).getSnippet();
			Builder dialog = new AlertDialog.Builder(Passenger.this) ;   
			dialog.setTitle("������Ϣ") ; 
	        dialog.setMessage("�绰���룺   "+phonenum_py+"\n��       �ƣ�   "+information);
			
			dialog.setPositiveButton("����",
			         new DialogInterface.OnClickListener() {
		                 public void onClick(DialogInterface arg0, int arg1) {
		              	    // TODO Auto-generated method stub
		                	//������� parse������������
		                     Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenum_py));
		                     //֪ͨactivtity�������call����
		                     Passenger.this.startActivity(intent);
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
    		//Toast.makeText(Passenger.this, "γ�ȣ�"+latitude+" ���ȣ�"+longitude, 1).show();
    	}
    	else{
    		//Toast.makeText(Passenger.this, resultString, 1).show();
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
	private final int MENU_CHANGE = Menu.FIRST + 3;
	private final int MENU_SELFINFO = Menu.FIRST + 4;
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
		MenuItem applyItem = menu.add(0, MENU_APPLY, 0,"��");
		// ����
		MenuItem settingItem = menu.add(0, MENU_SETTING, 0,"����");
		MenuItem changeItem = menu.add(0, MENU_CHANGE, 0,"˾���б�");
		MenuItem selfinfoItem = menu.add(0, MENU_SELFINFO, 0,"������Ϣ");
		
	}

	/**
	 * �˵�����
	 * 
	 * @param item
	 */
	private void menuItemSelected(MenuItem item) {
		final EditText psng_destination_edit;
		switch (item.getItemId()) {
		// ��λ�Լ�
		case MENU_SELFLOCATION:
			GeoPoint gp1 = new GeoPoint(baidulat,baidulng);
	        mapController.setCenter(gp1);//�������ĵ�  
			break;
		// �����
		case MENU_APPLY:
			//TODO:�����������trade���е�����
			Builder dialog = new AlertDialog.Builder(Passenger.this);   
			dialog.setTitle("��ʾ") ; 
	        dialog.setMessage("ȷ�����´򳵣�");
			dialog.setPositiveButton("ȷ��", 
		        		             new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											int flag = ApplyUtils.ApplyCancelCall(phonenum);
											if(flag == 0) iscall = false; //����򳵱�־λ
											isFirst = true;
											if(mkDrivingRouteResult!=null){
												mkDrivingRouteResult = null;
												Builder dialog2 = new AlertDialog.Builder(Passenger.this);
												dialog2.setTitle("����") ; 
										        dialog2.setMessage("�Ƿ�������ۣ�");
										        dialog2.setPositiveButton("����", 
							        		             new DialogInterface.OnClickListener() {
															public void onClick(DialogInterface arg0, int arg1) {
																// TODO Auto-generated method stub
																
																new AlertDialog.Builder(Passenger.this)  
																	.setTitle("��ѡ��")  
																	.setIcon(android.R.drawable.ic_dialog_info)                  
																	.setSingleChoiceItems(new String[] {"1��","2��","3��","4��","5��"}, 2,   
																	    new DialogInterface.OnClickListener() {  
																	                              
																	        public void onClick(DialogInterface dialog, int which) {  
																	              
																	        	//Toast.makeText(Passenger.this, "which = "+which, 2).show();
																	        	select = which + 1;
																	        	
																	        	//dialog.dismiss();
																            }  
																	    }  
																	)  
																	.setPositiveButton("ȷ��", 
        		                                                           new DialogInterface.OnClickListener() {
								                                               public void onClick(DialogInterface arg0, int arg1) {
								                                            	// TODO Auto-generated method stub
								                                            	   int result = ApplyUtils.ApplyAppraise(phonenum_c, select);
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
	                                 });
			dialog.setNegativeButton("ȡ��", null);
	        dialog.show();
			break;
		// �����ò˵�
		case MENU_SETTING:
			//LayoutInflater inflater = getLayoutInflater();
			//View layout = inflater.inflate(R.layout.setting_dialog_pasenger,
			//     (ViewGroup) findViewById(R.id.passenger_setting_dialog));
		//	pasenger_destination=(EditText)this.findViewById(R.id.passenger_destination);
			final View contentView =(View)LayoutInflater.from(getApplicationContext()).inflate(R.layout.setting_dialog_pasenger,null);     //
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("����").setView(contentView).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface arg0, int arg1)
				 {
					// TODO Auto-generated method stub
					final EditText pasenger_destination;
					pasenger_destination=(EditText)contentView.findViewById(R.id.passenger_destination);
					String destination="" ;
					destination=pasenger_destination.getText().toString();
					if(destination==null||destination.trim().equals(""))
					{
						Toast.makeText(Passenger.this, "������Ŀ�ĵأ�", 1).show(); 
					}else
					{
						Toast.makeText(Passenger.this,destination, 3).show();   //  �˴����ַ����������ݿ�
						int flag = ApplyUtils.ApplyPsngSetting(phonenum, destination);
						//int flag=0;��־λ��Ϊ0ʱ��ʾ�޸ĳɹ�
						//�����޸�ʧ��
						System.out.println("========================>flag+"+flag);
						if(flag == 0){
							Toast.makeText(Passenger.this,"�޸ĳɹ�", 3).show();
						}else{
							Toast.makeText(Passenger.this,"�޸�ʧ��", 3).show();
						}
					}
				}})
			.setNegativeButton("ȡ��",null)
			.show();

			break;
		case MENU_CHANGE:
			Intent intent=new Intent(Passenger.this, DriverList.class);
			intent.putExtra("phonenum", phonenum);
			intent.putExtra("latitude",latitude);
			intent.putExtra("longitude",longitude);
			startActivity(intent);
			Passenger.this.finish();
			break;
			
		case MENU_SELFINFO:
			Intent intent_self=new Intent(Passenger.this, Setting.class);
			intent_self.putExtra("phonenum", phonenum);
			startActivity(intent_self); 
			Passenger.this.finish();
			break;
		}
	}
	
	
	public void onBackPressed() {  
        //Intent intent=new Intent(Regist.this, Login.class);
		//startActivity(intent); 
    	Builder dialog = new AlertDialog.Builder(Passenger.this);   
		dialog.setTitle("��ʾ") ; 
        dialog.setMessage("��ȷ���˳���");
        dialog.setPositiveButton("ȷ��", 
	            new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
                        Passenger.this.finish(); 
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