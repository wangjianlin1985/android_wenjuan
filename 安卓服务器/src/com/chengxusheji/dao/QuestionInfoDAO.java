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
import com.chengxusheji.domain.QuestionInfo;

@Service @Transactional
public class QuestionInfoDAO {

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
    public void AddQuestionInfo(QuestionInfo questionInfo) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(questionInfo);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<QuestionInfo> QueryQuestionInfoInfo(SurveyInfo questionPaperObj,String titleValue,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From QuestionInfo questionInfo where 1=1";
    	if(null != questionPaperObj && questionPaperObj.getPaperId()!=0) hql += " and questionInfo.questionPaperObj.paperId=" + questionPaperObj.getPaperId();
    	if(!titleValue.equals("")) hql = hql + " and questionInfo.titleValue like '%" + titleValue + "%'";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List questionInfoList = q.list();
    	return (ArrayList<QuestionInfo>) questionInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<QuestionInfo> QueryQuestionInfoInfo(SurveyInfo questionPaperObj,String titleValue) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From QuestionInfo questionInfo where 1=1";
    	if(null != questionPaperObj && questionPaperObj.getPaperId()!=0) hql += " and questionInfo.questionPaperObj.paperId=" + questionPaperObj.getPaperId();
    	if(!titleValue.equals("")) hql = hql + " and questionInfo.titleValue like '%" + titleValue + "%'";
    	Query q = s.createQuery(hql);
    	List questionInfoList = q.list();
    	return (ArrayList<QuestionInfo>) questionInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<QuestionInfo> QueryAllQuestionInfoInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From QuestionInfo";
        Query q = s.createQuery(hql);
        List questionInfoList = q.list();
        return (ArrayList<QuestionInfo>) questionInfoList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(SurveyInfo questionPaperObj,String titleValue) {
        Session s = factory.getCurrentSession();
        String hql = "From QuestionInfo questionInfo where 1=1";
        if(null != questionPaperObj && questionPaperObj.getPaperId()!=0) hql += " and questionInfo.questionPaperObj.paperId=" + questionPaperObj.getPaperId();
        if(!titleValue.equals("")) hql = hql + " and questionInfo.titleValue like '%" + titleValue + "%'";
        Query q = s.createQuery(hql);
        List questionInfoList = q.list();
        recordNumber = questionInfoList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public QuestionInfo GetQuestionInfoByTitileId(int titileId) {
        Session s = factory.getCurrentSession();
        QuestionInfo questionInfo = (QuestionInfo)s.get(QuestionInfo.class, titileId);
        return questionInfo;
    }

    /*����QuestionInfo��Ϣ*/
    public void UpdateQuestionInfo(QuestionInfo questionInfo) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(questionInfo);
    }

    /*ɾ��QuestionInfo��Ϣ*/
    public void DeleteQuestionInfo (int titileId) throws Exception {
        Session s = factory.getCurrentSession();
        Object questionInfo = s.load(QuestionInfo.class, titileId);
        s.delete(questionInfo);
    }

}
