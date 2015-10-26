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
 * @��Ŀ���ƣ����������ˣ�ͼ�飩
 * @������com.example.robot
 * @���ߣ�����
 * @����ʱ�䣺2015-10-5����11:10:48
 * @����������ҳ��activity
 */

public class DetailActivity extends Activity
{
	public static final String					DETAIL	= "detailUrl";
	private static final String					TAG		= "DetailActivity";
	private WebView								mWebView;
	private WebSettings							mSettings;
	private ProgressBar							mPbLoading;
	private FrameLayout							mVideoView;				// ȫ��ʱ��Ƶ����view
	private MyWebChromeClient					mWebChromeClient;
	private View								mCustomView;				// ����
																			// ȫ��ʱ���view
	private WebChromeClient.CustomViewCallback	mCustomViewCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		// ���actionbar����
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// ��ʼ��webview
		initwidget();

		// ������ҳ
		// �õ�������Intent
		Intent intent = getIntent();
		// ��� ��������������������
		String data = intent.getStringExtra(DETAIL);
		Log.d(TAG, data);
		mWebView.loadUrl(data);

	}
	/**
     * ���ò˵�ѡ��ĵ���¼� 
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
	 * ��ʼ��webview�ķ���
	 */
	private void initwidget()
	{
		mWebView = (WebView) findViewById(R.id.wv_detail);
		mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);
		// ����video����֮�����Ƶ�ŵ�������ȥ
		mVideoView = (FrameLayout) findViewById(R.id.video_view);

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
		// �Զ��򿪴���
		mSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		// �Ű���Ӧ��Ļ
		mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

		/**
		 * setAllowFileAccess ���û��ֹWebView�����ļ����� setBlockNetworkImage �Ƿ���ʾ����ͼ��
		 * setBuiltInZoomControls �����Ƿ�֧������ setCacheMode ���û����ģʽ
		 * setDefaultFontSize ����Ĭ�ϵ������С setDefaultTextEncodingName �����ڽ���ʱʹ�õ�Ĭ�ϱ���
		 * setFixedFontFamily ���ù̶�ʹ�õ����� setJavaSciptEnabled �����Ƿ�֧��Javascript
		 * setLayoutAlgorithm ���ò��ַ�ʽ setLightTouchEnabled ��������꼤�ѡ��
		 * setSupportZoom �����Ƿ�֧�ֱ佹
		 * */

		// �����û�����һ�㲻��
		// displayWebview.getSettings().setUserAgentString("User-Agent:Android");
//		ws.setSavePassword(true);
//		ws.setSaveFormData(true);// ���������
//		ws.setJavaScriptEnabled(true);
//		ws.setGeolocationEnabled(true);// ���õ���λ
//		ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// ���ö�λ�����ݿ�·��

		/* �����Ƶ�޷����ŵ����� */
		// û�еĻ������ ֧�ֲ��
		// mSettings.setPluginsEnabled(true); �����ʱ�������
		mSettings.setPluginState(PluginState.ON);

		mWebChromeClient = new MyWebChromeClient();
		mWebView.setWebChromeClient(mWebChromeClient);
		mWebView.setWebViewClient(new MyWebViewClientent());
	}

	/**
	 * ����Javascript�ĶԻ�����վͼ�ꡢ��վ�����Լ���ҳ���ؽ��ȵ�
	 * 
	 * @author
	 */
	public class MyWebChromeClient extends WebChromeClient
	{
		private Bitmap	xdefaltvideo;
		private View	xprogressvideo;

		@Override
		// ����������Ƶʱ,����ȫ���ᱻ���õķ���
				public void
				onShowCustomView(View view, WebChromeClient.CustomViewCallback callback)
		{
			Log.d(TAG, "����ȫ����");
			
			// ����Ϊ����״̬
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			mWebView.setVisibility(View.GONE);
			// ���һ��ȫ����ͼ�Ѿ����ڣ���ô������ֹ���½�һ��
			if (mCustomView != null)
			{
				callback.onCustomViewHidden();
				return;
			}
			// ��ȫ����ͼ ��ӽ� ȫ����ͼ������
			mVideoView.addView(view);
			// ���½���ȫ����ͼ���ص�
			mCustomView = view;
			mCustomViewCallback = callback;
			// ʹ ȫ����ͼ���� �ɼ�
			mVideoView.setVisibility(View.VISIBLE);
		}

		@Override
		// ��Ƶ�����˳�ȫ�� �ᱻ���õķ���
				public void
				onHideCustomView()
		{

			Log.d(TAG, "�˳�ȫ����");
			if (mCustomView == null)
			{
				// �������ȫ������״̬ ,ֱ�ӷ���
				return;
			}

			// ����Ϊ ���� ״̬
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			mCustomView.setVisibility(View.GONE);

			// Remove the custom view from its container.
			mVideoView.removeView(mCustomView);
			mCustomView = null;
			// ���� ȫ����ͼ����
			mVideoView.setVisibility(View.GONE);

			mCustomViewCallback.onCustomViewHidden();
			// ʹ webview �ɼ�
			mWebView.setVisibility(View.VISIBLE);

		}

		// ��Ƶ�������Ĭ��ͼ��
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

		// ��Ƶ����ʱ����loading
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

		// ��ҳ����
		@Override
		public void onReceivedTitle(WebView view, String title)
		{
			(DetailActivity.this).setTitle(title);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress)
		{
			// ҳ����ؽ��ȵĻص� newProgress��0~100֮��
			// Ӧ�ó���������ҳ��Ҫ��ʾ���ؽ�����ʱ
		}
	}

	/**
	 * �������֪ͨ��������¼�
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

		// ��������ڴ򿪰ٶ�����ʱ �򲻿���ҳ��������
		// ��д��shouldOverrideUrlLoading()��������ͱ�������Ҫ��һ����ҳ��ת����һ����ҳʱ��
		// ����ϣ��Ŀ����ҳ��Ȼ�ڵ�ǰWebView����ʾ�������Ǵ�ϵͳ�������
//		@Override
//		public boolean shouldOverrideUrlLoading(WebView view, String url)
//		{
//			view.loadUrl(url); // �����url��ָ���س�����ҳ���е�������url
//			return true; // ��ʾ��ǰWebView���Դ��������ҳ�����󣬲��ý���ϵͳ�����
//		}

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
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		// ��ҳ��pauseʱ����webview Ҳpause��������Ƶ��������ͣ���Ͱ汾����������
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
		// ��ҳ��resumeʱ����webview Ҳresume������Ƶ���²��ţ��Ͱ汾����������
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
	 * Ҫ����webview�����ҳʱ���㷵�ؼ�������webview�з��أ�������ֱ���˳�������ô�͵���дonKeyDown������
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			// �����ȫ�����˳�ȫ��
			if (inCustomView())
			{
				hideCustomView();

			}
			else if (mWebView.canGoBack())
			{
				// goBack()��ʾ����WebView����һҳ��
				mWebView.goBack();
			}
			else
			{
				// ���ҳ��û�пɷ��ص��ˣ������ٵ�ǰactivity
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * �ж��Ƿ� ����ȫ��
	 * 
	 * @return
	 */
	public boolean inCustomView()
	{
		return (mCustomView != null);
	}

	/**
	 * ȫ��ʱ�����Ӽ�ִ���˳�ȫ������
	 */
	public void hideCustomView()
	{
		mWebChromeClient.onHideCustomView();
	}

}
