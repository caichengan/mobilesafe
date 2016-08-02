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
 * 获取手机中所有已经安装的软件
 * @author Administrator
 *
 */
public class AppManagerInfos {

	@SuppressWarnings("unused")
	public static List<AppInfo> getAppManagerInfos(Context context) {

		List<AppInfo> appinfos = new ArrayList<AppInfo>();
		PackageManager pm = context.getPackageManager();
		//获得手机中安装的软件的所有信息的集合
		List<PackageInfo> infos = pm.getInstalledPackages(0);

		for (PackageInfo appInfo : infos) {
			AppInfo info = new AppInfo();
			// 获得包名
			String packagename = appInfo.packageName;
			info.setPakageName(packagename);
			// 获得应用名称
			String appname = appInfo.applicationInfo.loadLabel(pm).toString();
			info.setAppname(appname);
			// 获得应用图标
			Drawable icon = appInfo.applicationInfo.loadIcon(pm);
			info.setIcon(icon);
			// 获得应用app的绝对路径
			String path = appInfo.applicationInfo.sourceDir;
			info.setPath(path);
			// 获得应用app的大小
			File file = new File(path);
			long size = file.length();
			String sizedata = Formatter.formatFileSize(context, size);
			
			info.setSize(size);
			int flag = appInfo.applicationInfo.flags;
			if ((flag & ApplicationInfo.FLAG_SYSTEM) == 0) {
				// 用户应用
				info.setUserapp(true);
			} else {
				// 系统应用
				info.setUserapp(false);
			}
			if ((flag & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
				// 存在手机内存
				info.setInRom(true);
			} else {
				// 存在sd内存卡
				info.setInRom(false);
			}
			appinfos.add(info);
		}
	
		return appinfos;
	}
}
