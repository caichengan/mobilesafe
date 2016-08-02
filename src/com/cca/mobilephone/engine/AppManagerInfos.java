package com.cca.mobilephone.engine;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;

import com.cca.mobilephone.domain.AppInfo;
/**
 * ��ȡ�ֻ��������Ѿ���װ�����
 * @author Administrator
 *
 */
public class AppManagerInfos {

	@SuppressWarnings("unused")
	public static List<AppInfo> getAppManagerInfos(Context context) {

		List<AppInfo> appinfos = new ArrayList<AppInfo>();
		PackageManager pm = context.getPackageManager();
		//����ֻ��а�װ�������������Ϣ�ļ���
		List<PackageInfo> infos = pm.getInstalledPackages(0);

		for (PackageInfo appInfo : infos) {
			AppInfo info = new AppInfo();
			// ��ð���
			String packagename = appInfo.packageName;
			info.setPakageName(packagename);
			// ���Ӧ������
			String appname = appInfo.applicationInfo.loadLabel(pm).toString();
			info.setAppname(appname);
			// ���Ӧ��ͼ��
			Drawable icon = appInfo.applicationInfo.loadIcon(pm);
			info.setIcon(icon);
			// ���Ӧ��app�ľ���·��
			String path = appInfo.applicationInfo.sourceDir;
			info.setPath(path);
			// ���Ӧ��app�Ĵ�С
			File file = new File(path);
			long size = file.length();
			String sizedata = Formatter.formatFileSize(context, size);
			
			info.setSize(size);
			int flag = appInfo.applicationInfo.flags;
			if ((flag & ApplicationInfo.FLAG_SYSTEM) == 0) {
				// �û�Ӧ��
				info.setUserapp(true);
			} else {
				// ϵͳӦ��
				info.setUserapp(false);
			}
			if ((flag & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
				// �����ֻ��ڴ�
				info.setInRom(true);
			} else {
				// ����sd�ڴ濨
				info.setInRom(false);
			}
			appinfos.add(info);
		}
	
		return appinfos;
	}
}
