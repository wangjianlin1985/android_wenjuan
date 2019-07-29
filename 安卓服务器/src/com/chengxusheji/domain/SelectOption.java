package com.chengxusheji.domain;

import java.sql.Timestamp;
public class SelectOption {
    /*记录编号*/
    private int optionId;
    public int getOptionId() {
        return optionId;
    }
    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    /*问题信息*/
    private QuestionInfo questionObj;
    public QuestionInfo getQuestionObj() {
        return questionObj;
    }
    public void setQuestionObj(QuestionInfo questionObj) {
        this.questionObj = questionObj;
    }

    /*选项内容*/
    private String optionContent;
    public String getOptionContent() {
        return optionContent;
    }
    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }

}