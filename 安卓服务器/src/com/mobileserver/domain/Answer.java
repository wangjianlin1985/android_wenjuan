package com.mobileserver.domain;

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
    private int selectOptionObj;
    public int getSelectOptionObj() {
        return selectOptionObj;
    }
    public void setSelectOptionObj(int selectOptionObj) {
        this.selectOptionObj = selectOptionObj;
    }

    /*用户*/
    private String userObj;
    public String getUserObj() {
        return userObj;
    }
    public void setUserObj(String userObj) {
        this.userObj = userObj;
    }

}