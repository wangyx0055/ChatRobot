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
 * @项目名称：语音机器人（图灵）
 * @包名：com.example.robot
 * @作者：王彪
 * @创建时间：2015-10-5下午11:10:48
 * @描述：详情页的activity
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

		// 拿到发来的Intent
		Intent intent = getIntent();
		// 获得 带过来的新闻详情数据
		String data = intent.getStringExtra(DETAIL);

		mSettings = mWebView.getSettings();
		// 设置js可以直接打开窗口，如window.open()，默认为false
		mSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		// 设置js可用
		mSettings.setJavaScriptEnabled(true);
		// 是否可以缩放，默认true
		mSettings.setSupportZoom(true);
		// 显示 放大缩小按钮
		mSettings.setBuiltInZoomControls(true);
		// 设置此属性，可任意比例缩放。大视图模式
		mSettings.setUseWideViewPort(true);
		// 和setUseWideViewPort(true)一起解决网页自适应问题
		mSettings.setLoadWithOverviewMode(true);
		// 是否使用缓存 ,缓存不开启的时候，可能会有一些使用了这些存储的网页无法打开。
		mSettings.setAppCacheEnabled(true);
		// DOM Storage
		mSettings.setDomStorageEnabled(true);
		// 设置用户代理，一般不用
		// displayWebview.getSettings().setUserAgentString("User-Agent:Android");

		// 设置客户端，可以利用一些回调进行操作
		mWebView.setWebViewClient(new WebViewClient() {
			// 重写了shouldOverrideUrlLoading()方法。这就表明当需要从一个网页跳转到另一个网页时，
			// 我们希望目标网页仍然在当前WebView中显示，而不是打开系统浏览器。
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url); // 这里的url是指加载出来的页面中的你点击的url
				return true; // 表示当前WebView可以处理打开新网页的请求，不用借助系统浏览器
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				// 页面开始加载时的回调
				mPbLoading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url)
			{
				// 页面 加载完成时的回调
				mPbLoading.setVisibility(View.GONE);
			}
		});
		// 设置Chrome客户端，可以利用一些回调进行操作
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				// 页面加载进度的回调 newProgress在0~100之间
				// 应用场景：打开网页需要显示加载进度条时
			}
		});
		// 加载网页
		Log.d(TAG, data); // &searchDep=%E5%AE%89%E5%BA%86%E8%A5%BF&searchArr=%E5%B9%BF%E5%B7%9E%E4%B8%9C&startCity=%E5%AE%89%E5%BA%86&endCity=%E5%B9%BF%E5%B7%9E
		// data =
		// "http://touch.qunar.com/h5/train/trainOrderFillOpt?startStation=安庆西&endStation=广州东&searchType=stasta&trainNum=K311/K310&date=2015-10-07&sort=3&seatType=*";
		mWebView.loadUrl(data);
	}
}
