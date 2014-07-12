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
	//UI控件
	private Button mLoginButton;
	private Button mRegisterButton;
	private EditText mAccountNameEdt;
	private EditText mPasswordEdt;
	//http请求用的变量
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
		mFindViewByID();

		mLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						StringBuilder sb = new StringBuilder();
						HttpClient client = new DefaultHttpClient();
						HttpParams httpParams = client.getParams();
						HttpConnectionParams.setConnectionTimeout(httpParams,
								3000);
						HttpConnectionParams.setSoTimeout(httpParams, 5000);
						HttpResponse response = null;
						try {
							String phone = mAccountNameEdt.getText().toString();
							String password = mPasswordEdt.getText().toString();
							response = client.execute(new HttpGet(HTTPHOST
									+ "m=json&a=login&phone=" + phone
									+ "&password=" + password));
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						if(response==null)
						{
							Message ms = new Message();
							Bundle bundle = new Bundle();
							bundle.putInt("type", 0);
							ms.setData(bundle);
							handlerLogin.sendMessage(ms);
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
						String result = sb.toString();
						if(result.charAt(0)=='0')
						{
							Message ms = new Message();
							Bundle bundle = new Bundle();
							bundle.putInt("type", 0);
							ms.setData(bundle);
							handlerLogin.sendMessage(ms);
						}else if(result.charAt(0)=='1')
						{
							Message ms = new Message();
							Bundle bundle = new Bundle();
							bundle.putInt("type",1);
							ms.setData(bundle);
							handlerLogin.sendMessage(ms);
						}
					}
				}.start();
			}
		});

		mRegisterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						StringBuilder sb = new StringBuilder();
						HttpClient client = new DefaultHttpClient();
						HttpParams httpParams = client.getParams();
						HttpConnectionParams.setConnectionTimeout(httpParams,
								3000);
						HttpConnectionParams.setSoTimeout(httpParams, 5000);
						HttpResponse response = null;
						try {
							String phone = mAccountNameEdt.getText().toString();
							String password = mPasswordEdt.getText().toString();
							response = client.execute(new HttpGet(HTTPHOST
									+ "m=json&a=register&phone=" + phone
									+ "&password=" + password));
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						if(response==null)
						{
							Message ms = new Message();
							Bundle bundle = new Bundle();
							bundle.putInt("type",2);
							ms.setData(bundle);
							handlerRegister.sendMessage(ms);
							
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
						String result = sb.toString();
						if(result.charAt(0)=='0')
						{
							Message ms = new Message();
							Bundle bundle = new Bundle();
							bundle.putInt("type", 0);
							ms.setData(bundle);
							handlerRegister.sendMessage(ms);
						}else if(result.charAt(0)=='1')
						{
							Message ms = new Message();
							Bundle bundle = new Bundle();
							bundle.putInt("type",1);
							ms.setData(bundle);
							handlerRegister.sendMessage(ms);
						}
					}
				}.start();
			}
		});
	}

	private void mFindViewByID(){
		mRegisterButton = (Button) findViewById(R.id.button_register);
		mLoginButton = (Button) findViewById(R.id.button_login);
		mAccountNameEdt = (EditText) findViewById(R.id.editText_accountName);
		mPasswordEdt = (EditText) findViewById(R.id.editText_password);
	}
	
	private Handler handlerLogin = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			int temp=bundle.getInt("type");
			switch(temp){
			case 0:
				Toast.makeText(mContext, "用戶名不存在或者密碼錯誤", Toast.LENGTH_LONG).show();
				break;
			case 1:
				Intent intent = new Intent();
				intent.putExtra("accountPhone", mAccountNameEdt.getText().toString());
				intent.setClass(mContext,MainActivity.class);
				startActivity(intent);
				break;
				
			}
		}
	};
	
	private Handler handlerRegister = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			int temp=bundle.getInt("type");
			switch(temp){
			case 0:
				Toast.makeText(mContext, "这个手机号码被使用了", Toast.LENGTH_LONG).show();
				break;
			case 1:
				Toast.makeText(mContext, "成功注册！！！", Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.putExtra("accountPhone", mAccountNameEdt.getText().toString());
				intent.setClass(mContext,MainActivity.class);
				startActivity(intent);
				break;
			case 2:
				Toast.makeText(mContext, "服务器正忙", Toast.LENGTH_LONG).show();
				break;
			}

		}
	};
}

