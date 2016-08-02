package com.cca.mobilephone.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.ContactsInfoUtils;
import com.cca.mobilephone.Utils.ContactsInfoUtils.ContactsInfo;


public class ContactsActivity extends Activity {

	private ListView contactsList;
	private List<ContactsInfo> infos;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_contacts);
		sp=getSharedPreferences("config", MODE_PRIVATE);
		//找到listview的控件
		contactsList=(ListView) findViewById(R.id.list_contacts);
		//得到联系人集合
		infos = ContactsInfoUtils.getROMContacts(this);
		
		//设置适配器
		contactsList.setAdapter(new ContactsAdapter());
		//设置listview的点击事件
		contactsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent data=getIntent();
				String phone=infos.get(position).phone;
				data.putExtra("phone", phone);
				//返回点击条目的电话号码
				setResult(0, data);
				finish();
			}
		});
		
	}
	
	class ContactsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return infos.size();
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	
			View view;
			ViewHolder holder;
			if(convertView==null){
				//实例化view对象
				view = View.inflate(getApplicationContext(), R.layout.item_contacts, null);
				holder=new ViewHolder();
				holder.tv_name=(TextView) view.findViewById(R.id.tv_name);
				holder.tv_phone=(TextView) view.findViewById(R.id.tv_phone);
				//设置标签
				view.setTag(holder);
				
			}else{
				//复用历史的缓存convertview
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}
			holder.tv_name.setText(infos.get(position).name);
			holder.tv_phone.setText(infos.get(position).phone);
			
			return view;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
		class ViewHolder{
			TextView tv_name;
			TextView tv_phone;
		}
		
	}
}
