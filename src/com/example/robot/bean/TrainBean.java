package com.example.robot.bean;

import java.util.List;


/**
 * @项目名称：语音机器人（图灵）
 * @包名：com.example.robot.bean
 * @作者：王彪
 * @创建时间：2015-10-5下午12:43:55
 * @描述：列车类的bean
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
