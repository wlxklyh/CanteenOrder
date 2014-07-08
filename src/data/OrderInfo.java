package data;

import org.json.JSONObject;

public class OrderInfo extends JSONObject{

	private String canteenPhone;
	private int foodId;
	private int orderNum;
	public String getCanteenPhone() {
		return canteenPhone;
	}
	public void setCanteenPhone(String canteenPhone) {
		this.canteenPhone = canteenPhone;
	}
	public int getFoodId() {
		return foodId;
	}
	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

}
