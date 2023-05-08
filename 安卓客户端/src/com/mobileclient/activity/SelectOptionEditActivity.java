package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.SelectOption;
import com.mobileclient.service.SelectOptionService;
import com.mobileclient.domain.QuestionInfo;
import com.mobileclient.service.QuestionInfoService;
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

public class SelectOptionEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明记录编号TextView
	private TextView TV_optionId;
	// 声明问题信息下拉框
	private Spinner spinner_questionObj;
	private ArrayAdapter<String> questionObj_adapter;
	private static  String[] questionObj_ShowText  = null;
	private List<QuestionInfo> questionInfoList = null;
	/*问题信息管理业务逻辑层*/
	private QuestionInfoService questionInfoService = new QuestionInfoService();
	// 声明选项内容输入框
	private EditText ET_optionContent;
	protected String carmera_path;
	/*要保存的选项信息信息*/
	SelectOption selectOption = new SelectOption();
	/*选项信息管理业务逻辑层*/
	private SelectOptionService selectOptionService = new SelectOptionService();

	private int optionId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.selectoption_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑选项信息信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_optionId = (TextView) findViewById(R.id.TV_optionId);
		spinner_questionObj = (Spinner) findViewById(R.id.Spinner_questionObj);
		// 获取所有的问题信息
		try {
			questionInfoList = questionInfoService.QueryQuestionInfo(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int questionInfoCount = questionInfoList.size();
		questionObj_ShowText = new String[questionInfoCount];
		for(int i=0;i<questionInfoCount;i++) { 
			questionObj_ShowText[i] = questionInfoList.get(i).getTitleValue();
		}
		// 将可选内容与ArrayAdapter连接起来
		questionObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, questionObj_ShowText);
		// 设置图书类别下拉列表的风格
		questionObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_questionObj.setAdapter(questionObj_adapter);
		// 添加事件Spinner事件监听
		spinner_questionObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				selectOption.setQuestionObj(questionInfoList.get(arg2).getTitileId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_questionObj.setVisibility(View.VISIBLE);
		ET_optionContent = (EditText) findViewById(R.id.ET_optionContent);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		optionId = extras.getInt("optionId");
		/*单击修改选项信息按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取选项内容*/ 
					if(ET_optionContent.getText().toString().equals("")) {
						Toast.makeText(SelectOptionEditActivity.this, "选项内容输入不能为空!", Toast.LENGTH_LONG).show();
						ET_optionContent.setFocusable(true);
						ET_optionContent.requestFocus();
						return;	
					}
					selectOption.setOptionContent(ET_optionContent.getText().toString());
					/*调用业务逻辑层上传选项信息信息*/
					SelectOptionEditActivity.this.setTitle("正在更新选项信息信息，稍等...");
					String result = selectOptionService.UpdateSelectOption(selectOption);
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
	    selectOption = selectOptionService.GetSelectOption(optionId);
		this.TV_optionId.setText(optionId+"");
		for (int i = 0; i < questionInfoList.size(); i++) {
			if (selectOption.getQuestionObj() == questionInfoList.get(i).getTitileId()) {
				this.spinner_questionObj.setSelection(i);
				break;
			}
		}
		this.ET_optionContent.setText(selectOption.getOptionContent());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
