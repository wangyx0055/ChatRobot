package com.example.robot.controller;

import com.example.robot.Constants;
import com.example.robot.DetailActivity;
import com.example.robot.R;
import com.example.robot.bean.TrainBean.TrainListBean;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @��Ŀ���ƣ����������ˣ�mvc��
 * @������com.example.robot.controller
 * @���ߣ�����
 * @����ʱ�䣺2015-10-7����5:33:09
 * @����������б� ��Ŀ��controller
 */

public class ResultItemController extends BaseController
{
	private String		mItemUrl;
	private TextView	mTvUpTitle;
	private TextView	mTvDownTitle;
	private ImageView	mIvIcon; 
	private String mUpTitleText;
	private String mDownTitleText;
	private View	mItemView;

	public ResultItemController(Context context , String itemUrl, String upTitleText, String downTitleText) {
		super(context);
		this.mItemUrl = itemUrl;
		this.mUpTitleText = upTitleText;
		this.mDownTitleText = downTitleText;
		
		initData();		
	}

	@Override
	protected View initView(Context context)
	{
		mItemView = View.inflate(mContext, R.layout.result_item, null);
		mTvUpTitle = (TextView) mItemView.findViewById(R.id.result_item_uptitle_tv);
		mTvDownTitle = (TextView) mItemView.findViewById(R.id.result_item_downtitle_tv);
		mIvIcon = (ImageView) mItemView.findViewById(R.id.result_item_icon_iv);		
		
		return mItemView;
	}
	
	@Override
	public void initData()
	{
		mTvUpTitle.setText(mUpTitleText);
		mTvDownTitle.setText(mDownTitleText);

		// ��ÿһ����Ϣ��ӵ���¼�
		mItemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// �����������ҳ��
				Intent intent = new Intent(mContext, DetailActivity.class);
				intent.putExtra(Constants.DETAILURL, mItemUrl);
				mContext.startActivity(intent);
			}
		});
	}

	@Override
	public View getRootView()
	{
		return super.getRootView();
	}
	
	/**
	 *  ����ұ�ͼƬ��imageView
	 * @return
	 */
	public ImageView getIconView(){
		
		return mIvIcon;
	}

}
