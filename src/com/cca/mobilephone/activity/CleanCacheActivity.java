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
	 * ��Ϣ����
	 */
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case SCAN_STOP://ɨ�����
				
				Toast.makeText(getApplicationContext(), "ɨ�����", 0).show();
				fl_scan_states.setVisibility(View.GONE);
				if(cache.size()>0){
				//����������
					adapter=new MyAdapter();
				lv_scan_listview.setAdapter(adapter);
				}else{
					 ToastUtils.show(CleanCacheActivity.this, "��ϲ�㣬����ֻ�100��");
				}
				break;
			case SEND_SCAN://����ɨ��
				
				
				String appname=(String) msg.obj;
				tv_scan_cache.setText("��������"+appname);
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clean_cache);
		//��ʼ������
		pb=(ProgressBar) findViewById(R.id.pb);
		tv_scan_cache=(TextView) findViewById(R.id.tv_scan_cache);
		fl_scan_states=(FrameLayout) findViewById(R.id.fl_scan_states);
		lv_scan_listview=(ListView) findViewById(R.id.lv_scan_listview);
		pm=getPackageManager();
		//ɨ�軺��
		scanCache();
		
	
	}
	/**
	 * ɨ���ֻ�Ӧ�÷ֱ��ȡ������Ϣ
	 */
	private void scanCache() {
		fl_scan_states.setVisibility(View.VISIBLE);
		cache=new ArrayList<CacheHolder>();
		//�����߳�ɨ����򻺴�
		new Thread(){
			public void run() {
				pb.setMax(100);
				
				 int progress=0;
				//1��ɨ��Ӧ�ó���ȫ���İ���
				List<PackageInfo>infos=pm.getInstalledPackages(0);
				for(PackageInfo info:infos){
					try {
						//��ȡÿ������İ���
						String packagename=info.packageName;
						//���÷����ȡָ���ķ�����
						Method method=PackageManager.class.getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
						
						method.invoke(pm,packagename,new MyObserver());
					} catch (Exception e) {
						e.printStackTrace();
					}
					//������������
					progress++;
					pb.setProgress(progress);
					try {
						sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//2��֪ͨ�������
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
				//��ɨ�赽�İ������ͻ����������
					Message  msg=Message.obtain();
					msg.what=SEND_SCAN;
					String appname=pm.getPackageInfo(pStats.packageName, 0).
							applicationInfo.loadLabel(pm).toString();
					msg.obj=appname;
					handler.sendMessage(msg);
					//�����л����С�ĳ������Ҫ���������
					if(pStats.cacheSize>0){
						CacheHolder	holder=new CacheHolder();
						holder. cachesize=pStats.cacheSize;//�����С
						holder. packName=pStats.packageName;//�����С
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
	 * listview��������
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
			//������ʷ��������Ż�listview
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
			holder.cachesize.setText("�����С"+Formatter.formatFileSize(getApplicationContext(), cacheholder.cachesize));
			holder.clearcache.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//��Ӧ�ó�����Ϣ
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
	           ToastUtils.show(CleanCacheActivity.this, "���״̬"+succeeded);
	         }
	     }
	 /**
	  * ����ȫ���Ļ���ռ�
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
