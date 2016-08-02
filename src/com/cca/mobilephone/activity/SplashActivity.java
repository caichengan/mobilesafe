package com.cca.mobilephone.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.AppInfoUtils;
import com.cca.mobilephone.Utils.IntentUtils;
import com.cca.mobilephone.Utils.Logger;
import com.cca.mobilephone.Utils.StreamUtils;
import com.cca.mobilephone.Utils.ToastUtils;
import com.cca.mobilephone.db.dao.AntiVriusDao;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;




public class SplashActivity extends Activity {

    protected static final int NEED_UDPATE = 1;
	protected static final int HTTP_OK = 200;
	private TextView tv_version;
    private RelativeLayout rt_relative;
    /**
     * 信息处理机制
     * 下载更新
     */
    private Handler handler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch(msg.what){
    		case NEED_UDPATE:
    			showUpdateDialog(msg);
    			break;
    			default:
    				break;
    		}
    	}
    };
   
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
      tv_version = (TextView) findViewById(R.id.tv_splash_versoninfo);
      String versionName=AppInfoUtils.getAppInfoName(this);  
      tv_version.setText("测试版："+versionName);
      rt_relative=(RelativeLayout) findViewById(R.id.splash_rt_donghua);
      //设置进入动画透明状
      AlphaAnimation aa=new AlphaAnimation(0.2f, 1.0f);
      aa.setDuration(2000);
      rt_relative.startAnimation(aa);
      System.out.println("oncreate活动中");
      
      SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
      //拿到checkbox是否设置被点击
      boolean flag=sp.getBoolean("update", false);
      if(flag){
    	  checkVersion();
      }else{
    	  System.out.println("不更新");
    	  IntentUtils.startActivityDelayAndFinish(SplashActivity.this, MainActivity.class,2000);
  		
      }
      
      /**
       * 拷贝数据库到data/data/包名/files目录下
       */
      copyDB("address.db");
      copyDB("commonnum.db");
      copyDB("antivirus.db");
     
      /**
       * 更新数据库
       */
      udpateDb();
      /**
       * 创建快捷方式
       */
      createShortCut();
      
      /**
       * 消息的通知
       */
      createNotification();
    }
	//消息的通知、先下兼容低版本
	private void createNotification() {
		//获取通知管理者
		NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification=new Notification(R.drawable.ic_launcher, "破荒手机卫士正在保护你的手机！", System.currentTimeMillis());
		//设置通知的标志
		notification.flags=Notification.FLAG_NO_CLEAR;
		//意图打开主界面
		Intent intent=new Intent();
		intent.setAction("com.cca.mobilephonesafe");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		Logger.w("tag", "-----------------------------");
		PendingIntent contentIntent= PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(this, "破荒手机卫士", "正在保护你的手机", contentIntent);
		nm.notify(0, notification);
	}
	/**
	 *创建快捷图标
	 */
private void createShortCut() {
	SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
	boolean shortcut=sp.getBoolean("shortcut", false);
	if(!shortcut){
		//快捷方式的图片
		
	//快捷方式的名称
	//快捷方式干什么事情
	//快捷图标其实是显示在桌面的，让桌面帮我们创建快捷图标
	//给桌面发送消息
	Intent intent=new Intent(); //发送广播的意图
	intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
	//设置数据
	intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,"破荒卫士" );
	intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

	//快捷方式开启对应的意图
	Intent shortcutIntent=new Intent();
	shortcutIntent.setAction("com.cca.mobilesafe.home");
	shortcutIntent.addCategory(Intent.CATEGORY_DEFAULT);
	intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
	//发送创建快捷方式的广播
	sendBroadcast(intent);
	Editor edit=sp.edit();
	edit.putBoolean("shortcut", true);
	edit.commit();
	}
	
	}

/**
 * 更新病毒数据库
 */
	private void udpateDb() {
//连接服务器
		final String path="http://10.207.118.231:8888/udpatedb.txt";
		new Thread(){
			public void run() {
				try {
					URL url=new URL(path);
					HttpURLConnection conn=(HttpURLConnection) url.openConnection();
					InputStream is=conn.getInputStream();
					String jsonstr=StreamUtils.readStream(is);
					JSONObject obj=new JSONObject(jsonstr);
				
					int serversion=obj.getInt("version");
					int oldversion=AntiVriusDao.getVersion();
					if(serversion>oldversion){
						System.out.println("有新的数据库更新");
					String name=obj.getString("name");
					String type=obj.getString("type");
					String md5=obj.getString("md5");
					String desc=obj.getString("desc");
					}else{
						
						System.out.println("无需数据库的更新");		
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	/**
	 * 拷贝资产目录下的数据库到Android系统下
	 */
	private void copyDB(final String name) {

		/*
		 * 数据库多时可能耗时
		 */
		new Thread(){
			public void run() {
				File file=new File(getFilesDir(),name);
				if(file.exists()&&file.length()>0){
				System.out.println("数据库已经加载过，无需在加载！");
				}else{
					
				try {
					InputStream is=getAssets().open(name);
					FileOutputStream fos=new FileOutputStream(file);
					byte[] buffer=new byte[1024];
					int len=-1;
					while((len=is.read(buffer))!=-1){
						fos.write(buffer, 0, len);
					}
					is.close();
					fos.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				}
			};
		}.start();
	}
	
	

	/**
	 * 检查最新版本信息下载
	 * 
	 */
	private void checkVersion() {
		IntentUtils.startActivityDelayAndFinish(SplashActivity.this, MainActivity.class,2000);
		
		/**
		 * 	网络连接失败 TODO
		 * 
		 */
		/*
		new Thread(){
			public void run() {
				try {
					System.out.println("线程之中--------之中");
					//获取URL对象
					URL url=new URL(getString(R.string.serviceurl));
					System.out.println("网络地址："+url);
					//连接服务器
					HttpURLConnection conn=(HttpURLConnection) url.openConnection();
					//设置请求方式
					conn.setRequestMethod("GET");
					//设置请求超时时间
					conn.setConnectTimeout(5000);
					//获得返回码
					int code=conn.getResponseCode();
					System.out.println("code的值-----"+code);
					if(code==HTTP_OK){
						System.out.println("取数据成功------之中");
						//获取返回的输入流
						InputStream is=conn.getInputStream();
						//把一个输入流转换成字符串
						String json=StreamUtils.readStream(is);
						JSONObject jsonobj=new JSONObject(json);
						int serviceversion=jsonobj.getInt("version");
						String downLoadUrl=jsonobj.getString("downloadurl");
						String desc=jsonobj.getString("desc");
						//打印输出验证能否取到数据
						System.out.println("version:-----"+serviceversion);
						System.out.println("downLoad:----"+downLoadUrl);
						System.out.println("version:-----"+desc);
						
						if(serviceversion>AppInfoUtils.getAppInfoNumber(getApplication())){
							System.out.println("有新版本了，进入下载新版本");
							//消息处理，传递更新信息
							UpdateVersion data=new UpdateVersion();
							data.serviceversion=serviceversion;
							data.downLoadUrl=downLoadUrl;
							data.desc=desc;
							Message msg=Message.obtain();
							msg.obj=data;
							msg.what=NEED_UDPATE;
							handler.sendMessageDelayed(msg, 2000);
							//handler.sendMessage(msg);
							
							
						}else{
							System.out.println("版本一致，进入主界面");
							IntentUtils.startActivityDelayAndFinish(SplashActivity.this, MainActivity.class, 2000);
						}
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					ToastUtils.show(SplashActivity.this, "URL路径错误");
				} catch (IOException e) {
					e.printStackTrace();
					ToastUtils.show(SplashActivity.this, "网络错误");
				} 
				catch (JSONException e) {
					e.printStackTrace();
					ToastUtils.show(SplashActivity.this, "服务器配置信息错误");
				}	
			};
		}.start();*/
		
		
	}
	/**
	 * 展示下载提示对话框
	 * @param msg
	 */
	public void showUpdateDialog(android.os.Message msg) {
		final UpdateVersion data=(UpdateVersion) msg.obj;

		//弹出一个更新对话框
		 AlertDialog.Builder builder=new Builder(SplashActivity.this);
		builder.setTitle("升级更新！");
		builder.setMessage(data.desc);
		builder.setPositiveButton("立刻升级", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//开源项目断点下载xUtils
				HttpUtils http=new HttpUtils();
				final File file=new File(Environment.getExternalStorageDirectory(),"xxx.apk");
				http.download(data.downLoadUrl, file.getAbsolutePath(), true, new RequestCallBack<File>(){
					//下载失败
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						
					}
					//下载成功
					@Override
					public void onSuccess(ResponseInfo arg0) {
						//下载成功，替换安装模板代码
						ToastUtils.show(SplashActivity.this, "下载成功");
						Intent intent=new Intent();
						intent.setAction("android.intent.action.VIEW");
						intent.addCategory("android.intent.category.DEFAULT");
						intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
						startActivity(intent);
						
					}});
			}
		});
		builder.setNegativeButton("下次再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				IntentUtils.startActivityAndFinish(SplashActivity.this,MainActivity.class);
				
			}
		});
		
		builder.show();
	}
	class UpdateVersion{
		int serviceversion;
		String downLoadUrl;
		String desc;
	}
}
