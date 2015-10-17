package com.example.robot.controller;

import android.content.Context;
import android.view.View;

/**
 * @��Ŀ���ƣ����������ˣ�ͼ�飩
 * @������com.example.robot.controller
 * @���ߣ�����
 * @����ʱ�䣺2015-10-7����9:43:48
 * @������controller�Ļ���
 */

public abstract class BaseController
{

	public View mRootView;
	public Context mContext;
	
	public BaseController(Context context){
		this.mContext = context;
		// �ڹ�����  �ͼ�����ʾ��view
		mRootView = initView(context);
		
	}
	/**
	 * ��ʼ��view�ķ���������ȥʵ��
	 * @return
	 */
	protected abstract View initView(Context context);
	
	/**
	 * �������ݵķ������������ʵ�֣�Ҳ���Բ�ʵ��
	 */
	public void initData(){		
	}

	/**
	 * ��¶��ȥ�Ļ�ø�view�ķ���
	 * @return
	 */
	public View getRootView()
	{
		return mRootView;
	}
	
}
