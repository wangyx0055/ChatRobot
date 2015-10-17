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
 * @项目名称：语音机器人（mvc）
 * @包名：com.example.robot.controller
 * @作者：王彪
 * @创建时间：2015-10-7下午5:33:09
 * @描述：结果列表 条目的controller
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

		// 给每一条信息添加点击事件
		mItemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// 点击进入详情页面
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
	 *  获得右边图片的imageView
	 * @return
	 */
	public ImageView getIconView(){
		
		return mIvIcon;
	}

}
