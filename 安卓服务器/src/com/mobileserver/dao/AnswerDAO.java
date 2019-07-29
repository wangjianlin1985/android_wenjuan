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
	/* 传入答卷信息对象，进行答卷信息的添加业务 */
	public String AddAnswer(Answer answer) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新答卷信息 */
			String sqlString = "insert into Answer(selectOptionObj,userObj) values (";
			sqlString += answer.getSelectOptionObj() + ",";
			sqlString += "'" + answer.getUserObj() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "答卷信息添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "答卷信息添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除答卷信息 */
	public String DeleteAnswer(int answerId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Answer where answerId=" + answerId;
			db.executeUpdate(sqlString);
			result = "答卷信息删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "答卷信息删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据记录编号获取到答卷信息 */
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
	/* 更新答卷信息 */
	public String UpdateAnswer(Answer answer) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update Answer set ";
			sql += "selectOptionObj=" + answer.getSelectOptionObj() + ",";
			sql += "userObj='" + answer.getUserObj() + "'";
			sql += " where answerId=" + answer.getAnswerId();
			db.executeUpdate(sql);
			result = "答卷信息更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "答卷信息更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
