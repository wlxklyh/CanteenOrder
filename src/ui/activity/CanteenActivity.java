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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ui.adapter.CanteenListViewAdapter;
import ui.adapter.CanteenViewpagerAdapter;

import com.example.canteenorder.R;

import data.FoodInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class CanteenActivity extends Activity {
	private int mFoodIconR[]=new int[20];
	private int mFoodNum=0;
	private Context mContext;
	private ViewPager mAdsViewPager;
	private ListView mFoodListView;
	private CanteenListViewAdapter mCanteenListViewAdapter;
	private CanteenViewpagerAdapter mCanteenViewpagerAdapter;
	public static List<FoodInfo> foodInfoList = new ArrayList<FoodInfo>();
	private String mCanteenPhone;
	private String mTitleStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_canteen);
		foodInfoList.clear();
		
		mFoodIconR[0]=R.drawable.icon_food1;
		mFoodIconR[1]=R.drawable.icon_food2;
		mFoodIconR[2]=R.drawable.icon_food3;
		mFoodIconR[3]=R.drawable.icon_food4;
		mFoodIconR[4]=R.drawable.icon_food5;
		mFoodIconR[5]=R.drawable.icon_food6;
		mFoodIconR[6]=R.drawable.icon_food7;
		mFoodIconR[7]=R.drawable.icon_food8;
		mFoodIconR[8]=R.drawable.icon_food9;
		mFoodIconR[9]=R.drawable.icon_food10;
		mFoodIconR[10]=R.drawable.icon_food11;
		mFoodIconR[11]=R.drawable.icon_food12;
		
		// 进来初始化
		Intent intent = getIntent();
		mTitleStr = intent.getStringExtra("name");
		mCanteenPhone = intent.getStringExtra("phone");
		mFindViewByID();
		
		TextView titleTextView = (TextView) findViewById(R.id.button_title);
		titleTextView.setText(mTitleStr);
		titleTextView.setTextSize(25);
		
		new Thread() {
			public void run() {
				StringBuilder sb = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = client.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
				HttpConnectionParams.setSoTimeout(httpParams, 5000);
				HttpResponse response = null;
				try {
					response = client.execute(new HttpGet(LoginActivity.HTTPHOST + "m=json&a=foodlist&canteenPhone="+mCanteenPhone));
					Log.d("lyh", LoginActivity.HTTPHOST + "m=json&a=foodlist&canteenPhone="+mCanteenPhone);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (response == null) {
					inti_food_info_handler.sendEmptyMessage(0);
					return;
				}
				HttpEntity entity = response.getEntity();
				Log.d("lyh", "1");
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				String result = sb.toString();
				Log.d("lyh", result);
				JSONArray jsonArr = null;
				try {
					jsonArr = new JSONArray(sb.toString());
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JSONObject obj = null;
				try {
					for (int i = 0; i < jsonArr.length(); i++) {
						obj = jsonArr.getJSONObject(i);
						FoodInfo food = new FoodInfo();
						food.canteenPhone=(String) obj.get("canteenPhone");
						food.name=(String) obj.get("name");
						food.foodId=(int) obj.get("foodId");
						food.introduce=(String) obj.get("introduce");
						food.monthSale=(int) obj.get("monthSale");
						food.price=Double.valueOf(obj.get("price").toString());
						food.starNum=(int) obj.get("starNum");
						food.iconResource = mFoodIconR[(mFoodNum++)%12];
						foodInfoList.add(food);
					}
					
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				inti_food_info_handler.sendEmptyMessage(0);
			}
		}.start();
		
	}

	
	public Handler inti_food_info_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			initData();
			setData();
			
		}
	};
	private void mFindViewByID() {
		mFoodListView = (ListView) findViewById(R.id.listView_food);
		mAdsViewPager = (ViewPager) findViewById(R.id.ads_viewpager);
		mCanteenViewpagerAdapter = new CanteenViewpagerAdapter(mContext);
		mAdsViewPager.setAdapter(mCanteenViewpagerAdapter);
		mAdsViewPager.setCurrentItem(mCanteenViewpagerAdapter
				.getStartPosition());
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handlerViewPagerMove.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	
	
	private void initData() {
		mCanteenListViewAdapter = new CanteenListViewAdapter(mContext);
		mCanteenListViewAdapter.setList(foodInfoList);
	}

	private void setData() {
		mFoodListView.setAdapter(mCanteenListViewAdapter);
		mFoodListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
	}

	public Handler handlerViewPagerMove = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mAdsViewPager
					.setCurrentItem(++mCanteenViewpagerAdapter.nowPosition);
		}
	};

	public void onClickBack(View view) {
		finish();
	}

	public void onClickOder(View view) {
		Intent intent = new Intent();
		intent.setClass(CanteenActivity.this,OderInfoActivity.class);
		String value = "订单：";
		for(int i=0;i<foodInfoList.size();i++){
			if(foodInfoList.get(i).oderNum>0){
				value+="\n"+foodInfoList.get(i).oderNum+"份"+foodInfoList.get(i).name;
			}
		}
		intent.putExtra("content", value);
		startActivity(intent);
	}

}
