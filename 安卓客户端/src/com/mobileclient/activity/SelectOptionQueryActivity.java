package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.SelectOption;
import com.mobileclient.domain.QuestionInfo;
import com.mobileclient.service.QuestionInfoService;

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
public class SelectOptionQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明问题信息下拉框
	private Spinner spinner_questionObj;
	private ArrayAdapter<String> questionObj_adapter;
	private static  String[] questionObj_ShowText  = null;
	private List<QuestionInfo> questionInfoList = null; 
	/*问题信息管理业务逻辑层*/
	private QuestionInfoService questionInfoService = new QuestionInfoService();
	// 声明选项内容输入框
	private EditText ET_optionContent;
	/*查询过滤条件保存到这个对象中*/
	private SelectOption queryConditionSelectOption = new SelectOption();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.selectoption_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置选项信息查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_questionObj = (Spinner) findViewById(R.id.Spinner_questionObj);
		// 获取所有的问题信息
		try {
			questionInfoList = questionInfoService.QueryQuestionInfo(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int questionInfoCount = questionInfoList.size();
		questionObj_ShowText = new String[questionInfoCount+1];
		questionObj_ShowText[0] = "不限制";
		for(int i=1;i<=questionInfoCount;i++) { 
			questionObj_ShowText[i] = questionInfoList.get(i-1).getTitleValue();
		} 
		// 将可选内容与ArrayAdapter连接起来
		questionObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, questionObj_ShowText);
		// 设置问题信息下拉列表的风格
		questionObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_questionObj.setAdapter(questionObj_adapter);
		// 添加事件Spinner事件监听
		spinner_questionObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionSelectOption.setQuestionObj(questionInfoList.get(arg2-1).getTitileId()); 
				else
					queryConditionSelectOption.setQuestionObj(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_questionObj.setVisibility(View.VISIBLE);
		ET_optionContent = (EditText) findViewById(R.id.ET_optionContent);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionSelectOption.setOptionContent(ET_optionContent.getText().toString());
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionSelectOption", queryConditionSelectOption);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
