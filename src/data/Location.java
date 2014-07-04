package data;
import com.baidu.location.*;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.utils.CoordinateConvert;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.canteenorder.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Process;
import android.os.Vibrator;

public class Location extends Application {
	public LocationClient mLocationClient = null;
	
	public LocationClient mLocationClient_sendorder = null;
	
	public LocationClient mLocationClient_quickorder = null;
	private String mData;  
	public MyLocationListenner myListener = new MyLocationListenner();
	public EditText address_edit;
	public NotifyLister mNotifyer=null;
	public Vibrator mVibrator01;
	public String myLocation = "";
	EditText quick_order_location;
	ItemizedOverlay itemOverlay;
//	double latitude;
//	double lontitude;
	public MapView mMapView;
	public OverlayItem myLocationItem;
	public SharedPreferences myLocationSp;
	public TextView curr_city;
	
	Dialog d;
	public BDLocationListener myListener_quickorder = new BDLocationListener()
	{
		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location == null)
				return ;
			StringBuffer sb = new StringBuffer(256);
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append(location.getAddrStr());	
			}
			setTextQuickOrder(sb.toString());
		}
		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	public BDLocationListener myListener_sendorder = new BDLocationListener()
	{

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location == null)
				return ;
			StringBuffer sb = new StringBuffer(256);
			if (location.getLocType() == BDLocation.TypeGpsLocation){
					sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				if( location.getCity() == null || location.getDistrict() == null || location.getStreet() == null  )
				{
					sb.append("");
				}
				else
				{
					sb.append(location.getCity());
					sb.append(location.getDistrict());
					sb.append(location.getStreet());
					sb.append(location.getStreetNumber());
				}
			}
			
			if( !(location.getCity().equals("广州市") || location.getCity().equals("惠州市") || location.getCity().equals("深圳市") || location.getCity().equals("珠海市")))
			{
				Toast.makeText(address_edit.getContext(), "您所在的城市暂未开通代驾服务，敬请期待", Toast.LENGTH_SHORT).show();
			}
			myLocation = sb.toString();
			setTextOrder(sb.toString());
			mLocationClient.stop();
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	@Override
	public void onCreate() {
		myLocationSp = getSharedPreferences("SP", MODE_PRIVATE);
		mLocationClient = new LocationClient( getApplicationContext() );
		mLocationClient.registerLocationListener( myListener );
		
		mLocationClient_sendorder = new LocationClient( getApplicationContext() );
		mLocationClient_sendorder.registerLocationListener( myListener_sendorder );
		
		mLocationClient_quickorder = new LocationClient( getApplicationContext() );
		mLocationClient_quickorder.registerLocationListener( myListener_quickorder );

		super.onCreate(); 
		Log.d("locSDK_Demo1", "... Application onCreate... pid=" + Process.myPid());
	}
	
	/**
	 * ��ʾ�ַ�
	 * @param str
	 */
	public void setTextQuickOrder(String str) {
		try {
			if ( quick_order_location != null )
			{	

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setTextOrder(String str) {
		try {
			if ( address_edit != null )
			{	
				address_edit.setText(str);
				mLocationClient_sendorder.stop();}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void mapLocation(double latitude,double lontitude) {
		try {
			if ( mMapView != null )
			{
				mMapView.setBuiltInZoomControls(true);
				MapController mMapController = mMapView.getController();		
				GeoPoint point = new GeoPoint((int) (latitude* 1E6),
						(int) (lontitude* 1E6));				
				//GeoPoint point2 = CoordinateConvert.fromWgs84ToBaidu(point);				
				mMapController.animateTo(point);
				//mMapController.setCenter(point2);
				mMapController.setZoom(mMapView.getZoomLevel());
				mLocationClient.stop();
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			StringBuffer sb = new StringBuffer(256);
			mapLocation(location.getLatitude(),location.getLongitude());
			if( itemOverlay != null )
				mMapView.getOverlays().remove(itemOverlay);
			
			GeoPoint point = new GeoPoint((int) (location.getLatitude()* 1E6),
					(int) (location.getLongitude()* 1E6));
			Drawable mark = getResources().getDrawable(R.drawable.mylocation);
			myLocationItem = new OverlayItem(point, "item1", "item1");
			myLocationItem.setMarker(mark);
			itemOverlay = new ItemizedOverlay<OverlayItem>(mark, mMapView);
			mMapView.getOverlays().add(itemOverlay);
			itemOverlay.addItem(myLocationItem);
			mMapView.refresh();
			
			Editor editor = myLocationSp.edit();
			editor.putString("latitude",""+location.getLatitude());
			editor.putString("lontitude",""+location.getLongitude());
			editor.commit();
			mLocationClient.stop();
		}

		
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			} 
			if(poiLocation.hasPoi()){
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			}else{				
				sb.append("noPoi information");
			}
			mLocationClient.stop();
		}
	}
	/**
	 * λ�����ѻص�����
	 */
	public class NotifyLister extends BDNotifyListener{
		public void onNotify(BDLocation mlocation, float distance){
			mVibrator01.vibrate(1000);
		}
	}
}