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
    /*每页显示记录数目*/
    private final int PAGE_SIZE = 10;

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加图书信息*/
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
    	/*计算当前显示页码的开始记录*/
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

    /*计算总的页数和记录数*/
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

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Answer GetAnswerByAnswerId(int answerId) {
        Session s = factory.getCurrentSession();
        Answer answer = (Answer)s.get(Answer.class, answerId);
        return answer;
    }

    /*更新Answer信息*/
    public void UpdateAnswer(Answer answer) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(answer);
    }

    /*删除Answer信息*/
    public void DeleteAnswer (int answerId) throws Exception {
        Session s = factory.getCurrentSession();
        Object answer = s.load(Answer.class, answerId);
        s.delete(answer);
    }

}
