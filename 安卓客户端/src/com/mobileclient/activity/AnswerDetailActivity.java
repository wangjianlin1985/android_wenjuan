package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Answer;
import com.mobileclient.service.AnswerService;
import com.mobileclient.domain.SelectOption;
import com.mobileclient.service.SelectOptionService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
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
public class AnswerDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明记录编号控件
	private TextView TV_answerId;
	// 声明选项信息控件
	private TextView TV_selectOptionObj;
	// 声明用户控件
	private TextView TV_userObj;
	/* 要保存的答卷信息信息 */
	Answer answer = new Answer(); 
	/* 答卷信息管理业务逻辑层 */
	private AnswerService answerService = new AnswerService();
	private SelectOptionService selectOptionService = new SelectOptionService();
	private UserInfoService userInfoService = new UserInfoService();
	private int answerId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.answer_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看答卷信息详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_answerId = (TextView) findViewById(R.id.TV_answerId);
		TV_selectOptionObj = (TextView) findViewById(R.id.TV_selectOptionObj);
		TV_userObj = (TextView) findViewById(R.id.TV_userObj);
		Bundle extras = this.getIntent().getExtras();
		answerId = extras.getInt("answerId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AnswerDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    answer = answerService.GetAnswer(answerId); 
		this.TV_answerId.setText(answer.getAnswerId() + "");
		SelectOption selectOptionObj = selectOptionService.GetSelectOption(answer.getSelectOptionObj());
		this.TV_selectOptionObj.setText(selectOptionObj.getOptionContent());
		UserInfo userObj = userInfoService.GetUserInfo(answer.getUserObj());
		this.TV_userObj.setText(userObj.getName());
	} 
}
