package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.UserInfo;
import com.mobileserver.util.DB;

public class UserInfoDAO {

	public List<UserInfo> QueryUserInfo(String userInfoname,String name,Timestamp birthday,String telephone) {
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		DB db = new DB();
		String sql = "select * from UserInfo where 1=1";
		if (!userInfoname.equals(""))
			sql += " and userInfoname like '%" + userInfoname + "%'";
		if (!name.equals(""))
			sql += " and name like '%" + name + "%'";
		if(birthday!=null)
			sql += " and birthday='" + birthday + "'";
		if (!telephone.equals(""))
			sql += " and telephone like '%" + telephone + "%'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				UserInfo userInfo = new UserInfo();
				userInfo.setUserInfoname(rs.getString("userInfoname"));
				userInfo.setPassword(rs.getString("password"));
				userInfo.setName(rs.getString("name"));
				userInfo.setSex(rs.getString("sex"));
				userInfo.setBirthday(rs.getTimestamp("birthday"));
				userInfo.setTelephone(rs.getString("telephone"));
				userInfo.setEmail(rs.getString("email"));
				userInfo.setAddress(rs.getString("address"));
				userInfo.setUserPhoto(rs.getString("userPhoto"));
				userInfoList.add(userInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return userInfoList;
	}
	/* �����û���Ϣ���󣬽����û���Ϣ�����ҵ�� */
	public String AddUserInfo(UserInfo userInfo) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в������û���Ϣ */
			String sqlString = "insert into UserInfo(userInfoname,password,name,sex,birthday,telephone,email,address,userPhoto) values (";
			sqlString += "'" + userInfo.getUserInfoname() + "',";
			sqlString += "'" + userInfo.getPassword() + "',";
			sqlString += "'" + userInfo.getName() + "',";
			sqlString += "'" + userInfo.getSex() + "',";
			sqlString += "'" + userInfo.getBirthday() + "',";
			sqlString += "'" + userInfo.getTelephone() + "',";
			sqlString += "'" + userInfo.getEmail() + "',";
			sqlString += "'" + userInfo.getAddress() + "',";
			sqlString += "'" + userInfo.getUserPhoto() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "�û���Ϣ��ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�û���Ϣ���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ���û���Ϣ */
	public String DeleteUserInfo(String userInfoname) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from UserInfo where userInfoname='" + userInfoname + "'";
			db.executeUpdate(sqlString);
			result = "�û���Ϣɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�û���Ϣɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* �����û�����ȡ���û���Ϣ */
	public UserInfo GetUserInfo(String userInfoname) {
		UserInfo userInfo = null;
		DB db = new DB();
		String sql = "select * from UserInfo where userInfoname='" + userInfoname + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				userInfo = new UserInfo();
				userInfo.setUserInfoname(rs.getString("userInfoname"));
				userInfo.setPassword(rs.getString("password"));
				userInfo.setName(rs.getString("name"));
				userInfo.setSex(rs.getString("sex"));
				userInfo.setBirthday(rs.getTimestamp("birthday"));
				userInfo.setTelephone(rs.getString("telephone"));
				userInfo.setEmail(rs.getString("email"));
				userInfo.setAddress(rs.getString("address"));
				userInfo.setUserPhoto(rs.getString("userPhoto"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return userInfo;
	}
	/* �����û���Ϣ */
	public String UpdateUserInfo(UserInfo userInfo) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update UserInfo set ";
			sql += "password='" + userInfo.getPassword() + "',";
			sql += "name='" + userInfo.getName() + "',";
			sql += "sex='" + userInfo.getSex() + "',";
			sql += "birthday='" + userInfo.getBirthday() + "',";
			sql += "telephone='" + userInfo.getTelephone() + "',";
			sql += "email='" + userInfo.getEmail() + "',";
			sql += "address='" + userInfo.getAddress() + "',";
			sql += "userPhoto='" + userInfo.getUserPhoto() + "'";
			sql += " where userInfoname='" + userInfo.getUserInfoname() + "'";
			db.executeUpdate(sql);
			result = "�û���Ϣ���³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�û���Ϣ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
