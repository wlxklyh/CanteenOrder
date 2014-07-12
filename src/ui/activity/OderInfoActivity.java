package ui.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.client.utils.URLEncodedUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.canteenorder.R;

import data.FoodInfo;
import data.OrderInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OderInfoActivity extends Activity {
	
	private String mContentStr;
	public static String OrderID;
	private Button mSureButton;
	private Context mContext;
	private JSONArray mOrderJsonArray=new JSONArray();   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oderinfo);
		mContext=this;
		TextView text = (TextView)findViewById(R.id.textView_content);
		Intent intent = getIntent();
		String str = intent.getStringExtra("content");
		text.setText(str);
		mContentStr=str;
		Calendar now =Calendar.getInstance(); 
		OrderID = ""+now.getTimeInMillis();
		
		int numOfFood = CanteenActivity.foodInfoList.size();
		for(int i=0;i<numOfFood;i++){
			JSONObject temp = new JSONObject();
			try {
				temp.put("canteenPhone", CanteenActivity.foodInfoList.get(i).canteenPhone);
				temp.put("foodId", CanteenActivity.foodInfoList.get(i).foodId);
				temp.put("orderNum", CanteenActivity.foodInfoList.get(i).oderNum);
				temp.put("orderId", OrderID);
				Log.d("lyh", " MainActivity.sAccountPhone"+ MainActivity.sAccountPhone);
				temp.put("accountPhone", MainActivity.sAccountPhone);
				Log.d("lyh", ""+temp);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				mOrderJsonArray.put(i, temp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Log.d("lyh", ""+mOrderJsonArray);
		final String tempJsonStr = (""+mOrderJsonArray);
		
		Log.d("lyh", "tempJsonStr:"+tempJsonStr);
		

		
		mSureButton = (Button)findViewById(R.id.button_sure);
		mSureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Log.d("lyh", "asdsad1");
				new Thread() {
					public void run() {
						Log.d("lyh", "asdsad2");
						StringBuilder sb = new StringBuilder();
						HttpClient client = new DefaultHttpClient();
						HttpParams httpParams = client.getParams();
						HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
						HttpConnectionParams.setSoTimeout(httpParams, 5000);
						HttpResponse response = null;
						Log.d("lyh", "asdsad2");
						try {
							Log.d("lyh", "asdsad2");
							List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
							nameValuePair.add(new BasicNameValuePair("m", "json"));
							nameValuePair.add(new BasicNameValuePair("a", "order"));
							nameValuePair.add(new BasicNameValuePair("jsonString", tempJsonStr));
				            HttpPost httpGet = new HttpPost(LoginActivity.HTTPHOST);//"m=json&"+"a=order&"+"jsonString="+ URLEncodedUtils.format(nameValuePair,HTTP.UTF_8) );
				            httpGet.setEntity(new UrlEncodedFormEntity(nameValuePair,HTTP.UTF_8));
							response = client.execute(httpGet);
							
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						Log.d("lyh", "asdsad2");
						if (response == null) {
							Message ms = new Message();
							Bundle bundle = new Bundle();
							bundle.putInt("type", 0);
							ms.setData(bundle);
							order_food_handler.sendMessage(ms);
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
						
						Message ms = new Message();
						Bundle bundle = new Bundle();
						bundle.putInt("type", 1);
						ms.setData(bundle);
						order_food_handler.sendMessage(ms);
					}
				}.start();
				
			}
		});
	}
	
	
	private Handler order_food_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			int temp=bundle.getInt("type");
			switch(temp){
			case 0:
				Toast.makeText(mContext, "暂时无法链接服务器", Toast.LENGTH_LONG).show();
				break;
			case 1:
				Toast.makeText(mContext, "订单发送成功", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

	
	public void onClickBack(View view) {
		finish();
	}
}
