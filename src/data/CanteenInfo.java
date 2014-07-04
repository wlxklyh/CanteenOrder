package data;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class CanteenInfo {

	public CanteenInfo(String mName, String mAddress, double mLatitude,
			double mLongitude) {
		super();
		this.name = mName;
		this.address = mAddress;
		this.latitude = mLatitude;
		this.longitude = mLongitude;
		this.cantteenLoca = new GeoPoint((int)(mLatitude*1E6),(int)(mLongitude*1E6));
	}
	public String name;
	public String address;
	public double latitude;				//����
	public double longitude;				//γ��
	public GeoPoint cantteenLoca;
}
