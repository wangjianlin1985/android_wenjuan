package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.Answer;
import com.mobileclient.service.AnswerService;
import com.mobileclient.domain.SelectOption;
import com.mobileclient.service.SelectOptionService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
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
public class AnswerAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明选项信息下拉框
	private Spinner spinner_selectOptionObj;
	private ArrayAdapter<String> selectOptionObj_adapter;
	private static  String[] selectOptionObj_ShowText  = null;
	private List<SelectOption> selectOptionList = null;
	/*选项信息管理业务逻辑层*/
	private SelectOptionService selectOptionService = new SelectOptionService();
	// 声明用户下拉框
	private Spinner spinner_userObj;
	private ArrayAdapter<String> userObj_adapter;
	private static  String[] userObj_ShowText  = null;
	private List<UserInfo> userInfoList = null;
	/*用户管理业务逻辑层*/
	private UserInfoService userInfoService = new UserInfoService();
	protected String carmera_path;
	/*要保存的答卷信息信息*/
	Answer answer = new Answer();
	/*答卷信息管理业务逻辑层*/
	private AnswerService answerService = new AnswerService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.answer_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加答卷信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		spinner_selectOptionObj = (Spinner) findViewById(R.id.Spinner_selectOptionObj);
		// 获取所有的选项信息
		try {
			selectOptionList = selectOptionService.QuerySelectOption(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int selectOptionCount = selectOptionList.size();
		selectOptionObj_ShowText = new String[selectOptionCount];
		for(int i=0;i<selectOptionCount;i++) { 
			selectOptionObj_ShowText[i] = selectOptionList.get(i).getOptionContent();
		}
		// 将可选内容与ArrayAdapter连接起来
		selectOptionObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, selectOptionObj_ShowText);
		// 设置下拉列表的风格
		selectOptionObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_selectOptionObj.setAdapter(selectOptionObj_adapter);
		// 添加事件Spinner事件监听
		spinner_selectOptionObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				answer.setSelectOptionObj(selectOptionList.get(arg2).getOptionId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_selectOptionObj.setVisibility(View.VISIBLE);
		spinner_userObj = (Spinner) findViewById(R.id.Spinner_userObj);
		// 获取所有的用户
		try {
			userInfoList = userInfoService.QueryUserInfo(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int userInfoCount = userInfoList.size();
		userObj_ShowText = new String[userInfoCount];
		for(int i=0;i<userInfoCount;i++) { 
			userObj_ShowText[i] = userInfoList.get(i).getName();
		}
		// 将可选内容与ArrayAdapter连接起来
		userObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, userObj_ShowText);
		// 设置下拉列表的风格
		userObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_userObj.setAdapter(userObj_adapter);
		// 添加事件Spinner事件监听
		spinner_userObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				answer.setUserObj(userInfoList.get(arg2).getUserInfoname()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_userObj.setVisibility(View.VISIBLE);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加答卷信息按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*调用业务逻辑层上传答卷信息信息*/
					AnswerAddActivity.this.setTitle("正在上传答卷信息信息，稍等...");
					String result = answerService.AddAnswer(answer);
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
