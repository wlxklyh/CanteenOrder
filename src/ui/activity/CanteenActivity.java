package ui.activity;

import java.util.ArrayList;
import java.util.List;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class CanteenActivity extends Activity {

	private Context mContext;
	private ViewPager mAdsViewPager;
	private ListView mFoodListView;

	private CanteenListViewAdapter mCanteenListViewAdapter;
	private CanteenViewpagerAdapter mCanteenViewpagerAdapter;

	private List<FoodInfo> foodInfoList = new ArrayList<FoodInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_canteen);

		// ������ʼ��
		Intent intent = getIntent();
		String titleStr = intent.getStringExtra("name");
		TextView titleTextView = (TextView) findViewById(R.id.button_title);
		titleTextView.setText(titleStr);
		titleTextView.setTextSize(25);

		mFindViewByID();
		initData();
		setData();
	}

	private void mFindViewByID() {
		mFoodListView = (ListView) findViewById(R.id.listView_food);
		mAdsViewPager = (ViewPager) findViewById(R.id.ads_viewpager);
	}

	private void initData() {
		mCanteenListViewAdapter = new CanteenListViewAdapter(mContext);
		mCanteenViewpagerAdapter = new CanteenViewpagerAdapter(mContext);
		foodInfoList
				.add(new FoodInfo(true, "���±�ⷹ�ײ�",
						"����͸���ʶ����壬Ũ±֭��������飬ɫ�����ˣ���������������У�����θ�ڴ󿪡����Ƿǳ��õ��·���", 5,
						24, 20.0,R.drawable.icon_food1));
		foodInfoList
				.add(new FoodInfo(true, "�����ŹǷ� ",
						"����͸���ʶ����壬Ũ±֭��������飬ɫ�����ˣ���������������У�����θ�ڴ󿪡����Ƿǳ��õ��·���", 5,
						24, 20.0,R.drawable.icon_food2));
		foodInfoList
				.add(new FoodInfo(true, "����±�ⷹ ",
						"����͸���ʶ����壬Ũ±֭��������飬ɫ�����ˣ���������������У�����θ�ڴ󿪡����Ƿǳ��õ��·���", 5,
						24, 20.0,R.drawable.icon_food3));
		foodInfoList
				.add(new FoodInfo(true, "ơ��Ѽ��",
						"����͸���ʶ����壬Ũ±֭��������飬ɫ�����ˣ���������������У�����θ�ڴ󿪡����Ƿǳ��õ��·���", 5,
						24, 20.0,R.drawable.icon_food4));
		foodInfoList
				.add(new FoodInfo(true, "����ţ��",
						"����͸���ʶ����壬Ũ±֭��������飬ɫ�����ˣ���������������У�����θ�ڴ󿪡����Ƿǳ��õ��·���", 5,
						24, 20.0,R.drawable.icon_food5));
		foodInfoList
				.add(new FoodInfo(true, "���ǳ��� ",
						"����͸���ʶ����壬Ũ±֭��������飬ɫ�����ˣ���������������У�����θ�ڴ󿪡����Ƿǳ��õ��·���", 5,
						24, 20.0,R.drawable.icon_food6));
		foodInfoList
				.add(new FoodInfo(true, "������ ",
						"����͸���ʶ����壬Ũ±֭��������飬ɫ�����ˣ���������������У�����θ�ڴ󿪡����Ƿǳ��õ��·���", 5,
						24, 20.0,R.drawable.icon_food7));
		foodInfoList
				.add(new FoodInfo(true, "����ź�� ",
						"����͸���ʶ����壬Ũ±֭��������飬ɫ�����ˣ���������������У�����θ�ڴ󿪡����Ƿǳ��õ��·���", 5,
						24, 20.0,R.drawable.icon_food8));
		foodInfoList
				.add(new FoodInfo(true, "÷�˿���",
						"����͸���ʶ����壬Ũ±֭��������飬ɫ�����ˣ���������������У�����θ�ڴ󿪡����Ƿǳ��õ��·���", 5,
						24, 20.0,R.drawable.icon_food9));
		foodInfoList
				.add(new FoodInfo(true, "Ƥ������",
						"����͸���ʶ����壬Ũ±֭��������飬ɫ�����ˣ���������������У�����θ�ڴ󿪡����Ƿǳ��õ��·���", 5,
						24, 20.0,R.drawable.icon_food10));
		foodInfoList
				.add(new FoodInfo(true, "������빽",
						"����͸���ʶ����壬Ũ±֭��������飬ɫ�����ˣ���������������У�����θ�ڴ󿪡����Ƿǳ��õ��·���", 5,
						24, 20.0,R.drawable.icon_food11));
		foodInfoList
				.add(new FoodInfo(true, "����",
						"����͸���ʶ����壬Ũ±֭��������飬ɫ�����ˣ���������������У�����θ�ڴ󿪡����Ƿǳ��õ��·���", 5,
						24, 20.0,R.drawable.icon_food12));
		mCanteenListViewAdapter.setList(foodInfoList);

	}

	private void setData() {
		mFoodListView.setAdapter(mCanteenListViewAdapter);
		mFoodListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
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
		String value = "������";
		for(int i=0;i<foodInfoList.size();i++){
			if(foodInfoList.get(i).oderNum>0){
				value+="\n"+foodInfoList.get(i).oderNum+"��"+foodInfoList.get(i).name;
			}
		}
		intent.putExtra("content", value);
		startActivity(intent);
	}

}
