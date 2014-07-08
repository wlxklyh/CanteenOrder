package ui.activity.menuactivity;

import com.example.canteenorder.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends Activity {

	private Button mTitleButton;
	private TextView mContentTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		mTitleButton = (Button)findViewById(R.id.button_title);
		mContentTextView = (TextView)findViewById(R.id.textView_content);
		
		Intent intent = getIntent();
		int temp = intent.getIntExtra("type",0);
		switch(temp){
			case 0:
				mTitleButton.setText("历史餐厅");
				mContentTextView.setText("orz    无订单            orz");
				break;
			case 1:
				mTitleButton.setText("历史订单");
				mContentTextView.setText("orz    无订单            orz");
				break;
			case 2:
				mTitleButton.setText("版本更新");
				mContentTextView.setText("       v1.01    ");
				break;
			case 3:
				mTitleButton.setText("关于我们");
				mContentTextView.setText("华南理工大学  计算机科学与工程学院  2011级 计联班 林炎厚 201130480315\n 华南理工大学  计算机科学与工程学院  2011级 计联班 纪秋佳 201130480315\n ");
				break;
			case 4:
				mTitleButton.setText("设置");
				mContentTextView.setText("暂时没有此功能");
				break;
			case 5:
				mTitleButton.setText("建议反馈");
				mContentTextView.setText("暂时没有反馈");
				break;
		}
	}
	
	
	public void onClickBack(View view){
		finish();
	}
}
