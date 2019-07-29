package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.SelectOption;
import com.mobileclient.util.HttpUtil;

/*选项信息管理业务逻辑层*/
public class SelectOptionService {
	/* 添加选项信息 */
	public String AddSelectOption(SelectOption selectOption) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("optionId", selectOption.getOptionId() + "");
		params.put("questionObj", selectOption.getQuestionObj() + "");
		params.put("optionContent", selectOption.getOptionContent());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SelectOptionServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询选项信息 */
	public List<SelectOption> QuerySelectOption(SelectOption queryConditionSelectOption) throws Exception {
		String urlString = HttpUtil.BASE_URL + "SelectOptionServlet?action=query";
		if(queryConditionSelectOption != null) {
			urlString += "&questionObj=" + queryConditionSelectOption.getQuestionObj();
			urlString += "&optionContent=" + URLEncoder.encode(queryConditionSelectOption.getOptionContent(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		SelectOptionListHandler selectOptionListHander = new SelectOptionListHandler();
		xr.setContentHandler(selectOptionListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<SelectOption> selectOptionList = selectOptionListHander.getSelectOptionList();
		return selectOptionList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<SelectOption> selectOptionList = new ArrayList<SelectOption>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				SelectOption selectOption = new SelectOption();
				selectOption.setOptionId(object.getInt("optionId"));
				selectOption.setQuestionObj(object.getInt("questionObj"));
				selectOption.setOptionContent(object.getString("optionContent"));
				selectOptionList.add(selectOption);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectOptionList;
	}

	/* 更新选项信息 */
	public String UpdateSelectOption(SelectOption selectOption) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("optionId", selectOption.getOptionId() + "");
		params.put("questionObj", selectOption.getQuestionObj() + "");
		params.put("optionContent", selectOption.getOptionContent());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SelectOptionServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除选项信息 */
	public String DeleteSelectOption(int optionId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("optionId", optionId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SelectOptionServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "选项信息信息删除失败!";
		}
	}

	/* 根据记录编号获取选项信息对象 */
	public SelectOption GetSelectOption(int optionId)  {
		List<SelectOption> selectOptionList = new ArrayList<SelectOption>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("optionId", optionId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SelectOptionServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				SelectOption selectOption = new SelectOption();
				selectOption.setOptionId(object.getInt("optionId"));
				selectOption.setQuestionObj(object.getInt("questionObj"));
				selectOption.setOptionContent(object.getString("optionContent"));
				selectOptionList.add(selectOption);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = selectOptionList.size();
		if(size>0) return selectOptionList.get(0); 
		else return null; 
	}
}
