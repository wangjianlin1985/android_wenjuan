package com.chengxusheji.domain;

import java.sql.Timestamp;
public class SelectOption {
    /*��¼���*/
    private int optionId;
    public int getOptionId() {
        return optionId;
    }
    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    /*������Ϣ*/
    private QuestionInfo questionObj;
    public QuestionInfo getQuestionObj() {
        return questionObj;
    }
    public void setQuestionObj(QuestionInfo questionObj) {
        this.questionObj = questionObj;
    }

    /*ѡ������*/
    private String optionContent;
    public String getOptionContent() {
        return optionContent;
    }
    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }

}