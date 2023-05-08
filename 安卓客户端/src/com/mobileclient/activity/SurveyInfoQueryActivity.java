package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.SurveyInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.ImageView;
import android.widget.TextView;
public class SurveyInfoQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明问卷名称输入框
	private EditText ET_questionPaperName;
	// 声明发起人输入框
	private EditText ET_faqiren;
	// 发起日期控件
	private DatePicker dp_startDate;
	private CheckBox cb_startDate;
	// 结束日期控件
	private DatePicker dp_endDate;
	private CheckBox cb_endDate;
	/*查询过滤条件保存到这个对象中*/
	private SurveyInfo queryConditionSurveyInfo = new SurveyInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.surveyinfo_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置问卷信息查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_questionPaperName = (EditText) findViewById(R.id.ET_questionPaperName);
		ET_faqiren = (EditText) findViewById(R.id.ET_faqiren);
		dp_startDate = (DatePicker) findViewById(R.id.dp_startDate);
		cb_startDate = (CheckBox) findViewById(R.id.cb_startDate);
		dp_endDate = (DatePicker) findViewById(R.id.dp_endDate);
		cb_endDate = (CheckBox) findViewById(R.id.cb_endDate);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionSurveyInfo.setQuestionPaperName(ET_questionPaperName.getText().toString());
					queryConditionSurveyInfo.setFaqiren(ET_faqiren.getText().toString());
					if(cb_startDate.isChecked()) {
						/*获取发起日期*/
						Date startDate = new Date(dp_startDate.getYear()-1900,dp_startDate.getMonth(),dp_startDate.getDayOfMonth());
						queryConditionSurveyInfo.setStartDate(new Timestamp(startDate.getTime()));
					} else {
						queryConditionSurveyInfo.setStartDate(null);
					} 
					if(cb_endDate.isChecked()) {
						/*获取结束日期*/
						Date endDate = new Date(dp_endDate.getYear()-1900,dp_endDate.getMonth(),dp_endDate.getDayOfMonth());
						queryConditionSurveyInfo.setEndDate(new Timestamp(endDate.getTime()));
					} else {
						queryConditionSurveyInfo.setEndDate(null);
					} 
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionSurveyInfo", queryConditionSurveyInfo);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
