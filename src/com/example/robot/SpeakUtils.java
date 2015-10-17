package com.example.robot;

import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * @项目名称：语音机器人
 * @包名：com.example.robot
 * @作者：王彪
 * @创建时间：2015-10-4下午4:10:33
 * @描述：说的工具类
 */

public class SpeakUtils
{
	private Context mContext;
	public SpeakUtils(Context context) {
		this.mContext = context;
		// 将“12345678”替换成您申请的 APPID，申请地址：http://www.xfyun.cn
		// 请勿在“=”与 appid 之间添加任务空字符或者转义符
		SpeechUtility.createUtility(context, SpeechConstant.APPID + "=560f988c");
	}
	/**
	 * 听的方法
	 * @param listener
	 */
	public void listen(RecognizerDialogListener listener)
	{
		// 1.创建SpeechRecognizer对象，第二个参数： 本地听写时传InitListener
		RecognizerDialog iatDialog = new RecognizerDialog(mContext, null);
		// 2.设置听写参数， 同上节
		// 3.设置回调接口
		iatDialog.setListener(listener);
		// 4.开始听写
		iatDialog.show();
	}

	/**
	 * 说的方法
	 * @param text
	 * @param listener
	 */
	public void speak(String text, SynthesizerListener listener) {
		// 1.创建 SpeechSynthesizer 对象, 第二个参数： 本地合成时传 InitListener
		SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(mContext,
				null);
		// 2.合成参数设置，详见《科大讯飞MSC API手册(Android)》 SpeechSynthesizer 类
		// 设置发音人（更多在线发音人，用户可参见 附录12.2
		mTts.setParameter(SpeechConstant.VOICE_NAME, "vixx");
		mTts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
		mTts.setParameter(SpeechConstant.VOLUME, "100");// 设置音量，范围 0~100
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端
		// 设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
		// 保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
		// 仅支持保存为 pcm 格式， 如果不需要保存合成音频，注释该行代码
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
		// 3.开始合成
		mTts.startSpeaking(text, listener);
	}

}
