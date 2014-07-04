package ui.adapter;

import com.example.canteenorder.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CanteenViewpagerAdapter extends PagerAdapter {

	private int mViewNumber = 1 << 20;
	public int nowPosition = 1 << 19;
	private final static int sView = 4;
	private View mAdViews[] = new View[sView];

	private Context mContext;

	public CanteenViewpagerAdapter(Context ct) {
		mContext = ct;
	}
	public int getStartPosition(){
		return nowPosition;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mViewNumber;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(mAdViews[position % sView]);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		if (mAdViews[position % sView] == null) {
			mAdViews[position % sView] = new ImageView(mContext);
		}
		if (position % 3 == 0)
			mAdViews[position % sView]
					.setBackgroundResource(R.drawable.icon_ads1);
		else if (position % 3 == 1)
			mAdViews[position % sView]
					.setBackgroundResource(R.drawable.icon_ads2);
		else if (position % 3 == 2)
			mAdViews[position % sView]
					.setBackgroundResource(R.drawable.icon_ads3);
		container.addView(mAdViews[position % sView]);
		return mAdViews[position % sView];
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

}
