package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class SelectOptionAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.selectoption_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加选项信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
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
		// 设置下拉列表的风格
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
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加选项信息按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取选项内容*/ 
					if(ET_optionContent.getText().toString().equals("")) {
						Toast.makeText(SelectOptionAddActivity.this, "选项内容输入不能为空!", Toast.LENGTH_LONG).show();
						ET_optionContent.setFocusable(true);
						ET_optionContent.requestFocus();
						return;	
					}
					selectOption.setOptionContent(ET_optionContent.getText().toString());
					/*调用业务逻辑层上传选项信息信息*/
					SelectOptionAddActivity.this.setTitle("正在上传选项信息信息，稍等...");
					String result = selectOptionService.AddSelectOption(selectOption);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
