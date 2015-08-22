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
	private BMapManager bMapManager;//加载地图引擎
	private MKSearch mkSearch;//用于检索的类
	//private EditText pasenger_destination=null;
	private String keystring = "125C5C47491C0E734F6A9344CF7DDC8E0FD6DB09";
	//添加百度地图一些控件
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
	//定义起始点和终点
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
        //加载百度key
        bMapManager.init(keystring, new MKGeneralListener(){

			public void onGetNetworkState(int arg0) {
				// TODO Auto-generated method stub
				if (arg0==300){
					Toast.makeText(Passenger.this, "key有误！", 1).show();
				}
			}

			public void onGetPermissionState(int arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        this.initMapActivity(bMapManager);
        mapView.setBuiltInZoomControls(true);//可以设置缩放功能
        mapController = mapView.getController();
        
        //实例化地图查询类
        mkSearch = new MKSearch();
        mkSearch.init(bMapManager, new MySearchListener());
        start = new MKPlanNode();
        end = new MKPlanNode();
        mkSearch.setDrivingPolicy(MKSearch.EBUS_TIME_FIRST);//采用时间优先策略
        
        
        //phonenum = "15242044192";//自己的电话号码，通过意图切换获得
        phonenum_c = "";//要打车的司机电话号码的初始化
        select = 3;//评分
        iscall = false;
        isFirst = true;
        //GPS
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        //设置服务商
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);
        
        //更新坐标
        updateWithNewLocation(location);
        
        //覆盖物列表实例化
        overlayList = mapView.getOverlays();
        
        //注册一个周期性的更新，3000ms更新一次
        //locationListener用来监听定位信息的改变
        locationManager.requestLocationUpdates(provider, 3000, 0, gpslocationListener);
        
        GeoPoint gp1 = new GeoPoint(latitude,longitude);
		GeoPoint gp2 = CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(gp1));
		baidulat = gp2.getLatitudeE6();
		baidulng = gp2.getLongitudeE6();
		
		list = null;
		listtux = 1;//list访问控制
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
    
    
    //向服务器发送自己的坐标，接受发回来的他人坐标信息，并刷新图层的线程
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
    
    
    //拼友线程
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
    
    
    //刷新图层线程，每2秒刷新一次
    private class Freshmap extends Thread{
    	
		public void run()
		{   
			GeoPoint gp1 = new GeoPoint(baidulat,baidulng);
			Log.i("Passenger",baidulat+"  "+baidulng);
	        mapController.setCenter(gp1);//设置中心点
	        mapController.setZoom(12);//设置缩放级别为12
	        
			while(true){
				overlayList.clear();
		        //覆盖物图片
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
				//刷新MapView
				mapView.postInvalidate();
				try {
					sleep(2000);
			    } catch (InterruptedException e) {}
			    
		    }
			
		}
	}
    //覆盖物图层--自己
    public class SelfOverLayItem extends ItemizedOverlay<OverlayItem>{

    	private List<OverlayItem> listitems = new ArrayList<OverlayItem>();
    	
		public SelfOverLayItem(Drawable arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
			
			GeoPoint geoPoint = new GeoPoint(baidulat, baidulng);
			//Projection projection = mapView.getProjection(); //mapview为MapView对象  
			//Point point = new Point();
			//projection.toPixels(geoPoint, point);
			//point.x = point.x-10;
			//point.y = point.y-10;
			//geoPoint = projection.fromPixels(point.x, point.y);
			//projection.fromPixels(point.x, point.y);
			listitems.add(new OverlayItem(geoPoint,"自己","在这里"));
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
    
    
    //覆盖物图层---司机
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
					//Projection projection = mapView.getProjection(); //mapview为MapView对象  
					//Point point = new Point();
					//projection.toPixels(geoPoint, point);
					//point.x = point.x-15;
					//point.y = point.y-15;
					//geoPoint = projection.fromPixels(point.x, point.y);
					listitems.add(new OverlayItem(geoPoint,phonenum,name+"\n信  任  度：   "+score+"分"+"\n车  牌  号：   "+carnum+"\n乘  客  数：   "+sit+"\n目  的  地：   "+destination));
					//maps.get(i);
				}
				listtux = 1;
				//GeoPoint geoPoint = new GeoPoint(latitude, longitude);
				//listitems.add(new OverlayItem(geoPoint,"GPS","真实GPS在这里"));
				populate();
			}
			
			//for test
			//GeoPoint geoPoint = new GeoPoint(latitude, longitude);
			//listitems.add(new OverlayItem(geoPoint,"GPS","真实GPS在这里"));
			
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
			dialog.setTitle("基本信息") ; 
	        dialog.setMessage("电话号码：   "+phonenum_c+"\n昵       称：   "+information);
			if(!iscall){
				dialog.setPositiveButton("打车", 
	        		             new DialogInterface.OnClickListener() {
									 public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
	                                    int flag = ApplyUtils.ApplyCall(phonenum, phonenum_c);
	                                    iscall = true;
									} 
                                 });
            }
			dialog.setNeutralButton("呼叫",
			         new DialogInterface.OnClickListener() {
		                 public void onClick(DialogInterface arg0, int arg1) {
		              	    // TODO Auto-generated method stub
		                	//传入服务， parse（）解析号码
		                     Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenum_c));
		                     //通知activtity处理传入的call服务
		                     Passenger.this.startActivity(intent);
		                 } 
                    });
			dialog.setNegativeButton("取消", null);
	        dialog.show();
    		return true;
    	}
    	
    }
    
    //覆盖物图层---拼友
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
					
					listitems.add(new OverlayItem(geoPoint,phonenum,name+"\n信  任  度：   "+score+"分"+"\n目  的  地：   " + destination));
					
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
			dialog.setTitle("基本信息") ; 
	        dialog.setMessage("电话号码：   "+phonenum_py+"\n昵       称：   "+information);
			
			dialog.setPositiveButton("呼叫",
			         new DialogInterface.OnClickListener() {
		                 public void onClick(DialogInterface arg0, int arg1) {
		              	    // TODO Auto-generated method stub
		                	//传入服务， parse（）解析号码
		                     Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenum_py));
		                     //通知activtity处理传入的call服务
		                     Passenger.this.startActivity(intent);
		                 } 
                    });
			dialog.setNegativeButton("取消", null);
	        dialog.show();
    		return true;
    	}
    	
    }
    
    //根据GPS获取经纬度
    protected void updateWithNewLocation(Location location){
        //String resultString = "定位失败！";
    	if (location!=null){
    		double geolat = location.getLatitude()*1E6;
    		latitude = (int)geolat;
    		double geolng = location.getLongitude()*1E6;
    		longitude = (int)geolng;
    		GeoPoint gp1 = new GeoPoint(latitude,longitude);
			GeoPoint gp2 = CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(gp1));
			baidulat = gp2.getLatitudeE6();
			baidulng = gp2.getLongitudeE6();
    		//Toast.makeText(Passenger.this, "纬度："+latitude+" 经度："+longitude, 1).show();
    	}
    	else{
    		//Toast.makeText(Passenger.this, resultString, 1).show();
    	}
    }
    //loactionListener监听器类 ，实时更新坐标
    private final LocationListener gpslocationListener = new LocationListener(){

    	//坐标更改时触发此函数
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			updateWithNewLocation(location);
		}

		//Provider禁用时触发此函数，比如GPS被关闭
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			updateWithNewLocation(null);
		}

		//Provider启动时触发此函数，比如GPS被打开
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		//Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
    	
    };
    
    
	
        
	//MENU键事件处理    
       
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
	 * 创建菜单
	 * 
	 * @param menu
	 */
	private void createMenu(Menu menu) {
		menu.clear();
		// 自己定位
		MenuItem selfItem = menu.add(0, MENU_SELFLOCATION, 0, "自己定位");
		//selfItem.setVisible(false);//不可见
		//settingItem.setEnabled(bluetoothM.isSearch());//不可用，灰色
		// 申请打车或载客
		MenuItem applyItem = menu.add(0, MENU_APPLY, 0,"打车");
		// 设置
		MenuItem settingItem = menu.add(0, MENU_SETTING, 0,"设置");
		MenuItem changeItem = menu.add(0, MENU_CHANGE, 0,"司机列表");
		MenuItem selfinfoItem = menu.add(0, MENU_SELFINFO, 0,"个人信息");
		
	}

	/**
	 * 菜单项被点击
	 * 
	 * @param item
	 */
	private void menuItemSelected(MenuItem item) {
		final EditText psng_destination_edit;
		switch (item.getItemId()) {
		// 定位自己
		case MENU_SELFLOCATION:
			GeoPoint gp1 = new GeoPoint(baidulat,baidulng);
	        mapController.setCenter(gp1);//设置中心点  
			break;
		// 申请打车
		case MENU_APPLY:
			//TODO:清除服务器端trade表中的数据
			Builder dialog = new AlertDialog.Builder(Passenger.this);   
			dialog.setTitle("提示") ; 
	        dialog.setMessage("确定重新打车？");
			dialog.setPositiveButton("确定", 
		        		             new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											int flag = ApplyUtils.ApplyCancelCall(phonenum);
											if(flag == 0) iscall = false; //清除打车标志位
											isFirst = true;
											if(mkDrivingRouteResult!=null){
												mkDrivingRouteResult = null;
												Builder dialog2 = new AlertDialog.Builder(Passenger.this);
												dialog2.setTitle("评价") ; 
										        dialog2.setMessage("是否进行评价？");
										        dialog2.setPositiveButton("评价", 
							        		             new DialogInterface.OnClickListener() {
															public void onClick(DialogInterface arg0, int arg1) {
																// TODO Auto-generated method stub
																
																new AlertDialog.Builder(Passenger.this)  
																	.setTitle("请选择")  
																	.setIcon(android.R.drawable.ic_dialog_info)                  
																	.setSingleChoiceItems(new String[] {"1分","2分","3分","4分","5分"}, 2,   
																	    new DialogInterface.OnClickListener() {  
																	                              
																	        public void onClick(DialogInterface dialog, int which) {  
																	              
																	        	//Toast.makeText(Passenger.this, "which = "+which, 2).show();
																	        	select = which + 1;
																	        	
																	        	//dialog.dismiss();
																            }  
																	    }  
																	)  
																	.setPositiveButton("确定", 
        		                                                           new DialogInterface.OnClickListener() {
								                                               public void onClick(DialogInterface arg0, int arg1) {
								                                            	// TODO Auto-generated method stub
								                                            	   int result = ApplyUtils.ApplyAppraise(phonenum_c, select);
								                                               }
																	       })
																    .setNegativeButton("取消", null) 
																    .show(); 

															}
										                 });
										        dialog2.setNegativeButton("取消", null);
										        dialog2.show();
											}
										} 
	                                 });
			dialog.setNegativeButton("取消", null);
	        dialog.show();
			break;
		// 打开设置菜单
		case MENU_SETTING:
			//LayoutInflater inflater = getLayoutInflater();
			//View layout = inflater.inflate(R.layout.setting_dialog_pasenger,
			//     (ViewGroup) findViewById(R.id.passenger_setting_dialog));
		//	pasenger_destination=(EditText)this.findViewById(R.id.passenger_destination);
			final View contentView =(View)LayoutInflater.from(getApplicationContext()).inflate(R.layout.setting_dialog_pasenger,null);     //
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("设置").setView(contentView).setPositiveButton("确定", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface arg0, int arg1)
				 {
					// TODO Auto-generated method stub
					final EditText pasenger_destination;
					pasenger_destination=(EditText)contentView.findViewById(R.id.passenger_destination);
					String destination="" ;
					destination=pasenger_destination.getText().toString();
					if(destination==null||destination.trim().equals(""))
					{
						Toast.makeText(Passenger.this, "请输入目的地！", 1).show(); 
					}else
					{
						Toast.makeText(Passenger.this,destination, 3).show();   //  此处将字符串传到数据库
						int flag = ApplyUtils.ApplyPsngSetting(phonenum, destination);
						//int flag=0;标志位，为0时表示修改成功
						//否则修改失败
						System.out.println("========================>flag+"+flag);
						if(flag == 0){
							Toast.makeText(Passenger.this,"修改成功", 3).show();
						}else{
							Toast.makeText(Passenger.this,"修改失败", 3).show();
						}
					}
				}})
			.setNegativeButton("取消",null)
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
		dialog.setTitle("提示") ; 
        dialog.setMessage("您确定退出吗？");
        dialog.setPositiveButton("确定", 
	            new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
                        Passenger.this.finish(); 
					}
				});
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }  
	
	//程序生命周期控制
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