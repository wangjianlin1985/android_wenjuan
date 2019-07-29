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
	/* 传入问题信息对象，进行问题信息的添加业务 */
	public String AddQuestionInfo(QuestionInfo questionInfo) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新问题信息 */
			String sqlString = "insert into QuestionInfo(questionPaperObj,titleValue) values (";
			sqlString += questionInfo.getQuestionPaperObj() + ",";
			sqlString += "'" + questionInfo.getTitleValue() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "问题信息添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "问题信息添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除问题信息 */
	public String DeleteQuestionInfo(int titileId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from QuestionInfo where titileId=" + titileId;
			db.executeUpdate(sqlString);
			result = "问题信息删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "问题信息删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据记录编号获取到问题信息 */
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
	/* 更新问题信息 */
	public String UpdateQuestionInfo(QuestionInfo questionInfo) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update QuestionInfo set ";
			sql += "questionPaperObj=" + questionInfo.getQuestionPaperObj() + ",";
			sql += "titleValue='" + questionInfo.getTitleValue() + "'";
			sql += " where titileId=" + questionInfo.getTitileId();
			db.executeUpdate(sql);
			result = "问题信息更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "问题信息更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
