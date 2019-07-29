package com.mobileclient.activity;

import java.util.Date;
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
public class UserInfoDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明用户名控件
	private TextView TV_userInfoname;
	// 声明登录密码控件
	private TextView TV_password;
	// 声明姓名控件
	private TextView TV_name;
	// 声明性别控件
	private TextView TV_sex;
	// 声明出生日期控件
	private TextView TV_birthday;
	// 声明联系电话控件
	private TextView TV_telephone;
	// 声明邮箱地址控件
	private TextView TV_email;
	// 声明用户住址控件
	private TextView TV_address;
	// 声明个人照片图片框
	private ImageView iv_userPhoto;
	/* 要保存的用户信息信息 */
	UserInfo userInfo = new UserInfo(); 
	/* 用户信息管理业务逻辑层 */
	private UserInfoService userInfoService = new UserInfoService();
	private String userInfoname;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.userinfo_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看用户信息详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_userInfoname = (TextView) findViewById(R.id.TV_userInfoname);
		TV_password = (TextView) findViewById(R.id.TV_password);
		TV_name = (TextView) findViewById(R.id.TV_name);
		TV_sex = (TextView) findViewById(R.id.TV_sex);
		TV_birthday = (TextView) findViewById(R.id.TV_birthday);
		TV_telephone = (TextView) findViewById(R.id.TV_telephone);
		TV_email = (TextView) findViewById(R.id.TV_email);
		TV_address = (TextView) findViewById(R.id.TV_address);
		iv_userPhoto = (ImageView) findViewById(R.id.iv_userPhoto); 
		Bundle extras = this.getIntent().getExtras();
		userInfoname = extras.getString("userInfoname");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UserInfoDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    userInfo = userInfoService.GetUserInfo(userInfoname); 
		this.TV_userInfoname.setText(userInfo.getUserInfoname());
		this.TV_password.setText(userInfo.getPassword());
		this.TV_name.setText(userInfo.getName());
		this.TV_sex.setText(userInfo.getSex());
		Date birthday = new Date(userInfo.getBirthday().getTime());
		String birthdayStr = (birthday.getYear() + 1900) + "-" + (birthday.getMonth()+1) + "-" + birthday.getDate();
		this.TV_birthday.setText(birthdayStr);
		this.TV_telephone.setText(userInfo.getTelephone());
		this.TV_email.setText(userInfo.getEmail());
		this.TV_address.setText(userInfo.getAddress());
		byte[] userPhoto_data = null;
		try {
			// 获取图片数据
			userPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + userInfo.getUserPhoto());
			Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0,userPhoto_data.length);
			this.iv_userPhoto.setImageBitmap(userPhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
