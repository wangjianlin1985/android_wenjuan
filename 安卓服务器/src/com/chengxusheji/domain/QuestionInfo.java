package com.chengxusheji.domain;

import java.sql.Timestamp;
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
    private SurveyInfo questionPaperObj;
    public SurveyInfo getQuestionPaperObj() {
        return questionPaperObj;
    }
    public void setQuestionPaperObj(SurveyInfo questionPaperObj) {
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