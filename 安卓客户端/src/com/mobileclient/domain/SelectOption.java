package com.mobileclient.domain;

import java.io.Serializable;

public class SelectOption implements Serializable {
    /*记录编号*/
    private int optionId;
    public int getOptionId() {
        return optionId;
    }
    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    /*问题信息*/
    private int questionObj;
    public int getQuestionObj() {
        return questionObj;
    }
    public void setQuestionObj(int questionObj) {
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