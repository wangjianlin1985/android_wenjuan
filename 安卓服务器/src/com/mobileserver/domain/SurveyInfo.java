package com.mobileserver.domain;

public class SurveyInfo {
    /*��¼���*/
    private int paperId;
    public int getPaperId() {
        return paperId;
    }
    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    /*�ʾ�����*/
    private String questionPaperName;
    public String getQuestionPaperName() {
        return questionPaperName;
    }
    public void setQuestionPaperName(String questionPaperName) {
        this.questionPaperName = questionPaperName;
    }

    /*������*/
    private String faqiren;
    public String getFaqiren() {
        return faqiren;
    }
    public void setFaqiren(String faqiren) {
        this.faqiren = faqiren;
    }

    /*�ʾ�����*/
    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /*��������*/
    private java.sql.Timestamp startDate;
    public java.sql.Timestamp getStartDate() {
        return startDate;
    }
    public void setStartDate(java.sql.Timestamp startDate) {
        this.startDate = startDate;
    }

    /*��������*/
    private java.sql.Timestamp endDate;
    public java.sql.Timestamp getEndDate() {
        return endDate;
    }
    public void setEndDate(java.sql.Timestamp endDate) {
        this.endDate = endDate;
    }

    /*����ͼƬ*/
    private String zhutitupian;
    public String getZhutitupian() {
        return zhutitupian;
    }
    public void setZhutitupian(String zhutitupian) {
        this.zhutitupian = zhutitupian;
    }

    /*��˱�־*/
    private int publishFlag;
    public int getPublishFlag() {
        return publishFlag;
    }
    public void setPublishFlag(int publishFlag) {
        this.publishFlag = publishFlag;
    }

}