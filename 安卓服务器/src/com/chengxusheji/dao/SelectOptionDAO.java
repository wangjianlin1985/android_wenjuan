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
import com.chengxusheji.domain.QuestionInfo;
import com.chengxusheji.domain.SelectOption;

@Service @Transactional
public class SelectOptionDAO {

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
    public void AddSelectOption(SelectOption selectOption) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(selectOption);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SelectOption> QuerySelectOptionInfo(QuestionInfo questionObj,String optionContent,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From SelectOption selectOption where 1=1";
    	if(null != questionObj && questionObj.getTitileId()!=0) hql += " and selectOption.questionObj.titileId=" + questionObj.getTitileId();
    	if(!optionContent.equals("")) hql = hql + " and selectOption.optionContent like '%" + optionContent + "%'";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List selectOptionList = q.list();
    	return (ArrayList<SelectOption>) selectOptionList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SelectOption> QuerySelectOptionInfo(QuestionInfo questionObj,String optionContent) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From SelectOption selectOption where 1=1";
    	if(null != questionObj && questionObj.getTitileId()!=0) hql += " and selectOption.questionObj.titileId=" + questionObj.getTitileId();
    	if(!optionContent.equals("")) hql = hql + " and selectOption.optionContent like '%" + optionContent + "%'";
    	Query q = s.createQuery(hql);
    	List selectOptionList = q.list();
    	return (ArrayList<SelectOption>) selectOptionList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SelectOption> QueryAllSelectOptionInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From SelectOption";
        Query q = s.createQuery(hql);
        List selectOptionList = q.list();
        return (ArrayList<SelectOption>) selectOptionList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(QuestionInfo questionObj,String optionContent) {
        Session s = factory.getCurrentSession();
        String hql = "From SelectOption selectOption where 1=1";
        if(null != questionObj && questionObj.getTitileId()!=0) hql += " and selectOption.questionObj.titileId=" + questionObj.getTitileId();
        if(!optionContent.equals("")) hql = hql + " and selectOption.optionContent like '%" + optionContent + "%'";
        Query q = s.createQuery(hql);
        List selectOptionList = q.list();
        recordNumber = selectOptionList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public SelectOption GetSelectOptionByOptionId(int optionId) {
        Session s = factory.getCurrentSession();
        SelectOption selectOption = (SelectOption)s.get(SelectOption.class, optionId);
        return selectOption;
    }

    /*����SelectOption��Ϣ*/
    public void UpdateSelectOption(SelectOption selectOption) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(selectOption);
    }

    /*ɾ��SelectOption��Ϣ*/
    public void DeleteSelectOption (int optionId) throws Exception {
        Session s = factory.getCurrentSession();
        Object selectOption = s.load(SelectOption.class, optionId);
        s.delete(selectOption);
    }

}
