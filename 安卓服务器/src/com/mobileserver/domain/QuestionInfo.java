package com.mobileserver.domain;

public class QuestionInfo {
    /*��¼���*/
    private int titileId;
    public int getTitileId() {
        return titileId;
    }
    public void setTitileId(int titileId) {
        this.titileId = titileId;
    }

    /*�ʾ�����*/
    private int questionPaperObj;
    public int getQuestionPaperObj() {
        return questionPaperObj;
    }
    public void setQuestionPaperObj(int questionPaperObj) {
        this.questionPaperObj = questionPaperObj;
    }

    /*��������*/
    private String titleValue;
    public String getTitleValue() {
        return titleValue;
    }
    public void setTitleValue(String titleValue) {
        this.titleValue = titleValue;
    }

}