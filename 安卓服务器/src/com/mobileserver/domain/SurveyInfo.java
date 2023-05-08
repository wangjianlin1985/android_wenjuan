package com.mobileserver.domain;

public class SurveyInfo {
    /*记录编号*/
    private int paperId;
    public int getPaperId() {
        return paperId;
    }
    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    /*问卷名称*/
    private String questionPaperName;
    public String getQuestionPaperName() {
        return questionPaperName;
    }
    public void setQuestionPaperName(String questionPaperName) {
        this.questionPaperName = questionPaperName;
    }

    /*发起人*/
    private String faqiren;
    public String getFaqiren() {
        return faqiren;
    }
    public void setFaqiren(String faqiren) {
        this.faqiren = faqiren;
    }

    /*问卷描述*/
    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /*发起日期*/
    private java.sql.Timestamp startDate;
    public java.sql.Timestamp getStartDate() {
        return startDate;
    }
    public void setStartDate(java.sql.Timestamp startDate) {
        this.startDate = startDate;
    }

    /*结束日期*/
    private java.sql.Timestamp endDate;
    public java.sql.Timestamp getEndDate() {
        return endDate;
    }
    public void setEndDate(java.sql.Timestamp endDate) {
        this.endDate = endDate;
    }

    /*主题图片*/
    private String zhutitupian;
    public String getZhutitupian() {
        return zhutitupian;
    }
    public void setZhutitupian(String zhutitupian) {
        this.zhutitupian = zhutitupian;
    }

    /*审核标志*/
    private int publishFlag;
    public int getPublishFlag() {
        return publishFlag;
    }
    public void setPublishFlag(int publishFlag) {
        this.publishFlag = publishFlag;
    }

}