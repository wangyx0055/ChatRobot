package com.example.robot.bean;

import java.util.List;

/**
 * @��Ŀ���ƣ����������ˣ�ͼ�飩
 * @������com.example.robot.bean
 * @���ߣ�����
 * @����ʱ�䣺2015-10-5����2:55:38
 * @������TODO
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
