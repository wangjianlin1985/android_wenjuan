package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.SurveyInfo;
import com.mobileclient.util.HttpUtil;

/*问卷信息管理业务逻辑层*/
public class SurveyInfoService {
	/* 添加问卷信息 */
	public String AddSurveyInfo(SurveyInfo surveyInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("paperId", surveyInfo.getPaperId() + "");
		params.put("questionPaperName", surveyInfo.getQuestionPaperName());
		params.put("faqiren", surveyInfo.getFaqiren());
		params.put("description", surveyInfo.getDescription());
		params.put("startDate", surveyInfo.getStartDate().toString());
		params.put("endDate", surveyInfo.getEndDate().toString());
		params.put("zhutitupian", surveyInfo.getZhutitupian());
		params.put("publishFlag", surveyInfo.getPublishFlag() + "");
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SurveyInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询问卷信息 */
	public List<SurveyInfo> QuerySurveyInfo(SurveyInfo queryConditionSurveyInfo) throws Exception {
		String urlString = HttpUtil.BASE_URL + "SurveyInfoServlet?action=query";
		if(queryConditionSurveyInfo != null) {
			urlString += "&questionPaperName=" + URLEncoder.encode(queryConditionSurveyInfo.getQuestionPaperName(), "UTF-8") + "";
			urlString += "&faqiren=" + URLEncoder.encode(queryConditionSurveyInfo.getFaqiren(), "UTF-8") + "";
			if(queryConditionSurveyInfo.getStartDate() != null) {
				urlString += "&startDate=" + URLEncoder.encode(queryConditionSurveyInfo.getStartDate().toString(), "UTF-8");
			}
			if(queryConditionSurveyInfo.getEndDate() != null) {
				urlString += "&endDate=" + URLEncoder.encode(queryConditionSurveyInfo.getEndDate().toString(), "UTF-8");
			}
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		SurveyInfoListHandler surveyInfoListHander = new SurveyInfoListHandler();
		xr.setContentHandler(surveyInfoListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<SurveyInfo> surveyInfoList = surveyInfoListHander.getSurveyInfoList();
		return surveyInfoList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<SurveyInfo> surveyInfoList = new ArrayList<SurveyInfo>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				SurveyInfo surveyInfo = new SurveyInfo();
				surveyInfo.setPaperId(object.getInt("paperId"));
				surveyInfo.setQuestionPaperName(object.getString("questionPaperName"));
				surveyInfo.setFaqiren(object.getString("faqiren"));
				surveyInfo.setDescription(object.getString("description"));
				surveyInfo.setStartDate(Timestamp.valueOf(object.getString("startDate")));
				surveyInfo.setEndDate(Timestamp.valueOf(object.getString("endDate")));
				surveyInfo.setZhutitupian(object.getString("zhutitupian"));
				surveyInfo.setPublishFlag(object.getInt("publishFlag"));
				surveyInfoList.add(surveyInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return surveyInfoList;
	}

	/* 更新问卷信息 */
	public String UpdateSurveyInfo(SurveyInfo surveyInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("paperId", surveyInfo.getPaperId() + "");
		params.put("questionPaperName", surveyInfo.getQuestionPaperName());
		params.put("faqiren", surveyInfo.getFaqiren());
		params.put("description", surveyInfo.getDescription());
		params.put("startDate", surveyInfo.getStartDate().toString());
		params.put("endDate", surveyInfo.getEndDate().toString());
		params.put("zhutitupian", surveyInfo.getZhutitupian());
		params.put("publishFlag", surveyInfo.getPublishFlag() + "");
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SurveyInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除问卷信息 */
	public String DeleteSurveyInfo(int paperId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("paperId", paperId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SurveyInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "问卷信息信息删除失败!";
		}
	}

	/* 根据记录编号获取问卷信息对象 */
	public SurveyInfo GetSurveyInfo(int paperId)  {
		List<SurveyInfo> surveyInfoList = new ArrayList<SurveyInfo>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("paperId", paperId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SurveyInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				SurveyInfo surveyInfo = new SurveyInfo();
				surveyInfo.setPaperId(object.getInt("paperId"));
				surveyInfo.setQuestionPaperName(object.getString("questionPaperName"));
				surveyInfo.setFaqiren(object.getString("faqiren"));
				surveyInfo.setDescription(object.getString("description"));
				surveyInfo.setStartDate(Timestamp.valueOf(object.getString("startDate")));
				surveyInfo.setEndDate(Timestamp.valueOf(object.getString("endDate")));
				surveyInfo.setZhutitupian(object.getString("zhutitupian"));
				surveyInfo.setPublishFlag(object.getInt("publishFlag"));
				surveyInfoList.add(surveyInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = surveyInfoList.size();
		if(size>0) return surveyInfoList.get(0); 
		else return null; 
	}
}
