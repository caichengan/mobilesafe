package com.cca.mobilephone.activity;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.text.format.Formatter;

public class TrafficActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		long mobileRx=TrafficStats.getMobileRxBytes();//��ȡ�ֻ����ص�������Ϣ
		long mobileTx=TrafficStats.getMobileTxBytes();//��ȡ�ֻ��ϴ���������Ϣ
		
		long TotalRx=TrafficStats.getTotalRxBytes();//��ȡȫ�����ص�������Ϣ
		long TotalTx	=TrafficStats.getTotalTxBytes();//��ȡȫ���ϴ���������Ϣ
		
		long intentRx=TrafficStats.getUidRxBytes(1004); //��ȡ��������ص�������Ϣ
		long intentTx=TrafficStats.getUidTxBytes(1004);//��ȡ������ϴ���������Ϣ
		
		System.out.println("�ֻ����ص�������Ϣ"+Formatter.formatFileSize(getApplicationContext(), mobileRx));
		System.out.println("�ֻ��ϴ���������Ϣ"+Formatter.formatFileSize(getApplicationContext(), mobileTx));
		System.out.println("wifi���ص�������Ϣ"+Formatter.formatFileSize(getApplicationContext(), TotalRx-mobileRx));
		System.out.println("wifi�ϴ���������Ϣ"+Formatter.formatFileSize(getApplicationContext(), TotalTx-mobileTx));
		System.out.println("�ֻ����ص�������Ϣ"+Formatter.formatFileSize(getApplicationContext(), intentRx));
		System.out.println("�ֻ����ص�������Ϣ"+Formatter.formatFileSize(getApplicationContext(), intentTx));
		
	}
}
