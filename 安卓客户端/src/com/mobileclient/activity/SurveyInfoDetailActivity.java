package com.mobileclient.activity;

import java.util.Date;
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
public class SurveyInfoDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明记录编号控件
	private TextView TV_paperId;
	// 声明问卷名称控件
	private TextView TV_questionPaperName;
	// 声明发起人控件
	private TextView TV_faqiren;
	// 声明问卷描述控件
	private TextView TV_description;
	// 声明发起日期控件
	private TextView TV_startDate;
	// 声明结束日期控件
	private TextView TV_endDate;
	// 声明主题图片图片框
	private ImageView iv_zhutitupian;
	// 声明审核标志控件
	private TextView TV_publishFlag;
	/* 要保存的问卷信息信息 */
	SurveyInfo surveyInfo = new SurveyInfo(); 
	/* 问卷信息管理业务逻辑层 */
	private SurveyInfoService surveyInfoService = new SurveyInfoService();
	private int paperId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.surveyinfo_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看问卷信息详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_paperId = (TextView) findViewById(R.id.TV_paperId);
		TV_questionPaperName = (TextView) findViewById(R.id.TV_questionPaperName);
		TV_faqiren = (TextView) findViewById(R.id.TV_faqiren);
		TV_description = (TextView) findViewById(R.id.TV_description);
		TV_startDate = (TextView) findViewById(R.id.TV_startDate);
		TV_endDate = (TextView) findViewById(R.id.TV_endDate);
		iv_zhutitupian = (ImageView) findViewById(R.id.iv_zhutitupian); 
		TV_publishFlag = (TextView) findViewById(R.id.TV_publishFlag);
		Bundle extras = this.getIntent().getExtras();
		paperId = extras.getInt("paperId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SurveyInfoDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    surveyInfo = surveyInfoService.GetSurveyInfo(paperId); 
		this.TV_paperId.setText(surveyInfo.getPaperId() + "");
		this.TV_questionPaperName.setText(surveyInfo.getQuestionPaperName());
		this.TV_faqiren.setText(surveyInfo.getFaqiren());
		this.TV_description.setText(surveyInfo.getDescription());
		Date startDate = new Date(surveyInfo.getStartDate().getTime());
		String startDateStr = (startDate.getYear() + 1900) + "-" + (startDate.getMonth()+1) + "-" + startDate.getDate();
		this.TV_startDate.setText(startDateStr);
		Date endDate = new Date(surveyInfo.getEndDate().getTime());
		String endDateStr = (endDate.getYear() + 1900) + "-" + (endDate.getMonth()+1) + "-" + endDate.getDate();
		this.TV_endDate.setText(endDateStr);
		byte[] zhutitupian_data = null;
		try {
			// 获取图片数据
			zhutitupian_data = ImageService.getImage(HttpUtil.BASE_URL + surveyInfo.getZhutitupian());
			Bitmap zhutitupian = BitmapFactory.decodeByteArray(zhutitupian_data, 0,zhutitupian_data.length);
			this.iv_zhutitupian.setImageBitmap(zhutitupian);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.TV_publishFlag.setText(surveyInfo.getPublishFlag() + "");
	} 
}
