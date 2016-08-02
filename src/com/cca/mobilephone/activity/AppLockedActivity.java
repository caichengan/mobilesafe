package com.cca.mobilephone.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cca.mobilephone.R;
import com.cca.mobilephone.db.dao.AppClockDao;
import com.cca.mobilephone.domain.AppInfo;
import com.cca.mobilephone.engine.AppManagerInfos;
/**
 * 设置程序加锁的Activity
 * @author Administrator
 *
 */
public class AppLockedActivity extends Activity implements OnClickListener {

	private TextView app_unlock;
	private TextView app_locked;
	/**
	 * 两种线性布局
	 */
	private LinearLayout ll_applocked;
	private LinearLayout ll_appunlock;
	/**
	 * 两种listview
	 */
	private ListView lv_locked;
	private ListView lv_unlock;
	/**
	 * 正在加载的进度条
	 */
	private LinearLayout ll_loading;
	/**
	 * 所有程序的集合
	 */
	private List<AppInfo> infos;
	/**
	 * 显示未加锁程序的个数
	 */
	private TextView tv_unlock_count;
	/**
	 * 显示加锁程序的个数
	 */
	private TextView tv_locked_count;
	/**
	 * 加锁的数据库
	 */
	private AppClockDao dao;
	/**
	 * 加锁集合
	 */
	private List<AppInfo> lockedInfo;
	/**
	 * 未加锁集合
	 */
	private List<AppInfo> unlockInfo;
	/**
	 * 未加锁
	 */
	private MylockAdapter unlockadapter;
	/**
	 * 加锁适配器
	 */
	private MylockAdapter lockedadapter;
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			// 加载未加锁的适配器
			lv_unlock.setAdapter(unlockadapter);
			// 加载加锁的适配器
			lv_locked.setAdapter(lockedadapter);
			ll_loading.setVisibility(View.INVISIBLE);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_applocked);
		tv_unlock_count = (TextView) findViewById(R.id.tv_unlock_count);
		tv_locked_count = (TextView) findViewById(R.id.tv_locked_count);
		
		ll_loading=(LinearLayout) findViewById(R.id.ll_loading);
		dao = new AppClockDao(this);

		app_unlock = (TextView) findViewById(R.id.app_unlock);
		app_locked = (TextView) findViewById(R.id.app_locked);
		// 两种listview
		lv_locked = (ListView) findViewById(R.id.lv_locked);
		lv_unlock = (ListView) findViewById(R.id.lv_unlock);
		// 两个不同点击事件
		app_locked.setOnClickListener(this);
		app_unlock.setOnClickListener(this);
		// 两个线性布局
		ll_applocked = (LinearLayout) findViewById(R.id.ll_applocked);
		ll_appunlock = (LinearLayout) findViewById(R.id.ll_appunlock);

		lockedInfo = new ArrayList<AppInfo>();
		unlockInfo = new ArrayList<AppInfo>();
		unlockadapter = new MylockAdapter(true);
		lockedadapter = new MylockAdapter(false);
		new Thread(){
			public void run() {
		// 获得全部软件程序
		infos = AppManagerInfos.getAppManagerInfos(getApplicationContext());
		
		/**
		 * 遍历集合，区分加锁和未加锁
		 */
		for (AppInfo info : infos) {
			if (dao.find(info.getPakageName())) {
				lockedInfo.add(info);
			} else {
				unlockInfo.add(info);
			}
		}
				handler.sendEmptyMessage(0);
			};
		}.start();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.app_unlock:// 点击未加锁
			ll_appunlock.setVisibility(View.VISIBLE);
			ll_applocked.setVisibility(View.GONE);
			app_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
			app_locked.setBackgroundResource(R.drawable.tab_left_default);
			break;
		case R.id.app_locked:// 点击已加锁
			ll_appunlock.setVisibility(View.GONE);
			ll_applocked.setVisibility(View.VISIBLE);
			app_unlock.setBackgroundResource(R.drawable.tab_left_default);
			app_locked.setBackgroundResource(R.drawable.tab_left_pressed);
			break;
		}
	}
	/**
	 * listview的适配器
	 * 
	 * @author Administrator
	 *
	 */
	private class MylockAdapter extends BaseAdapter {
		/**
		 * isunlock是否加锁的标识符 true 为未加锁 、 false 已加锁
		 */
		boolean isunlock;
		public MylockAdapter(boolean isunlock) {
			this.isunlock = isunlock;
		}
		@Override
		public int getCount() {
			int count = 0;
			if (isunlock) {
				count = unlockInfo.size();
				tv_unlock_count.setText("未加锁软件：" + count);
			} else {
				count = lockedInfo.size();
				tv_locked_count.setText("加锁软件：" + count);
			}
			return count;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final View view;
			ViewHolder holder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.item_unlock_app, null);
				holder = new ViewHolder();
				holder.img_appicom = (ImageView) view
						.findViewById(R.id.img_appicom);
				holder.tv_appname = (TextView) view
						.findViewById(R.id.tv_appname);
				holder.img_locked = (ImageView) view
						.findViewById(R.id.img_locked);
				view.setTag(holder);
			}

			final AppInfo info;
			if (isunlock) {
				info = unlockInfo.get(position);
				holder.img_locked
						.setImageResource(R.drawable.list_button_lock_pressed);
			} else {
				info = lockedInfo.get(position);
				holder.img_locked
						.setImageResource(R.drawable.list_button_unlock_pressed);

			}

			holder.img_appicom.setImageDrawable(info.getIcon());
			holder.tv_appname.setText(info.getAppname());
			/**
			 * 点击加锁按钮移出条目，把数据加到数据库
			 */
			holder.img_locked.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (isunlock) {
						TranslateAnimation am = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, 
								Animation.RELATIVE_TO_SELF, 1.0f, 
								Animation.RELATIVE_TO_SELF, 0, 
												Animation.RELATIVE_TO_SELF, 0);		
						am.setDuration(500);
						view.startAnimation(am);
						am.setAnimationListener(new AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {
							}
							@Override
							public void onAnimationRepeat(Animation animation) {
							}
							@Override
							public void onAnimationEnd(Animation animation) {
								// 未加锁
								unlockInfo.remove(info);
								lockedInfo.add(info);
								dao.insert(info.getPakageName());
								// 通知界面更新
								// notifyDataSetChanged();
								unlockadapter.notifyDataSetChanged();
								lockedadapter.notifyDataSetChanged();
							}
						});
					} else {
						TranslateAnimation am = new TranslateAnimation(
								Animation.RELATIVE_TO_SELF, 0, 
								Animation.RELATIVE_TO_SELF, -1.0f, 
								Animation.RELATIVE_TO_SELF, 0, 
												Animation.RELATIVE_TO_SELF, 0);		
						am.setDuration(500);
						view.startAnimation(am);
						am.setAnimationListener(new AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {
							}
							@Override
							public void onAnimationRepeat(Animation animation) {
							}
							@Override
							public void onAnimationEnd(Animation animation) {
								// 已经加锁
								lockedInfo.remove(info);
								unlockInfo.add(info);
								dao.delete(info.getPakageName());
								// 通知界面更新
								// notifyDataSetChanged();
								unlockadapter.notifyDataSetChanged();
								lockedadapter.notifyDataSetChanged();
							}
						});
					}
				}
			});
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
	}
	static class ViewHolder {
		TextView tv_appname;
		ImageView img_appicom;
		ImageView img_locked;
	}
}
