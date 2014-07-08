package ui.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ui.activity.menuactivity.MenuActivity;

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

import data.CanteenInfo;
import data.CanteenOverlayManager;
import data.Location;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.internal.widget.ActivityChooserModel.HistoricalRecord;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
	// �ٶȵ�ͼ
	public static final String strKey = "63418012748CD126610D926A0546374D0BFC86D5";
	private ItemizedOverlay mItemOverlay;
	private OverlayItem myLocationItem;
	private BMapManager mBMapManager = null;
	private CanteenOverlayManager mCanteenOverlayManager;
	private List<CanteenInfo> mCanteenList = new ArrayList<CanteenInfo>();
	private LocationClient mLocClient;

	// ����ؼ�
	private MapView mMapView = null;
	private ImageButton mTriggerButton;
	private ProgressDialog mLocationProD;
	private SlidingMenu mSlidingMenu;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initEngineManager(this);
		setContentView(R.layout.activity_main);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mLocClient = ((Location) getApplication()).mLocationClient;
		((Location) getApplication()).myLocationItem = myLocationItem;
		((Location) getApplication()).mMapView = mMapView;
		findViewAndInit();
		mCanteenOverlayManager = new CanteenOverlayManager(mContext, mMapView);
		new Thread() {
			public void run() {
				StringBuilder sb = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = client.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
				HttpConnectionParams.setSoTimeout(httpParams, 5000);
				HttpResponse response = null;
				try {
					response = client.execute(new HttpGet(
							LoginActivity.HTTPHOST + "m=json&a=canteenlist"));
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (response == null) {
					inti_canteen_info_handler.sendEmptyMessage(0);
					return;
				}
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					BufferedReader reader = null;
					try {
						reader = new BufferedReader(new InputStreamReader(
								entity.getContent(), "UTF-8"), 8192);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					String line = null;
					try {
						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				String result = sb.toString();
				JSONArray jsonArr = null;
				try {
					jsonArr = new JSONArray(sb.toString());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				JSONObject obj = null;
				try {
					for (int i = 0; i < jsonArr.length(); i++) {
						obj = jsonArr.getJSONObject(i);
						CanteenInfo can = new CanteenInfo();
						can.setName((String) obj.get("name"));
						can.setPhone((String) obj.get("phone"));
						can.setLatitude((double) obj.get("latitude"));
						can.setLongitude((double) obj.get("longitude"));
						can.setCantteenLoca(new GeoPoint((int) ((double) obj
								.get("latitude") * 1E6), (int) ((double) obj
								.get("longitude") * 1E6)));
						can.setIconResource(R.drawable.icon_canteen1);
						mCanteenList.add(can);
					}

				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				inti_canteen_info_handler.sendEmptyMessage(0);
			}
		}.start();
	}

	@Override
	public void onBackPressed() {
		if (mSlidingMenu != null && mSlidingMenu.isMenuShowing()) {
			mSlidingMenu.showContent();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			mSlidingMenu.toggle();
			return true;
		}
		return super.onKeyDown(keyCode, event);
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

	private Handler inti_canteen_info_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			initMapView();
			mCanteenOverlayManager = new CanteenOverlayManager(mContext,
					mMapView);
			if (mCanteenList.size() > 0)
				mCanteenOverlayManager.addCanteenList(mCanteenList);
			else
				Toast.makeText(mContext, "�������������⣬���Ժ�", Toast.LENGTH_LONG)
						.show();
		}
	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mLocationProD != null)
				mLocationProD.dismiss();
		}
	};

	// ��ʼ����ͼ
	private void initMapView() {
		mMapView.setBuiltInZoomControls(true);
		MapController mMapController = mMapView.getController();
		GeoPoint point = new GeoPoint((int) (23.06292 * 1E6),
				(int) (113.398428 * 1E6));
		setLocationOption();
		mLocClient.start();
		mMapController.setZoom(17);
	}

	private void findViewAndInit() {
		mTriggerButton = (ImageButton) findViewById(R.id.ibtn_right_menu);
		init();
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

	private void init() {
		mTriggerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSlidingMenu.toggle();
			}
		});
		mSlidingMenu = new SlidingMenu(this);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE); // ������ʽ
		mSlidingMenu.setShadowDrawable(R.drawable.shadow_right); // ��Ӱ
		mSlidingMenu.setShadowWidth(30); // ��Ӱ���
		mSlidingMenu.setBehindOffset(80); // ǰ�����ͼʣ�¶���
		mSlidingMenu.setMode(SlidingMenu.RIGHT); // �󻬳������һ���
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		mSlidingMenu.setMenu(R.layout.menu_frame); // ����menu����
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.menu_frame, new MenuFragment())
				.commit();
	}

	public void location(View v) {
		// ����һ�����ؽ�����
		mLocationProD = ProgressDialog.show(this, "��λ", "���ڶ�λ�����Եȡ�");
		new Thread() {
			public void run() {
				try {
					sleep(500);
					setLocationOption();
					mLocClient.start();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");
		option.setCoorType("bd09ll");
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setTimeOut(5000);
		mLocClient.setLocOption(option);
	}

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
				Toast.makeText(mContext,
						"���� DemoApplication.java�ļ�������ȷ����ȨKey��",
						Toast.LENGTH_LONG).show();
			}
		}
	}

}
