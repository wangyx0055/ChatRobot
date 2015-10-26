package com.example.robot;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
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
	public static final String					DETAIL	= "detailUrl";
	private static final String					TAG		= "DetailActivity";
	private WebView								mWebView;
	private WebSettings							mSettings;
	private ProgressBar							mPbLoading;
	private FrameLayout							mVideoView;				// 全屏时视频加载view
	private MyWebChromeClient					mWebChromeClient;
	private View								mCustomView;				// 接收
																			// 全屏时候的view
	private WebChromeClient.CustomViewCallback	mCustomViewCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		// 获得actionbar对象
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// 初始化webview
		initwidget();

		// 加载网页
		// 拿到发来的Intent
		Intent intent = getIntent();
		// 获得 带过来的新闻详情数据
		String data = intent.getStringExtra(DETAIL);
		Log.d(TAG, data);
		mWebView.loadUrl(data);

	}
	/**
     * 设置菜单选项的点击事件 
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId())
		{
			case android.R.id.home:
				
				this.finish();
				
				break;

			default:
				break;
		}    	
    	return super.onOptionsItemSelected(item);
    }

	/**
	 * 初始化webview的方法
	 */
	private void initwidget()
	{
		mWebView = (WebView) findViewById(R.id.wv_detail);
		mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);
		// 声明video，把之后的视频放到这里面去
		mVideoView = (FrameLayout) findViewById(R.id.video_view);

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
		// 自动打开窗口
		mSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		// 排版适应屏幕
		mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

		/**
		 * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
		 * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
		 * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
		 * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
		 * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
		 * setSupportZoom 设置是否支持变焦
		 * */

		// 设置用户代理，一般不用
		// displayWebview.getSettings().setUserAgentString("User-Agent:Android");
//		ws.setSavePassword(true);
//		ws.setSaveFormData(true);// 保存表单数据
//		ws.setJavaScriptEnabled(true);
//		ws.setGeolocationEnabled(true);// 启用地理定位
//		ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径

		/* 解决视频无法播放的问题 */
		// 没有的话会黑屏 支持插件
		// mSettings.setPluginsEnabled(true); 这个过时用下面的
		mSettings.setPluginState(PluginState.ON);

		mWebChromeClient = new MyWebChromeClient();
		mWebView.setWebChromeClient(mWebChromeClient);
		mWebView.setWebViewClient(new MyWebViewClientent());
	}

	/**
	 * 处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
	 * 
	 * @author
	 */
	public class MyWebChromeClient extends WebChromeClient
	{
		private Bitmap	xdefaltvideo;
		private View	xprogressvideo;

		@Override
		// 播放网络视频时,进入全屏会被调用的方法
				public void
				onShowCustomView(View view, WebChromeClient.CustomViewCallback callback)
		{
			Log.d(TAG, "进入全屏了");
			
			// 调整为横屏状态
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			mWebView.setVisibility(View.GONE);
			// 如果一个全屏视图已经存在，那么立刻终止并新建一个
			if (mCustomView != null)
			{
				callback.onCustomViewHidden();
				return;
			}
			// 将全屏视图 添加进 全屏视图容器中
			mVideoView.addView(view);
			// 重新接收全屏视图及回调
			mCustomView = view;
			mCustomViewCallback = callback;
			// 使 全屏视图容器 可见
			mVideoView.setVisibility(View.VISIBLE);
		}

		@Override
		// 视频播放退出全屏 会被调用的方法
				public void
				onHideCustomView()
		{

			Log.d(TAG, "退出全屏了");
			if (mCustomView == null)
			{
				// 如果不是全屏播放状态 ,直接返回
				return;
			}

			// 调整为 竖屏 状态
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			mCustomView.setVisibility(View.GONE);

			// Remove the custom view from its container.
			mVideoView.removeView(mCustomView);
			mCustomView = null;
			// 隐藏 全屏视图容器
			mVideoView.setVisibility(View.GONE);

			mCustomViewCallback.onCustomViewHidden();
			// 使 webview 可见
			mWebView.setVisibility(View.VISIBLE);

		}

		// 视频加载添加默认图标
		@Override
		public Bitmap getDefaultVideoPoster()
		{

			if (xdefaltvideo == null)
			{
				xdefaltvideo = BitmapFactory.decodeResource(
															getResources(), R.drawable.ic_launcher);
			}
			return xdefaltvideo;
		}

		// 视频加载时进程loading
		@Override
		public View getVideoLoadingProgressView()
		{

			if (xprogressvideo == null)
			{
				LayoutInflater inflater = LayoutInflater.from(DetailActivity.this);
				xprogressvideo = inflater.inflate(R.layout.video_loading_progress, null);
			}
			return xprogressvideo;
		}

		// 网页标题
		@Override
		public void onReceivedTitle(WebView view, String title)
		{
			(DetailActivity.this).setTitle(title);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress)
		{
			// 页面加载进度的回调 newProgress在0~100之间
			// 应用场景：打开网页需要显示加载进度条时
		}
	}

	/**
	 * 处理各种通知、请求等事件
	 * 
	 * @author
	 */
	public class MyWebViewClientent extends WebViewClient
	{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			return false;
		}

		// 下面这个在打开百度贴吧时 打不开网页，有问题
		// 重写了shouldOverrideUrlLoading()方法。这就表明当需要从一个网页跳转到另一个网页时，
		// 我们希望目标网页仍然在当前WebView中显示，而不是打开系统浏览器。
//		@Override
//		public boolean shouldOverrideUrlLoading(WebView view, String url)
//		{
//			view.loadUrl(url); // 这里的url是指加载出来的页面中的你点击的url
//			return true; // 表示当前WebView可以处理打开新网页的请求，不用借助系统浏览器
//		}

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
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		// 当页面pause时，让webview 也pause，否则视频声音不会停（低版本测试正常）
		try
		{
			mWebView.getClass().getMethod("onPause").invoke(mWebView, (Object[]) null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		// 当页面resume时，让webview 也resume，让视频重新播放（低版本测试正常）
		try
		{
			mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 要想在webview浏览网页时，点返回键是想在webview中返回，而不是直接退出程序，那么就得重写onKeyDown方法。
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			// 如果是全屏就退出全屏
			if (inCustomView())
			{
				hideCustomView();

			}
			else if (mWebView.canGoBack())
			{
				// goBack()表示返回WebView的上一页面
				mWebView.goBack();
			}
			else
			{
				// 如果页面没有可返回的了，就销毁当前activity
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 判断是否 处于全屏
	 * 
	 * @return
	 */
	public boolean inCustomView()
	{
		return (mCustomView != null);
	}

	/**
	 * 全屏时按返加键执行退出全屏方法
	 */
	public void hideCustomView()
	{
		mWebChromeClient.onHideCustomView();
	}

}
