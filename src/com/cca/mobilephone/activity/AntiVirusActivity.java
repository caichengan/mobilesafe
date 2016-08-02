package com.cca.mobilephone.activity;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.MD5Utils;
import com.cca.mobilephone.db.dao.AntiVriusDao;

public class AntiVirusActivity extends Activity {
	private ImageView img_rotate;
	private LinearLayout ll_add_text;
	private ProgressBar verits_pb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_antivures);
		img_rotate=(ImageView) findViewById(R.id.img_rotate);
		ll_add_text=(LinearLayout) findViewById(R.id.ll_add_text);
		verits_pb=(ProgressBar) findViewById(R.id.verits_pb);
		/*
		 * 旋转动画
		 */
		RotateAnimation ra=new RotateAnimation(0, 360, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(2000);
		ra.setRepeatCount(Animation.INFINITE);
		img_rotate.startAnimation(ra);
		//扫描手机应用程序
		scanVirus();
	}
	/**
	 * 扫描手机应用程序，查找手机病毒程序
	 */
	private void scanVirus() {
		new Thread(){
			public void run() {
				/**
				 * 遍历手机应用程序的信息，查询他的特征码在病毒数据库中是否存在
				 */
				PackageManager pm=getPackageManager();
				List<PackageInfo> pakageinfos=pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES+PackageManager.GET_SIGNATURES);
				verits_pb.setMax(pakageinfos.size());
				int progress=0;
				for(PackageInfo info:pakageinfos){
					try {
					
						/**
						 * 获取应用程序的签名信息、使用MD5加密，要加上标志位PackageManager.GET_SIGNATURES ，系统默认不解析 TODO
						 */
						System.out.println("程序名"+info.applicationInfo.loadLabel(pm));
						System.out.println("签名："+MD5Utils.encode(info.signatures[0].toCharsString()));
						
						/**
						 * 获取程序的校验码
						 */
						String apkpath=info.applicationInfo.sourceDir;
						File file=new File(apkpath);
						MessageDigest digest=MessageDigest.getInstance("md5");
						FileInputStream fis=new FileInputStream(file);
						byte[] buffer=new byte[1024];
						int len=0;
						while((len=fis.read(buffer))!=-1){
							digest.update(buffer, 0, len);
						}
						byte [] result=digest.digest();
						StringBuffer sb=new StringBuffer();
						for(byte b:result){
							String str=Integer.toHexString(b&0xff);
							if(str.length()==1){
								sb.append("0");
							}
							sb.append(str);
						}
						fis.close();
						progress++;
						verits_pb.setProgress(progress);
						String md5=sb.toString();
						/**
						 * 查找md5是否存在病毒数据库中
						 */
						final String desc=AntiVriusDao.isVriusdb(md5);
						final String appname=(String) info.applicationInfo.loadLabel(pm);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								TextView tv=new TextView(AntiVirusActivity.this);
								if(desc!=null){
									//发现病毒
									tv.setTextColor(Color.RED);
									tv.setText(appname+"发现病毒");
								}else{
									//扫描安全
									tv.setTextColor(Color.GREEN);
									tv.setText(appname+"扫描安全");
								}
								ll_add_text.addView(tv, 0);
							}
						});
						Thread.sleep(50);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
}
