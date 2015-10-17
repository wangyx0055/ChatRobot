package com.example.robot;

import java.util.List;

/**
 * @项目名称：语音机器人
 * @包名：com.example.robot
 * @作者：王彪
 * @创建时间：2015-10-4下午5:10:58
 * @描述：读取语音返回的解析结果的bean
 */

public class JsonItem
{
	public int bg;
	public int ed;
	public boolean ls;
	public int sn;
	public List<WSBean> ws;
	
	public class WSBean{
		public int bg;
		public List<CWBean> cw;
	}
	
	public class CWBean{
		public float sc;
		public String w;
	}
}
