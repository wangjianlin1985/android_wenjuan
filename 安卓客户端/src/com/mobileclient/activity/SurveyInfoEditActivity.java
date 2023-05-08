package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
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

public class SurveyInfoEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明记录编号TextView
	private TextView TV_paperId;
	// 声明问卷名称输入框
	private EditText ET_questionPaperName;
	// 声明发起人输入框
	private EditText ET_faqiren;
	// 声明问卷描述输入框
	private EditText ET_description;
	// 出版发起日期控件
	private DatePicker dp_startDate;
	// 出版结束日期控件
	private DatePicker dp_endDate;
	// 声明主题图片图片框控件
	private ImageView iv_zhutitupian;
	private Button btn_zhutitupian;
	protected int REQ_CODE_SELECT_IMAGE_zhutitupian = 1;
	private int REQ_CODE_CAMERA_zhutitupian = 2;
	// 声明审核标志输入框
	private EditText ET_publishFlag;
	protected String carmera_path;
	/*要保存的问卷信息信息*/
	SurveyInfo surveyInfo = new SurveyInfo();
	/*问卷信息管理业务逻辑层*/
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
		setContentView(R.layout.surveyinfo_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑问卷信息信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_paperId = (TextView) findViewById(R.id.TV_paperId);
		ET_questionPaperName = (EditText) findViewById(R.id.ET_questionPaperName);
		ET_faqiren = (EditText) findViewById(R.id.ET_faqiren);
		ET_description = (EditText) findViewById(R.id.ET_description);
		dp_startDate = (DatePicker)this.findViewById(R.id.dp_startDate);
		dp_endDate = (DatePicker)this.findViewById(R.id.dp_endDate);
		iv_zhutitupian = (ImageView) findViewById(R.id.iv_zhutitupian);
		/*单击图片显示控件时进行图片的选择*/
		iv_zhutitupian.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SurveyInfoEditActivity.this,photoListActivity.class);
				startActivityForResult(intent,REQ_CODE_SELECT_IMAGE_zhutitupian);
			}
		});
		btn_zhutitupian = (Button) findViewById(R.id.btn_zhutitupian);
		btn_zhutitupian.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				carmera_path = HttpUtil.FILE_PATH + "/carmera_zhutitupian.bmp";
				File out = new File(carmera_path); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); 
				startActivityForResult(intent, REQ_CODE_CAMERA_zhutitupian);  
			}
		});
		ET_publishFlag = (EditText) findViewById(R.id.ET_publishFlag);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		paperId = extras.getInt("paperId");
		/*单击修改问卷信息按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取问卷名称*/ 
					if(ET_questionPaperName.getText().toString().equals("")) {
						Toast.makeText(SurveyInfoEditActivity.this, "问卷名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_questionPaperName.setFocusable(true);
						ET_questionPaperName.requestFocus();
						return;	
					}
					surveyInfo.setQuestionPaperName(ET_questionPaperName.getText().toString());
					/*验证获取发起人*/ 
					if(ET_faqiren.getText().toString().equals("")) {
						Toast.makeText(SurveyInfoEditActivity.this, "发起人输入不能为空!", Toast.LENGTH_LONG).show();
						ET_faqiren.setFocusable(true);
						ET_faqiren.requestFocus();
						return;	
					}
					surveyInfo.setFaqiren(ET_faqiren.getText().toString());
					/*验证获取问卷描述*/ 
					if(ET_description.getText().toString().equals("")) {
						Toast.makeText(SurveyInfoEditActivity.this, "问卷描述输入不能为空!", Toast.LENGTH_LONG).show();
						ET_description.setFocusable(true);
						ET_description.requestFocus();
						return;	
					}
					surveyInfo.setDescription(ET_description.getText().toString());
					/*获取出版日期*/
					Date startDate = new Date(dp_startDate.getYear()-1900,dp_startDate.getMonth(),dp_startDate.getDayOfMonth());
					surveyInfo.setStartDate(new Timestamp(startDate.getTime()));
					/*获取出版日期*/
					Date endDate = new Date(dp_endDate.getYear()-1900,dp_endDate.getMonth(),dp_endDate.getDayOfMonth());
					surveyInfo.setEndDate(new Timestamp(endDate.getTime()));
					if (!surveyInfo.getZhutitupian().startsWith("upload/")) {
						//如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						SurveyInfoEditActivity.this.setTitle("正在上传图片，稍等...");
						String zhutitupian = HttpUtil.uploadFile(surveyInfo.getZhutitupian());
						SurveyInfoEditActivity.this.setTitle("图片上传完毕！");
						surveyInfo.setZhutitupian(zhutitupian);
					} 
					/*验证获取审核标志*/ 
					if(ET_publishFlag.getText().toString().equals("")) {
						Toast.makeText(SurveyInfoEditActivity.this, "审核标志输入不能为空!", Toast.LENGTH_LONG).show();
						ET_publishFlag.setFocusable(true);
						ET_publishFlag.requestFocus();
						return;	
					}
					surveyInfo.setPublishFlag(Integer.parseInt(ET_publishFlag.getText().toString()));
					/*调用业务逻辑层上传问卷信息信息*/
					SurveyInfoEditActivity.this.setTitle("正在更新问卷信息信息，稍等...");
					String result = surveyInfoService.UpdateSurveyInfo(surveyInfo);
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
	    surveyInfo = surveyInfoService.GetSurveyInfo(paperId);
		this.TV_paperId.setText(paperId+"");
		this.ET_questionPaperName.setText(surveyInfo.getQuestionPaperName());
		this.ET_faqiren.setText(surveyInfo.getFaqiren());
		this.ET_description.setText(surveyInfo.getDescription());
		Date startDate = new Date(surveyInfo.getStartDate().getTime());
		this.dp_startDate.init(startDate.getYear() + 1900,startDate.getMonth(), startDate.getDate(), null);
		Date endDate = new Date(surveyInfo.getEndDate().getTime());
		this.dp_endDate.init(endDate.getYear() + 1900,endDate.getMonth(), endDate.getDate(), null);
		byte[] zhutitupian_data = null;
		try {
			// 获取图片数据
			zhutitupian_data = ImageService.getImage(HttpUtil.BASE_URL + surveyInfo.getZhutitupian());
			Bitmap zhutitupian = BitmapFactory.decodeByteArray(zhutitupian_data, 0, zhutitupian_data.length);
			this.iv_zhutitupian.setImageBitmap(zhutitupian);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.ET_publishFlag.setText(surveyInfo.getPublishFlag() + "");
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_CAMERA_zhutitupian  && resultCode == Activity.RESULT_OK) {
			carmera_path = HttpUtil.FILE_PATH + "/carmera_zhutitupian.bmp"; 
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(carmera_path, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			try {
				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts);
				String jpgFileName = "carmera_zhutitupian.jpg";
				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
				try {
					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// 把数据写入文件 
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				this.iv_zhutitupian.setImageBitmap(booImageBm);
				this.iv_zhutitupian.setScaleType(ScaleType.FIT_CENTER);
				this.surveyInfo.setZhutitupian(jpgFileName);
			} catch (OutOfMemoryError err) {  }
		}

		if(requestCode == REQ_CODE_SELECT_IMAGE_zhutitupian && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String filename =  bundle.getString("fileName");
			String filepath = HttpUtil.FILE_PATH + "/" + filename;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true; 
			BitmapFactory.decodeFile(filepath, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 128*128);
			opts.inJustDecodeBounds = false; 
			try { 
				Bitmap bm = BitmapFactory.decodeFile(filepath, opts);
				this.iv_zhutitupian.setImageBitmap(bm); 
				this.iv_zhutitupian.setScaleType(ScaleType.FIT_CENTER); 
			} catch (OutOfMemoryError err) {  } 
			surveyInfo.setZhutitupian(filename); 
		}
	}
}
