package com.example.robot;

import java.util.List;

/**
 * @��Ŀ���ƣ�����������
 * @������com.example.robot
 * @���ߣ�����
 * @����ʱ�䣺2015-10-4����5:10:58
 * @��������ȡ�������صĽ��������bean
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
