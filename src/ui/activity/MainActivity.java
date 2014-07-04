package ui.activity;

import java.util.ArrayList;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import com.example.canteenorder.R;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import data.Location;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	private Context mContext;
	ItemizedOverlay itemOverlay;
	public BMapManager mBMapManager = null;
	public MapView mMapView = null;
	public static final String strKey = "63418012748CD126610D926A0546374D0BFC86D5";
	OverlayItem myLocationItem;
	private ImageButton ibtn_trigger;
	//等待对话框
	ProgressDialog pd_location;
	private SlidingMenu slidingMenu;

	
	private LocationClient mLocClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initEngineManager(this);
		setContentView(R.layout.activity_main);

		mContext = this;
		mMapView = (MapView) findViewById(R.id.bmapView);
		mLocClient = ((Location)getApplication()).mLocationClient;
		((Location)getApplication()).myLocationItem = myLocationItem;
		((Location)getApplication()).mMapView = mMapView;
		findView();
		init();
		
		new Thread() {
			public void run() {					
				inti_driver_info_handler.sendEmptyMessage(0);
			}
		}.start();	
	}
	private Handler inti_driver_info_handler =new Handler(){
		   @Override
		   //当有消息发送出来的时候就执行Handler的这个方法
		   public void handleMessage(Message msg){
		      super.handleMessage(msg);
		      //只要执行到这里就关闭对话框
				initMapView();
		   }
	};
	
	private Handler handler =new Handler(){
		   @Override
		   //当有消息发送出来的时候就执行Handler的这个方法
		   public void handleMessage(Message msg){
		      super.handleMessage(msg);
		      //只要执行到这里就关闭对话框
		      if( pd_location != null )
		    	  pd_location.dismiss();
		   }
	};
	// 初始化地图
	private void initMapView() {
		mMapView.setBuiltInZoomControls(true);
		MapController mMapController = mMapView.getController();
		GeoPoint point = new GeoPoint((int) (23.06292 * 1E6), (int) (113.398428 * 1E6));
		
		Drawable mark = getResources().getDrawable(R.drawable.mylocation);
		
		//GeoPoint point2 = CoordinateConvert.fromWgs84ToBaidu(point);
		// 用OverlayItem准备Overlay数据
		myLocationItem = new OverlayItem(point, "item1", "item1");
		// 使用setMarker()方法设置overlay图片,如果不设置则使用构建ItemizedOverlay时的默认设置
		myLocationItem.setMarker(mark);
		// 创建IteminizedOverlay
		itemOverlay = new ItemizedOverlay<OverlayItem>(mark, mMapView);
		// 将IteminizedOverlay添加到MapView中
		//mMapView.getOverlays().clear();
		
		mMapView.getOverlays().add(itemOverlay);
		// 现在所有准备工作已准备好，使用以下方法管理overlay.
		// 添加overlay, 当批量添加Overlay时使用addItem(List<OverlayItem>)效率更高
		itemOverlay.addItem(myLocationItem);
		mMapController.setCenter(point);
		mMapController.setZoom(16);
	}

	
	private void findView() {
		ibtn_trigger = (ImageButton) findViewById(R.id.ibtn_right_menu);
	}

	
	private void init() {
		ibtn_trigger.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slidingMenu.toggle();
			}
		});

		slidingMenu = new SlidingMenu(this);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE); // 滑动方式
		slidingMenu.setShadowDrawable(R.drawable.shadow_right); // 阴影
		slidingMenu.setShadowWidth(30); // 阴影宽度
		slidingMenu.setBehindOffset(80); // 前面的视图剩下多少
		slidingMenu.setMode(SlidingMenu.RIGHT); // 左滑出不是右滑出
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setMenu(R.layout.menu_frame); // 设置menu容器

		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.menu_frame, new MenuFragment())
				.commit();
	}

	
	public void location( View v )
	{
		locationProcessThread();
	}
	
	private void locationProcessThread(){
	      //构建一个下载进度条
		pd_location= ProgressDialog.show(this, "定位", "正在定位，请稍等…");
	      new Thread(){
	         public void run(){
	            try {
					sleep(500);
					location();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            handler.sendEmptyMessage(0);
	         }
	      }.start();
	}

	private void setLocationOption(){
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);				//打开gps
		//option.setCoorType(mCoorEdit.getText().toString());		//设置坐标类型
		option.setAddrType("all");		//设置地址信息，仅设置为“all”时有地址信息，默认无地址信息mAddrEdit.getText().toString()
		//打开gps
		option.setCoorType("bd09ll");		//设置坐标类型
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setTimeOut(5000);
//		option.setPoiExtraInfo(true);
		mLocClient.setLocOption(option);
	}
	void location()
	{
		setLocationOption();
		mLocClient.start();
		//myLocationItem.notify();
	}
	
	// 按下返回键时
	@Override
	public void onBackPressed() {
		if (slidingMenu != null && slidingMenu.isMenuShowing()) {
			slidingMenu.showContent();
		} else {
			super.onBackPressed();
		}
	}

	// 按下菜单键时
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			slidingMenu.toggle();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(strKey, new MyGeneralListener())) {
			Toast.makeText(context, "BMapManager  初始化错误!", Toast.LENGTH_LONG)
					.show();
		}
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(mContext, "您的网络出错啦！", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(mContext, "输入正确的检索条件！", Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(mContext,
						"请在 DemoApplication.java文件输入正确的授权Key！",
						Toast.LENGTH_LONG).show();
			}
		}
	}
	@Override
	// 建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onDestroy() {
		// TODO Auto-generated method stub
		mMapView.destroy();
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		if (mBMapManager != null) {
			mBMapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		if (mBMapManager != null) {
			mBMapManager.start();
		}
		super.onResume();
	}
}
