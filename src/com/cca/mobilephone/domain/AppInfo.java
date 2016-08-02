package com.cca.mobilephone.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {

	/**
	 * 应用程序的包名
	 */
	private String pakageName;
	
	/**
	 * 应用程序的图标
	 */
	private Drawable icon;
	/**
	 * 应用程序的名称
	 */
	private String appname;
	
	/**
	 * 是否是用户应用程序
	 */
	private boolean userapp;
	
	/**
	 * 是否装在手机内存
	 */
	private boolean inRom;
	/**
	 * 应用程序的大小
	 */
	private long size;
	/**
	 * apk的安装路径
	 * 
	 * @return
	 */
	private String path;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPakageName() {
		return pakageName;
	}
	public void setPakageName(String pakageName) {
		this.pakageName = pakageName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public boolean isUserapp() {
		return userapp;
	}
	public void setUserapp(boolean userapp) {
		this.userapp = userapp;
	}
	public boolean isInRom() {
		return inRom;
	}
	public void setInRom(boolean inRom) {
		this.inRom = inRom;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
}
