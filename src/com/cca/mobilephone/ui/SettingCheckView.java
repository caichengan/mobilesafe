package com.cca.mobilephone.ui;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cca.mobilephone.R;

public class SettingCheckView extends LinearLayout {
	private CheckBox set_update;
	public SettingCheckView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initial(context);
		String bigtitle=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.cca.mobilephone", "bigtitle");
		TextView tv_title=(TextView) findViewById(R.id.tv_ui_setting);
		tv_title.setText(bigtitle);
		
	}
	public SettingCheckView(Context context) {
		super(context);
		initial(context);
	}
	private void initial(Context context) {
		this.setOrientation(LinearLayout.VERTICAL);
		this.addView(View.inflate(context, R.layout.ui_setting_view, null));
		set_update=(CheckBox) findViewById(R.id.cb_set_update);
	}
	/**
	 * 判断checkbox是否被勾选
	 */
	public boolean isChecked(){
		
		return set_update.isChecked();
	}
	/**
	 * 设置checkbox是否被勾选
	 */
	public void setChecked(boolean checked){
		set_update.setChecked(checked);
		
	}
}
