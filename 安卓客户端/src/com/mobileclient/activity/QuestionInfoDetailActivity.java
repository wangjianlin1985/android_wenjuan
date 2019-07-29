package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.QuestionInfo;
import com.mobileclient.service.QuestionInfoService;
import com.mobileclient.domain.SurveyInfo;
import com.mobileclient.service.SurveyInfoService;
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
public class QuestionInfoDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明记录编号控件
	private TextView TV_titileId;
	// 声明问卷名称控件
	private TextView TV_questionPaperObj;
	// 声明问题内容控件
	private TextView TV_titleValue;
	/* 要保存的问题信息信息 */
	QuestionInfo questionInfo = new QuestionInfo(); 
	/* 问题信息管理业务逻辑层 */
	private QuestionInfoService questionInfoService = new QuestionInfoService();
	private SurveyInfoService surveyInfoService = new SurveyInfoService();
	private int titileId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.questioninfo_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看问题信息详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_titileId = (TextView) findViewById(R.id.TV_titileId);
		TV_questionPaperObj = (TextView) findViewById(R.id.TV_questionPaperObj);
		TV_titleValue = (TextView) findViewById(R.id.TV_titleValue);
		Bundle extras = this.getIntent().getExtras();
		titileId = extras.getInt("titileId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				QuestionInfoDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    questionInfo = questionInfoService.GetQuestionInfo(titileId); 
		this.TV_titileId.setText(questionInfo.getTitileId() + "");
		SurveyInfo questionPaperObj = surveyInfoService.GetSurveyInfo(questionInfo.getQuestionPaperObj());
		this.TV_questionPaperObj.setText(questionPaperObj.getQuestionPaperName());
		this.TV_titleValue.setText(questionInfo.getTitleValue());
	} 
}
