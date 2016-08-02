package com.cca.mobilephone.activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.ToastUtils;

public class CleanCacheActivity extends Activity {

	protected static final int SCAN_STOP = 1;
	public static final int SEND_SCAN = 2;
	private ProgressBar pb;
	private TextView tv_scan_cache;
	private FrameLayout fl_scan_states;
	private PackageManager pm;
	private ListView lv_scan_listview;
	private List<CacheHolder>cache;
	private MyAdapter adapter;
	/**
	 * 消息机制
	 */
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case SCAN_STOP://扫描结束
				
				Toast.makeText(getApplicationContext(), "扫描完毕", 0).show();
				fl_scan_states.setVisibility(View.GONE);
				if(cache.size()>0){
				//设置适配器
					adapter=new MyAdapter();
				lv_scan_listview.setAdapter(adapter);
				}else{
					 ToastUtils.show(CleanCacheActivity.this, "恭喜你，你的手机100分");
				}
				break;
			case SEND_SCAN://正在扫描
				
				
				String appname=(String) msg.obj;
				tv_scan_cache.setText("正在清理："+appname);
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clean_cache);
		//初始化数据
		pb=(ProgressBar) findViewById(R.id.pb);
		tv_scan_cache=(TextView) findViewById(R.id.tv_scan_cache);
		fl_scan_states=(FrameLayout) findViewById(R.id.fl_scan_states);
		lv_scan_listview=(ListView) findViewById(R.id.lv_scan_listview);
		pm=getPackageManager();
		//扫描缓存
		scanCache();
		
	
	}
	/**
	 * 扫描手机应用分别获取缓存信息
	 */
	private void scanCache() {
		fl_scan_states.setVisibility(View.VISIBLE);
		cache=new ArrayList<CacheHolder>();
		//开子线程扫描程序缓存
		new Thread(){
			public void run() {
				pb.setMax(100);
				
				 int progress=0;
				//1、扫描应用程序全部的包名
				List<PackageInfo>infos=pm.getInstalledPackages(0);
				for(PackageInfo info:infos){
					try {
						//获取每个程序的包名
						String packagename=info.packageName;
						//利用反射获取指定的方法名
						Method method=PackageManager.class.getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
						
						method.invoke(pm,packagename,new MyObserver());
					} catch (Exception e) {
						e.printStackTrace();
					}
					//进度条的设置
					progress++;
					pb.setProgress(progress);
					try {
						sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//2、通知界面更新
				Message msg=Message.obtain();
				msg.what=SCAN_STOP;
				handler.sendMessage(msg);
			};
		}.start();
	}
	private class MyObserver extends IPackageStatsObserver.Stub{
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
				try {
				//把扫描到的包名发送回主界面更新
					Message  msg=Message.obtain();
					msg.what=SEND_SCAN;
					String appname=pm.getPackageInfo(pStats.packageName, 0).
							applicationInfo.loadLabel(pm).toString();
					msg.obj=appname;
					handler.sendMessage(msg);
					//主有有缓存大小的程序才需要存进集合中
					if(pStats.cacheSize>0){
						CacheHolder	holder=new CacheHolder();
						holder. cachesize=pStats.cacheSize;//缓存大小
						holder. packName=pStats.packageName;//代码大小
						holder. icon=pm.getPackageInfo(holder. packName, 0).applicationInfo.loadIcon(pm);
						holder. appName=appname;
						cache.add(holder);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	private class CacheHolder{
		long cachesize;
		String packName;
		Drawable icon;
		String appName;
	}
	/**
	 * listview的适配器
	 * @author Administrator
	 *
	 */
	private class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return cache.size();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			//服用历史缓存对象，优化listview
			if(convertView!=null){
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}else{
				holder=new ViewHolder();
				view=View.inflate(getApplicationContext(), R.layout.item_cache_listview, null);
				holder.icon=(ImageView) view.findViewById(R.id.img_icon);
				holder.apname=(TextView) view.findViewById(R.id.tv_appname);
				holder.cachesize=(TextView) view.findViewById(R.id.tv_cachesize);
				holder.clearcache=(ImageView) view.findViewById(R.id.img_clear_button);
				view.setTag(holder);
			}
			final CacheHolder cacheholder=cache.get(position);
			holder.icon.setImageDrawable(cacheholder.icon);
			holder.apname.setText(cacheholder.appName);
			holder.cachesize.setText("缓存大小"+Formatter.formatFileSize(getApplicationContext(), cacheholder.cachesize));
			holder.clearcache.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//打开应用程序信息
					Intent intent =new Intent();
					intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
					intent.addCategory("android.intent.category.DEFAULT" );
					intent.setData(Uri.parse("package:"+cacheholder.packName));
					startActivity(intent);
					
				}
			});
			if(cacheholder.cachesize==0){
				cache.remove(cacheholder);
				adapter.notifyDataSetChanged();
			}
			return view;
		}
	}
	private class ViewHolder{
		ImageView icon;
		TextView apname;
		TextView cachesize;
		ImageView clearcache;
	}
	 class ClearCacheObserver extends IPackageDataObserver.Stub {
	        public void onRemoveCompleted(final String packageName, final boolean succeeded) {
	           ToastUtils.show(CleanCacheActivity.this, "清除状态"+succeeded);
	         }
	     }
	 /**
	  * 清理全部的缓存空间
	  * @param view
	  */
	 public void AllClearCache(View view){
		 Method[] methods=PackageManager.class.getMethods();
		 for(Method method:methods){
			 if("freeStorageAndNotify".equals(method.getName())){
				 try {
					method.invoke(pm, Long.MAX_VALUE*1024,new ClearCacheObserver());
				} catch (Exception e) {
					e.printStackTrace();
				}
				 scanCache();
				 return ;
			 }
		 }
	 }
}
