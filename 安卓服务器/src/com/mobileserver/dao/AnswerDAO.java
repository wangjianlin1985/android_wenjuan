package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.Answer;
import com.mobileserver.util.DB;

public class AnswerDAO {

	public List<Answer> QueryAnswer(int selectOptionObj,String userObj) {
		List<Answer> answerList = new ArrayList<Answer>();
		DB db = new DB();
		String sql = "select * from Answer where 1=1";
		if (selectOptionObj != 0)
			sql += " and selectOptionObj=" + selectOptionObj;
		if (!userObj.equals(""))
			sql += " and userObj = '" + userObj + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				Answer answer = new Answer();
				answer.setAnswerId(rs.getInt("answerId"));
				answer.setSelectOptionObj(rs.getInt("selectOptionObj"));
				answer.setUserObj(rs.getString("userObj"));
				answerList.add(answer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return answerList;
	}
	/* ��������Ϣ���󣬽��д����Ϣ�����ҵ�� */
	public String AddAnswer(Answer answer) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в����´����Ϣ */
			String sqlString = "insert into Answer(selectOptionObj,userObj) values (";
			sqlString += answer.getSelectOptionObj() + ",";
			sqlString += "'" + answer.getUserObj() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "�����Ϣ��ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�����Ϣ���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ�������Ϣ */
	public String DeleteAnswer(int answerId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Answer where answerId=" + answerId;
			db.executeUpdate(sqlString);
			result = "�����Ϣɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�����Ϣɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ���ݼ�¼��Ż�ȡ�������Ϣ */
	public Answer GetAnswer(int answerId) {
		Answer answer = null;
		DB db = new DB();
		String sql = "select * from Answer where answerId=" + answerId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				answer = new Answer();
				answer.setAnswerId(rs.getInt("answerId"));
				answer.setSelectOptionObj(rs.getInt("selectOptionObj"));
				answer.setUserObj(rs.getString("userObj"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return answer;
	}
	/* ���´����Ϣ */
	public String UpdateAnswer(Answer answer) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update Answer set ";
			sql += "selectOptionObj=" + answer.getSelectOptionObj() + ",";
			sql += "userObj='" + answer.getUserObj() + "'";
			sql += " where answerId=" + answer.getAnswerId();
			db.executeUpdate(sql);
			result = "�����Ϣ���³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�����Ϣ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
