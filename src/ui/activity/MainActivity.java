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
	//�ȴ��Ի���
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
		   //������Ϣ���ͳ�����ʱ���ִ��Handler���������
		   public void handleMessage(Message msg){
		      super.handleMessage(msg);
		      //ֻҪִ�е�����͹رնԻ���
				initMapView();
		   }
	};
	
	private Handler handler =new Handler(){
		   @Override
		   //������Ϣ���ͳ�����ʱ���ִ��Handler���������
		   public void handleMessage(Message msg){
		      super.handleMessage(msg);
		      //ֻҪִ�е�����͹رնԻ���
		      if( pd_location != null )
		    	  pd_location.dismiss();
		   }
	};
	// ��ʼ����ͼ
	private void initMapView() {
		mMapView.setBuiltInZoomControls(true);
		MapController mMapController = mMapView.getController();
		GeoPoint point = new GeoPoint((int) (23.06292 * 1E6), (int) (113.398428 * 1E6));
		
		Drawable mark = getResources().getDrawable(R.drawable.mylocation);
		
		//GeoPoint point2 = CoordinateConvert.fromWgs84ToBaidu(point);
		// ��OverlayItem׼��Overlay����
		myLocationItem = new OverlayItem(point, "item1", "item1");
		// ʹ��setMarker()��������overlayͼƬ,�����������ʹ�ù���ItemizedOverlayʱ��Ĭ������
		myLocationItem.setMarker(mark);
		// ����IteminizedOverlay
		itemOverlay = new ItemizedOverlay<OverlayItem>(mark, mMapView);
		// ��IteminizedOverlay��ӵ�MapView��
		//mMapView.getOverlays().clear();
		
		mMapView.getOverlays().add(itemOverlay);
		// ��������׼��������׼���ã�ʹ�����·�������overlay.
		// ���overlay, ���������Overlayʱʹ��addItem(List<OverlayItem>)Ч�ʸ���
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
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE); // ������ʽ
		slidingMenu.setShadowDrawable(R.drawable.shadow_right); // ��Ӱ
		slidingMenu.setShadowWidth(30); // ��Ӱ���
		slidingMenu.setBehindOffset(80); // ǰ�����ͼʣ�¶���
		slidingMenu.setMode(SlidingMenu.RIGHT); // �󻬳������һ���
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setMenu(R.layout.menu_frame); // ����menu����

		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.menu_frame, new MenuFragment())
				.commit();
	}

	
	public void location( View v )
	{
		locationProcessThread();
	}
	
	private void locationProcessThread(){
	      //����һ�����ؽ�����
		pd_location= ProgressDialog.show(this, "��λ", "���ڶ�λ�����Եȡ�");
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
		option.setOpenGps(true);				//��gps
		//option.setCoorType(mCoorEdit.getText().toString());		//������������
		option.setAddrType("all");		//���õ�ַ��Ϣ��������Ϊ��all��ʱ�е�ַ��Ϣ��Ĭ���޵�ַ��ϢmAddrEdit.getText().toString()
		//��gps
		option.setCoorType("bd09ll");		//������������
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
	
	// ���·��ؼ�ʱ
	@Override
	public void onBackPressed() {
		if (slidingMenu != null && slidingMenu.isMenuShowing()) {
			slidingMenu.showContent();
		} else {
			super.onBackPressed();
		}
	}

	// ���²˵���ʱ
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
			Toast.makeText(context, "BMapManager  ��ʼ������!", Toast.LENGTH_LONG)
					.show();
		}
	}

	// �����¼���������������ͨ�������������Ȩ��֤�����
	class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(mContext, "���������������", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(mContext, "������ȷ�ļ���������", Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// ��ȨKey����
				Toast.makeText(mContext,
						"���� DemoApplication.java�ļ�������ȷ����ȨKey��",
						Toast.LENGTH_LONG).show();
			}
		}
	}
	@Override
	// ��������app���˳�֮ǰ����mapadpi��destroy()�����������ظ���ʼ��������ʱ������
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
