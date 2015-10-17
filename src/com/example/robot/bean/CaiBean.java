package com.example.robot.bean;

import java.util.List;

/**
 * @项目名称：语音机器人（图灵）
 * @包名：com.example.robot.bean
 * @作者：王彪
 * @创建时间：2015-10-5下午2:55:38
 * @描述：TODO
 */

public class CaiBean
{
	public int					code;
	public String				text;
	public List<CaiListBean>	list;

	public class CaiListBean
	{
		public String	detailurl;
		public String	icon;
		public String	info;
		public String	name;
	}
}
