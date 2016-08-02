package com.cca.mobilephone.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.IntentUtils;
import com.cca.mobilephone.Utils.ProcessInfoUtils;
import com.cca.mobilephone.Utils.ToastUtils;
import com.cca.mobilephone.domain.ProcessInfo;
import com.cca.mobilephone.engine.TaskInfoProvifer;

public class TaskManagerActivity extends Activity {

	private TextView tv_process_byte;
	private TextView tv_usabletotal_byte;
	private LinearLayout ll_loading;
	private TextView tv_process_biaoshi;
	private ListView lv_process_listview;
private long avialRam;
private long totalRam;
private int RunningProcessCount;
	/**
	 * 用户进程集合
	 */
	private List<ProcessInfo>userprocess;
	/**
	 * 系统进程集合
	 */
	private List<ProcessInfo>systemprocess;
	
	private MyProcessAdapter adapter;
	/**
	 * 定义handler机制更新界面
	 */
	private  Handler handler=new Handler(){

		public void handleMessage(android.os.Message msg) {
			adapter = new MyProcessAdapter();
			lv_process_listview.setAdapter(adapter);
			ll_loading.setVisibility(View.INVISIBLE);
			
		};
	} ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_task_manager);
		tv_process_byte=(TextView) findViewById(R.id.tv_process_byte);
		tv_usabletotal_byte=(TextView) findViewById(R.id.tv_usabletotal_byte);
		//获取进程的信息
		 RunningProcessCount=ProcessInfoUtils.getRunningProcessCount(this);
		 avialRam=ProcessInfoUtils.getAvialRam(this);
		 totalRam=ProcessInfoUtils.getTotalRam(this);
		tv_process_byte.setText("用户进程"+RunningProcessCount+"个");
		tv_usabletotal_byte.setText("可用空间/总空间"+Formatter.formatFileSize(this, avialRam)+"/"+
														Formatter.formatFileSize(this, totalRam));
		
		ll_loading=(LinearLayout) findViewById(R.id.ll_loading);
		lv_process_listview=(ListView) findViewById(R.id.lv_process_listview);
		tv_process_biaoshi=(TextView) findViewById(R.id.tv_process_biaoshi);
		
		
		//填充数据
		fillData();
		/**
		 * 给listview注册一个滚动监听器
		 */
		lv_process_listview.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(userprocess!=null&& systemprocess!=null){
					if(firstVisibleItem<userprocess.size()){
						tv_process_biaoshi.setText("用户进程" + userprocess.size() + "个");
						
					}else{
						tv_process_biaoshi.setText("系统进程" + systemprocess.size() + "个");
						
					}
				}
			}
		});
		
		/**
		 * 给listview注册一个点击事件
		 */
		lv_process_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 ProcessInfo proinfo;
				if(position==0){
					return ;
				}else if(position==userprocess.size()+1){
					return ;
				}else if(position<=userprocess.size()){
					//用户进程
					proinfo=userprocess.get(position-1);
							
				}else{
					//系统进程
					proinfo=systemprocess.get(position-1-userprocess.size()-1);
				}
				
				//包名相同不响应点击事件
				if(proinfo.getPackageName().equals(getPackageName())){
					return ;
				}
				
				if(proinfo.isIschecked()){
					proinfo.setIschecked(false);
				}else{
					proinfo.setIschecked(true);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
	/**
	 * 填充数据，获取用户系统进程
	 */
	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
	new Thread(){
			public void run() {
				List<ProcessInfo> processinfo=TaskInfoProvifer.getRunningProcessInfo(getApplicationContext());
				userprocess=new ArrayList<ProcessInfo>();
				systemprocess=new ArrayList<ProcessInfo>();
				
				for(ProcessInfo process:processinfo){
						if(process.isUserProcess()){
							//用户程序
							userprocess.add(process);
						}else{
							//系统程序
							systemprocess.add(process);
						}
					
				}
				//通知界面更新
				handler.sendEmptyMessage(0);
				
			};
		}.start();
	}
	/**
	 * 进程的适配器
	 * @author Administrator
	 *
	 */
	private class MyProcessAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(getSharedPreferences("config", MODE_PRIVATE).getBoolean("showcheck", true)){
				return userprocess.size()+systemprocess.size()+2;
			}else{
				return userprocess.size()+1;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 ProcessInfo proinfo;
			if(position==0){
				TextView tv=new TextView(getApplicationContext());
				tv.setTextSize(14);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("用户进程"+userprocess.size()+"个");
				return tv;
			}else if(position==userprocess.size()+1){
				TextView tv=new TextView(getApplicationContext());
				tv.setTextSize(14);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("系统进程"+systemprocess.size()+"个");
				return tv;
			}else if(position<=userprocess.size()){
				//用户进程
				proinfo=userprocess.get(position-1);
						
			}else{
				//系统进程
				proinfo=systemprocess.get(position-1-userprocess.size()-1);
			}
			
			View view;
			ViewHolder holder;
			if(convertView!=null && convertView instanceof RelativeLayout){
				view=convertView;
				holder=(ViewHolder) view.getTag();
				
			}else{
				view=View.inflate(getApplicationContext(),R.layout.item_task_process, null);
				holder=new ViewHolder();
				holder.app_name=(TextView) view.findViewById(R.id.app_name);
				holder.app_icon=(ImageView) view.findViewById(R.id.app_icom);
				holder.app_mensize=(TextView) view.findViewById(R.id.app_mensize);
				holder.ch_selected=(CheckBox) view.findViewById(R.id.ch_selected);
				view.setTag(holder);
			}
			holder.app_name.setText(proinfo.getProcessName());
			holder.app_icon.setImageDrawable(proinfo.getIcon());
			holder.app_mensize.setText(Formatter.formatFileSize(getApplicationContext(),proinfo.getMenSize()));
			holder.ch_selected.setChecked(proinfo.isIschecked());
		
			//自身不可清除
			if(proinfo.getPackageName().equals(getPackageName())){
				holder.ch_selected.setVisibility(View.GONE);
			}else{
				holder.ch_selected.setVisibility(View.VISIBLE);
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

		
	}
	static class ViewHolder{
		ImageView app_icon;
		TextView app_name;
		TextView app_mensize;
		CheckBox ch_selected;
		
	}
	/**
	 * 全选功能
	 */
	public void btn_selectAll(View view){
		for(ProcessInfo process:userprocess){
			if(process.getPackageName().equals(getPackageName())){
				continue;
			}
				process.setIschecked(true);
			
		}
		for(ProcessInfo process:systemprocess){
				process.setIschecked(true);
				
		}
		adapter.notifyDataSetChanged();
	}
	/**
	 * 反选
	 */
	public void btn_selectOpposition(View view){
		
		for(ProcessInfo process:userprocess){
			if(process.getPackageName().equals(getPackageName())){
				continue;
			}
			if(process.isIschecked()){
				process.setIschecked(false);
			}else{
				process.setIschecked(true);
			}
				
			
		}
		for(ProcessInfo process:systemprocess){
			if(process.getPackageName()!=getPackageName()){
				if(process.isIschecked()){
					process.setIschecked(false);
				}else{
					process.setIschecked(true);
				}
				
			}
		}
		adapter.notifyDataSetChanged();
	}
	/**
	 * 杀死选中的进程
	 */
	public void btn_selectClear(View view){
		int count=0;
		long memsize=0;
		List<ProcessInfo>killProcess=new ArrayList<ProcessInfo>();
		ActivityManager am=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for(ProcessInfo process:userprocess){
			if(process.getPackageName()!=getPackageName()){
				if(process.isIschecked()){
				
					am.killBackgroundProcesses(process.getPackageName());
					count++;
					memsize+=process.getMenSize();
					killProcess.add(process);
				}
			}
		}
		for(ProcessInfo process:systemprocess){
			if(process.getPackageName()!=getPackageName()){
				if(process.isIschecked()){
				
					am.killBackgroundProcesses(process.getPackageName());
					count++;
					memsize+=process.getMenSize();
					killProcess.add(process);
					
				}
			}
		}
		for(ProcessInfo info:killProcess){
			if(info.isUserProcess()){
				userprocess.remove(info);
			}else{
				systemprocess.remove(info);
			}
			
		}
		ToastUtils.show(TaskManagerActivity.this, "清理了"+count+"个进程"+",释放了"+memsize);
		
		adapter.notifyDataSetChanged();
		RunningProcessCount-=count;
		avialRam+=memsize;
		tv_process_byte.setText("用户进程"+RunningProcessCount+"个");
		tv_usabletotal_byte.setText("可用空间/总空间"+Formatter.formatFileSize(this, avialRam)+"/"+
														Formatter.formatFileSize(this, totalRam));
		
		
	}
	/**
	 * 进程设置界面
	 */
	public void btn_selectsetting(View view){
		IntentUtils.startActivityInfo(TaskManagerActivity.this, TaskManagerSetting.class);
	}
	
	/**
	 * 重新回到界面就更新的逻辑
	 */
	@Override
	protected void onStart() {
		super.onStart();
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
		
	}
}
