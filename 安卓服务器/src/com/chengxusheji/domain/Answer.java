package com.chengxusheji.domain;

import java.sql.Timestamp;
public class Answer {
    /*��¼���*/
    private int answerId;
    public int getAnswerId() {
        return answerId;
    }
    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    /*ѡ����Ϣ*/
    private SelectOption selectOptionObj;
    public SelectOption getSelectOptionObj() {
        return selectOptionObj;
    }
    public void setSelectOptionObj(SelectOption selectOptionObj) {
        this.selectOptionObj = selectOptionObj;
    }

    /*�û�*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

}