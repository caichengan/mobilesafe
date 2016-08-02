package com.cca.mobilephone.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.ToastUtils;
import com.cca.mobilephone.domain.AppInfo;
import com.cca.mobilephone.engine.AppManagerInfos;
/**
 * 
 * @author Administrator
 *
 */
public class AppManagerActivity extends Activity implements OnClickListener {
	private TextView tv_shji_byte;
	private TextView tv_biaoshi;
	private TextView tv_sd_byte;
	private ListView lv_listview;
	private LinearLayout ll_loading;
	private List<AppInfo> infos;
	private List<AppInfo> userapp;
	private List<AppInfo> systemapp;
	private PopupWindow popupwindow;
	private MyAppManagerAdapter adapter;
	private AppInfo info;
	private InnerUninstallAppReceiver innerReceiver;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adapter = new MyAppManagerAdapter();
			lv_listview.setAdapter(adapter);
			ll_loading.setVisibility(View.INVISIBLE);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		// ��ʼ���ؼ�
		tv_shji_byte = (TextView) findViewById(R.id.tv_shji_byte);
		tv_sd_byte = (TextView) findViewById(R.id.tv_sd_byte);
		lv_listview = (ListView) findViewById(R.id.lv_listview);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		tv_biaoshi = (TextView) findViewById(R.id.tv_biaoshi);
		innerReceiver = new InnerUninstallAppReceiver();

		userapp = new ArrayList<AppInfo>();// �û�����
		systemapp = new ArrayList<AppInfo>();// ϵͳ����

		// ע��㲥������
		innerReceiver = new InnerUninstallAppReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(innerReceiver, filter);

		// ��ȡsd�����ֻ��ڴ���ÿռ�
		File datafile = Environment.getDataDirectory();
		long datasize = datafile.getFreeSpace();
		File sdfile = Environment.getExternalStorageDirectory();
		long sdsize = sdfile.getFreeSpace();
		tv_shji_byte.setText("�ֻ������ڴ�"
				+ Formatter.formatFileSize(this, datasize));
		tv_sd_byte.setText("sd�������ڴ�" + Formatter.formatFileSize(this, sdsize));

		// �������
		fillData();
		/**
		 * ��listviewע��һ������������
		 */
		lv_listview.setOnScrollListener(new OnScrollListener() {
			/**
			 * ��״̬�����ı�ʱִ�д˷���
			 */
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			/**
			 * ��listview����ʱִ�д˷���
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (firstVisibleItem > userapp.size()) {
					tv_biaoshi.setText("ϵͳ����" + systemapp.size() + "��");
				} else {
					tv_biaoshi.setText("�û�����" + userapp.size() + "��");
				}
				dismissPopupwindow();
			}
		});
		/**
		 * listview ��Ŀ�ĵ���¼�
		 */
		lv_listview.setOnItemClickListener(new OnItemClickListener() {

			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					return;
				} else if (position == userapp.size() + 1) {
					return;
				} else if (position < userapp.size() + 1) {
					// �û�����
					info = userapp.get(position - 1);
				} else {
					// ϵͳ����
					info = systemapp.get(position - 2 - userapp.size());
				}
				dismissPopupwindow();
				// �����Ŀ������������
				View convertView = View.inflate(getApplicationContext(),
						R.layout.item_app_popupwindow, null);
				// -2�ǰ�������
				popupwindow = new PopupWindow(convertView, -2, -2);
				// popupwindowĬ����û�б����ģ�������Ҫ����
				popupwindow.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));

				// ��ȡ�������Ŀview���󵽴���Ŀ��(���϶���)����location��x��y
				int[] location = new int[2];
				view.getLocationInWindow(location);
				popupwindow.showAtLocation(parent, Gravity.TOP + Gravity.LEFT,
						80, location[1]);
				// ����
				AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
				aa.setDuration(1000);
				ScaleAnimation sa = new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0.5f);
				sa.setDuration(1000);
				AnimationSet set = new AnimationSet(false);
				set.addAnimation(aa);
				set.addAnimation(sa);
				convertView.startAnimation(set);
				/**
				 * ��Ŀ�е����������еĹ��ܵĵ���¼�
				 * 
				 */
				LinearLayout ll_openapp = (LinearLayout) convertView
						.findViewById(R.id.ll_openapp);
				LinearLayout ll_uninstallapp = (LinearLayout) convertView
						.findViewById(R.id.ll_uninstallapp);
				LinearLayout ll_sharedapp = (LinearLayout) convertView
						.findViewById(R.id.ll_sharedapp);
				LinearLayout ll_infoapp = (LinearLayout) convertView
						.findViewById(R.id.ll_infoapp);

				ll_openapp.setOnClickListener(AppManagerActivity.this);
				ll_uninstallapp.setOnClickListener(AppManagerActivity.this);
				ll_sharedapp.setOnClickListener(AppManagerActivity.this);
				ll_infoapp.setOnClickListener(AppManagerActivity.this);
			}
		});
	}

	/**
	 * ȡ����ǰ��ʾ���������壬��ֻ֤��һ��
	 */
	public void dismissPopupwindow() {
		if (popupwindow != null && popupwindow.isShowing()) {
			popupwindow.dismiss();
			popupwindow = null;
		}
	}

	/**
	 * �������
	 */
	private void fillData() {
		new Thread() {
			public void run() {
				infos = AppManagerInfos
						.getAppManagerInfos(AppManagerActivity.this);
				for (AppInfo info : infos) {
					if (info.isUserapp()) {
						// �û�����
						userapp.add(info);
					} else {
						systemapp.add(info);
						// ϵͳ����
					}
				}
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	private class MyAppManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userapp.size() + systemapp.size() + 2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view;
			HoldView holder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (HoldView) view.getTag();
			} else {
				holder = new HoldView();
				view = View.inflate(AppManagerActivity.this,
						R.layout.item_app_manager, null);
				holder.app_name = (TextView) view.findViewById(R.id.app_name);
				holder.app_location = (TextView) view
						.findViewById(R.id.app_location);
				holder.app_icon = (ImageView) view.findViewById(R.id.app_icom);
				holder.app_size = (TextView) view.findViewById(R.id.app_size);
				view.setTag(holder);
			}

			if (position == 0) {// ��ʾtextView�û�����
				TextView tv_user = new TextView(AppManagerActivity.this);
				tv_user.setTextSize(15);
				tv_user.setBackgroundColor(Color.GREEN);
				tv_user.setTextColor(Color.BLACK);
				tv_user.setText("�û�����" + userapp.size() + "��");
				return tv_user;
			} else if (position == userapp.size() + 1) {
				TextView tv_system = new TextView(AppManagerActivity.this);
				tv_system.setTextSize(15);
				tv_system.setBackgroundColor(Color.GREEN);
				tv_system.setTextColor(Color.BLACK);
				tv_system.setText("ϵͳ����" + systemapp.size() + "��");
				return tv_system;

			} else if (position < userapp.size() + 1) {
				// �û�����
				info = userapp.get(position - 1);
			} else {
				// ϵͳ����
				info = systemapp.get(position - 2 - userapp.size());
			}
			holder.app_name.setText(info.getAppname());
			holder.app_icon.setImageDrawable(info.getIcon());
			holder.app_size.setText(Formatter.formatFileSize(
					AppManagerActivity.this, info.getSize()) + "M");
			if (info.isInRom()) {
				holder.app_location.setText("�ֻ��ڴ�");
			} else {
				holder.app_location.setText("sd������");
			}
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

		private class HoldView {
			TextView app_name;
			TextView app_location;
			ImageView app_icon;
			TextView app_size;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(innerReceiver);
		dismissPopupwindow();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.ll_openapp :
				// System.out.println("�������"+info.getAppname());
				openApp();
				break;
			case R.id.ll_uninstallapp :
				// System.out.println("ж�����"+info.getAppname());
				uninstallapp();
				break;
			case R.id.ll_sharedapp :
				// System.out.println("�������"+info.getAppname());
				sharedApp();
				break;
			case R.id.ll_infoapp :
				// System.out.println("�����Ϣ"+info.getAppname());
				showApp();
				break;

			default :
				break;
		}
		dismissPopupwindow();
	}

	/**
	 * �鿴Ӧ�ó���ȫ����Ϣ
	 */
	private void showApp() {
		// �����ϲ�Ӧ�ó���Դ��
		/*
		 * <action android:name="android.settings.APPLICATION_DETAILS_SETTINGS"
		 * /> <category android:name="android.intent.category.DEFAULT" /> <data
		 * android:scheme="package" />
		 */
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + info.getPakageName()));
		startActivity(intent);
	}

	/**
	 * ж���������
	 */
	private void uninstallapp() {
		if (info.isUserapp()) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:" + info.getPakageName()));
			startActivity(intent);
		} else {
			ToastUtils.show(this, "Ӧ�ó�����ҪrootȨ�޲���ж��");
		}
	}

	/**
	 * �ڲ��࣬�㲥�����ߣ��������ж�ص��¼�
	 * 
	 * @author Administrator
	 * 
	 */
	private class InnerUninstallAppReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (info.isUserapp()) {
				userapp.remove(info);
			} else {
				systemapp.remove(info);
			}
			adapter.notifyDataSetChanged();
			dismissPopupwindow();
		}
	}

	/**
	 * ������Ĺ���
	 */
	public void openApp() {
		PackageManager pm = getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(info.getPakageName());
		if (intent != null) {
			startActivity(intent);
		} else {
			ToastUtils.show(this, "����޷�������");
		}
	}

	/**
	 * һ������,����Ҫ���Ӧ�ó��򶼻ᱻ���ѡ�����
	 */
	public void sharedApp() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "��ʹ��������" + info.getAppname());
		startActivity(intent);
	}
}
