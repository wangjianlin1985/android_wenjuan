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
	/* �����ʾ���Ϣ���󣬽����ʾ���Ϣ�����ҵ�� */
	public String AddSurveyInfo(SurveyInfo surveyInfo) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в������ʾ���Ϣ */
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
			result = "�ʾ���Ϣ��ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�ʾ���Ϣ���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ���ʾ���Ϣ */
	public String DeleteSurveyInfo(int paperId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from SurveyInfo where paperId=" + paperId;
			db.executeUpdate(sqlString);
			result = "�ʾ���Ϣɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�ʾ���Ϣɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ���ݼ�¼��Ż�ȡ���ʾ���Ϣ */
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
	/* �����ʾ���Ϣ */
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
			result = "�ʾ���Ϣ���³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�ʾ���Ϣ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
