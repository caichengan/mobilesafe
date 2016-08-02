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
		// 初始化控件
		tv_shji_byte = (TextView) findViewById(R.id.tv_shji_byte);
		tv_sd_byte = (TextView) findViewById(R.id.tv_sd_byte);
		lv_listview = (ListView) findViewById(R.id.lv_listview);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		tv_biaoshi = (TextView) findViewById(R.id.tv_biaoshi);
		innerReceiver = new InnerUninstallAppReceiver();

		userapp = new ArrayList<AppInfo>();// 用户程序
		systemapp = new ArrayList<AppInfo>();// 系统程序

		// 注册广播接收者
		innerReceiver = new InnerUninstallAppReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(innerReceiver, filter);

		// 获取sd卡和手机内存可用空间
		File datafile = Environment.getDataDirectory();
		long datasize = datafile.getFreeSpace();
		File sdfile = Environment.getExternalStorageDirectory();
		long sdsize = sdfile.getFreeSpace();
		tv_shji_byte.setText("手机可用内存"
				+ Formatter.formatFileSize(this, datasize));
		tv_sd_byte.setText("sd卡可用内存" + Formatter.formatFileSize(this, sdsize));

		// 填充数据
		fillData();
		/**
		 * 给listview注册一个滚动监听器
		 */
		lv_listview.setOnScrollListener(new OnScrollListener() {
			/**
			 * 当状态发生改变时执行此方法
			 */
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			/**
			 * 当listview滚动时执行此方法
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (firstVisibleItem > userapp.size()) {
					tv_biaoshi.setText("系统程序" + systemapp.size() + "个");
				} else {
					tv_biaoshi.setText("用户程序" + userapp.size() + "个");
				}
				dismissPopupwindow();
			}
		});
		/**
		 * listview 条目的点击事件
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
					// 用户程序
					info = userapp.get(position - 1);
				} else {
					// 系统程序
					info = systemapp.get(position - 2 - userapp.size());
				}
				dismissPopupwindow();
				// 点击条目弹出悬浮窗体
				View convertView = View.inflate(getApplicationContext(),
						R.layout.item_app_popupwindow, null);
				// -2是包裹内容
				popupwindow = new PopupWindow(convertView, -2, -2);
				// popupwindow默认是没有背景的，动画需要背景
				popupwindow.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));

				// 获取点击的条目view对象到窗体的宽高(左上对齐)存在location中x、y
				int[] location = new int[2];
				view.getLocationInWindow(location);
				popupwindow.showAtLocation(parent, Gravity.TOP + Gravity.LEFT,
						80, location[1]);
				// 动画
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
				 * 条目中的悬浮窗体中的功能的点击事件
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
	 * 取消当前显示的悬浮窗体，保证只有一个
	 */
	public void dismissPopupwindow() {
		if (popupwindow != null && popupwindow.isShowing()) {
			popupwindow.dismiss();
			popupwindow = null;
		}
	}

	/**
	 * 填充数据
	 */
	private void fillData() {
		new Thread() {
			public void run() {
				infos = AppManagerInfos
						.getAppManagerInfos(AppManagerActivity.this);
				for (AppInfo info : infos) {
					if (info.isUserapp()) {
						// 用户程序
						userapp.add(info);
					} else {
						systemapp.add(info);
						// 系统程序
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

			if (position == 0) {// 显示textView用户程序
				TextView tv_user = new TextView(AppManagerActivity.this);
				tv_user.setTextSize(15);
				tv_user.setBackgroundColor(Color.GREEN);
				tv_user.setTextColor(Color.BLACK);
				tv_user.setText("用户程序" + userapp.size() + "个");
				return tv_user;
			} else if (position == userapp.size() + 1) {
				TextView tv_system = new TextView(AppManagerActivity.this);
				tv_system.setTextSize(15);
				tv_system.setBackgroundColor(Color.GREEN);
				tv_system.setTextColor(Color.BLACK);
				tv_system.setText("系统程序" + systemapp.size() + "个");
				return tv_system;

			} else if (position < userapp.size() + 1) {
				// 用户程序
				info = userapp.get(position - 1);
			} else {
				// 系统程序
				info = systemapp.get(position - 2 - userapp.size());
			}
			holder.app_name.setText(info.getAppname());
			holder.app_icon.setImageDrawable(info.getIcon());
			holder.app_size.setText(Formatter.formatFileSize(
					AppManagerActivity.this, info.getSize()) + "M");
			if (info.isInRom()) {
				holder.app_location.setText("手机内存");
			} else {
				holder.app_location.setText("sd卡储存");
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
				// System.out.println("启动软件"+info.getAppname());
				openApp();
				break;
			case R.id.ll_uninstallapp :
				// System.out.println("卸载软件"+info.getAppname());
				uninstallapp();
				break;
			case R.id.ll_sharedapp :
				// System.out.println("分享软件"+info.getAppname());
				sharedApp();
				break;
			case R.id.ll_infoapp :
				// System.out.println("软件信息"+info.getAppname());
				showApp();
				break;

			default :
				break;
		}
		dismissPopupwindow();
	}

	/**
	 * 查看应用程序全部信息
	 */
	private void showApp() {
		// 查找上层应用程序源码
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
	 * 卸载软件程序
	 */
	private void uninstallapp() {
		if (info.isUserapp()) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:" + info.getPakageName()));
			startActivity(intent);
		} else {
			ToastUtils.show(this, "应用程序需要root权限才能卸载");
		}
	}

	/**
	 * 内部类，广播接收者，监听软件卸载的事件
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
	 * 打开软件的功能
	 */
	public void openApp() {
		PackageManager pm = getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(info.getPakageName());
		if (intent != null) {
			startActivity(intent);
		} else {
			ToastUtils.show(this, "软件无法启动！");
		}
	}

	/**
	 * 一键分享,满足要求的应用程序都会被激活，选择分享
	 */
	public void sharedApp() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "请使用这款软件" + info.getAppname());
		startActivity(intent);
	}
}
