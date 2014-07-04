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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
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
		canteens = new  ArrayList<CanteenInfo>();
	}

	public void addCanteenList(List<CanteenInfo> canteenList)
	{
		canteenOverlayItemList = new ArrayList<OverlayItem>();
		canteens = canteenList;
		for(int i=0;i<canteenList.size();i++)
		{
			OverlayItem tempoverlayItem = new OverlayItem(canteenList.get(i).cantteenLoca, "item1", "item1");
			canteenOverlayItemList.add(tempoverlayItem);
			View layoutView = LayoutInflater.from(context).inflate(R.layout.layout_canteen,
					null);
			TextView  nameTectView = (TextView)layoutView.findViewById(R.id.canteen_name_text);
			nameTectView.setText(canteenList.get(i).name);
			
			ImageView  iconImgeView = (ImageView)layoutView.findViewById(R.id.imageView_canteen_icon);
			iconImgeView.setBackgroundResource(canteenList.get(i).iconResource);
			
			Bitmap bm = convertViewToBitMap(layoutView);
			bm=small(bm);
			BitmapDrawable bd = new BitmapDrawable(bm);
			tempoverlayItem.setMarker(bd);
		}
		canteenOverlay.addItem(canteenOverlayItemList);
		mMapView.refresh();
	}
	
	
	public void addCanteen(GeoPoint p, int layoutSource, int imgeRecource,String text) {
		OverlayItem driverItem = new OverlayItem(p, "item1", "item1");
		View layoutView = LayoutInflater.from(context).inflate(layoutSource,
				null);
		TextView  nameTectView = (TextView)layoutView.findViewById(R.id.canteen_name_text);
		nameTectView.setText(text);
		
		ImageView  iconImgeView = (ImageView)layoutView.findViewById(R.id.imageView_canteen_icon);
		iconImgeView.setBackgroundResource(imgeRecource);
		
		canteens.add(new CanteenInfo(imgeRecource,text, "addresss", p.getLatitudeE6(), p.getLongitudeE6()));
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
		matrix.postScale(0.8f, 0.8f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	private Bitmap convertViewToBitMap(View v) {
		// 启用绘图缓存
		v.setDrawingCacheEnabled(true);
		// 调用下面这个方法非常重要，如果没有调用这个方法，得到的bitmap为null
		v.measure(MeasureSpec.makeMeasureSpec(210, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(120, MeasureSpec.EXACTLY));
		// 这个方法也非常重要，设置布局的尺寸和位置
		v.layout(0, 0, v.getMeasuredWidth() + 20, v.getMeasuredHeight());
		v.buildDrawingCache();
		return v.getDrawingCache();
	}

	class DriverOverlay extends ItemizedOverlay<OverlayItem> {
		// 用MapView构造ItemizedOverlay
		public DriverOverlay(Drawable mark, MapView mapView) {
			super(mark, mapView);
		}
		protected boolean onTap(int index) {
			// 在此处理item点击事件
			System.out.println("item onTap: " + index);
			Log.d("lyh","asdf");
			Intent intent = new Intent();
			intent.putExtra("name", canteens.get(index).name);
			intent.setClass(context,CanteenActivity.class);
			context.startActivity(intent);
			return true;
		}
		public boolean onTap(GeoPoint pt, MapView mapView) {
			// 在此处理MapView的点击事件，当返回 true时
			super.onTap(pt, mapView);
			return false;
		}
	}
}


