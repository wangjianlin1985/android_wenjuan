package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.SelectOption;
import com.mobileserver.util.DB;

public class SelectOptionDAO {

	public List<SelectOption> QuerySelectOption(int questionObj,String optionContent) {
		List<SelectOption> selectOptionList = new ArrayList<SelectOption>();
		DB db = new DB();
		String sql = "select * from SelectOption where 1=1";
		if (questionObj != 0)
			sql += " and questionObj=" + questionObj;
		if (!optionContent.equals(""))
			sql += " and optionContent like '%" + optionContent + "%'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				SelectOption selectOption = new SelectOption();
				selectOption.setOptionId(rs.getInt("optionId"));
				selectOption.setQuestionObj(rs.getInt("questionObj"));
				selectOption.setOptionContent(rs.getString("optionContent"));
				selectOptionList.add(selectOption);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return selectOptionList;
	}
	/* ����ѡ����Ϣ���󣬽���ѡ����Ϣ�����ҵ�� */
	public String AddSelectOption(SelectOption selectOption) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в�����ѡ����Ϣ */
			String sqlString = "insert into SelectOption(questionObj,optionContent) values (";
			sqlString += selectOption.getQuestionObj() + ",";
			sqlString += "'" + selectOption.getOptionContent() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "ѡ����Ϣ��ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "ѡ����Ϣ���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ��ѡ����Ϣ */
	public String DeleteSelectOption(int optionId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from SelectOption where optionId=" + optionId;
			db.executeUpdate(sqlString);
			result = "ѡ����Ϣɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "ѡ����Ϣɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ���ݼ�¼��Ż�ȡ��ѡ����Ϣ */
	public SelectOption GetSelectOption(int optionId) {
		SelectOption selectOption = null;
		DB db = new DB();
		String sql = "select * from SelectOption where optionId=" + optionId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				selectOption = new SelectOption();
				selectOption.setOptionId(rs.getInt("optionId"));
				selectOption.setQuestionObj(rs.getInt("questionObj"));
				selectOption.setOptionContent(rs.getString("optionContent"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return selectOption;
	}
	/* ����ѡ����Ϣ */
	public String UpdateSelectOption(SelectOption selectOption) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update SelectOption set ";
			sql += "questionObj=" + selectOption.getQuestionObj() + ",";
			sql += "optionContent='" + selectOption.getOptionContent() + "'";
			sql += " where optionId=" + selectOption.getOptionId();
			db.executeUpdate(sql);
			result = "ѡ����Ϣ���³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "ѡ����Ϣ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
