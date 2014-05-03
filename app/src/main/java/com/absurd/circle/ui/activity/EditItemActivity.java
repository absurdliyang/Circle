package com.absurd.circle.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.util.StringUtil;

/**
 * Created by absurd on 14-4-12.
 */
public class EditItemActivity extends BaseActivity {

    private EditText mContentEt;
    private String mTag;

    public EditItemActivity(){
        setRightBtnStatus(RIGHT_TEXT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBusy(false);
        setRightBtnStatus(RIGHT_TEXT);
        mTag = (String)getIntent().getExtras().get("tag");

        setContentView(R.layout.activity_edit_item);
        mContentEt = (EditText)findViewById(R.id.et_edit_item_content);
        initContent();

    }

    private void initContent(){
        if(mTag == "nickname"){
            mContentEt.setText(MyProfileActivity.user.getName());
        }
    }

    @Override
    protected String actionBarTitle() {
        return "编辑";
    }

    protected String setRightBtnTxt(){
        return "确定";
    }


    public void onRightBtnClicked(View view){
        String content = mContentEt.getText().toString().trim();
        if(StringUtil.isEmpty(content)){
            warning("内容不能为空");
        }
        if(mTag.equals("nickname")){
            MyProfileActivity.user.setName(content);
        }else if(mTag.equals("description")){
            MyProfileActivity.user.setDescription(content);
        }else if(mTag.equals("interest")){
            MyProfileActivity.user.setHobby(content);
        }else if(mTag.equals("job")){
            MyProfileActivity.user.setProfession(content);
        }else if(mTag.equals("phone")){
            MyProfileActivity.user.setPhone(content);
        }
        finish();
    }




}
