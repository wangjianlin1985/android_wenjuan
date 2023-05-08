package com.chengxusheji.domain;

import java.sql.Timestamp;
public class Answer {
    /*记录编号*/
    private int answerId;
    public int getAnswerId() {
        return answerId;
    }
    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    /*选项信息*/
    private SelectOption selectOptionObj;
    public SelectOption getSelectOptionObj() {
        return selectOptionObj;
    }
    public void setSelectOptionObj(SelectOption selectOptionObj) {
        this.selectOptionObj = selectOptionObj;
    }

    /*用户*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

}