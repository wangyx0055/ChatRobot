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
 * @��Ŀ���ƣ����������ˣ�ͼ�飩
 * @������com.example.robot.controller
 * @���ߣ�����
 * @����ʱ�䣺2015-10-7����9:47:08
 * @�����������Ϣ�б��controller
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
		// ��ʼ������б�ĸ�view
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
		
		// �� �����Ϣ�б�ͷ���õ���¼�
		mRlTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// �����������ҳ��
				Intent intent = new Intent(mContext, DetailActivity.class);
				intent.putExtra(Constants.DETAILURL, mTitleUrl);
				mContext.startActivity(intent);
			}
		});
		
		
		
		/****************** ���  �����Ϣ�б��е���Ŀ����������ӵ���¼�*****************/
		// �������Ŀ֮ǰ����ԭ������գ���ֹ�ظ����
		/*mLlItemContainer.removeAllViews();
		
		for (int i = 0; i < mAnswerList.size(); i++)
		{
			new ResultItemController(mContext, itemUrl, upTitleText, downTitleText);
			
			final TrainListBean trainListBean = mAnswerList.get(i);
			tvUpTitle.setText(trainListBean.start+"-"+trainListBean.terminal+","+trainListBean.trainnum);
			tvDownTitle.setText("����ʱ��:"+trainListBean.starttime+",��վʱ��:"+trainListBean.endtime);
			
			
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
	 * ���ñ��ⲿ�ֵ�ͼƬ
	 */
	public void setTitlePic(int resid)
	{
		mRlTitle.setBackgroundResource(resid);		
	}

}
