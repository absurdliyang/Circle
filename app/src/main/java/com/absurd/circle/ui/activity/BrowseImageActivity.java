package com.absurd.circle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.util.ImageUtil;

import java.io.File;

/**
 * Created by absurd on 14-5-3.
 */
public class BrowseImageActivity extends BaseActivity {

    private WebView webView;

    private String path;


    public BrowseImageActivity(){
        setRightBtnStatus(RIGHT_TEXT);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_image);
        webView = (WebView) findViewById(R.id.iv);

        webView.setBackgroundColor(getResources().getColor(R.color.transparent));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        path = getIntent().getStringExtra("path");

        if (!TextUtils.isEmpty(path)) {

            if (ImageUtil.isThisBitmapCanRead(path)) {
                int[] size = ImageUtil.getBitmapSize(path);
                getActionBar().setSubtitle(String.valueOf(size[0]) + "x" + String.valueOf(size[1]));
            }

            File file = new File(path);

            String str1 = "file://" + file.getAbsolutePath().replace("/mnt/sdcard/", "/sdcard/");
            String str2 =
                    "<html>\n<head>\n     <style>\n          html,body{background:transparent;margin:0;padding:0;}          *{-webkit-tap-highlight-color:rgba(0, 0, 0, 0);}\n     </style>\n     <script type=\"text/javascript\">\n     var imgUrl = \""
                            + str1 + "\";" + "     var objImage = new Image();\n"
                            + "     var realWidth = 0;\n" + "     var realHeight = 0;\n" + "\n"
                            + "     function onLoad() {\n"
                            + "          objImage.onload = function() {\n"
                            + "               realWidth = objImage.width;\n"
                            + "               realHeight = objImage.height;\n" + "\n"
                            + "               document.gagImg.src = imgUrl;\n"
                            + "               onResize();\n" + "          }\n"
                            + "          objImage.src = imgUrl;\n" + "     }\n" + "\n"
                            + "     function onResize() {\n" + "          var scale = 1;\n"
                            + "          var newWidth = document.gagImg.width;\n"
                            + "          if (realWidth > newWidth) {\n"
                            + "               scale = realWidth / newWidth;\n"
                            + "          } else {\n"
                            + "               scale = newWidth / realWidth;\n" + "          }\n"
                            + "\n" + "          hiddenHeight = Math.ceil(30 * scale);\n"
                            + "          document.getElementById('hiddenBar').style.height = hiddenHeight + \"px\";\n"
                            + "          document.getElementById('hiddenBar').style.marginTop = -hiddenHeight + \"px\";\n"
                            + "     }\n" + "     </script>\n" + "</head>\n"
                            + "<body onload=\"onLoad()\" onresize=\"onResize()\" onclick=\"Android.toggleOverlayDisplay();\">\n"
                            + "     <table style=\"width: 100%;height:100%;\">\n"
                            + "          <tr style=\"width: 100%;\">\n"
                            + "               <td valign=\"middle\" align=\"center\" style=\"width: 100%;\">\n"
                            + "                    <div style=\"display:block\">\n"
                            + "                         <img name=\"gagImg\" src=\"\" width=\"100%\" style=\"\" />\n"
                            + "                    </div>\n"
                            + "                    <div id=\"hiddenBar\" style=\"position:absolute; width: 100%; background: transparent;\"></div>\n"
                            + "               </td>\n" + "          </tr>\n" + "     </table>\n"
                            + "</body>\n" + "</html>";
            webView.loadDataWithBaseURL("file:///android_asset/", str2, "text/html", "utf-8", null);
            webView.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.loadUrl("about:blank");
        webView.stopLoading();
        webView.clearCache(true);
    }



    @Override
    protected String actionBarTitle() {
        return "查看图片";
    }


    @Override
    protected String setRightBtnTxt() {
        return "删除";
    }

    @Override
    public void onRightBtnClicked(View view) {
        super.onRightBtnClicked(view);
        Intent deletedIntent = new Intent();
        deletedIntent.putExtra("deleted", true);
        setResult(RESULT_OK, deletedIntent);
        finish();
    }
}
