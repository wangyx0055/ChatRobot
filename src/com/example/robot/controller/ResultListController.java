package com.example.robot.controller;

import java.util.List;

import com.example.robot.Constants;
import com.example.robot.DetailActivity;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.bean.TrainBean.TrainListBean;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @项目名称：语音机器人（图灵）
 * @包名：com.example.robot.controller
 * @作者：王彪
 * @创建时间：2015-10-7上午9:47:08
 * @描述：结果信息列表的controller
 */

public class ResultListController extends BaseController
{

	protected static final String	TAG	= "ResultListController";
	private RelativeLayout			mRlTitle;
	private TextView				mTvTitle;
	private LinearLayout			mLlItemContainer;
	private String					mTitleUrl;
	private String					mTitleText;

	public ResultListController(Context context, String titleUrl , String titleText) {
		super(context);
		this.mTitleUrl = titleUrl;
		this.mTitleText = titleText;
		
		initData();
	}

	@Override
	protected View initView(Context context)
	{
		// 初始化结果列表的根view
		mRootView = View.inflate(mContext, R.layout.result_list, null);
		mRlTitle = (RelativeLayout) mRootView.findViewById(R.id.result_title_rl);
		mTvTitle = (TextView) mRootView.findViewById(R.id.result_title_tv);
		mLlItemContainer = (LinearLayout) mRootView.findViewById(R.id.result_item_container);	

		return mRootView;
	}

	@Override
	public void initData()
	{
		mTvTitle.setText(mTitleText);
		
		// 给 结果信息列表头设置点击事件
		mRlTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// 点击进入详情页面
				Intent intent = new Intent(mContext, DetailActivity.class);
				intent.putExtra(Constants.DETAILURL, mTitleUrl);
				mContext.startActivity(intent);
			}
		});
		
		
		
		/****************** 添加  结果信息列表中的条目，并各自添加点击事件*****************/
		// 在添加条目之前，把原来的清空，防止重复添加
		/*mLlItemContainer.removeAllViews();
		
		for (int i = 0; i < mAnswerList.size(); i++)
		{
			new ResultItemController(mContext, itemUrl, upTitleText, downTitleText);
			
			final TrainListBean trainListBean = mAnswerList.get(i);
			tvUpTitle.setText(trainListBean.start+"-"+trainListBean.terminal+","+trainListBean.trainnum);
			tvDownTitle.setText("发车时间:"+trainListBean.starttime+",到站时间:"+trainListBean.endtime);
			
			
			mLlItemContainer.addView(itemView);			
		}	*/
	}

	@Override
	public View getRootView()
	{
		return super.getRootView();
	}
	
	public LinearLayout getItemContainer(){
		return mLlItemContainer;
	}
	/**
	 * 设置标题部分的图片
	 */
	public void setTitlePic(int resid)
	{
		mRlTitle.setBackgroundResource(resid);		
	}

}
