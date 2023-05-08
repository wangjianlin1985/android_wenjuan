package com.mobileserver.domain;

public class QuestionInfo {
    /*记录编号*/
    private int titileId;
    public int getTitileId() {
        return titileId;
    }
    public void setTitileId(int titileId) {
        this.titileId = titileId;
    }

    /*问卷名称*/
    private int questionPaperObj;
    public int getQuestionPaperObj() {
        return questionPaperObj;
    }
    public void setQuestionPaperObj(int questionPaperObj) {
        this.questionPaperObj = questionPaperObj;
    }

    /*问题内容*/
    private String titleValue;
    public String getTitleValue() {
        return titleValue;
    }
    public void setTitleValue(String titleValue) {
        this.titleValue = titleValue;
    }

}