package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.QuestionInfo;
import com.mobileclient.domain.SurveyInfo;
import com.mobileclient.service.SurveyInfoService;

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
public class QuestionInfoQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明问卷名称下拉框
	private Spinner spinner_questionPaperObj;
	private ArrayAdapter<String> questionPaperObj_adapter;
	private static  String[] questionPaperObj_ShowText  = null;
	private List<SurveyInfo> surveyInfoList = null; 
	/*问卷信息管理业务逻辑层*/
	private SurveyInfoService surveyInfoService = new SurveyInfoService();
	// 声明问题内容输入框
	private EditText ET_titleValue;
	/*查询过滤条件保存到这个对象中*/
	private QuestionInfo queryConditionQuestionInfo = new QuestionInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.questioninfo_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置问题信息查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_questionPaperObj = (Spinner) findViewById(R.id.Spinner_questionPaperObj);
		// 获取所有的问卷信息
		try {
			surveyInfoList = surveyInfoService.QuerySurveyInfo(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int surveyInfoCount = surveyInfoList.size();
		questionPaperObj_ShowText = new String[surveyInfoCount+1];
		questionPaperObj_ShowText[0] = "不限制";
		for(int i=1;i<=surveyInfoCount;i++) { 
			questionPaperObj_ShowText[i] = surveyInfoList.get(i-1).getQuestionPaperName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		questionPaperObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, questionPaperObj_ShowText);
		// 设置问卷名称下拉列表的风格
		questionPaperObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_questionPaperObj.setAdapter(questionPaperObj_adapter);
		// 添加事件Spinner事件监听
		spinner_questionPaperObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionQuestionInfo.setQuestionPaperObj(surveyInfoList.get(arg2-1).getPaperId()); 
				else
					queryConditionQuestionInfo.setQuestionPaperObj(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_questionPaperObj.setVisibility(View.VISIBLE);
		ET_titleValue = (EditText) findViewById(R.id.ET_titleValue);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionQuestionInfo.setTitleValue(ET_titleValue.getText().toString());
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionQuestionInfo", queryConditionQuestionInfo);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
