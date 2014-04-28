package com.absurd.circle.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.ui.adapter.FacesAdapter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by absurd on 14-4-19.
 */
public class FacesUtil {

    public static final HashMap<String,String> faces = parseFaces(1);

    public static final List<String> tags = getFacesTags();

    public static HashMap<String,String> parseFaces(int mode){
        HashMap<String, String> faceMaps = new HashMap<String, String>();
        try {
            InputStream inputStream  = AppContext.getContext().getAssets().open("info.plist");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputStream);
            Element root = document.getRootElement();
            List<Element> childElements = root.elements();
            for(Element child : childElements){
                List<Element> childs = child.elements();
                String key = "";
                String value = "";
                for(int i = 0; i < childs.size(); i++){
                    if(mode == 1) {
                        if (i == 1)
                            key = childs.get(i).getText();
                        if (i == 7)
                            value = childs.get(i).getText();
                    }else{
                        if (i == 1)
                            value = childs.get(i).getText();
                        if (i == 7)
                            key = childs.get(i).getText();
                    }
                }
                faceMaps.put(key,value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (DocumentException e) {
            e.printStackTrace();
        }
        return faceMaps;
    }


    public static List<String> getFacesTags(){
        Iterator iter = faces.entrySet().iterator();

        List<String> valList = new ArrayList<String>();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String)entry.getKey();
            String val = (String) entry.getValue();
            valList.add(val);
        }
        // Sort the tags
        for(int i = 0; i < valList.size(); i++){
            int min = i;
            for(int j = i + 1; j < valList.size(); j++){
                if(valList.get(min).compareTo(valList.get(j)) > 0){
                    min = j;
                }
            }
            String tmp = valList.get(min);
            valList.set(min,valList.get(i));
            valList.set(i,tmp);
        }

        HashMap<String, String> reverseMap = parseFaces(2);
        List<String> tagList = new ArrayList<String>();
        for(String val : valList){
            tagList.add(reverseMap.get(val));
        }
        return tagList;
    }


    public static SpannableStringBuilder parseFaceByText(Context context,
                                                         String content) {
        Pattern facePattern = Pattern
                .compile("\\[{1}([\\s\\S&&[^\\[\\]]]{1,4})\\]{1}");
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        Matcher matcher = facePattern.matcher(content);
        while (matcher.find()) {
            String tag = matcher.group(1);
            tag = "[" + tag + "]";
            int resId = 0;
            try {
                resId = FacesAdapter.getImageIds()[FacesUtil.tags.indexOf(tag)];
                Drawable d = context.getResources().getDrawable(resId);
                d.setBounds(0, 0, 35, 35);// 设置表情图片的显示大小
                ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
                builder.setSpan(span, matcher.start(), matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return builder;
    }

}
