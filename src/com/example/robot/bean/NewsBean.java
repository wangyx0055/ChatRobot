package com.example.robot.bean;

import java.util.List;

/**
 * @项目名称：语音机器人（图灵）
 * @包名：com.example.robot.bean
 * @作者：王彪
 * @创建时间：2015-10-5上午11:13:46
 * @描述：新闻类的bean
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
