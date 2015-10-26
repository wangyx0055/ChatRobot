package com.example.robot;

import android.content.Context;

import com.example.robot.utils.PreferenceUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * @��Ŀ���ƣ�����������
 * @������com.example.robot
 * @���ߣ�����
 * @����ʱ�䣺2015-10-4����4:10:33
 * @������˵�Ĺ�����
 */

public class SpeakUtils
{
	private Context mContext;
	private SpeechSynthesizer	mTts;
	
	public SpeakUtils(Context context) {
		this.mContext = context;
		// ����12345678���滻��������� APPID�������ַ��http://www.xfyun.cn
		// �����ڡ�=���� appid ֮�����������ַ�����ת���
		SpeechUtility.createUtility(context, SpeechConstant.APPID + "=560f988c");
	}
	/**
	 * ���ķ���
	 * @param listener
	 */
	public void listen(RecognizerDialogListener listener)
	{
		// 1.����SpeechRecognizer���󣬵ڶ��������� ������дʱ��InitListener
		RecognizerDialog iatDialog = new RecognizerDialog(mContext, null);
		// 2.������д������ ͬ�Ͻ�
		// 3.���ûص��ӿ�
		iatDialog.setListener(listener);
		// 4.��ʼ��д
		iatDialog.show();
	}

	/**
	 * ˵�ķ���
	 * @param text
	 * @param listener
	 */
	public void speak(String text, SynthesizerListener listener) {
		mTts = SpeechSynthesizer.createSynthesizer(mContext,
				null);
		// 2.�ϳɲ������ã�������ƴ�Ѷ��MSC API�ֲ�(Android)�� SpeechSynthesizer ��
		// ���÷����ˣ��������߷����ˣ��û��ɲμ� ��¼12.2		
		mTts.setParameter(SpeechConstant.VOICE_NAME, PreferenceUtils.getString(mContext, Constants.SPEAKVOICE, "vixx"));
		mTts.setParameter(SpeechConstant.SPEED, PreferenceUtils.getInt(mContext, Constants.SPEAKSPEED, 50)+"");// ��������
		mTts.setParameter(SpeechConstant.VOLUME, "100");// ������������Χ 0~100
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // �����ƶ�
		// ���úϳ���Ƶ����λ�ã����Զ��屣��λ�ã��������ڡ�./sdcard/iflytek.pcm��
		// ������ SD ����Ҫ�� AndroidManifest.xml ���д SD ��Ȩ��
		// ��֧�ֱ���Ϊ pcm ��ʽ�� �������Ҫ����ϳ���Ƶ��ע�͸��д���
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
		// 3.��ʼ�ϳ�
		mTts.startSpeaking(text, listener);

	}
	/**
	 * ��ָ������˵�ķ���
	 * @param text
	 * @param listener
	 */
	public void speakByMyspeed(String text, String speed) {
		mTts = SpeechSynthesizer.createSynthesizer(mContext,
		                                           null);
		// 2.�ϳɲ������ã�������ƴ�Ѷ��MSC API�ֲ�(Android)�� SpeechSynthesizer ��
		// ���÷����ˣ��������߷����ˣ��û��ɲμ� ��¼12.2		
		mTts.setParameter(SpeechConstant.VOICE_NAME, PreferenceUtils.getString(mContext, Constants.SPEAKVOICE, "vixx"));
		mTts.setParameter(SpeechConstant.SPEED, speed);// ��������
		mTts.setParameter(SpeechConstant.VOLUME, "100");// ������������Χ 0~100
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // �����ƶ�
		// ���úϳ���Ƶ����λ�ã����Զ��屣��λ�ã��������ڡ�./sdcard/iflytek.pcm��
		// ������ SD ����Ҫ�� AndroidManifest.xml ���д SD ��Ȩ��
		// ��֧�ֱ���Ϊ pcm ��ʽ�� �������Ҫ����ϳ���Ƶ��ע�͸��д���
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
		// 3.��ʼ�ϳ�
		mTts.startSpeaking(text, null);		
	}
//	/**
//	 * ����˵�����ٵķ���
//	 * @param speed
//	 */
//	public void setSpeakSpeed(String speed){
//		mTts.setParameter(SpeechConstant.SPEED, speed);// ��������
//	}
	/**
	 * ��ͣ˵�ķ���
	 */
	public void pauseSpeaking() {
		mTts.pauseSpeaking();

	}
	/**
	 * ����˵�ķ���
	 */
	public void resumeSpeaking() {
		mTts.resumeSpeaking();

	}
	/**
	 * ֹͣ˵�ķ���
	 */
	public void stopSpeaking() {
		mTts.stopSpeaking();
	}
	

}
