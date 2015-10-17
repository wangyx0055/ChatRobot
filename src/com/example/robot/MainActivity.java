package com.example.robot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
	private ListView				mLv;
	private SpeakUtils				mUtils;
	private List<ConversationBean>	mDatas	= new ArrayList<ConversationBean>();
	private MyAdapter				mMyAdapter;
	private String mDetailUrl;
	

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
		// ��ʼ��ImageLoader
		ImageLoader.getInstance().init(config);

		mUtils = new SpeakUtils(this);		
		
		mLv = (ListView) findViewById(R.id.lv);
		mMyAdapter = new MyAdapter();
		mLv.setAdapter(mMyAdapter);
	}

	// ������˵
	public void clickListen(View v)
	{
		// ������ת��Ϊ����---����ʾ��listView
		mUtils.listen(mDialogListener);

	}

	private RecognizerDialogListener	mDialogListener	= 
			new RecognizerDialogListener() {

			String	speakContent	= "";

			// ��д����ص��ӿ�(����Json��ʽ������û��ɲμ���¼12.1)��
			// һ������»�ͨ��onResults�ӿڶ�η��ؽ����������ʶ�������Ƕ�ν�����ۼӣ�
			// ���ڽ���Json�Ĵ���ɲμ�MscDemo��JsonParser�ࣻ
			// isLast����trueʱ�Ự������
			public void onResult(RecognizerResult results, boolean isLast)
			{
				String resultString = results.getResultString();
				Log.d("Result:", resultString);

				// resultString--->Object--->
				// ��ȡ��w,��ϳ�һ�仰
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
				// ˵��˵��������
				Log.d(TAG, "˵������:" + speakContent);
				// ������ת��Ϊ����---����ʾ��listView

				/********************* �� **************************/
				ConversationBean ask = new ConversationBean();
				ask.isAsk = true;
				ask.askContent = speakContent;
				mDatas.add(ask);
				// ui����
				mMyAdapter.notifyDataSetChanged();

				/********************* �� **************************/
				final String askContent = speakContent;
				// �����ʵ����������ش�
				// -->
				// ��˵������ �ύ �� ͼ����������������ݸ��ͻ�����ʾ
				// ���������ȡ����
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
									
									// �������ص�json�������װ��bean����
									// �õ�״̬��
									String[] split = result.split(",");
									String[] split2 = split[0].split(":");
									int code = Integer.parseInt(split2[1]);
									
									Log.d(TAG, "code:"+code);									
									
									// ���ݲ�ͬ��״̬���װ�ɶ�Ӧ��bean
									Gson gson = new Gson();
									ConversationBean bean = new ConversationBean();
									bean.isAsk = false;
									switch (code)
									{
										case 100000:										
										case 200000:
											//������ / ������
											TextBean textBean = gson.fromJson(result, TextBean.class);
											bean.answerContent = textBean.text;
											mDetailUrl = textBean.url;
											bean.answerUrl = textBean.url;						
											
											
											
											break;
										case 302000:
											// ������
											NewsBean newsBean = gson.fromJson(result, NewsBean.class);
											List<NewsListBean> newsList = newsBean.list;
											
											bean.answerList = newsList;
											
											bean.answerContent = newsBean.text;
											for (int i = 0; i < newsList.size(); i++)
											{
												NewsListBean newsListBean = newsList.get(i);
												String news = newsListBean.article+"\n"+newsListBean.source;
												bean.answerContent = bean.answerContent+"\n"+news+"\n";
											}	
											
											bean.answerUrl = newsList.get(0).icon;
											
											break;
										case 305000:
											// �г���
											TrainBean trainBean = gson.fromJson(result, TrainBean.class);
											List<TrainListBean> trainList = trainBean.list;
											
											bean.answerList = trainList;
											
											
											bean.answerContent = trainBean.text;
											for (int i = 0; i < trainList.size(); i++)
											{
												TrainListBean trainListBean = trainList.get(i);
												String trains = trainListBean.start+"-"+trainListBean.terminal+"\n"+trainListBean.trainnum+"\n"+trainListBean.starttime+"-"+trainListBean.endtime;
												bean.answerContent = bean.answerContent+"\n"+trains+"\n";
											}	
											
											bean.answerUrl = trainList.get(0).icon;
											
											break;
										case 308000:
											// ������ ����Ƶ��С˵
											CaiBean caiBean = gson.fromJson(result, CaiBean.class);
											List<CaiListBean> caiList = caiBean.list;
											bean.answerContent = caiBean.text;
											for (int i = 0; i < caiList.size(); i++)
											{
												CaiListBean caiListBean = caiList.get(i);
												String cais = caiListBean.name+"\n"+caiListBean.info;
												bean.answerContent = bean.answerContent+"\n"+cais+"\n";
											}
											bean.answerUrl = caiList.get(0).icon;
											
											break;

										default:
											break;
									}		
									bean.askContent = askContent;
									bean.backCode = code;

									// ���ӵ�list��
									mDatas.add(bean);
									// UI����
									mMyAdapter.notifyDataSetChanged();

									mLv.smoothScrollToPosition(mMyAdapter
																			.getCount());

									// ˵����
									mUtils.speak(bean.answerContent, null);
								}
							});

				// �������
				speakContent = "";
			}

			// �Ự��������ص��ӿ�
			public void onError(SpeechError error)
			{
				error.getPlainDescription(true); // ��ȡ����������
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
			// û�и���
			if (convertView == null)
			{
				convertView = View.inflate(getApplicationContext(), R.layout.item, null);
				holder = new ViewHolder();
				// ��view
				holder.askContainer = (ViewGroup) convertView.findViewById(R.id.ask_container);  // �ʵ����ݵ�����
				holder.answerContainer = (ViewGroup) convertView.findViewById(R.id.answer_container); // ������ݵ�����
				holder.resultListContainer = (ViewGroup) convertView.findViewById(R.id.result_list_container); // �ش���г�����Ϣ����������
				
				holder.tvAskContent = (TextView) convertView.findViewById(R.id.ask_tv_content);  // �ʵ���������
				holder.tvAnswerContent = (TextView) convertView.findViewById(R.id.answer_tv_content); // �����������
				
				//holder.trainListContainer = (ViewGroup) convertView.findViewById(R.id.train_list_container);  // �ش���г���Ϣ�б�������
				//holder.trainTitle = (ViewGroup) convertView.findViewById(R.id.train_title);  // �ش���г���Ϣ�б��� ����
				
				holder.ivAnswerPic = (ImageView) convertView.findViewById(R.id.answer_iv_pic);  // �ش��ͼƬ
				holder.ivAnswerPic.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v)
					{
							// ͼƬ�����������ҳ��
							Intent intent = new Intent(MainActivity.this,DetailActivity.class);
							intent.putExtra("detailUrl", mDetailUrl);
							startActivity(intent);		
					}
				});

				// ���ñ��
				convertView.setTag(holder);
			}
			else
			{
				// �и���
				holder = (ViewHolder) convertView.getTag();
			}
			// ����view������
			final ConversationBean bean = mDatas.get(position);
			if (bean.isAsk)
			{
				// ������ʵ������
				holder.askContainer.setVisibility(View.VISIBLE);
				holder.answerContainer.setVisibility(View.GONE);
				holder.resultListContainer.setVisibility(View.GONE);

				holder.tvAskContent.setText(bean.askContent);				
			}
			else
			{
				// ����Ǵ�����
				holder.askContainer.setVisibility(View.GONE);
				
				ResultListController resultListController = null;				
				// ���ݷ������жϷ�����Ϣ
				switch (bean.backCode)
				{					
					case 100000:
						
						//������ /
						
						// ������
						if(bean.askContent.contains("����")){
							// ���� �ش�����
							holder.answerContainer.setVisibility(View.GONE);
							// "����:10/15 ����,11-25�� 21�� �� ΢��С��3��;10/16 ����,12-25�� �� ΢��С��3��;10/17 ����,13-23�� С�� ΢��С��3��;10/18 ����,11-18�� ���� ΢��С��3��;"
							String[] answerSplit = bean.answerContent.split(":");
							
							String titleUrl = "https://wap.baidu.com/s?word="+bean.askContent;
							String titleText = "�ף��Ѱ����ҵ�"+answerSplit[0]+"������Ϣ";
							resultListController = new ResultListController(MainActivity.this, titleUrl, titleText);
							// ���ñ��ⲿ�ֱ���ͼƬ
							resultListController.setTitlePic(R.drawable.weather);	
							
							/****************** ���� ���� ��Ϣ�б��е���Ŀ�����������ӵ���¼�*****************/
							// ��������Ŀ֮ǰ����ԭ������գ���ֹ�ظ�����
							resultListController.getItemContainer().removeAllViews();
							
							// ���ش���ı����ݲ��list����
							String[] dayInfo = answerSplit[1].split(";");							
							
							// ���������Ŀ
							for (int i = 0; i < dayInfo.length; i++)
							{
								String[] dayInfoSplit = dayInfo[i].split(",");
								
								String itemUrl = titleUrl;
								String upTitleText = dayInfoSplit[0];
								String downTitleText = dayInfoSplit[1];
								
								ResultItemController resultItemController = new ResultItemController(MainActivity.this, itemUrl, upTitleText, downTitleText);
								// ������Ŀ�ұߵ�ͼƬ
								int weatherIcon = 0;
								if(dayInfoSplit[1].contains("��")){
									weatherIcon = R.drawable.qing;
								}else if(dayInfoSplit[1].contains("��")){
									weatherIcon = R.drawable.duoyun;									
								}else if(dayInfoSplit[1].contains("��")){
									weatherIcon = R.drawable.yin;									
								}else if(dayInfoSplit[1].contains("��")){
									weatherIcon = R.drawable.yu;									
								}else if(dayInfoSplit[1].contains("ѩ")){
									weatherIcon = R.drawable.xue;									
								}else if(dayInfoSplit[1].contains("��")){
									weatherIcon = R.drawable.leizhen;									
								}else if(dayInfoSplit[1].contains("��")||dayInfoSplit[1].contains("��")){
									weatherIcon = R.drawable.wumai;									
								}
								if(weatherIcon!=0){
									resultItemController.getIconView().setImageResource(weatherIcon);									
								}
								// ����Ŀview���ӽ�����
								View resultItemView = resultItemController.getRootView();							
								resultListController.getItemContainer().addView(resultItemView);
							}							
							
							holder.resultListContainer.removeAllViews();
							holder.resultListContainer.addView(resultListController.getRootView());		
							
							
						} else{					
						
						holder.answerContainer.setVisibility(View.VISIBLE);
						holder.resultListContainer.setVisibility(View.GONE);

						holder.tvAnswerContent.setText(bean.answerContent);
						holder.ivAnswerPic.setVisibility(View.GONE);
						
						}
						
						break;
					case 200000:	
						// ������
						holder.answerContainer.setVisibility(View.VISIBLE);
						holder.resultListContainer.setVisibility(View.GONE);
						holder.ivAnswerPic.setVisibility(View.GONE);

						holder.tvAnswerContent.setText(bean.answerContent+",������!");
						// ���õ���¼�����ҳ
						holder.answerContainer.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v)
							{
								// �����������ҳ��
								Intent intent = new Intent(MainActivity.this,DetailActivity.class);								
								intent.putExtra(Constants.DETAILURL, bean.answerUrl);
								startActivity(intent);								
							}
						});
						
						break;
					case 302000:
						// ������		
						holder.answerContainer.setVisibility(View.GONE);
						
						List<NewsListBean> answerList = (List<NewsListBean>) bean.answerList;
						
						String titleUrl = "http://news.163.com/mobile/?from=index.sitemap";
						String titleText = bean.answerContent ;
						resultListController = new ResultListController(MainActivity.this, titleUrl, titleText);
						// ���ñ��ⲿ�ֱ���ͼƬ
						resultListController.setTitlePic(R.drawable.news);
						
						/****************** ����  ���� ��Ϣ�б��е���Ŀ�����������ӵ���¼�*****************/
						// ��������Ŀ֮ǰ����ԭ������գ���ֹ�ظ�����
						resultListController.getItemContainer().removeAllViews();
						// ���������Ŀ
						for (int i = 0; i < answerList.size(); i++)
						{
							final NewsListBean newsListBean = answerList.get(i);
							
							String itemUrl = newsListBean.detailurl;
							String upTitleText = newsListBean.article;
							String downTitleText = newsListBean.source;
							
							ResultItemController resultItemController = new ResultItemController(MainActivity.this, itemUrl, upTitleText, downTitleText);
							// ������Ŀ�ұߵ�ͼƬ
							ImageLoader.getInstance().displayImage(newsListBean.icon, resultItemController.getIconView());					
							// ����Ŀview���ӽ�����
							View resultItemView = resultItemController.getRootView();							
							resultListController.getItemContainer().addView(resultItemView);
						}						
						
						holder.resultListContainer.removeAllViews();
						holder.resultListContainer.addView(resultListController.getRootView());	
						
						break;
						
					case 305000:
						// ������г���Ϣ	
						holder.answerContainer.setVisibility(View.GONE);						
						
						List<TrainListBean> answerList_train = (List<TrainListBean>) bean.answerList;
						final TrainListBean train0Bean = answerList_train.get(0);
						// ��ȡ ��Ҫ��ѯ�� ����
						String TrainDate = getQueryDate(bean);
						String titleUrl_train = train0Bean.detailurl+"trainList?startStation="+train0Bean.start+"&endStation="+train0Bean.terminal+"&searchType=stasta&date="+TrainDate;
						String titleText_train = bean.answerContent ;
						resultListController = new ResultListController(MainActivity.this, titleUrl_train, titleText_train);
						
						/****************** ���ӻ���Ϣ�б��е���Ŀ�����������ӵ���¼�*****************/
						// ��������Ŀ֮ǰ����ԭ������գ���ֹ�ظ�����
						resultListController.getItemContainer().removeAllViews();
						// ���������Ŀ
						for (int i = 0; i < answerList_train.size(); i++)
						{
							final TrainListBean trainListBean = answerList_train.get(i);
							// ȥ������������ģ�ע��  ���ŵ� ת��
							String[] trainnumSplit = trainListBean.trainnum.split("\\(");									
							String itemUrl = trainListBean.detailurl+"trainOrderFillOpt?startStation="+trainListBean.start+"&endStation="+trainListBean.terminal+"&searchType=stasta&trainNum="+trainnumSplit[0]+"&date="+TrainDate+"&sort=3&seatType=*";
							String upTitleText = trainListBean.start+"-"+trainListBean.terminal+","+trainListBean.trainnum;
							String downTitleText = "����:"+trainListBean.starttime+",��վ:"+trainListBean.endtime;
							
							ResultItemController resultItemController = new ResultItemController(MainActivity.this, itemUrl, upTitleText, downTitleText);
							// ������Ŀ�ұߵ�ͼƬ
							resultItemController.getIconView().setImageResource(R.drawable.train);				
							// ����Ŀview���ӽ�����
							View resultItemView = resultItemController.getRootView();						
							resultListController.getItemContainer().addView(resultItemView);
						}						
						
						holder.resultListContainer.removeAllViews();
						holder.resultListContainer.addView(resultListController.getRootView());	
						
						break;
					case 308000:
						// ������ ����Ƶ��С˵
//						CaiBean caiBean = gson.fromJson(result, CaiBean.class);
//						List<CaiListBean> caiList = caiBean.list;
//						bean.answerContent = caiBean.text;
//						for (int i = 0; i < caiList.size(); i++)
//						{
//							CaiListBean caiListBean = caiList.get(i);
//							String cais = caiListBean.name+"\n"+caiListBean.info;
//							bean.answerContent = bean.answerContent+"\n"+cais+"\n";
//						}
//						bean.answerUrl = caiList.get(0).icon;
						
						break;

					default:
						break;
				}

				// ȥ�����ȡͼƬ
				String uri = null;
				//String uri = "http://192.168.0.104/AndroidTestWeb/icon?src="
				
				Log.d(TAG, "bean.askContent:"+bean.askContent);
				if(bean.askContent.contains("�˹��")){					
					uri = "http://g.picphotos.baidu.com/album/s%3D1100%3Bq%3D90/sign=7941e0b244a7d933bba8e0729d7bea62/64380cd7912397ddd7b642a25f82b2b7d1a2874d.jpg";
				} else if(bean.askContent.contains("����")){
					uri = "http://a.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=9ade6e4ef4d3572c62e29ed8ba28121a/ca1349540923dd54c6419ecfd709b3de9c824862.jpg";
				} else if(bean.askContent.contains("���庣")){
					uri = "http://a.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=94fefc883c292df593c3ae118c0a2d5d/4bed2e738bd4b31c8711ad1c81d6277f9e2ff863.jpg";
				} else if(bean.askContent.contains("�κ���")){
					uri = "http://g.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=3f378184652762d0843ea6bb90d779c7/9358d109b3de9c824783c7286a81800a18d843b6.jpg";
				} else if(bean.askContent.contains("������")){
					uri = "http://a.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=572e6b9ecf3d70cf48faa809c8e7a03d/42166d224f4a20a46e33056096529822720ed063.jpg";
				} else if(bean.askContent.contains("�˷���")){
					uri = "http://d.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=188c8aaafedcd100c99cfa2542b0362d/472309f79052982251451964d1ca7bcb0a46d426.jpg";
				} else if(bean.askContent.contains("�˸���")){
					uri = "http://d.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=188c8aaafedcd100c99cfa2542b0362d/472309f79052982251451964d1ca7bcb0a46d426.jpg";
				} else if(bean.askContent.contains("�˵���")){
					uri = "http://d.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=188c8aaafedcd100c99cfa2542b0362d/472309f79052982251451964d1ca7bcb0a46d426.jpg";
				} else {
					uri = bean.answerUrl;
				}
				
				Log.d(TAG, "uri:"+uri);
				
				if(uri!=null){
					holder.ivAnswerPic.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(uri, holder.ivAnswerPic);					
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
		
		ViewGroup	trainListContainer;
		ViewGroup	trainTitle;

	}
	/**
	 * ��� ��Ҫ��ѯ������
	 * @param bean
	 * @return
	 */
	private String getQueryDate(ConversationBean bean)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		long currentTime = System.currentTimeMillis();
		Date date = null;
		String queryDate = null;
		
		if(bean.askContent.contains("����")){
			currentTime = currentTime + 1000*60*60*24;
		} else if(bean.askContent.contains("����")){
			currentTime = currentTime + 1000*60*60*24*2;
		}
		
		date = new Date(currentTime);
		queryDate = formatter.format(date);	
		
		return queryDate;
	}
}