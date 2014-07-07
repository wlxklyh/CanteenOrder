package data;

public class FoodInfo {
	public FoodInfo(boolean hasNow, String name, String introduce, int starNum,
			int monthSale, double price,int iconResource) {
		this.iconResource=iconResource;
		this.hasNow = hasNow;
		this.name = name;
		this.introduce = introduce;
		this.starNum = starNum;
		this.monthSale = monthSale;
		this.price = price;
	}
	public FoodInfo() {

	}
	
	public int foodId;
	public String canteenPhone;
	public String name;
	public String introduce;	
	public int starNum;
	public int monthSale;
	public double price;
	
	public int oderNum=0;
	public int iconResource;
	public boolean hasNow;
}
