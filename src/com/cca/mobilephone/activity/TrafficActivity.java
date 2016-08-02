package com.cca.mobilephone.activity;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.text.format.Formatter;

public class TrafficActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		long mobileRx=TrafficStats.getMobileRxBytes();//获取手机下载的流量信息
		long mobileTx=TrafficStats.getMobileTxBytes();//获取手机上传的流量信息
		
		long TotalRx=TrafficStats.getTotalRxBytes();//获取全部下载的流量信息
		long TotalTx	=TrafficStats.getTotalTxBytes();//获取全部上传的流量信息
		
		long intentRx=TrafficStats.getUidRxBytes(1004); //获取浏览器下载的流量信息
		long intentTx=TrafficStats.getUidTxBytes(1004);//获取浏览器上传的流量信息
		
		System.out.println("手机下载的流量信息"+Formatter.formatFileSize(getApplicationContext(), mobileRx));
		System.out.println("手机上传的流量信息"+Formatter.formatFileSize(getApplicationContext(), mobileTx));
		System.out.println("wifi下载的流量信息"+Formatter.formatFileSize(getApplicationContext(), TotalRx-mobileRx));
		System.out.println("wifi上传的流量信息"+Formatter.formatFileSize(getApplicationContext(), TotalTx-mobileTx));
		System.out.println("手机下载的流量信息"+Formatter.formatFileSize(getApplicationContext(), intentRx));
		System.out.println("手机下载的流量信息"+Formatter.formatFileSize(getApplicationContext(), intentTx));
		
	}
}
