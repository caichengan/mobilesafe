package com.cca.mobilephone.domain;

import android.graphics.drawable.Drawable;

/**
 * 进程包含的信息
 * @author Administrator
 *
 */
public class ProcessInfo {

	private String packageName;
	private String processName;
	private Drawable icon;
	private long menSize;
	private boolean userProcess;
	private boolean ischecked;
	
	public boolean isIschecked() {
		return ischecked;
	}
	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public long getMenSize() {
		return menSize;
	}
	public void setMenSize(long menSize) {
		this.menSize = menSize;
	}
	public boolean isUserProcess() {
		return userProcess;
	}
	public void setUserProcess(boolean userProcess) {
		this.userProcess = userProcess;
	}
	
}
