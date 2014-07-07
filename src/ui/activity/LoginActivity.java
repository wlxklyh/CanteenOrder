package ui.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.canteenorder.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button mLoginButton;
	private Button mRegisterButton;
	private EditText mAccountNameEdt;
	private EditText mPasswordEdt;

	private Context mContext;
	private HttpClient mClient = new DefaultHttpClient();
	private HttpParams httpParams = mClient.getParams();
	public static final String HTTPHOST = "http://125.216.247.2:8080/OderService/OderServlet?";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext=this;
		mRegisterButton = (Button) findViewById(R.id.button_register);
		mLoginButton = (Button) findViewById(R.id.button_login);
		mAccountNameEdt = (EditText) findViewById(R.id.editText_accountName);
		mPasswordEdt = (EditText) findViewById(R.id.editText_password);
		
		Log.d("lyh", "asdasdasd");
		
		
		
		mLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("lyh", "mLoginButton");
				new Thread() {
					public void run() {
						Log.d("lyh", "mLoginButton1");
						StringBuilder sb = new StringBuilder();
						HttpClient client = new DefaultHttpClient();
						HttpParams httpParams = client.getParams();
						// 璁剧疆缃戠粶瓒呮椂鍙傛暟
						HttpConnectionParams.setConnectionTimeout(httpParams,
								3000);
						HttpConnectionParams.setSoTimeout(httpParams, 5000);
						HttpResponse response = null;
						Log.d("lyh", "mLoginButton2");
						try {
							Log.d("lyh", "mLoginButton3");
							String phone = mAccountNameEdt.getText().toString();
							Log.d("lyh", "mLoginButton4");
							String password = mPasswordEdt.getText().toString();
							Log.d("lyh", "mLoginButton5");
							Log.d("lyh", phone);
							Log.d("lyh", password);
							Log.d("lyh", "mLoginButton6");
							Log.d("lyh", HTTPHOST
									+ "m=json&a=login&phone=" + phone
									+ "&password=" + password);
							
							response = client.execute(new HttpGet(HTTPHOST
									+ "m=json&a=login&phone=" + phone
									+ "&password=" + password));
							
							Log.d("lyh", HTTPHOST
									+ "m=json&a=login&phone=" + phone
									+ "&password=" + password);
							
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(response==null)
						{
							Log.d("lyh", "response is empty");
							Message ms = new Message();
							Bundle bundle = new Bundle();
							bundle.putInt("type", 0);
							ms.setData(bundle);
							handler.sendMessage(ms);
							return ;
						}
						HttpEntity entity = response.getEntity();
						if (entity != null) {
							BufferedReader reader = null;
							try {
								reader = new BufferedReader(
										new InputStreamReader(entity
												.getContent(), "UTF-8"), 8192);
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
						Log.d("lyh", "real"+sb.toString()+"a");
						String result = sb.toString();
						Log.d("lyh", "result"+result+"a");

						if(result.charAt(0)=='0')
						{
							Log.d("lyh", "Handler00000");
							Message ms = new Message();
							Bundle bundle = new Bundle();
							bundle.putInt("type", 0);
							ms.setData(bundle);
							handler.sendMessage(ms);
						}else if(result.charAt(0)=='1')
						{
							Log.d("lyh", "Handler111");
							Message ms = new Message();
							Bundle bundle = new Bundle();
							bundle.putInt("type",1);
							ms.setData(bundle);
							handler.sendMessage(ms);
						}
					}
				}.start();
			}
		});

		mRegisterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("lyh", "mRegisterButton");
			}
		});
	}

	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			int temp=bundle.getInt("type");
			Log.d("lyh", "Handler");
			
			Log.d("lyh", "Handler"+temp+"a");
			switch(temp){
			case 0:
				Log.d("lyh", "Handler"+temp+"a"); 
				Toast.makeText(mContext, "用戶名不存在或者密碼錯誤", Toast.LENGTH_LONG).show();
				break;
			case 1:
				Log.d("lyh", "Handler"+temp+"a");
				Intent intent = new Intent();
				intent.setClass(mContext,MainActivity.class);
				startActivity(intent);
				break;
			}

		}
	};
}



// JSONArray jsonArr = null;
// try {
// jsonArr = new JSONArray(sb.toString());
// } catch (JSONException e1) {
// // TODO Auto-generated catch block
// e1.printStackTrace();
// }
// JSONObject obj = null;
// try {
// driverNames = new String[jsonArr.length()];
// driverStars = new int[jsonArr.length()];
// driverState = new int[jsonArr.length()];
// driveringYears = new int[jsonArr.length()];
// driveringTimes = new int[jsonArr.length()];
// driverAddress = new String[jsonArr.length()];
// driverPhoneNumber = new String[jsonArr.length()];
//
// for (int i = 0; i < jsonArr.length(); i++) {
// obj = jsonArr.getJSONObject(i);
// driverNames[i] = (String) obj.get("sex");
// driverStars[i] = (Integer) obj.getInt("sex");
// driverState[i] = (Integer) obj.getInt("sex");
// driveringYears[i] = (Integer) obj.getInt("sex");
// driveringTimes[i] = (Integer) obj.getInt("sex");
// driverAddress[i] = (String) obj.get("sex");
// driverPhoneNumber[i] = (String) obj.get("sex");
// }
//
// } catch (JSONException e1) {
// // TODO Auto-generated catch block
// e1.printStackTrace();
// }
// inti_driver_info_handler.sendEmptyMessage(0);
