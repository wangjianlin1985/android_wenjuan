package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.SurveyInfo;
import com.mobileserver.util.DB;

public class SurveyInfoDAO {

	public List<SurveyInfo> QuerySurveyInfo(String questionPaperName,String faqiren,Timestamp startDate,Timestamp endDate) {
		List<SurveyInfo> surveyInfoList = new ArrayList<SurveyInfo>();
		DB db = new DB();
		String sql = "select * from SurveyInfo where 1=1";
		if (!questionPaperName.equals(""))
			sql += " and questionPaperName like '%" + questionPaperName + "%'";
		if (!faqiren.equals(""))
			sql += " and faqiren like '%" + faqiren + "%'";
		if(startDate!=null)
			sql += " and startDate='" + startDate + "'";
		if(endDate!=null)
			sql += " and endDate='" + endDate + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				SurveyInfo surveyInfo = new SurveyInfo();
				surveyInfo.setPaperId(rs.getInt("paperId"));
				surveyInfo.setQuestionPaperName(rs.getString("questionPaperName"));
				surveyInfo.setFaqiren(rs.getString("faqiren"));
				surveyInfo.setDescription(rs.getString("description"));
				surveyInfo.setStartDate(rs.getTimestamp("startDate"));
				surveyInfo.setEndDate(rs.getTimestamp("endDate"));
				surveyInfo.setZhutitupian(rs.getString("zhutitupian"));
				surveyInfo.setPublishFlag(rs.getInt("publishFlag"));
				surveyInfoList.add(surveyInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return surveyInfoList;
	}
	/* 传入问卷信息对象，进行问卷信息的添加业务 */
	public String AddSurveyInfo(SurveyInfo surveyInfo) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新问卷信息 */
			String sqlString = "insert into SurveyInfo(questionPaperName,faqiren,description,startDate,endDate,zhutitupian,publishFlag) values (";
			sqlString += "'" + surveyInfo.getQuestionPaperName() + "',";
			sqlString += "'" + surveyInfo.getFaqiren() + "',";
			sqlString += "'" + surveyInfo.getDescription() + "',";
			sqlString += "'" + surveyInfo.getStartDate() + "',";
			sqlString += "'" + surveyInfo.getEndDate() + "',";
			sqlString += "'" + surveyInfo.getZhutitupian() + "',";
			sqlString += surveyInfo.getPublishFlag();
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "问卷信息添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "问卷信息添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除问卷信息 */
	public String DeleteSurveyInfo(int paperId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from SurveyInfo where paperId=" + paperId;
			db.executeUpdate(sqlString);
			result = "问卷信息删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "问卷信息删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据记录编号获取到问卷信息 */
	public SurveyInfo GetSurveyInfo(int paperId) {
		SurveyInfo surveyInfo = null;
		DB db = new DB();
		String sql = "select * from SurveyInfo where paperId=" + paperId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				surveyInfo = new SurveyInfo();
				surveyInfo.setPaperId(rs.getInt("paperId"));
				surveyInfo.setQuestionPaperName(rs.getString("questionPaperName"));
				surveyInfo.setFaqiren(rs.getString("faqiren"));
				surveyInfo.setDescription(rs.getString("description"));
				surveyInfo.setStartDate(rs.getTimestamp("startDate"));
				surveyInfo.setEndDate(rs.getTimestamp("endDate"));
				surveyInfo.setZhutitupian(rs.getString("zhutitupian"));
				surveyInfo.setPublishFlag(rs.getInt("publishFlag"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return surveyInfo;
	}
	/* 更新问卷信息 */
	public String UpdateSurveyInfo(SurveyInfo surveyInfo) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update SurveyInfo set ";
			sql += "questionPaperName='" + surveyInfo.getQuestionPaperName() + "',";
			sql += "faqiren='" + surveyInfo.getFaqiren() + "',";
			sql += "description='" + surveyInfo.getDescription() + "',";
			sql += "startDate='" + surveyInfo.getStartDate() + "',";
			sql += "endDate='" + surveyInfo.getEndDate() + "',";
			sql += "zhutitupian='" + surveyInfo.getZhutitupian() + "',";
			sql += "publishFlag=" + surveyInfo.getPublishFlag();
			sql += " where paperId=" + surveyInfo.getPaperId();
			db.executeUpdate(sql);
			result = "问卷信息更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "问卷信息更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
