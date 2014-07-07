package data;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class CanteenInfo {

	public CanteenInfo(int iconResource,String mName, String phone, double mLatitude,
			double mLongitude) {
		super();
		this.iconResource = iconResource;
		this.name = mName;
		this.phone = phone;
		this.latitude = mLatitude;
		this.longitude = mLongitude;
		this.cantteenLoca = new GeoPoint((int)(mLatitude*1E6),(int)(mLongitude*1E6));
	}
	public CanteenInfo() {
	}
	public int iconResource;
	public String name;
	public String phone;
	public double latitude;				//¾«¶È
	public double longitude;				//Î³¶È
	public GeoPoint cantteenLoca;
	
	
	
	
	
	public int getIconResource() {
		return iconResource;
	}
	public void setIconResource(int iconResource) {
		this.iconResource = iconResource;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public GeoPoint getCantteenLoca() {
		return cantteenLoca;
	}
	public void setCantteenLoca(GeoPoint cantteenLoca) {
		this.cantteenLoca = cantteenLoca;
	}
	
}
