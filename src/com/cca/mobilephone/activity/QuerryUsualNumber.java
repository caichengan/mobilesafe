package com.cca.mobilephone.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cca.mobilephone.R;
import com.cca.mobilephone.db.dao.UsualNumberDao;

public class QuerryUsualNumber extends Activity {

	private ExpandableListView exl_listview;
	private SQLiteDatabase db;
	private TextView tv;
	private MyExpandableAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db=SQLiteDatabase.openDatabase("/data/data/com.cca.mobilephone/files/commonnum.db", null, SQLiteDatabase.OPEN_READONLY);
		
		setContentView(R.layout.activity_querryusualnumber);
		
		exl_listview = (ExpandableListView) findViewById(R.id.exl_listview);
		adapter = new MyExpandableAdapter();
		//设置适配器
		exl_listview.setAdapter(adapter);
		
		//孩子被点击的监听器
		exl_listview.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				//TODO 还没有获得点击的内容
				
				
				String data=UsualNumberDao.getChildrenNameByPosition(db, groupPosition, childPosition);
				String datas[]=data.split("\n");
				String name=datas[0];
				
				String number=datas[1];
				Toast.makeText(getApplicationContext(), groupPosition+"--"+childPosition+":"+name+":"+number, 0).show();
				
				//点击之后进行拨打指定号码的电话
				Intent intent =new Intent();
				intent.setAction(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:"+number));
				startActivity(intent);

				return true;
			}
		});
	}
	private class MyExpandableAdapter extends BaseExpandableListAdapter{
		/**
		 * 获取分组的个数
		 */
		@Override
		public int getGroupCount() {
			return UsualNumberDao.getGroupCount(db);
		}
		/**
		 * 获取每个分组的孩子的个数
		 */
		@Override
		public int getChildrenCount(int groupPosition) {
			return UsualNumberDao.getChildGroupCount(db,groupPosition);
		}
		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}
		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}
		/**
		 * 返回每个分组的view对象
		 */
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			//TextView tv;
			if(convertView==null){
			tv=new TextView(QuerryUsualNumber.this);
			}else{
				tv=(TextView) convertView;
			}
			tv.setTextSize(25);
			tv.setTextColor(Color.RED);
			tv.setText("      "+UsualNumberDao.getNameByGroupCountposition(db,groupPosition));
			return tv;
		}
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
		
			if(convertView==null){
			tv=new TextView(QuerryUsualNumber.this);
			}else{
				tv=(TextView) convertView;
			}	
			tv.setTextSize(20);
			tv.setTextColor(Color.BLACK);
			tv.setText(" "+UsualNumberDao.getChildrenNameByPosition(db,groupPosition, childPosition));
			return tv;
		}
		@Override
		public boolean isChildSelectable(int groupPosition,
				int childPosition) {

			return true;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
		
	}
}
