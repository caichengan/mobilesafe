package com.cca.mobilephone.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.IntentUtils;
import com.cca.mobilephone.Utils.MD5Utils;
import com.cca.mobilephone.Utils.ToastUtils;

public class MainActivity extends Activity {

	private GridView gridview;
	private SharedPreferences sp;
	private String savedpassword;
	private static String name[]={"�ֻ�����","�ֻ�ɱ��","�߼�����","ͨѶ����","�������","���̹���",
									"��������","�ֻ�����","��������"};
	private static int icon[]={R.drawable.private_space_lock_icon,R.drawable.short_cut_anti_scan,R.drawable.security_scan_bg,R.drawable.privacy_guid_img1,
		R.drawable.ic_launcher,R.drawable.desktop_assist_clean_deep_speedup,R.drawable.desktop_assist_clean_anim_anzai_launch,R.drawable.shoujiweishi1,
		R.drawable.appmgr_onekey_gear,};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		sp=getSharedPreferences("config", MODE_PRIVATE);
		gridview = (GridView) findViewById(R.id.item_main);
		
		gridview.setAdapter(new MyAdapter());
		
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View convertview,
					int position, long id) {

				switch(position){
				case 0:
					//��ת���ֻ�����
					savedpassword = sp.getString("password", null);
					if(TextUtils.isEmpty(savedpassword)){
						//�������������͵���һ����������Ի���
						showSetupDialog();
					}else{
						//���ھ͵���һ������������Ի���
						showEnterDialog();
					}	
				 break;
				case 1:
					//��ת���ֻ�ɱ��
					IntentUtils.startActivityInfo(MainActivity.this,AntiVirusActivity.class);
					break;
				case 2:
					//��ת���߼�����
					IntentUtils.startActivityInfo(MainActivity.this,ToolActivity.class);
					break;
				case 3:
					//��ת���ֻ�ͨѶ��ʿ
					IntentUtils.startActivityInfo(MainActivity.this, CallSmsActivity.class);
					break;
				case 4:
					//��ת���������
					IntentUtils.startActivityInfo(MainActivity.this, AppManagerActivity.class);
					break;
				case 5:
					//��ת�����̹���
					IntentUtils.startActivityInfo(MainActivity.this, TaskManagerActivity.class);
					break;
				case 6:
					//��ת��������������
					IntentUtils.startActivityInfo(MainActivity.this, CleanCacheActivity.class);
					break;
				case 7:
					//��ת��������������
					IntentUtils.startActivityInfo(MainActivity.this, TrafficActivity.class);
					break;
				case 8:
					//��ת����������
					IntentUtils.startActivityInfo(MainActivity.this, SettingActivity.class);
					break;
				}
			}
			/**
			 * ������¼����Ի���
			 */
			private void showEnterDialog() {
				//�������ʱ����
				builder=new Builder(MainActivity.this);
				view=View.inflate(MainActivity.this, R.layout.show_input_dialog, null);
				builder.setView(view);
				final EditText et_password_set = (EditText) view.findViewById(R.id.et_passeord);
				btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
				btn_ok = (Button) view.findViewById(R.id.btn_ok);
				
				btn_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					//��ȡ�������ֵ
						String enterpassword=et_password_set.getText().toString().trim();
						String sav_password=sp.getString("password", null);
						if(MD5Utils.encode(enterpassword).equals(sav_password)){
							ToastUtils.show(MainActivity.this, "������ȷ��׼�������ֻ��������棡",0);
							boolean finishsetup=sp.getBoolean("finishsetup", false);
							if(finishsetup){
								ToastUtils.show(MainActivity.this, "�Ѿ����й��ֻ��������ã�",0);
								//�Ѿ�����ֻ�����
								IntentUtils.startActivityInfo(MainActivity.this, PhoneFangDaoActivity.class);
							}else{
								//û������ֻ�����
								IntentUtils.startActivityInfo(MainActivity.this, Setup1Activity.class);
							}
							dialog.dismiss();
						}else
						{
							ToastUtils.show(MainActivity.this, "��������������������룡");
						}
				}
				});
				btn_cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog=builder.show();
			}

			/**
			 * ��Ԫģʽ
			 */
			private AlertDialog dialog;
			private AlertDialog.Builder builder;
			private View view;
			private EditText et_password;
			private Button btn_cancel;
			private Button btn_ok;
			
			/**
			 * �������öԻ���
			 */
			public void showSetupDialog() {
					
					builder = new Builder(MainActivity.this);
					//����һ������
					view = View.inflate(MainActivity.this, R.layout.show_setup_dialog, null);
					builder.setView(view);
					et_password = (EditText) view.findViewById(R.id.et_set_passeord);
					final EditText et_password_config=(EditText) view.findViewById(R.id.et_passeord_config);
					
					btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
					btn_ok = (Button) view.findViewById(R.id.btn_ok);
					//�Ի����ȷ���¼�
					btn_ok.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							//��ȡ�������ֵ
							String password=et_password.getText().toString().trim();
							String passwordConfig=et_password_config.getText().toString().trim();
							if(TextUtils.isEmpty(password)||TextUtils.isEmpty(passwordConfig)){
								ToastUtils.show(MainActivity.this, "���벻��Ϊ��");
							}
							if(!password.equals(passwordConfig)){
								ToastUtils.show(MainActivity.this, "�����������벻һ�£�");
							}
							if(password.equals(passwordConfig)){
								Editor edit=sp.edit();
								//�Ѽ��ܺ�����������
								edit.putString("password", MD5Utils.encode(password));
								edit.commit();
								dialog.dismiss();
								
								//�����¼����
								showEnterDialog();
							}	
						}
					});
					btn_cancel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					dialog=builder.show();
					
			}
		});	
	}
	//�Զ���������
	class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return 9;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if(convertView==null){
				view=View.inflate(getApplicationContext(), R.layout.item_main_text, null);
				TextView tv_name=(TextView) view.findViewById(R.id.item_main_name);
				ImageView iv_img=(ImageView) view.findViewById(R.id.item_main_img);
				//���þŹ����ͼ�������
				tv_name.setText(name[position]);
				iv_img.setImageResource(icon[position]);
				return view;
			}else{
				view=convertView;
				return view;
			}
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
}
