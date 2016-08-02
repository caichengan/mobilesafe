package com.cca.mobilephone.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cca.mobilephone.R;
import com.cca.mobilephone.Utils.ToastUtils;
import com.cca.mobilephone.db.dao.BlackNumberDao;
import com.cca.mobilephone.domain.BlackNumberInfo;

public class CallSmsSafe extends Activity {
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			fl_loading.setVisibility(View.INVISIBLE);
			if(adapter==null){
				
			adapter=new MyBlackAdapter();
			lv_black_number.setAdapter(adapter);
			}else{
				//֪ͨlistviewȥ��������
				adapter.notifyDataSetChanged();
			}
		
		};
	};

	private ListView lv_black_number;
	private BlackNumberDao dao;
	private List<BlackNumberInfo> infos;
	private  MyBlackAdapter adapter;
	private LinearLayout fl_loading;
	private int startIndex=0;
	private int maxCount=10;//ÿ�μ��ص�����
	private int pagper=1;
	/*
	 * ���ݿ��е�����Ŀ
	 */
	private int total=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_callsmssafe);
		fl_loading=(LinearLayout) findViewById(R.id.fl_loading);
		lv_black_number = (ListView) findViewById(R.id.lv_black_number);
		dao=new BlackNumberDao(this);
		 total=dao.getTotalCount();
		fillData();
		lv_black_number.setOnScrollListener(new OnScrollListener() {
			/**
			 * ״̬�����ı�ʱ���ô˷���
			 */
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch(scrollState){
				case OnScrollListener.SCROLL_STATE_IDLE://����ʱ
					/*
					 * �жϿ���ʱ���������һ���û��ɼ�����Ŀ
					 */
					int position=lv_black_number.getLastVisiblePosition();
					int size=infos.size();//��ü��ϵ��ܴ�С
					if(position==size-1){
						startIndex +=maxCount;
						if(startIndex>=total){
							ToastUtils.show(CallSmsSafe.this, "û�����ݿɼ����ˣ�");
							return ;
						}
						//�������
						fillData();
					}
					
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://����ʱ�ı�
					break;
				case OnScrollListener.SCROLL_STATE_FLING://����ʱ
					break;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		
	}
	
	/**
	 * �������
	 */
	public void fillData() {
		fl_loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run() {
				if(infos==null){
				
				//infos = dao.findPart(startIndex, maxCount);
				infos=dao.findPagper(pagper);
				//infos = dao.findAll();
				}else{
					infos.addAll(dao.findPart(startIndex, maxCount));
				}
				handler.sendEmptyMessage(0);
	
			};
		}.start();
	}
	/**
	 * �����Ӱ�ť��Ӻ�����
	 * @param view
	 */
	public void addBlackNumber(View view){
		AlertDialog.Builder builder=new Builder(CallSmsSafe.this);
		View dialogview=View.inflate(getApplicationContext(), R.layout.add_black_dialog, null);
		final AlertDialog dialog=builder.create();
		final EditText add_blacknumber=(EditText) dialogview.findViewById(R.id.et_add_blacknumber);
		final RadioGroup rg_mode=(RadioGroup) dialogview.findViewById(R.id.rg_mode);
		Button btn_cancel=(Button) dialogview.findViewById(R.id.btn_cancel);
		Button btn_ok=(Button) dialogview.findViewById(R.id.btn_ok);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
			});
			btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//��ȡҪ��ӵĺ���
				String add_phone=add_blacknumber.getText().toString().trim();
				if(TextUtils.isEmpty(add_phone)){
					ToastUtils.show(CallSmsSafe.this, "���������벻��Ϊ�գ�");
					return ;
				}
				int id=rg_mode.getCheckedRadioButtonId();
				String mode="3";
				switch(id){
					case R.id.rb_phone:
						mode="1";
						break;
					case R.id.rb_sms:
						mode="2";
						break;
					case R.id.rb_all:
						mode="3";
						break;
				}
				
				//ֻ�ǰ����ݼӵ������ݿ���
				boolean result=dao.add(add_phone, mode);
				if(result){
					ToastUtils.show(CallSmsSafe.this, "��ӳɹ�");
					BlackNumberInfo object=new BlackNumberInfo();
					object.setPhone(add_phone);
					object.setMode(mode);
					infos.add(0, object);
					//֪ͨlistview��������
					adapter.notifyDataSetChanged();
				}else{
					ToastUtils.show(CallSmsSafe.this, "���ʧ��");
				}
				dialog.dismiss();
			}
			});
		dialog.setView(dialogview, 0, 0, 0, 0);
		dialog.show();
	}

	private class MyBlackAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return infos.size();
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			viewHolder holder;
			if(convertView!=null){
				//������ʷ��view����
				view=convertView;
				holder=(viewHolder) view.getTag();
			}else{
				//�����µĺ���ʱ���ϱ�ǩ
				 holder=new viewHolder();
				 view=View.inflate(getApplicationContext(), R.layout.item_callsmssafe, null);
				 holder.black_phone=(TextView) view.findViewById(R.id.tv_black_phone);
				 holder.black_mode=(TextView) view.findViewById(R.id.tv_black_mode);
				 holder.black_delete=(ImageView) view.findViewById(R.id.img_black_delete);
				 view.setTag(holder);
			}
			String mode=infos.get(position).getMode();
			//System.out.println("getView -----"+position);
			if("1".equals(mode)){
				holder.black_mode.setText("�绰����");
			}else if("2".equals(mode)){
				holder.black_mode.setText("��������");
			}else if("3".equals(mode)){
				holder.black_mode.setText("ȫ������");
			}
			final String phone=infos.get(position).getPhone();
			   holder.black_phone.setText(phone);
			   holder.black_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					AlertDialog.Builder builder=new Builder(CallSmsSafe.this);
					builder.setTitle("���棡");
					builder.setMessage("ȷ��Ҫɾ�����������������");
					builder.setNegativeButton("ȡ��", null);
					builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							boolean result=dao.delete(phone);
							if(result){
								ToastUtils.show(CallSmsSafe.this, "ɾ���ɹ�");
								infos.remove(position);
								//��������
								adapter.notifyDataSetChanged();
							}else{
								ToastUtils.show(CallSmsSafe.this, "ɾ��ʧ��");
							}
						}
					});
					builder.show();
				}
			});
			return view;
		}
		
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
	class viewHolder{
		public TextView black_phone;
		public TextView black_mode;
		public ImageView black_delete;
	}
}
