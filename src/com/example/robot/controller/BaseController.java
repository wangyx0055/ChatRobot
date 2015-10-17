package com.example.robot.controller;

import android.content.Context;
import android.view.View;

/**
 * @项目名称：语音机器人（图灵）
 * @包名：com.example.robot.controller
 * @作者：王彪
 * @创建时间：2015-10-7上午9:43:48
 * @描述：controller的基类
 */

public abstract class BaseController
{

	public View mRootView;
	public Context mContext;
	
	public BaseController(Context context){
		this.mContext = context;
		// 在构造中  就加载显示的view
		mRootView = initView(context);
		
	}
	/**
	 * 初始化view的方法让子类去实现
	 * @return
	 */
	protected abstract View initView(Context context);
	
	/**
	 * 加载数据的方法，子类可以实现，也可以不实现
	 */
	public void initData(){		
	}

	/**
	 * 暴露出去的获得根view的方法
	 * @return
	 */
	public View getRootView()
	{
		return mRootView;
	}
	
}
