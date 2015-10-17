package com.example.robot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * @��Ŀ���ƣ����������ˣ�ͼ�飩
 * @������com.example.robot
 * @���ߣ�����
 * @����ʱ�䣺2015-10-5����11:10:48
 * @����������ҳ��activity
 */

public class DetailActivity extends Activity
{
	public static final String	DETAIL	= "detailUrl";
	private static final String	TAG		= "DetailActivity";
	private WebView				mWebView;
	private WebSettings			mSettings;
	private ProgressBar			mPbLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		mWebView = (WebView) findViewById(R.id.wv_detail);
		mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);

		// �õ�������Intent
		Intent intent = getIntent();
		// ��� ��������������������
		String data = intent.getStringExtra(DETAIL);

		mSettings = mWebView.getSettings();
		// ����js����ֱ�Ӵ򿪴��ڣ���window.open()��Ĭ��Ϊfalse
		mSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		// ����js����
		mSettings.setJavaScriptEnabled(true);
		// �Ƿ�������ţ�Ĭ��true
		mSettings.setSupportZoom(true);
		// ��ʾ �Ŵ���С��ť
		mSettings.setBuiltInZoomControls(true);
		// ���ô����ԣ�������������š�����ͼģʽ
		mSettings.setUseWideViewPort(true);
		// ��setUseWideViewPort(true)һ������ҳ����Ӧ����
		mSettings.setLoadWithOverviewMode(true);
		// �Ƿ�ʹ�û��� ,���治������ʱ�򣬿��ܻ���һЩʹ������Щ�洢����ҳ�޷��򿪡�
		mSettings.setAppCacheEnabled(true);
		// DOM Storage
		mSettings.setDomStorageEnabled(true);
		// �����û�����һ�㲻��
		// displayWebview.getSettings().setUserAgentString("User-Agent:Android");

		// ���ÿͻ��ˣ���������һЩ�ص����в���
		mWebView.setWebViewClient(new WebViewClient() {
			// ��д��shouldOverrideUrlLoading()��������ͱ�������Ҫ��һ����ҳ��ת����һ����ҳʱ��
			// ����ϣ��Ŀ����ҳ��Ȼ�ڵ�ǰWebView����ʾ�������Ǵ�ϵͳ�������
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url); // �����url��ָ���س�����ҳ���е�������url
				return true; // ��ʾ��ǰWebView���Դ��������ҳ�����󣬲��ý���ϵͳ�����
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				// ҳ�濪ʼ����ʱ�Ļص�
				mPbLoading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url)
			{
				// ҳ�� �������ʱ�Ļص�
				mPbLoading.setVisibility(View.GONE);
			}
		});
		// ����Chrome�ͻ��ˣ���������һЩ�ص����в���
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				// ҳ����ؽ��ȵĻص� newProgress��0~100֮��
				// Ӧ�ó���������ҳ��Ҫ��ʾ���ؽ�����ʱ
			}
		});
		// ������ҳ
		Log.d(TAG, data); // &searchDep=%E5%AE%89%E5%BA%86%E8%A5%BF&searchArr=%E5%B9%BF%E5%B7%9E%E4%B8%9C&startCity=%E5%AE%89%E5%BA%86&endCity=%E5%B9%BF%E5%B7%9E
		// data =
		// "http://touch.qunar.com/h5/train/trainOrderFillOpt?startStation=������&endStation=���ݶ�&searchType=stasta&trainNum=K311/K310&date=2015-10-07&sort=3&seatType=*";
		mWebView.loadUrl(data);
	}
}
