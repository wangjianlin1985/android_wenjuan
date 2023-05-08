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
import com.chengxusheji.domain.SelectOption;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.domain.Answer;

@Service @Transactional
public class AnswerDAO {

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
    public void AddAnswer(Answer answer) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(answer);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Answer> QueryAnswerInfo(SelectOption selectOptionObj,UserInfo userObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Answer answer where 1=1";
    	if(null != selectOptionObj && selectOptionObj.getOptionId()!=0) hql += " and answer.selectOptionObj.optionId=" + selectOptionObj.getOptionId();
    	if(null != userObj && !userObj.getUserInfoname().equals("")) hql += " and answer.userObj.userInfoname='" + userObj.getUserInfoname() + "'";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List answerList = q.list();
    	return (ArrayList<Answer>) answerList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Answer> QueryAnswerInfo(SelectOption selectOptionObj,UserInfo userObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Answer answer where 1=1";
    	if(null != selectOptionObj && selectOptionObj.getOptionId()!=0) hql += " and answer.selectOptionObj.optionId=" + selectOptionObj.getOptionId();
    	if(null != userObj && !userObj.getUserInfoname().equals("")) hql += " and answer.userObj.userInfoname='" + userObj.getUserInfoname() + "'";
    	Query q = s.createQuery(hql);
    	List answerList = q.list();
    	return (ArrayList<Answer>) answerList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Answer> QueryAllAnswerInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From Answer";
        Query q = s.createQuery(hql);
        List answerList = q.list();
        return (ArrayList<Answer>) answerList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(SelectOption selectOptionObj,UserInfo userObj) {
        Session s = factory.getCurrentSession();
        String hql = "From Answer answer where 1=1";
        if(null != selectOptionObj && selectOptionObj.getOptionId()!=0) hql += " and answer.selectOptionObj.optionId=" + selectOptionObj.getOptionId();
        if(null != userObj && !userObj.getUserInfoname().equals("")) hql += " and answer.userObj.userInfoname='" + userObj.getUserInfoname() + "'";
        Query q = s.createQuery(hql);
        List answerList = q.list();
        recordNumber = answerList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Answer GetAnswerByAnswerId(int answerId) {
        Session s = factory.getCurrentSession();
        Answer answer = (Answer)s.get(Answer.class, answerId);
        return answer;
    }

    /*����Answer��Ϣ*/
    public void UpdateAnswer(Answer answer) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(answer);
    }

    /*ɾ��Answer��Ϣ*/
    public void DeleteAnswer (int answerId) throws Exception {
        Session s = factory.getCurrentSession();
        Object answer = s.load(Answer.class, answerId);
        s.delete(answer);
    }

}
