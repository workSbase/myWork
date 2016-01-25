package com.lenovo.lenovorobotmobile.speech;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 管理词表的类
 *
 * @author kongqw
 */
public class Worder {
    // Log标签
    private static final String TAG = "Worder";
    // 上下文
    private Context mContext;
    // 语音听写对象
    private SpeechRecognizer mIat;

    /**
     * 构造方法
     *
     * @param context
     */
    public Worder(Context context) {
        // 获取上下文
        mContext = context;
        // 设置APPID
        SpeechUtility
                .createUtility(context, SpeechConstant.APPID + "=555ebcf5");
        // 初始化识别对象
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
        // 指定引擎类型
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
    }

    /**
     * 上传用户词表
     *
     * @param
     */
    public void uploadeWords() {
        Toast.makeText(mContext, "上传词表", Toast.LENGTH_SHORT).show();
        HashMap<String, ArrayList<String>> words = new HashMap<String, ArrayList<String>>();
        ArrayList<String> w = new ArrayList<String>();
        w.add("前进");
        w.add("后退");
        w.add("左转");
        w.add("右转");
        w.add("厨房");
        w.add("卧室");
        w.add("客厅");
        w.add("显示");
        w.add("剩余");
        w.add("电量");
        w.add("家庭");
        w.add("成员");
        w.add("习惯图");
        w.add("热图");
        w.add("热度图");
        w.add("日程");
        w.add("删除");
        w.add("第");
        w.add("条");
        w.add("打开");
        w.add("关闭");
        w.add("灯");
        w.add("电视");
        w.add("在家");
        w.add("都有谁");
        w.add("返回");
        w.add("章叁");
        words.put("phone", w);

        // 获取上传词表的JSON
        String contents = makeJsonFromWords(words);

		/*
         * 上传词表
		 */
        int ret = mIat.updateLexicon("userword", contents, lexiconListener);
        if (ret != ErrorCode.SUCCESS) {
            Toast.makeText(mContext, "上传热词失败,错误码：" + ret, Toast.LENGTH_SHORT)
                    .show();
        }

    }

    /**
     * 生产上传词表的JSON
     */
    private String makeJsonFromWords(HashMap<String, ArrayList<String>> words) {
        // 拆分List，拼接JSON词表
        StringBuffer buffer = new StringBuffer();
        buffer.append("{\"userword\":[");
        Iterator<Entry<String, ArrayList<String>>> iterator = words.entrySet()
                .iterator();
        while (iterator.hasNext()) {
            Entry<String, ArrayList<String>> next = iterator.next();
            buffer.append("{\"name\":\"").append(next.getKey())
                    .append("\",\"words\":[");
            ArrayList<String> value = next.getValue();
            for (int arrIndex = 0; arrIndex < value.size(); arrIndex++) {
                buffer.append("\"").append(value.get(arrIndex)).append("\"");
                if (arrIndex < value.size() - 1) {
                    buffer.append(",");
                }
            }
            if (iterator.hasNext()) {
                buffer.append("]},");
            } else {
                buffer.append("]}");
            }
        }

        buffer.append("]}");
        return buffer.toString();
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                // 打印LOG
                Log.i(TAG, "上传词表初始化失败,错误码：" + code);
                Toast.makeText(mContext, "上传词表初始化失败,错误码：" + code,
                        Toast.LENGTH_SHORT).show();
            } else {
                // 打印LOG
                Log.i(TAG, "上传词表初始化成功");
                Toast.makeText(mContext, "上传词表初始化成功", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    /**
     * 上传联系人/词表监听器。
     */
    private LexiconListener lexiconListener = new LexiconListener() {

        @Override
        public void onLexiconUpdated(String lexiconId, SpeechError error) {
            Log.d(TAG, "onLexiconUpdated() error = " + error);
            if (error != null) {
                // 打印LOG
                Log.i(TAG, "词表上传失败" + error.toString());
                Toast.makeText(mContext, "词表上传失败" + error.toString(),
                        Toast.LENGTH_SHORT).show();
            } else {
                // 打印LOG
                Log.i(TAG, "词表上传成功");
                Toast.makeText(mContext, "词表上传成功", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
