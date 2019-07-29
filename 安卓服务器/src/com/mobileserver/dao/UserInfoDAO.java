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
	/* 传入用户信息对象，进行用户信息的添加业务 */
	public String AddUserInfo(UserInfo userInfo) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新用户信息 */
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
			result = "用户信息添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "用户信息添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除用户信息 */
	public String DeleteUserInfo(String userInfoname) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from UserInfo where userInfoname='" + userInfoname + "'";
			db.executeUpdate(sqlString);
			result = "用户信息删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "用户信息删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据用户名获取到用户信息 */
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
	/* 更新用户信息 */
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
			result = "用户信息更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "用户信息更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
