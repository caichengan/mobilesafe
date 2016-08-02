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
		//�ҵ�listview�Ŀؼ�
		contactsList=(ListView) findViewById(R.id.list_contacts);
		//�õ���ϵ�˼���
		infos = ContactsInfoUtils.getROMContacts(this);
		
		//����������
		contactsList.setAdapter(new ContactsAdapter());
		//����listview�ĵ���¼�
		contactsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent data=getIntent();
				String phone=infos.get(position).phone;
				data.putExtra("phone", phone);
				//���ص����Ŀ�ĵ绰����
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
				//ʵ����view����
				view = View.inflate(getApplicationContext(), R.layout.item_contacts, null);
				holder=new ViewHolder();
				holder.tv_name=(TextView) view.findViewById(R.id.tv_name);
				holder.tv_phone=(TextView) view.findViewById(R.id.tv_phone);
				//���ñ�ǩ
				view.setTag(holder);
				
			}else{
				//������ʷ�Ļ���convertview
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
