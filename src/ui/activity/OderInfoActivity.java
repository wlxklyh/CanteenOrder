package ui.activity;

import com.example.canteenorder.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OderInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oderinfo);
		TextView text = (TextView)findViewById(R.id.textView_content);
		Intent intent = getIntent();
		String str = intent.getStringExtra("content");
		text.setText(str);
	}
	public void onClickBack(View view) {
		finish();
	}
}
