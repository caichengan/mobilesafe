package com.cca.mobilephone.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cca.mobilephone.R;

public class SettingChangedView extends RelativeLayout {

	private RelativeLayout rel_settingchanged;
	private View view;
	private TextView tv_content;
	private TextView tv_title;

	public SettingChangedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		String title=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.cca.mobilephone", "title");
		String desc=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.cca.mobilephone", "desc");
		
		tv_title.setText(title);
		tv_content.setText(desc);
		
	}

	public SettingChangedView(Context context) {
		super(context,null);
		init(context);
	}

	private void init(Context context) {
		rel_settingchanged=(RelativeLayout) findViewById(R.id.rel_settingchanged);
		view = View.inflate(context, R.layout.ui_setting_changed, null);
		this.addView(view);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}
	/**
	 * 
	 * @param text
	 */
	public void setContent(String text){
		tv_content.setText(text);
	}
	

}
