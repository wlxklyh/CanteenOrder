package data;

import java.util.ArrayList;
import java.util.List;

import ui.activity.CanteenActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.RatingBar;
import android.widget.TextView;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.canteenorder.R;



public class CanteenOverlayManager {
	public MapView mMapView = null;
	public Context context = null;
	public Drawable canteenOverlayDrawable = null;
	public DriverOverlay canteenOverlay = null;
	public GeoPoint canteenLoc = null;
	public List<OverlayItem> canteenOverlayItemList;
	public List<CanteenInfo> canteens;
	
	
	public CanteenOverlayManager(Context ct, MapView mv) {
		mMapView = mv;
		context = ct;
		canteenOverlayDrawable = context.getResources().getDrawable(
				R.drawable.ic_launcher);
		canteenOverlay = new DriverOverlay(canteenOverlayDrawable, mMapView);
		mMapView.getOverlays().add(canteenOverlay);
	}

	public void addDriverList(List<CanteenInfo> driverList)
	{
		canteenOverlayItemList = new ArrayList<OverlayItem>();
		canteens = driverList;
		for(int i=0;i<driverList.size();i++)
		{
			OverlayItem tempoverlayItem = new OverlayItem(driverList.get(i).cantteenLoca, "item1", "item1");
			
			canteenOverlayItemList.add(tempoverlayItem);
			
			View layoutView = LayoutInflater.from(context).inflate(R.layout.canteen_overlay,
					null);
			Bitmap bm = convertViewToBitMap(layoutView);
			
			bm=small(bm);
			BitmapDrawable bd = new BitmapDrawable(bm);
			tempoverlayItem.setMarker(bd);
		}
		canteenOverlay.addItem(canteenOverlayItemList);
		mMapView.refresh();
	}
	
	
	public void addDriver(GeoPoint p, int layoutSource, String text, int starNum,int state) {
		OverlayItem driverItem = new OverlayItem(p, "item1", "item1");
		View layoutView = LayoutInflater.from(context).inflate(layoutSource,
				null);

		Bitmap bm = convertViewToBitMap(layoutView);
		bm=small(bm);
		BitmapDrawable bd = new BitmapDrawable(bm);
		driverItem.setMarker(bd);
		
		canteenOverlay.addItem(driverItem);
		mMapView.refresh();
	}

	public void deleteDriver() {

	}

	public void deleteAllDriver() {
		canteenOverlay.removeAll();
	}

	private static Bitmap small(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.8f, 0.8f); // ���Ϳ�Ŵ���С�ı���
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	private Bitmap convertViewToBitMap(View v) {
		// ���û�ͼ����
		v.setDrawingCacheEnabled(true);
		// ����������������ǳ���Ҫ�����û�е�������������õ���bitmapΪnull
		v.measure(MeasureSpec.makeMeasureSpec(210, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(120, MeasureSpec.EXACTLY));
		// �������Ҳ�ǳ���Ҫ�����ò��ֵĳߴ��λ��
		v.layout(0, 0, v.getMeasuredWidth() + 20, v.getMeasuredHeight());
		// ��û�ͼ�����е�Bitmap
		v.buildDrawingCache();
		return v.getDrawingCache();
	}

	class DriverOverlay extends ItemizedOverlay<OverlayItem> {
		// ��MapView����ItemizedOverlay
		public DriverOverlay(Drawable mark, MapView mapView) {
			super(mark, mapView);
		}
		protected boolean onTap(int index) {
			// �ڴ˴���item����¼�
			System.out.println("item onTap: " + index);
			Intent intent = new Intent();
			intent.putExtra("name", canteens.get(index).name);
			intent.putExtra("address", canteens.get(index).address);
			
			intent.setClass(context,CanteenActivity.class);
			context.startActivity(intent);
			return true;
		}
		public boolean onTap(GeoPoint pt, MapView mapView) {
			// �ڴ˴���MapView�ĵ���¼��������� trueʱ
			super.onTap(pt, mapView);
			return false;
		}
	}
}


