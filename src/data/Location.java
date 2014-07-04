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
	public MyLocationListenner myListener = new MyLocationListenner();

	private ItemizedOverlay itemOverlay;
	public MapView mMapView;
	public OverlayItem myLocationItem;
	public SharedPreferences myLocationSp;
	
	@Override
	public void onCreate() {
		myLocationSp = getSharedPreferences("SP", MODE_PRIVATE);
		mLocationClient = new LocationClient( getApplicationContext() );
		mLocationClient.registerLocationListener( myListener );
		super.onCreate(); 
		Log.d("locSDK_Demo1", "... Application onCreate... pid=" + Process.myPid());
	}
	
	public void mapLocation(double latitude,double lontitude) {
		try {
			if ( mMapView != null )
			{
				mMapView.setBuiltInZoomControls(true);
				MapController mMapController = mMapView.getController();		
				GeoPoint point = new GeoPoint((int) (latitude* 1E6),
						(int) (lontitude* 1E6));			
				mMapController.animateTo(point);
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
			mLocationClient.stop();
		}
	}
}