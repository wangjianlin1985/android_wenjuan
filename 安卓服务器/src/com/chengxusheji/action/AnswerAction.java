package com.chengxusheji.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.ActionContext;
import com.chengxusheji.dao.AnswerDAO;
import com.chengxusheji.domain.Answer;
import com.chengxusheji.dao.SelectOptionDAO;
import com.chengxusheji.domain.SelectOption;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class AnswerAction extends BaseAction {

    /*�������Ҫ��ѯ������: ѡ����Ϣ*/
    private SelectOption selectOptionObj;
    public void setSelectOptionObj(SelectOption selectOptionObj) {
        this.selectOptionObj = selectOptionObj;
    }
    public SelectOption getSelectOptionObj() {
        return this.selectOptionObj;
    }

    /*�������Ҫ��ѯ������: �û�*/
    private UserInfo userObj;
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }
    public UserInfo getUserObj() {
        return this.userObj;
    }

    /*��ǰ�ڼ�ҳ*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*һ������ҳ*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    private int answerId;
    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }
    public int getAnswerId() {
        return answerId;
    }

    /*��ǰ��ѯ���ܼ�¼��Ŀ*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*ҵ������*/
    @Resource SelectOptionDAO selectOptionDAO;
    @Resource UserInfoDAO userInfoDAO;
    @Resource AnswerDAO answerDAO;

    /*��������Answer����*/
    private Answer answer;
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
    public Answer getAnswer() {
        return this.answer;
    }

    /*��ת�����Answer��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�SelectOption��Ϣ*/
        List<SelectOption> selectOptionList = selectOptionDAO.QueryAllSelectOptionInfo();
        ctx.put("selectOptionList", selectOptionList);
        /*��ѯ���е�UserInfo��Ϣ*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*���Answer��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddAnswer() {
        ActionContext ctx = ActionContext.getContext();
        try {
            SelectOption selectOptionObj = selectOptionDAO.GetSelectOptionByOptionId(answer.getSelectOptionObj().getOptionId());
            answer.setSelectOptionObj(selectOptionObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUserInfoname(answer.getUserObj().getUserInfoname());
            answer.setUserObj(userObj);
            answerDAO.AddAnswer(answer);
            ctx.put("message",  java.net.URLEncoder.encode("Answer��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Answer���ʧ��!"));
            return "error";
        }
    }

    /*��ѯAnswer��Ϣ*/
    public String QueryAnswer() {
        if(currentPage == 0) currentPage = 1;
        List<Answer> answerList = answerDAO.QueryAnswerInfo(selectOptionObj, userObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        answerDAO.CalculateTotalPageAndRecordNumber(selectOptionObj, userObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = answerDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = answerDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("answerList",  answerList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("selectOptionObj", selectOptionObj);
        List<SelectOption> selectOptionList = selectOptionDAO.QueryAllSelectOptionInfo();
        ctx.put("selectOptionList", selectOptionList);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryAnswerOutputToExcel() { 
        List<Answer> answerList = answerDAO.QueryAnswerInfo(selectOptionObj,userObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Answer��Ϣ��¼"; 
        String[] headers = { "��¼���","ѡ����Ϣ","�û�"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<answerList.size();i++) {
        	Answer answer = answerList.get(i); 
        	dataset.add(new String[]{answer.getAnswerId() + "",answer.getSelectOptionObj().getOptionContent(),
answer.getUserObj().getName()
});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Answer.xls");//filename�����ص�xls���������������Ӣ�� 
			response.setContentType("application/msexcel;charset=UTF-8");//�������� 
			response.setHeader("Pragma","No-cache");//����ͷ 
			response.setHeader("Cache-Control","no-cache");//����ͷ 
			response.setDateHeader("Expires", 0);//��������ͷ  
			String rootPath = ServletActionContext.getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
		return null;
    }
    /*ǰ̨��ѯAnswer��Ϣ*/
    public String FrontQueryAnswer() {
        if(currentPage == 0) currentPage = 1;
        List<Answer> answerList = answerDAO.QueryAnswerInfo(selectOptionObj, userObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        answerDAO.CalculateTotalPageAndRecordNumber(selectOptionObj, userObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = answerDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = answerDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("answerList",  answerList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("selectOptionObj", selectOptionObj);
        List<SelectOption> selectOptionList = selectOptionDAO.QueryAllSelectOptionInfo();
        ctx.put("selectOptionList", selectOptionList);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�Answer��Ϣ*/
    public String ModifyAnswerQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������answerId��ȡAnswer����*/
        Answer answer = answerDAO.GetAnswerByAnswerId(answerId);

        List<SelectOption> selectOptionList = selectOptionDAO.QueryAllSelectOptionInfo();
        ctx.put("selectOptionList", selectOptionList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("answer",  answer);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�Answer��Ϣ*/
    public String FrontShowAnswerQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������answerId��ȡAnswer����*/
        Answer answer = answerDAO.GetAnswerByAnswerId(answerId);

        List<SelectOption> selectOptionList = selectOptionDAO.QueryAllSelectOptionInfo();
        ctx.put("selectOptionList", selectOptionList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("answer",  answer);
        return "front_show_view";
    }

    /*�����޸�Answer��Ϣ*/
    public String ModifyAnswer() {
        ActionContext ctx = ActionContext.getContext();
        try {
            SelectOption selectOptionObj = selectOptionDAO.GetSelectOptionByOptionId(answer.getSelectOptionObj().getOptionId());
            answer.setSelectOptionObj(selectOptionObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUserInfoname(answer.getUserObj().getUserInfoname());
            answer.setUserObj(userObj);
            answerDAO.UpdateAnswer(answer);
            ctx.put("message",  java.net.URLEncoder.encode("Answer��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Answer��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��Answer��Ϣ*/
    public String DeleteAnswer() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            answerDAO.DeleteAnswer(answerId);
            ctx.put("message",  java.net.URLEncoder.encode("Answerɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Answerɾ��ʧ��!"));
            return "error";
        }
    }

}
