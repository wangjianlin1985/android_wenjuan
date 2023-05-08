package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.QuestionInfo;
import com.mobileclient.util.HttpUtil;

/*问题信息管理业务逻辑层*/
public class QuestionInfoService {
	/* 添加问题信息 */
	public String AddQuestionInfo(QuestionInfo questionInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("titileId", questionInfo.getTitileId() + "");
		params.put("questionPaperObj", questionInfo.getQuestionPaperObj() + "");
		params.put("titleValue", questionInfo.getTitleValue());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "QuestionInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询问题信息 */
	public List<QuestionInfo> QueryQuestionInfo(QuestionInfo queryConditionQuestionInfo) throws Exception {
		String urlString = HttpUtil.BASE_URL + "QuestionInfoServlet?action=query";
		if(queryConditionQuestionInfo != null) {
			urlString += "&questionPaperObj=" + queryConditionQuestionInfo.getQuestionPaperObj();
			urlString += "&titleValue=" + URLEncoder.encode(queryConditionQuestionInfo.getTitleValue(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		QuestionInfoListHandler questionInfoListHander = new QuestionInfoListHandler();
		xr.setContentHandler(questionInfoListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<QuestionInfo> questionInfoList = questionInfoListHander.getQuestionInfoList();
		return questionInfoList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<QuestionInfo> questionInfoList = new ArrayList<QuestionInfo>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				QuestionInfo questionInfo = new QuestionInfo();
				questionInfo.setTitileId(object.getInt("titileId"));
				questionInfo.setQuestionPaperObj(object.getInt("questionPaperObj"));
				questionInfo.setTitleValue(object.getString("titleValue"));
				questionInfoList.add(questionInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return questionInfoList;
	}

	/* 更新问题信息 */
	public String UpdateQuestionInfo(QuestionInfo questionInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("titileId", questionInfo.getTitileId() + "");
		params.put("questionPaperObj", questionInfo.getQuestionPaperObj() + "");
		params.put("titleValue", questionInfo.getTitleValue());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "QuestionInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除问题信息 */
	public String DeleteQuestionInfo(int titileId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("titileId", titileId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "QuestionInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "问题信息信息删除失败!";
		}
	}

	/* 根据记录编号获取问题信息对象 */
	public QuestionInfo GetQuestionInfo(int titileId)  {
		List<QuestionInfo> questionInfoList = new ArrayList<QuestionInfo>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("titileId", titileId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "QuestionInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				QuestionInfo questionInfo = new QuestionInfo();
				questionInfo.setTitileId(object.getInt("titileId"));
				questionInfo.setQuestionPaperObj(object.getInt("questionPaperObj"));
				questionInfo.setTitleValue(object.getString("titleValue"));
				questionInfoList.add(questionInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = questionInfoList.size();
		if(size>0) return questionInfoList.get(0); 
		else return null; 
	}
}
