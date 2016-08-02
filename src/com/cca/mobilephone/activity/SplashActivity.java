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
     * ��Ϣ�������
     * ���ظ���
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
      tv_version.setText("���԰棺"+versionName);
      rt_relative=(RelativeLayout) findViewById(R.id.splash_rt_donghua);
      //���ý��붯��͸��״
      AlphaAnimation aa=new AlphaAnimation(0.2f, 1.0f);
      aa.setDuration(2000);
      rt_relative.startAnimation(aa);
      System.out.println("oncreate���");
      
      SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
      //�õ�checkbox�Ƿ����ñ����
      boolean flag=sp.getBoolean("update", false);
      if(flag){
    	  checkVersion();
      }else{
    	  System.out.println("������");
    	  IntentUtils.startActivityDelayAndFinish(SplashActivity.this, MainActivity.class,2000);
  		
      }
      
      /**
       * �������ݿ⵽data/data/����/filesĿ¼��
       */
      copyDB("address.db");
      copyDB("commonnum.db");
      copyDB("antivirus.db");
     
      /**
       * �������ݿ�
       */
      udpateDb();
      /**
       * ������ݷ�ʽ
       */
      createShortCut();
      
      /**
       * ��Ϣ��֪ͨ
       */
      createNotification();
    }
	//��Ϣ��֪ͨ�����¼��ݵͰ汾
	private void createNotification() {
		//��ȡ֪ͨ������
		NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification=new Notification(R.drawable.ic_launcher, "�ƻ��ֻ���ʿ���ڱ�������ֻ���", System.currentTimeMillis());
		//����֪ͨ�ı�־
		notification.flags=Notification.FLAG_NO_CLEAR;
		//��ͼ��������
		Intent intent=new Intent();
		intent.setAction("com.cca.mobilephonesafe");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		Logger.w("tag", "-----------------------------");
		PendingIntent contentIntent= PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(this, "�ƻ��ֻ���ʿ", "���ڱ�������ֻ�", contentIntent);
		nm.notify(0, notification);
	}
	/**
	 *�������ͼ��
	 */
private void createShortCut() {
	SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
	boolean shortcut=sp.getBoolean("shortcut", false);
	if(!shortcut){
		//��ݷ�ʽ��ͼƬ
		
	//��ݷ�ʽ������
	//��ݷ�ʽ��ʲô����
	//���ͼ����ʵ����ʾ������ģ�����������Ǵ������ͼ��
	//�����淢����Ϣ
	Intent intent=new Intent(); //���͹㲥����ͼ
	intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
	//��������
	intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,"�ƻ���ʿ" );
	intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

	//��ݷ�ʽ������Ӧ����ͼ
	Intent shortcutIntent=new Intent();
	shortcutIntent.setAction("com.cca.mobilesafe.home");
	shortcutIntent.addCategory(Intent.CATEGORY_DEFAULT);
	intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
	//���ʹ�����ݷ�ʽ�Ĺ㲥
	sendBroadcast(intent);
	Editor edit=sp.edit();
	edit.putBoolean("shortcut", true);
	edit.commit();
	}
	
	}

/**
 * ���²������ݿ�
 */
	private void udpateDb() {
//���ӷ�����
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
						System.out.println("���µ����ݿ����");
					String name=obj.getString("name");
					String type=obj.getString("type");
					String md5=obj.getString("md5");
					String desc=obj.getString("desc");
					}else{
						
						System.out.println("�������ݿ�ĸ���");		
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	/**
	 * �����ʲ�Ŀ¼�µ����ݿ⵽Androidϵͳ��
	 */
	private void copyDB(final String name) {

		/*
		 * ���ݿ��ʱ���ܺ�ʱ
		 */
		new Thread(){
			public void run() {
				File file=new File(getFilesDir(),name);
				if(file.exists()&&file.length()>0){
				System.out.println("���ݿ��Ѿ����ع��������ڼ��أ�");
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
	 * ������°汾��Ϣ����
	 * 
	 */
	private void checkVersion() {
		IntentUtils.startActivityDelayAndFinish(SplashActivity.this, MainActivity.class,2000);
		
		/**
		 * 	��������ʧ�� TODO
		 * 
		 */
		/*
		new Thread(){
			public void run() {
				try {
					System.out.println("�߳�֮��--------֮��");
					//��ȡURL����
					URL url=new URL(getString(R.string.serviceurl));
					System.out.println("�����ַ��"+url);
					//���ӷ�����
					HttpURLConnection conn=(HttpURLConnection) url.openConnection();
					//��������ʽ
					conn.setRequestMethod("GET");
					//��������ʱʱ��
					conn.setConnectTimeout(5000);
					//��÷�����
					int code=conn.getResponseCode();
					System.out.println("code��ֵ-----"+code);
					if(code==HTTP_OK){
						System.out.println("ȡ���ݳɹ�------֮��");
						//��ȡ���ص�������
						InputStream is=conn.getInputStream();
						//��һ��������ת�����ַ���
						String json=StreamUtils.readStream(is);
						JSONObject jsonobj=new JSONObject(json);
						int serviceversion=jsonobj.getInt("version");
						String downLoadUrl=jsonobj.getString("downloadurl");
						String desc=jsonobj.getString("desc");
						//��ӡ�����֤�ܷ�ȡ������
						System.out.println("version:-----"+serviceversion);
						System.out.println("downLoad:----"+downLoadUrl);
						System.out.println("version:-----"+desc);
						
						if(serviceversion>AppInfoUtils.getAppInfoNumber(getApplication())){
							System.out.println("���°汾�ˣ����������°汾");
							//��Ϣ�������ݸ�����Ϣ
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
							System.out.println("�汾һ�£�����������");
							IntentUtils.startActivityDelayAndFinish(SplashActivity.this, MainActivity.class, 2000);
						}
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					ToastUtils.show(SplashActivity.this, "URL·������");
				} catch (IOException e) {
					e.printStackTrace();
					ToastUtils.show(SplashActivity.this, "�������");
				} 
				catch (JSONException e) {
					e.printStackTrace();
					ToastUtils.show(SplashActivity.this, "������������Ϣ����");
				}	
			};
		}.start();*/
		
		
	}
	/**
	 * չʾ������ʾ�Ի���
	 * @param msg
	 */
	public void showUpdateDialog(android.os.Message msg) {
		final UpdateVersion data=(UpdateVersion) msg.obj;

		//����һ�����¶Ի���
		 AlertDialog.Builder builder=new Builder(SplashActivity.this);
		builder.setTitle("�������£�");
		builder.setMessage(data.desc);
		builder.setPositiveButton("��������", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//��Դ��Ŀ�ϵ�����xUtils
				HttpUtils http=new HttpUtils();
				final File file=new File(Environment.getExternalStorageDirectory(),"xxx.apk");
				http.download(data.downLoadUrl, file.getAbsolutePath(), true, new RequestCallBack<File>(){
					//����ʧ��
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						
					}
					//���سɹ�
					@Override
					public void onSuccess(ResponseInfo arg0) {
						//���سɹ����滻��װģ�����
						ToastUtils.show(SplashActivity.this, "���سɹ�");
						Intent intent=new Intent();
						intent.setAction("android.intent.action.VIEW");
						intent.addCategory("android.intent.category.DEFAULT");
						intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
						startActivity(intent);
						
					}});
			}
		});
		builder.setNegativeButton("�´���˵", new OnClickListener() {
			
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
