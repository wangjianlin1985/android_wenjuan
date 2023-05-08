package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.QuestionInfo;
import com.mobileserver.util.DB;

public class QuestionInfoDAO {

	public List<QuestionInfo> QueryQuestionInfo(int questionPaperObj,String titleValue) {
		List<QuestionInfo> questionInfoList = new ArrayList<QuestionInfo>();
		DB db = new DB();
		String sql = "select * from QuestionInfo where 1=1";
		if (questionPaperObj != 0)
			sql += " and questionPaperObj=" + questionPaperObj;
		if (!titleValue.equals(""))
			sql += " and titleValue like '%" + titleValue + "%'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				QuestionInfo questionInfo = new QuestionInfo();
				questionInfo.setTitileId(rs.getInt("titileId"));
				questionInfo.setQuestionPaperObj(rs.getInt("questionPaperObj"));
				questionInfo.setTitleValue(rs.getString("titleValue"));
				questionInfoList.add(questionInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return questionInfoList;
	}
	/* ����������Ϣ���󣬽���������Ϣ�����ҵ�� */
	public String AddQuestionInfo(QuestionInfo questionInfo) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в�����������Ϣ */
			String sqlString = "insert into QuestionInfo(questionPaperObj,titleValue) values (";
			sqlString += questionInfo.getQuestionPaperObj() + ",";
			sqlString += "'" + questionInfo.getTitleValue() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "������Ϣ��ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Ϣ���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ��������Ϣ */
	public String DeleteQuestionInfo(int titileId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from QuestionInfo where titileId=" + titileId;
			db.executeUpdate(sqlString);
			result = "������Ϣɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Ϣɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ���ݼ�¼��Ż�ȡ��������Ϣ */
	public QuestionInfo GetQuestionInfo(int titileId) {
		QuestionInfo questionInfo = null;
		DB db = new DB();
		String sql = "select * from QuestionInfo where titileId=" + titileId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				questionInfo = new QuestionInfo();
				questionInfo.setTitileId(rs.getInt("titileId"));
				questionInfo.setQuestionPaperObj(rs.getInt("questionPaperObj"));
				questionInfo.setTitleValue(rs.getString("titleValue"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return questionInfo;
	}
	/* ����������Ϣ */
	public String UpdateQuestionInfo(QuestionInfo questionInfo) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update QuestionInfo set ";
			sql += "questionPaperObj=" + questionInfo.getQuestionPaperObj() + ",";
			sql += "titleValue='" + questionInfo.getTitleValue() + "'";
			sql += " where titileId=" + questionInfo.getTitileId();
			db.executeUpdate(sql);
			result = "������Ϣ���³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Ϣ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
