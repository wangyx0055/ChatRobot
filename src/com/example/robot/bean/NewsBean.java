package com.example.robot.bean;

import java.util.List;

/**
 * @��Ŀ���ƣ����������ˣ�ͼ�飩
 * @������com.example.robot.bean
 * @���ߣ�����
 * @����ʱ�䣺2015-10-5����11:13:46
 * @�������������bean
 */

public class NewsBean
{
	public int				code;
	public String			text;
	public List<NewsListBean>	list;

	public class NewsListBean
	{
		public String	article;
		public String	detailurl;
		public String	icon;
		public String	source;
	}
}
