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
		 * ��ת����
		 */
		RotateAnimation ra=new RotateAnimation(0, 360, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(2000);
		ra.setRepeatCount(Animation.INFINITE);
		img_rotate.startAnimation(ra);
		//ɨ���ֻ�Ӧ�ó���
		scanVirus();
	}
	/**
	 * ɨ���ֻ�Ӧ�ó��򣬲����ֻ���������
	 */
	private void scanVirus() {
		new Thread(){
			public void run() {
				/**
				 * �����ֻ�Ӧ�ó������Ϣ����ѯ�����������ڲ������ݿ����Ƿ����
				 */
				PackageManager pm=getPackageManager();
				List<PackageInfo> pakageinfos=pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES+PackageManager.GET_SIGNATURES);
				verits_pb.setMax(pakageinfos.size());
				int progress=0;
				for(PackageInfo info:pakageinfos){
					try {
					
						/**
						 * ��ȡӦ�ó����ǩ����Ϣ��ʹ��MD5���ܣ�Ҫ���ϱ�־λPackageManager.GET_SIGNATURES ��ϵͳĬ�ϲ����� TODO
						 */
						System.out.println("������"+info.applicationInfo.loadLabel(pm));
						System.out.println("ǩ����"+MD5Utils.encode(info.signatures[0].toCharsString()));
						
						/**
						 * ��ȡ�����У����
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
						 * ����md5�Ƿ���ڲ������ݿ���
						 */
						final String desc=AntiVriusDao.isVriusdb(md5);
						final String appname=(String) info.applicationInfo.loadLabel(pm);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								TextView tv=new TextView(AntiVirusActivity.this);
								if(desc!=null){
									//���ֲ���
									tv.setTextColor(Color.RED);
									tv.setText(appname+"���ֲ���");
								}else{
									//ɨ�谲ȫ
									tv.setTextColor(Color.GREEN);
									tv.setText(appname+"ɨ�谲ȫ");
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
