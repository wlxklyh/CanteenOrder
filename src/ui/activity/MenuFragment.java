package ui.activity;


import ui.activity.menuactivity.MenuActivity;

import com.example.canteenorder.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MenuFragment extends Fragment {
	
	private Context mContext;
	private final int HISTORYCANTEEN = 0;
	private final int HISTORYORDER = 1;
	private final int UPDATEVESION = 2;
	private final int ABOUTUS = 3;
	private final int SETTING = 4;
	private final int ADIVE = 5;

	private ImageButton mMenuItemImgBnt[] = new ImageButton[6];
	private MenuOnClickListener mMenuOnClickListener = new MenuOnClickListener();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		View view = inflater.inflate(R.layout.fragment_menu, container, false);
		mMenuItemImgBnt[HISTORYCANTEEN] = (ImageButton) view.findViewById(R.id.imageButton_historyCanteen);
		mMenuItemImgBnt[HISTORYORDER] = (ImageButton) view.findViewById(R.id.imageButton_historyOrder);
		mMenuItemImgBnt[UPDATEVESION] = (ImageButton) view.findViewById(R.id.imageButton_updateVersion);
		mMenuItemImgBnt[ABOUTUS] = (ImageButton) view.findViewById(R.id.imageButton_aboutUs);
		mMenuItemImgBnt[SETTING] = (ImageButton) view.findViewById(R.id.imageButton_setting);
		mMenuItemImgBnt[ADIVE] = (ImageButton) view.findViewById(R.id.imageButton_advice);
		for(int i=0;i<6;i++){
			mMenuItemImgBnt[i].setOnClickListener(new MenuOnClickListener());
		}
		return view;
	}
	
	
	
	class MenuOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			for(int i=0;i<6;i++){
				if(v==mMenuItemImgBnt[i]){
					intent.putExtra("type", i);
					intent.setClass(mContext, MenuActivity.class);
					startActivity(intent);
					break;
				}
			}

		}
	};
}