package com.cca.mobilephone.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {

	/**
	 * Ӧ�ó���İ���
	 */
	private String pakageName;
	
	/**
	 * Ӧ�ó����ͼ��
	 */
	private Drawable icon;
	/**
	 * Ӧ�ó��������
	 */
	private String appname;
	
	/**
	 * �Ƿ����û�Ӧ�ó���
	 */
	private boolean userapp;
	
	/**
	 * �Ƿ�װ���ֻ��ڴ�
	 */
	private boolean inRom;
	/**
	 * Ӧ�ó���Ĵ�С
	 */
	private long size;
	/**
	 * apk�İ�װ·��
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
