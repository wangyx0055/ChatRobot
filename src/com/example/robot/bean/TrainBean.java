package com.example.robot.bean;

import java.util.List;


/**
 * @��Ŀ���ƣ����������ˣ�ͼ�飩
 * @������com.example.robot.bean
 * @���ߣ�����
 * @����ʱ�䣺2015-10-5����12:43:55
 * @�������г����bean
 */

public class TrainBean
{
	public int				code;
	public String			text;
	public List<TrainListBean>	list;
	
	public class TrainListBean
	{
		public String	detailurl;
		public String	endtime;
		public String	icon;
		public String	start;
		public String	starttime;
		public String	terminal;
		public String	trainnum;
	}
}
