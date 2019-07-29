package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.QuestionInfo;
import com.mobileclient.service.QuestionInfoService;
import com.mobileclient.domain.SurveyInfo;
import com.mobileclient.service.SurveyInfoService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class QuestionInfoEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明记录编号TextView
	private TextView TV_titileId;
	// 声明问卷名称下拉框
	private Spinner spinner_questionPaperObj;
	private ArrayAdapter<String> questionPaperObj_adapter;
	private static  String[] questionPaperObj_ShowText  = null;
	private List<SurveyInfo> surveyInfoList = null;
	/*问卷名称管理业务逻辑层*/
	private SurveyInfoService surveyInfoService = new SurveyInfoService();
	// 声明问题内容输入框
	private EditText ET_titleValue;
	protected String carmera_path;
	/*要保存的问题信息信息*/
	QuestionInfo questionInfo = new QuestionInfo();
	/*问题信息管理业务逻辑层*/
	private QuestionInfoService questionInfoService = new QuestionInfoService();

	private int titileId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.questioninfo_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑问题信息信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_titileId = (TextView) findViewById(R.id.TV_titileId);
		spinner_questionPaperObj = (Spinner) findViewById(R.id.Spinner_questionPaperObj);
		// 获取所有的问卷名称
		try {
			surveyInfoList = surveyInfoService.QuerySurveyInfo(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int surveyInfoCount = surveyInfoList.size();
		questionPaperObj_ShowText = new String[surveyInfoCount];
		for(int i=0;i<surveyInfoCount;i++) { 
			questionPaperObj_ShowText[i] = surveyInfoList.get(i).getQuestionPaperName();
		}
		// 将可选内容与ArrayAdapter连接起来
		questionPaperObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, questionPaperObj_ShowText);
		// 设置图书类别下拉列表的风格
		questionPaperObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_questionPaperObj.setAdapter(questionPaperObj_adapter);
		// 添加事件Spinner事件监听
		spinner_questionPaperObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				questionInfo.setQuestionPaperObj(surveyInfoList.get(arg2).getPaperId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_questionPaperObj.setVisibility(View.VISIBLE);
		ET_titleValue = (EditText) findViewById(R.id.ET_titleValue);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		titileId = extras.getInt("titileId");
		/*单击修改问题信息按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取问题内容*/ 
					if(ET_titleValue.getText().toString().equals("")) {
						Toast.makeText(QuestionInfoEditActivity.this, "问题内容输入不能为空!", Toast.LENGTH_LONG).show();
						ET_titleValue.setFocusable(true);
						ET_titleValue.requestFocus();
						return;	
					}
					questionInfo.setTitleValue(ET_titleValue.getText().toString());
					/*调用业务逻辑层上传问题信息信息*/
					QuestionInfoEditActivity.this.setTitle("正在更新问题信息信息，稍等...");
					String result = questionInfoService.UpdateQuestionInfo(questionInfo);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
		initViewData();
	}

	/* 初始化显示编辑界面的数据 */
	private void initViewData() {
	    questionInfo = questionInfoService.GetQuestionInfo(titileId);
		this.TV_titileId.setText(titileId+"");
		for (int i = 0; i < surveyInfoList.size(); i++) {
			if (questionInfo.getQuestionPaperObj() == surveyInfoList.get(i).getPaperId()) {
				this.spinner_questionPaperObj.setSelection(i);
				break;
			}
		}
		this.ET_titleValue.setText(questionInfo.getTitleValue());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
