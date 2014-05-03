package com.absurd.circle.ui.widget.smileypicker;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.util.SystemUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by absurd on 14-5-3.
 */
public class SmileyPickerHelper {
    private LinkedHashMap<Integer, LinkedHashMap<String, Bitmap>> emotionsPic
            = new LinkedHashMap<Integer, LinkedHashMap<String, Bitmap>>();

    public synchronized Map<String, Bitmap> getEmotionsPics() {
        if (emotionsPic != null && emotionsPic.size() > 0) {
            return emotionsPic.get(SmileyMap.GENERAL_EMOTION_POSITION);
        } else {
            getEmotionsTask();
            return emotionsPic.get(SmileyMap.GENERAL_EMOTION_POSITION);
        }
    }

    public synchronized Map<String, Bitmap> getHuahuaPics() {
        if (emotionsPic != null && emotionsPic.size() > 0) {
            return emotionsPic.get(SmileyMap.HUAHUA_EMOTION_POSITION);
        } else {
            getEmotionsTask();
            return emotionsPic.get(SmileyMap.HUAHUA_EMOTION_POSITION);
        }
    }


    private void getEmotionsTask() {
        Map<String, String> general = SmileyMap.getInstance().getGeneral();
        emotionsPic.put(SmileyMap.GENERAL_EMOTION_POSITION, getEmotionsTask(general));
        Map<String, String> huahua = SmileyMap.getInstance().getHuahua();
        emotionsPic.put(SmileyMap.HUAHUA_EMOTION_POSITION, getEmotionsTask(huahua));
    }

    private LinkedHashMap<String, Bitmap> getEmotionsTask(Map<String, String> emotionMap) {
        List<String> index = new ArrayList<String>();
        index.addAll(emotionMap.keySet());
        LinkedHashMap<String, Bitmap> bitmapMap = new LinkedHashMap<String, Bitmap>();
        for (String str : index) {
            String name = emotionMap.get(str);
            AssetManager assetManager = AppContext.getContext().getAssets();
            InputStream inputStream;
            try {
                inputStream = assetManager.open(name);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                            SystemUtil.dip2px(25),
                            SystemUtil.dip2px(25),
                            true);
                    if (bitmap != scaledBitmap) {
                        bitmap.recycle();
                        bitmap = scaledBitmap;
                    }
                    bitmapMap.put(str, bitmap);
                }
            } catch (IOException ignored) {

            }
        }

        return bitmapMap;
    }

}
