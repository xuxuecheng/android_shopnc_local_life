﻿/**
 * ClassName:TuanAcitivity.java
 * PackageName:com.shopnc_local_life.android.ui.tuan
 * Create On 2013-8-6上午11:51:20
 * Site:http://weibo.com/hjgang or http://t.qq.com/hjgang_
 * EMAIL:hjgang@bizpower.com or hjgang@yahoo.cn
 * Copyrights 2013-2-18 hjgang All rights reserved.
 */
package com.shopnc_local_life.android.ui.tuan;

import java.util.ArrayList;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.shopnc_local_life.android.R;
import com.shopnc_local_life.android.Adapter.TuanListViewAdapter;
import com.shopnc_local_life.android.common.Constants;
import com.shopnc_local_life.android.common.MyApp;
import com.shopnc_local_life.android.handler.RemoteDataHandler;
import com.shopnc_local_life.android.handler.RemoteDataHandler.Callback;
import com.shopnc_local_life.android.modle.ResponseData;
import com.shopnc_local_life.android.modle.TuanList;
import com.shopnc_local_life.android.widget.ExpandTabView;
import com.shopnc_local_life.android.widget.PullView;
import com.shopnc_local_life.android.widget.PullView.OnRefreshListener;
import com.shopnc_local_life.android.widget.ViewLeft;
import com.shopnc_local_life.android.widget.ViewMiddle;
import com.shopnc_local_life.android.widget.ViewRight;

/**
 * Author:hjgang Create On 2013-8-6上午11:51:20 Site:http://weibo.com/hjgang or
 * http://t.qq.com/hjgang_ EMAIL:hjgang@bizpower.com or hjgang@yahoo.cn
 * Copyrights 2013-8-6 hjgang All rights reserved.
 */
public class TuanAcitivity extends Activity implements OnScrollListener {
	private PullView listview;
	private TuanListViewAdapter adapter;
	private ExpandTabView expandTabView;
	private ArrayList<View> mViewArray = new ArrayList<View>();
	private ArrayList<String> mTextArray = new ArrayList<String>();
	private ViewMiddle viewMiddle;
	private ViewLeft viewLeft;
	private ViewRight viewRight;
	// private MyProcessDialog dialog;
	private MyApp myApp;
	private int pageno = 1;
	private View moreView; // 加载更多页面
	private boolean list_flag = false;
	private int lastItem;
	private ArrayList<TuanList> tuan_list;
	private String flag;
	private ImageButton btn_back_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_tuan);
		// dialog =new MyProcessDialog(TuanAcitivity.this);
		myApp = (MyApp) TuanAcitivity.this.getApplication();
		btn_back_id = (ImageButton) findViewById(R.id.btn_back_id);
		listview = (PullView) findViewById(R.id.listview);
		moreView = getLayoutInflater().inflate(R.layout.list_more_load, null);
		adapter = new TuanListViewAdapter(TuanAcitivity.this);
		tuan_list = new ArrayList<TuanList>();
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		listview.setOnScrollListener(this); // 设置listview的滚动事件
		ListViewInFo(pageno = 1);

//		expandTabView = (ExpandTabView) findViewById(R.id.expandtab_view);
//		viewMiddle = new ViewMiddle(this);
//		viewLeft = new ViewLeft(this);
//		viewRight = new ViewRight(this);

//		mViewArray.add(viewMiddle);
//		mViewArray.add(viewLeft);
//		mViewArray.add(viewRight);
//		mTextArray.add("全部地区");
//		mTextArray.add("全部分类");
//		mTextArray.add("默认排序");
//		expandTabView.setValue(mTextArray, mViewArray);
//		expandTabView.setTitle(viewMiddle.getShowText(), 0);

		initView();
	    initVaule();
		initListener();

		viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {

			@Override
			public void getValue(String distance, String showText) {
				onRefresh(viewLeft, showText);
			}
		});
		viewRight.setOnSelectListener(new ViewRight.OnSelectListener() {

			@Override
			public void getValue(String distance, String showText) {
				onRefresh(viewRight, showText);
			}
		});

		viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener() {
			@Override
			public void getValue(String showText) {
				onRefresh(viewMiddle, showText);
			}
		});
		listview.setonRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				listview.removeFooterView(moreView);
				ListViewInFo(pageno = 1);
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TuanList tuanlist = (TuanList) listview.getItemAtPosition(arg2);
				Intent intent = new Intent(TuanAcitivity.this,
						TuanDetailsActivity.class);
				intent.putExtra("group_id", tuanlist.getGroup_id() + "");
				TuanAcitivity.this.startActivity(intent);
			}
		});
	}

	 private void initView() {
	
	 expandTabView = (ExpandTabView) findViewById(R.id.expandtab_view_tuan);
	 viewLeft = new ViewLeft(this);
	 viewMiddle = new ViewMiddle(this);
	 viewRight = new ViewRight(this);
	
	 }
	
	 private void initVaule() {
	
	 mViewArray.add(viewLeft);
	 mViewArray.add(viewMiddle);
	 mViewArray.add(viewRight);
	 ArrayList<String> mTextArray = new ArrayList<String>();
	 mTextArray.add("距离");
	 mTextArray.add("区域");
	 mTextArray.add("距离");
	 expandTabView.setValue(mTextArray, mViewArray);
	 // expandTabView.setTitle(viewLeft.getShowText(), 0);
	 // expandTabView.setTitle(viewMiddle.getShowText(), 1);
	 // expandTabView.setTitle(viewRight.getShowText(), 2);
	
	 }
	
	private void initListener() {
		btn_back_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TuanAcitivity.this.finish();
			}
		});
		//
		// viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {
		//
		// @Override
		// public void getValue(String distance, String showText) {
		// onRefresh(viewLeft, showText);
		// }
		// });
		//
		// viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener() {
		//
		// @Override
		// public void getValue(String showText) {
		//
		// onRefresh(viewMiddle, showText);
		//
		// }
		// });
		//
		// viewRight.setOnSelectListener(new ViewRight.OnSelectListener() {
		//
		// @Override
		// public void getValue(String distance, String showText) {
		// onRefresh(viewRight, showText);
		// }
		// });
		//
	}

	@Override
	protected void onResume() {
		super.onResume();
		// if(flag !=null && !flag.equals("0") && !flag.equals("")
		// && !myApp.getCity_id().equals(flag)){
		// listview.addFooterView(moreView);
		// tuan_list.clear();
		// adapter.notifyDataSetChanged();
		// ListViewInFo(1);
		// }
		if (!myApp.getCity_id().equals(flag) && !myApp.getCity_id().equals("0")) {
			listview.addFooterView(moreView);
			tuan_list.clear();
			adapter.notifyDataSetChanged();
			ListViewInFo(1);
		}
	}

	public void ListViewInFo(int pangeno) {
		if (myApp.getCity_id() == null || myApp.getCity_id().equals("0")
				|| myApp.getCity_id().equals("")) {
			Toast.makeText(TuanAcitivity.this, "城市没有获取到，请稍后重试",
					Toast.LENGTH_SHORT).show();
			listview.removeFooterView(moreView);
			listview.onRefreshComplete();
			return;
		}
		flag = myApp.getCity_id();
		String url = Constants.URL_GROUPBUY_LIST + "&city_id="
				+ myApp.getCity_id() + "&pagenumber=" + pangeno + "&pagesize="
				+ Constants.PARAM_PAGESIZE;
		RemoteDataHandler.asyncGet(url, new Callback() {
			@Override
			public void dataLoaded(ResponseData data) {
				listview.onRefreshComplete();
				if (data.getCode() == HttpStatus.SC_OK) {
					String json = data.getJson();
					// if(data.getCount() <= pageno * Constants.PARAM_PAGESIZE){
					// list_flag=true;
					// listview.removeFooterView(moreView);
					// }else{
					// list_flag=false;
					// moreView.setVisibility(View.VISIBLE);
					// }
					if (data.isHasMore()) {
						list_flag = false;
						listview.addFooterView(moreView);
					} else {
						list_flag = true;
						listview.removeFooterView(moreView);
					}
					int count = 0;
					if (pageno == 1) {
						tuan_list.clear();
						adapter.notifyDataSetChanged();
					} else {
						count = ((pageno - 1) * Constants.PARAM_PAGESIZE);
					}
					listview.setSelection(count);
					ArrayList<TuanList> t_list = TuanList.newInstanceList(json);
					tuan_list.addAll(t_list);
					adapter.setDatas(tuan_list);
					adapter.notifyDataSetChanged();
				} else {
					listview.removeFooterView(moreView);
					listview.onRefreshComplete();
					Toast.makeText(TuanAcitivity.this, "加载团购列表失败，请稍后重试",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
		if (lastItem == listview.getCount() - 1
				&& scrollState == this.SCROLL_STATE_IDLE) {
			if (list_flag) {
			} else {
				mHandler.sendEmptyMessage(0);
			}
		}
	}

	// 声明Handler
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				pageno = pageno + 1;
				ListViewInFo(pageno); // 加载更多数据，这里可以使用异步加载
				adapter.notifyDataSetChanged();
				break;
			case 1:
				break;
			default:
				break;
			}
		};
	};

	private void onRefresh(View view, String showText) {
		expandTabView.onPressBack();
		int position = getPositon(view);
		if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
			expandTabView.setTitle(showText, position);
		}
		Toast.makeText(TuanAcitivity.this, showText, Toast.LENGTH_SHORT).show();

	}

	private int getPositon(View tView) {
		for (int i = 0; i < mViewArray.size(); i++) {
			if (mViewArray.get(i) == tView) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void onBackPressed() {
		if (!expandTabView.onPressBack()) {
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!expandTabView.onPressBack()) {
				finish();
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
