package data;

public class FoodInfo {
	public FoodInfo(boolean hasNow, String name, String content, int starNum,
			int monthSale, double price,int iconResource) {
		this.iconResource=iconResource;
		this.hasNow = hasNow;
		this.name = name;
		this.content = content;
		this.starNum = starNum;
		this.monthSale = monthSale;
		this.price = price;
	}
	public int iconResource;
	public int oderNum=0;
	public boolean hasNow;
	public String name;
	public String content;	
	public int starNum;
	public int monthSale;
	public double price;
}
