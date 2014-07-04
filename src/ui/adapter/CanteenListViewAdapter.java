package ui.adapter;

import java.util.List;

import com.example.canteenorder.R;

import data.FoodInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CanteenListViewAdapter extends BaseAdapter {
	private int mViewNumber = 1 << 20;
	private Context mContext;
	private List<FoodInfo> mFoodInfoList;
	private ViewHolder mViewHolder = new ViewHolder();

	public CanteenListViewAdapter(Context ct) {
		mContext = ct;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFoodInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int tempPosition = position;
		if (convertView == null) {
			LayoutInflater li = LayoutInflater.from(mContext);
			convertView = li.inflate(R.layout.layout_canteen_fooditem, null);
		}
		mViewHolder.imageView = (ImageView) convertView
				.findViewById(R.id.imageView_food);
		mViewHolder.imageView.setBackgroundResource(mFoodInfoList.get(position).iconResource);
		
		mViewHolder.titleTextView = (TextView) convertView
				.findViewById(R.id.textView_fooditem_title);
		mViewHolder.contentTextView = (TextView) convertView
				.findViewById(R.id.textView_fooditem_content);
		mViewHolder.saleTextView = (TextView) convertView
				.findViewById(R.id.textView_fooditem_monthsale);
		mViewHolder.priceTextView = (TextView) convertView
				.findViewById(R.id.textView_fooditem_price);
		mViewHolder.numTextView = (TextView) convertView
				.findViewById(R.id.textView_num);
		mViewHolder.numTextView.setText(""+mFoodInfoList.get(position).oderNum);
		
		mViewHolder.addTextView = (TextView) convertView
				.findViewById(R.id.textView_add);
		mViewHolder.addTextView.setOnClickListener(new OnClickListener() {
			private TextView text=mViewHolder.numTextView;
			private int mIndex = tempPosition;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String tempStr=(String) text.getText();
				int t=Integer.parseInt(tempStr);
				t++;
				tempStr=""+t;
				mFoodInfoList.get(mIndex).oderNum=t;
				text.setText(tempStr);
			}
		});
		
		
		mViewHolder.minusTextView = (TextView) convertView
				.findViewById(R.id.textView_minus);
		
		mViewHolder.minusTextView.setOnClickListener(new OnClickListener() {
			private TextView text=mViewHolder.numTextView;
			private int mIndex = tempPosition;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String tempStr=(String) text.getText();
				int t=Integer.parseInt(tempStr);
				if(t==0)return;
				t--;
				tempStr=""+t;
				mFoodInfoList.get(mIndex).oderNum=t;
				text.setText(tempStr);
			}
		});

		
		
		
		mViewHolder.titleTextView.setText(mFoodInfoList.get(position).name);
		mViewHolder.contentTextView
				.setText(mFoodInfoList.get(position).content);
		mViewHolder.saleTextView.setText(""
				+ mFoodInfoList.get(position).monthSale);
		mViewHolder.priceTextView.setText(""
				+ mFoodInfoList.get(position).price);
		return convertView;
	}

	private class ViewHolder {
		ImageView imageView;
		TextView titleTextView;
		TextView contentTextView;
		TextView saleTextView;
		TextView priceTextView;

		TextView addTextView;
		TextView numTextView;
		TextView minusTextView;
	}

	public void setList(List<FoodInfo> foodInfoList) {
		// TODO Auto-generated method stub
		mFoodInfoList = foodInfoList;
	}

}
