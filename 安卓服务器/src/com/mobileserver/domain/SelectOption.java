package com.mobileserver.domain;

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
    private int questionObj;
    public int getQuestionObj() {
        return questionObj;
    }
    public void setQuestionObj(int questionObj) {
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