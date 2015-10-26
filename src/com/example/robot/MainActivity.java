package com.example.robot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robot.JsonItem.CWBean;
import com.example.robot.JsonItem.WSBean;
import com.example.robot.bean.CaiBean;
import com.example.robot.bean.CaiBean.CaiListBean;
import com.example.robot.bean.NewsBean;
import com.example.robot.bean.NewsBean.NewsListBean;
import com.example.robot.bean.TextBean;
import com.example.robot.bean.TrainBean;
import com.example.robot.bean.TrainBean.TrainListBean;
import com.example.robot.controller.ResultItemController;
import com.example.robot.controller.ResultListController;
import com.example.robot.utils.PreferenceUtils;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MainActivity extends Activity
{

	protected static final String	TAG		= "MainActivity";
	protected static final int	REQUESTCODE_CHAT_OR_MYFACE	= 100;
	private ListView				mLv;
	private SpeakUtils				mUtils;
	private List<ConversationBean>	mDatas	= new ArrayList<ConversationBean>();
	private MyAdapter				mMyAdapter;
	private String mDetailUrl;
	private long	mLastBackKeyTime = 0;
	private long	mLatestBackKeyTime;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ImageLoaderConfiguration config =
											new ImageLoaderConfiguration.Builder(
																					this)
																							.tasksProcessingOrder(QueueProcessingType.LIFO)
																							// default
																							.denyCacheImageMultipleSizesInMemory()
																							.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
																							.memoryCacheSize(2 * 1024 * 1024)
																							.memoryCacheSizePercentage(13)
																							// default
																							.diskCacheSize(50 * 1024 * 1024)
																							.diskCacheFileCount(100)
																							.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
																							// default
																							.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
																							// default
																							.writeDebugLogs()
																							.build();
		// 初始化ImageLoader
		ImageLoader.getInstance().init(config);
		
		// 初始化说话工具
		mUtils = new SpeakUtils(this);		
		
		mLv = (ListView) findViewById(R.id.lv);
		mMyAdapter = new MyAdapter();		
		mLv.setAdapter(mMyAdapter);
		
		/***************第一行机器人提示******************/
		ConversationBean bean = new ConversationBean();
		bean.isAsk = false;
		bean.answerContent = "亲爱的主人，你好！我是聪明可爱的语音机器人-小新，我可以为你提供很多服务哦，比如陪你聊天、给你讲笑话、讲故事，还能帮你查询你想要的信息，比如查天气、查火车、查航班、查美女、查帅哥，都不在话下。快和我说话吧，亲！";
		bean.backCode = 100000;
		bean.askContent = "";
		
		// 添加到list中
		mDatas.add(bean);
		// UI更新
		mMyAdapter.notifyDataSetChanged();
		// 说出来
		mUtils.speak(bean.answerContent, null);
	}
	
	/**
	 * 创建菜单
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
//        MenuItem aboutItem = menu.findItem(R.id.action_about);
//        aboutItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//			
//			@Override
//			public boolean onMenuItemClick(MenuItem item)
//			{
//				Intent intent = new Intent(MainActivity.this, AboutActivity.class);		
//				startActivity(intent);
//				return true;
//			}
//		});
        return true;
    }
    /**
     * 设置菜单选项的点击事件 
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	Intent intent = null;
    	switch (item.getItemId())
		{
			case R.id.action_settings:
				intent = new Intent(this, SettingActivity.class);		
				startActivityForResult(intent, REQUESTCODE_CHAT_OR_MYFACE);
				break;
			case R.id.action_about:
				
				intent = new Intent(this, AboutActivity.class);		
				startActivity(intent);
				
				break;
			case R.id.action_share:
				Toast.makeText(this, " 分享菜单被点击了", Toast.LENGTH_SHORT).show();
				intent = new Intent(this, DetailActivity.class);		
				intent.putExtra(Constants.DETAILURL, "http://wap.baidu.com");
				startActivity(intent);
				
				break;

			default:
				break;
		}    	
    	return super.onOptionsItemSelected(item);
    }
	
	@Override
	protected void onPause()
	{
		super.onPause();
		// 当activity 失去焦点时，语音 停止
		mUtils.stopSpeaking();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		// 当activity 重新拿到焦点时，语音 重新播放
		mUtils.resumeSpeaking();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		// 当activity 销毁时，语音 停止
		mUtils.stopSpeaking();
	}

	// 听我们说
	public void clickListen(View v)
	{
		// 将语音转换为文字---》显示到listView
		mUtils.listen(mDialogListener);

	}

	private RecognizerDialogListener	mDialogListener	= 
			new RecognizerDialogListener() {

			String	speakContent	= "";

			// 听写结果回调接口(返回Json格式结果，用户可参见附录12.1)；
			// 一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
			// 关于解析Json的代码可参见MscDemo中JsonParser类；
			// isLast等于true时会话结束。
			public void onResult(RecognizerResult results, boolean isLast)
			{
				String resultString = results.getResultString();
				Log.d("Result:", resultString);

				// resultString--->Object--->
				// 提取出w,组合成一句话
				Gson gson = new Gson();
				JsonItem jsonObject = gson.fromJson(resultString, JsonItem.class);

				StringBuilder sb = new StringBuilder();
				List<WSBean> wsList = jsonObject.ws;
				for (int i = 0; i < wsList.size(); i++)
				{
					List<CWBean> cwList = wsList.get(i).cw;
					for (int j = 0; j < cwList.size(); j++)
					{
						CWBean cw = cwList.get(j);
						sb.append(cw.w);
					}
				}

				speakContent += sb.toString();
				if (!isLast) {
				return;
				}
				// 说明说话结束了
				Log.d(TAG, "说话内容:" + speakContent);
				// 将语音转换为文字---》显示到listView

				/********************* 问 **************************/
				ConversationBean ask = new ConversationBean();
				ask.isAsk = true;
				ask.askContent = speakContent;
				mDatas.add(ask);
				// ui更新
				mMyAdapter.notifyDataSetChanged();

				/********************* 答 **************************/
				final String askContent = speakContent;
				// 根据问的内容做出回答
				// -->
				// 将说话内容 提交 到 图灵服务器，返回数据给客户端显示
				// 请求网络获取数据
				HttpUtils utils = new HttpUtils();
				RequestParams params = new RequestParams();
				// apikey:60e6445cf6f182663da5c8c700ab05f1
				params.addQueryStringParameter("key", "60e6445cf6f182663da5c8c700ab05f1");
				params.addQueryStringParameter("info", ask.askContent);

				utils.send(HttpMethod.GET,
							"http://www.tuling123.com/openapi/api", params,
							new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException e, String msg)
								{
									e.printStackTrace();
								}

								@Override
								public void onSuccess(ResponseInfo<String> responseInfo)
								{
									String result = responseInfo.result;

									Log.d(TAG, "tulingResult:"+result);
									
									// 解析返回的json结果，封装成bean对象
									// 拿到状态码
									String[] split = result.split(",");
									String[] split2 = split[0].split(":");
									int code = Integer.parseInt(split2[1]);
									
									Log.d(TAG, "code:"+code);									
									
									// 根据不同的状态码封装成对应的bean
									Gson gson = new Gson();
									ConversationBean bean = new ConversationBean();
									bean.isAsk = false;
									switch (code)
									{
										case 100000:										
										case 200000:
											//文字类 / 链接类
											TextBean textBean = gson.fromJson(result, TextBean.class);
											bean.answerContent = textBean.text;
											mDetailUrl = textBean.url;
											bean.answerUrl = textBean.url;	
											
											break;
										case 302000:
											// 新闻类
											NewsBean newsBean = gson.fromJson(result, NewsBean.class);
											List<NewsListBean> newsList = newsBean.list;
											
											bean.answerList = newsList;
											
											bean.answerContent = newsBean.text+"，点击可看详情";											
											
											bean.answerUrl = newsList.get(0).icon;
											
											break;
										case 305000:
											// 列车类
											TrainBean trainBean = gson.fromJson(result, TrainBean.class);
											List<TrainListBean> trainList = trainBean.list;
											
											bean.answerList = trainList;											
											
											bean.answerContent = trainBean.text+"，点击可看详情";											
											
											bean.answerUrl = trainList.get(0).icon;
											
											break;
										case 308000:
											// 菜谱类 、视频、小说
											CaiBean caiBean = gson.fromJson(result, CaiBean.class);
											List<CaiListBean> caiList = caiBean.list;
											
											bean.answerList = caiList;
											
											bean.answerContent = caiBean.text+"，点击可看详情";
											
											bean.answerUrl = caiList.get(0).icon;
											
											break;

										default:
											break;
									}		
									bean.askContent = askContent;
									bean.backCode = code;

									// 添加到list中
									mDatas.add(bean);
									// UI更新
									mMyAdapter.notifyDataSetChanged();

									mLv.smoothScrollToPosition(mMyAdapter
																			.getCount());

									// 说出来	
									if(bean.askContent.contains("天气")){
										// 如果是天气信息，
										String[] answerSplit = bean.answerContent.split(":");										
										String weatherInfo = "亲，已帮您找到"+answerSplit[0]+"天气信息，点击可看详情";
										mUtils.speak(weatherInfo, null);
									} else {
										mUtils.speak(bean.answerContent, null);
									}
								}
							});

				// 清空内容
				speakContent = "";
			}

			// 会话发生错误回调接口
			public void onError(SpeechError error)
			{
				error.getPlainDescription(true); // 获取错误码描述
			}
		};	

	private class MyAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			if (mDatas != null) { return mDatas.size(); }
			return 0;
		}

		@Override
		public Object getItem(int position)
		{
			if (mDatas != null) { return mDatas.get(position); }
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder = null;
			// 没有复用
			if (convertView == null)
			{
				convertView = View.inflate(getApplicationContext(), R.layout.item, null);
				holder = new ViewHolder();
				// 找view
				holder.askContainer = (ViewGroup) convertView.findViewById(R.id.ask_container);  // 问的内容的气泡
				holder.answerContainer = (ViewGroup) convertView.findViewById(R.id.answer_container); // 答的内容的气泡
				// 如果是第一个条目，距顶部距离要 有10dp
				if(position==0){
					// 获得当前手机的dpi
					DisplayMetrics displayMetrics = getResources().getDisplayMetrics();					
					int dpi = displayMetrics.densityDpi;		
					
					LayoutParams params = (LayoutParams) ((LinearLayout)holder.answerContainer).getLayoutParams();
					// 将10dp转换成像素值 px = dp*(dpi/160)
					params.topMargin = 10*(dpi/160);
					((LinearLayout)holder.answerContainer).setLayoutParams(params);
				}
				
				holder.resultListContainer = (ViewGroup) convertView.findViewById(R.id.result_list_container); // 回答的列车类信息整个的容器
				
				holder.tvAskContent = (TextView) convertView.findViewById(R.id.ask_tv_content);  // 问的文字内容
				holder.tvAnswerContent = (TextView) convertView.findViewById(R.id.answer_tv_content); // 答的文字内容
				
				holder.ivAnswerChatFace = (ImageView) convertView.findViewById(R.id.answer_iv_chatface);  // 回答的机器人头像图片
				holder.ivAskMyFace = (ImageView) convertView.findViewById(R.id.ask_iv_myface);  // 我的头像图片
				
				holder.ivAnswerPic = (ImageView) convertView.findViewById(R.id.answer_iv_pic);  // 回答的图片
				holder.ivAnswerPic.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v)
					{
							// 图片点击进入详情页面
							Intent intent = new Intent(MainActivity.this,DetailActivity.class);
							intent.putExtra("detailUrl", mDetailUrl);
							startActivity(intent);		
					}
				});

				// 设置标记
				convertView.setTag(holder);
			}
			else
			{
				// 有复用
				holder = (ViewHolder) convertView.getTag();
			}
			// 设置view的数据
			final ConversationBean bean = mDatas.get(position);
			if (bean.isAsk)
			{
				// 如果是问的情况下
				holder.askContainer.setVisibility(View.VISIBLE);
				holder.answerContainer.setVisibility(View.GONE);
				holder.resultListContainer.setVisibility(View.GONE);

				holder.tvAskContent.setText(bean.askContent);	
				
				// 设置 我的头像				
				// 先判断是不是系统头像
				if(PreferenceUtils.getBoolean(MainActivity.this, Constants.MYFACE_IS_SYS,true)){
					int currentMyFace = PreferenceUtils.getInt(MainActivity.this, Constants.MYFACE, R.drawable.icon60_1);
					holder.ivAskMyFace.setImageResource(currentMyFace);
				} else{
					String myFace_Uri = PreferenceUtils.getString(MainActivity.this, Constants.MYFACE_URI);
					Bitmap bitmap = BitmapFactory.decodeFile(myFace_Uri);
					holder.ivAskMyFace.setImageBitmap(bitmap);
				}				
			}
			else
			{
				// 如果是答的情况
				holder.askContainer.setVisibility(View.GONE);
				
				// 设置机器人头像				
				// 先判断是不是系统头像
				if(PreferenceUtils.getBoolean(MainActivity.this, Constants.CHATFACE_IS_SYS,true)){
					int currentChatFace = PreferenceUtils.getInt(MainActivity.this, Constants.CHATFACE, R.drawable.icon60_4);
					holder.ivAnswerChatFace.setImageResource(currentChatFace);
				} else{
					String chatFace_Uri = PreferenceUtils.getString(MainActivity.this, Constants.CHATFACE_URI);
					Bitmap bitmap = BitmapFactory.decodeFile(chatFace_Uri);
					holder.ivAnswerChatFace.setImageBitmap(bitmap);
				}
				
				ResultListController resultListController = null;				
				// 根据返回码判断返回信息
				switch (bean.backCode)
				{					
					case 100000:
						
						//文字类 /						
						// 天气类
						if(bean.askContent.contains("天气")){
							// 隐藏 回答气泡
							holder.answerContainer.setVisibility(View.GONE);
							// "北京:10/15 周四,11-25° 21° 晴 微风小于3级;10/16 周五,12-25° 霾 微风小于3级;10/17 周六,13-23° 小雨 微风小于3级;10/18 周日,11-18° 多云 微风小于3级;"
							String[] answerSplit = bean.answerContent.split(":");
							
							//String titleUrl = "https://wap.baidu.com/s?word="+bean.askContent;
							//String titleUrl = "http://m.sohu.com/weather/?city=%E7%BA%BD%E7%BA%A6";
							String titleUrl = "http://m.sohu.com/weather/?city="+answerSplit[0];
							String titleText = "亲，已帮您找到"+answerSplit[0]+"天气信息，点击可看详情";
							
							resultListController = new ResultListController(MainActivity.this, titleUrl, titleText);
							// 设置标题部分背景图片
							resultListController.setTitlePic(R.drawable.weather);	
							
							/****************** 添加 天气 信息列表中的条目，并各自添加点击事件*****************/
							// 在添加条目之前，把原来的清空，防止重复添加
							resultListController.getItemContainer().removeAllViews();
							
							// 将回答的文本内容拆成list集合
							String[] dayInfo = answerSplit[1].split(";");							
							
							// 逐个添加条目
							for (int i = 0; i < dayInfo.length; i++)
							{
								String[] dayInfoSplit = dayInfo[i].split(",");
								
								String itemUrl = titleUrl;
								String upTitleText = dayInfoSplit[0];
								String downTitleText = dayInfoSplit[1];
								
								ResultItemController resultItemController = new ResultItemController(MainActivity.this, itemUrl, upTitleText, downTitleText);
								// 设置条目右边的图片
								int weatherIcon = 0;
								if(dayInfoSplit[1].contains("晴")){
									weatherIcon = R.drawable.qing;
								}else if(dayInfoSplit[1].contains("云")){
									weatherIcon = R.drawable.duoyun;									
								}else if(dayInfoSplit[1].contains("阴")){
									weatherIcon = R.drawable.yin;									
								}else if(dayInfoSplit[1].contains("雨")){
									weatherIcon = R.drawable.yu;									
								}else if(dayInfoSplit[1].contains("雪")){
									weatherIcon = R.drawable.xue;									
								}else if(dayInfoSplit[1].contains("雷")){
									weatherIcon = R.drawable.leizhen;									
								}else if(dayInfoSplit[1].contains("雾")||dayInfoSplit[1].contains("霾")){
									weatherIcon = R.drawable.wumai;									
								}
								if(weatherIcon!=0){
									resultItemController.getIconView().setImageResource(weatherIcon);									
								}
								// 将条目view添加进容器
								View resultItemView = resultItemController.getRootView();							
								resultListController.getItemContainer().addView(resultItemView);
							}							
							
							holder.resultListContainer.removeAllViews();
							holder.resultListContainer.addView(resultListController.getRootView());	
							// 必须加上，否则有时不出现
							holder.resultListContainer.setVisibility(View.VISIBLE);
							
							
						} else{					
						
						holder.answerContainer.setVisibility(View.VISIBLE);
						holder.resultListContainer.setVisibility(View.GONE);

						holder.tvAnswerContent.setText(bean.answerContent);
						holder.ivAnswerPic.setVisibility(View.GONE);
						
						}
						
						break;
					case 200000:	
						// 链接类
						holder.answerContainer.setVisibility(View.VISIBLE);
						holder.resultListContainer.setVisibility(View.GONE);
						holder.ivAnswerPic.setVisibility(View.GONE);

						holder.tvAnswerContent.setText(bean.answerContent+",请点击打开!");
						// 设置点击事件打开网页
						holder.answerContainer.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v)
							{
								// 点击进入详情页面
								Intent intent = new Intent(MainActivity.this,DetailActivity.class);								
								intent.putExtra(Constants.DETAILURL, bean.answerUrl);
								startActivity(intent);								
							}
						});
						
						break;
					case 302000:
						// 新闻类		
						holder.answerContainer.setVisibility(View.GONE);
						
						List<NewsListBean> answerList = (List<NewsListBean>) bean.answerList;
						
						String titleUrl = "http://news.163.com/mobile/?from=index.sitemap";
						String titleText = bean.answerContent;
						resultListController = new ResultListController(MainActivity.this, titleUrl, titleText);
						// 设置标题部分背景图片
						resultListController.setTitlePic(R.drawable.news);
						
						/****************** 添加  新闻 信息列表中的条目，并各自添加点击事件*****************/
						// 在添加条目之前，把原来的清空，防止重复添加
						resultListController.getItemContainer().removeAllViews();
						// 逐个添加条目
						for (int i = 0; i < answerList.size(); i++)
						{
							final NewsListBean newsListBean = answerList.get(i);
							
							String itemUrl = newsListBean.detailurl;
							String upTitleText = newsListBean.article;
							String downTitleText = newsListBean.source;
							
							ResultItemController resultItemController = new ResultItemController(MainActivity.this, itemUrl, upTitleText, downTitleText);
							// 设置条目右边的图片
							ImageLoader.getInstance().displayImage(newsListBean.icon, resultItemController.getIconView());					
							// 将条目view添加进容器
							View resultItemView = resultItemController.getRootView();							
							resultListController.getItemContainer().addView(resultItemView);
						}						
						
						holder.resultListContainer.removeAllViews();
						holder.resultListContainer.addView(resultListController.getRootView());	
						// 必须加上，否则有时不出现
						holder.resultListContainer.setVisibility(View.VISIBLE);
						
						break;
						
					case 305000:
						// 如果是列车信息	
						holder.answerContainer.setVisibility(View.GONE);						
						
						List<TrainListBean> answerList_train = (List<TrainListBean>) bean.answerList;
						final TrainListBean train0Bean = answerList_train.get(0);
						// 获取 所要查询的 日期
						String TrainDate = getQueryDate(bean);
						String titleUrl_train = train0Bean.detailurl+"trainList?startStation="+train0Bean.start+"&endStation="+train0Bean.terminal+"&searchType=stasta&date="+TrainDate;
						String titleText_train = bean.answerContent;
						resultListController = new ResultListController(MainActivity.this, titleUrl_train, titleText_train);
						
						/****************** 添加火车信息列表中的条目，并各自添加点击事件*****************/
						// 在添加条目之前，把原来的清空，防止重复添加
						resultListController.getItemContainer().removeAllViews();
						// 逐个添加条目
						for (int i = 0; i < answerList_train.size(); i++)
						{
							final TrainListBean trainListBean = answerList_train.get(i);
							// 去掉车次里的中文，注意  括号的 转义
							String[] trainnumSplit = trainListBean.trainnum.split("\\(");									
							String itemUrl = trainListBean.detailurl+"trainOrderFillOpt?startStation="+trainListBean.start+"&endStation="+trainListBean.terminal+"&searchType=stasta&trainNum="+trainnumSplit[0]+"&date="+TrainDate+"&sort=3&seatType=*";
							String upTitleText = trainListBean.start+"-"+trainListBean.terminal+","+trainListBean.trainnum;
							String downTitleText = "发车:"+trainListBean.starttime+",到站:"+trainListBean.endtime;
							
							ResultItemController resultItemController = new ResultItemController(MainActivity.this, itemUrl, upTitleText, downTitleText);
							// 设置条目右边的图片
							resultItemController.getIconView().setImageResource(R.drawable.train);				
							// 将条目view添加进容器
							View resultItemView = resultItemController.getRootView();						
							resultListController.getItemContainer().addView(resultItemView);
						}						
						
						holder.resultListContainer.removeAllViews();
						holder.resultListContainer.addView(resultListController.getRootView());
						// 必须加上，否则有时不出现
						holder.resultListContainer.setVisibility(View.VISIBLE);
						
						break;
					case 308000:
						// 菜谱类 、视频、小说
						holder.answerContainer.setVisibility(View.GONE);
						
						List<CaiListBean> answerList_cai = (List<CaiListBean>) bean.answerList;
						
						String titleUrl_cai = "http://m.xiachufang.com/category";
						String titleText_cai = bean.answerContent;
						resultListController = new ResultListController(MainActivity.this, titleUrl_cai, titleText_cai);
						// 设置标题部分背景图片
						resultListController.setTitlePic(R.drawable.caipu);
						
						/****************** 添加  菜谱 信息列表中的条目，并各自添加点击事件*****************/
						// 在添加条目之前，把原来的清空，防止重复添加
						resultListController.getItemContainer().removeAllViews();
						// 逐个添加条目
						for (int i = 0; i < answerList_cai.size(); i++)
						{
							final CaiListBean caiListBean = answerList_cai.get(i);
							
							String itemUrl = caiListBean.detailurl;
							String upTitleText = caiListBean.name;
							String downTitleText = caiListBean.info;
							
							ResultItemController resultItemController = new ResultItemController(MainActivity.this, itemUrl, upTitleText, downTitleText);
							// 设置条目右边的图片
							ImageLoader.getInstance().displayImage(caiListBean.icon, resultItemController.getIconView());					
							// 将条目view添加进容器
							View resultItemView = resultItemController.getRootView();							
							resultListController.getItemContainer().addView(resultItemView);
						}						
						
						holder.resultListContainer.removeAllViews();
						holder.resultListContainer.addView(resultListController.getRootView());	
						// 必须加上，否则有时不出现
						holder.resultListContainer.setVisibility(View.VISIBLE);
						
						break;

					default:
						break;
				}

				// 去网络获取图片
				String uri = null;
				//String uri = "http://192.168.0.104/AndroidTestWeb/icon?src="
				
				Log.d(TAG, "bean.askContent:"+bean.askContent);
				if(bean.askContent.contains("潘桂芬")){					
					uri = "http://g.picphotos.baidu.com/album/s%3D1100%3Bq%3D90/sign=7941e0b244a7d933bba8e0729d7bea62/64380cd7912397ddd7b642a25f82b2b7d1a2874d.jpg";
				} else if(bean.askContent.contains("王彪")){
					uri = "http://a.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=9ade6e4ef4d3572c62e29ed8ba28121a/ca1349540923dd54c6419ecfd709b3de9c824862.jpg";
				} else if(bean.askContent.contains("潘义海")){
					uri = "http://a.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=94fefc883c292df593c3ae118c0a2d5d/4bed2e738bd4b31c8711ad1c81d6277f9e2ff863.jpg";
				} else if(bean.askContent.contains("何汉生")){
					uri = "http://g.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=3f378184652762d0843ea6bb90d779c7/9358d109b3de9c824783c7286a81800a18d843b6.jpg";
				} else if(bean.askContent.contains("何瑞琪")){
					uri = "http://a.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=572e6b9ecf3d70cf48faa809c8e7a03d/42166d224f4a20a46e33056096529822720ed063.jpg";
				} else if(bean.askContent.contains("潘佛文")){
					uri = "http://d.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=188c8aaafedcd100c99cfa2542b0362d/472309f79052982251451964d1ca7bcb0a46d426.jpg";
				} else if(bean.askContent.contains("潘福文")){
					uri = "http://d.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=188c8aaafedcd100c99cfa2542b0362d/472309f79052982251451964d1ca7bcb0a46d426.jpg";
				} else if(bean.askContent.contains("潘丹丹")){
					uri = "http://d.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=188c8aaafedcd100c99cfa2542b0362d/472309f79052982251451964d1ca7bcb0a46d426.jpg";
				} else {
					uri = bean.answerUrl;
				}
				
				Log.d(TAG, "uri:"+uri);
				
				if(uri!=null){
					if(uri != bean.answerUrl){
						holder.ivAnswerPic.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(uri, holder.ivAnswerPic);	
					}									
				}
			}

			return convertView;
		}

	}

	private class ViewHolder
	{
		
		ViewGroup	askContainer;
		ViewGroup	answerContainer;
		ViewGroup	resultListContainer;

		TextView	tvAskContent;
		TextView	tvAnswerContent;
		ImageView	ivAnswerPic;
		ImageView	ivAnswerChatFace;
		ImageView	ivAskMyFace;
		
//		ViewGroup	trainListContainer;
//		ViewGroup	trainTitle;

	}
	/**
	 * 获得 所要查询的日期
	 * @param bean
	 * @return
	 */
	private String getQueryDate(ConversationBean bean)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		long currentTime = System.currentTimeMillis();
		Date date = null;
		String queryDate = null;
		
		if(bean.askContent.contains("明天")){
			currentTime = currentTime + 1000*60*60*24;
		} else if(bean.askContent.contains("后天")){
			currentTime = currentTime + 1000*60*60*24*2;
		}
		
		date = new Date(currentTime);
		queryDate = formatter.format(date);	
		
		return queryDate;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
		if(resultCode == SettingActivity.RESULTCODE_CHAT_OR_MYFACE){
			
			switch (requestCode)
			{
				case REQUESTCODE_CHAT_OR_MYFACE:
					// 说明机器人头像  或者 我的头像 改变了,设置聊天界面的头像
					mMyAdapter.notifyDataSetChanged();
					Log.d(TAG, "onActivityResult设置聊天界面的头像");
					
					break;

				default:
					break;
			}
		}		
	}
	/**
	 * 重写back键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Log.d(TAG, "back按了");
			
			mLatestBackKeyTime = System.currentTimeMillis();
			// 如果是
			if (mLatestBackKeyTime - mLastBackKeyTime > 2000)
			{
				Toast.makeText(MainActivity.this, "再按一次退出机器人小新", Toast.LENGTH_SHORT).show();
				mLastBackKeyTime = mLatestBackKeyTime;
			}			
			else
			{
				// 如果连续点了两次就直接销毁
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
