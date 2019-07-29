package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Answer;
import com.mobileclient.util.HttpUtil;

/*答卷信息管理业务逻辑层*/
public class AnswerService {
	/* 添加答卷信息 */
	public String AddAnswer(Answer answer) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("answerId", answer.getAnswerId() + "");
		params.put("selectOptionObj", answer.getSelectOptionObj() + "");
		params.put("userObj", answer.getUserObj());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "AnswerServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询答卷信息 */
	public List<Answer> QueryAnswer(Answer queryConditionAnswer) throws Exception {
		String urlString = HttpUtil.BASE_URL + "AnswerServlet?action=query";
		if(queryConditionAnswer != null) {
			urlString += "&selectOptionObj=" + queryConditionAnswer.getSelectOptionObj();
			urlString += "&userObj=" + URLEncoder.encode(queryConditionAnswer.getUserObj(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		AnswerListHandler answerListHander = new AnswerListHandler();
		xr.setContentHandler(answerListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Answer> answerList = answerListHander.getAnswerList();
		return answerList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Answer> answerList = new ArrayList<Answer>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Answer answer = new Answer();
				answer.setAnswerId(object.getInt("answerId"));
				answer.setSelectOptionObj(object.getInt("selectOptionObj"));
				answer.setUserObj(object.getString("userObj"));
				answerList.add(answer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return answerList;
	}

	/* 更新答卷信息 */
	public String UpdateAnswer(Answer answer) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("answerId", answer.getAnswerId() + "");
		params.put("selectOptionObj", answer.getSelectOptionObj() + "");
		params.put("userObj", answer.getUserObj());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "AnswerServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除答卷信息 */
	public String DeleteAnswer(int answerId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("answerId", answerId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "AnswerServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "答卷信息信息删除失败!";
		}
	}

	/* 根据记录编号获取答卷信息对象 */
	public Answer GetAnswer(int answerId)  {
		List<Answer> answerList = new ArrayList<Answer>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("answerId", answerId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "AnswerServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Answer answer = new Answer();
				answer.setAnswerId(object.getInt("answerId"));
				answer.setSelectOptionObj(object.getInt("selectOptionObj"));
				answer.setUserObj(object.getString("userObj"));
				answerList.add(answer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = answerList.size();
		if(size>0) return answerList.get(0); 
		else return null; 
	}
}
