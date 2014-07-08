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
				mTitleButton.setText("��ʷ����");
				mContentTextView.setText("orz    �޶���            orz");
				break;
			case 1:
				mTitleButton.setText("��ʷ����");
				mContentTextView.setText("orz    �޶���            orz");
				break;
			case 2:
				mTitleButton.setText("�汾����");
				mContentTextView.setText("       v1.01    ");
				break;
			case 3:
				mTitleButton.setText("��������");
				mContentTextView.setText("��������ѧ  �������ѧ�빤��ѧԺ  2011�� ������ ���׺� 201130480315\n ��������ѧ  �������ѧ�빤��ѧԺ  2011�� ������ ����� 201130480315\n ");
				break;
			case 4:
				mTitleButton.setText("����");
				mContentTextView.setText("��ʱû�д˹���");
				break;
			case 5:
				mTitleButton.setText("���鷴��");
				mContentTextView.setText("��ʱû�з���");
				break;
		}
	}
	
	
	public void onClickBack(View view){
		finish();
	}
}
