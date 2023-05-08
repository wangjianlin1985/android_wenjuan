package com.chengxusheji.dao;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.chengxusheji.domain.SurveyInfo;

@Service @Transactional
public class SurveyInfoDAO {

	@Resource SessionFactory factory;
    /*ÿҳ��ʾ��¼��Ŀ*/
    private final int PAGE_SIZE = 10;

    /*�����ѯ���ܵ�ҳ��*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*�����ѯ�����ܼ�¼��*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*���ͼ����Ϣ*/
    public void AddSurveyInfo(SurveyInfo surveyInfo) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(surveyInfo);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SurveyInfo> QuerySurveyInfoInfo(String questionPaperName,String faqiren,String startDate,String endDate,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From SurveyInfo surveyInfo where 1=1";
    	if(!questionPaperName.equals("")) hql = hql + " and surveyInfo.questionPaperName like '%" + questionPaperName + "%'";
    	if(!faqiren.equals("")) hql = hql + " and surveyInfo.faqiren like '%" + faqiren + "%'";
    	if(!startDate.equals("")) hql = hql + " and surveyInfo.startDate like '%" + startDate + "%'";
    	if(!endDate.equals("")) hql = hql + " and surveyInfo.endDate like '%" + endDate + "%'";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List surveyInfoList = q.list();
    	return (ArrayList<SurveyInfo>) surveyInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SurveyInfo> QuerySurveyInfoInfo(String questionPaperName,String faqiren,String startDate,String endDate) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From SurveyInfo surveyInfo where 1=1";
    	if(!questionPaperName.equals("")) hql = hql + " and surveyInfo.questionPaperName like '%" + questionPaperName + "%'";
    	if(!faqiren.equals("")) hql = hql + " and surveyInfo.faqiren like '%" + faqiren + "%'";
    	if(!startDate.equals("")) hql = hql + " and surveyInfo.startDate like '%" + startDate + "%'";
    	if(!endDate.equals("")) hql = hql + " and surveyInfo.endDate like '%" + endDate + "%'";
    	Query q = s.createQuery(hql);
    	List surveyInfoList = q.list();
    	return (ArrayList<SurveyInfo>) surveyInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SurveyInfo> QueryAllSurveyInfoInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From SurveyInfo";
        Query q = s.createQuery(hql);
        List surveyInfoList = q.list();
        return (ArrayList<SurveyInfo>) surveyInfoList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String questionPaperName,String faqiren,String startDate,String endDate) {
        Session s = factory.getCurrentSession();
        String hql = "From SurveyInfo surveyInfo where 1=1";
        if(!questionPaperName.equals("")) hql = hql + " and surveyInfo.questionPaperName like '%" + questionPaperName + "%'";
        if(!faqiren.equals("")) hql = hql + " and surveyInfo.faqiren like '%" + faqiren + "%'";
        if(!startDate.equals("")) hql = hql + " and surveyInfo.startDate like '%" + startDate + "%'";
        if(!endDate.equals("")) hql = hql + " and surveyInfo.endDate like '%" + endDate + "%'";
        Query q = s.createQuery(hql);
        List surveyInfoList = q.list();
        recordNumber = surveyInfoList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public SurveyInfo GetSurveyInfoByPaperId(int paperId) {
        Session s = factory.getCurrentSession();
        SurveyInfo surveyInfo = (SurveyInfo)s.get(SurveyInfo.class, paperId);
        return surveyInfo;
    }

    /*����SurveyInfo��Ϣ*/
    public void UpdateSurveyInfo(SurveyInfo surveyInfo) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(surveyInfo);
    }

    /*ɾ��SurveyInfo��Ϣ*/
    public void DeleteSurveyInfo (int paperId) throws Exception {
        Session s = factory.getCurrentSession();
        Object surveyInfo = s.load(SurveyInfo.class, paperId);
        s.delete(surveyInfo);
    }

}
