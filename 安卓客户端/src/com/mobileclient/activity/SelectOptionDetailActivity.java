package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.SelectOption;
import com.mobileclient.service.SelectOptionService;
import com.mobileclient.domain.QuestionInfo;
import com.mobileclient.service.QuestionInfoService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
public class SelectOptionDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明记录编号控件
	private TextView TV_optionId;
	// 声明问题信息控件
	private TextView TV_questionObj;
	// 声明选项内容控件
	private TextView TV_optionContent;
	/* 要保存的选项信息信息 */
	SelectOption selectOption = new SelectOption(); 
	/* 选项信息管理业务逻辑层 */
	private SelectOptionService selectOptionService = new SelectOptionService();
	private QuestionInfoService questionInfoService = new QuestionInfoService();
	private int optionId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.selectoption_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看选项信息详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_optionId = (TextView) findViewById(R.id.TV_optionId);
		TV_questionObj = (TextView) findViewById(R.id.TV_questionObj);
		TV_optionContent = (TextView) findViewById(R.id.TV_optionContent);
		Bundle extras = this.getIntent().getExtras();
		optionId = extras.getInt("optionId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SelectOptionDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    selectOption = selectOptionService.GetSelectOption(optionId); 
		this.TV_optionId.setText(selectOption.getOptionId() + "");
		QuestionInfo questionObj = questionInfoService.GetQuestionInfo(selectOption.getQuestionObj());
		this.TV_questionObj.setText(questionObj.getTitleValue());
		this.TV_optionContent.setText(selectOption.getOptionContent());
	} 
}
