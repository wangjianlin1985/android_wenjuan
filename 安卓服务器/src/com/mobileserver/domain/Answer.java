package com.mobileserver.domain;

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
    private int selectOptionObj;
    public int getSelectOptionObj() {
        return selectOptionObj;
    }
    public void setSelectOptionObj(int selectOptionObj) {
        this.selectOptionObj = selectOptionObj;
    }

    /*�û�*/
    private String userObj;
    public String getUserObj() {
        return userObj;
    }
    public void setUserObj(String userObj) {
        this.userObj = userObj;
    }

}