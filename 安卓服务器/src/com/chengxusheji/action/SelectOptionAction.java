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
import com.chengxusheji.dao.SelectOptionDAO;
import com.chengxusheji.domain.SelectOption;
import com.chengxusheji.dao.QuestionInfoDAO;
import com.chengxusheji.domain.QuestionInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class SelectOptionAction extends BaseAction {

    /*�������Ҫ��ѯ������: ������Ϣ*/
    private QuestionInfo questionObj;
    public void setQuestionObj(QuestionInfo questionObj) {
        this.questionObj = questionObj;
    }
    public QuestionInfo getQuestionObj() {
        return this.questionObj;
    }

    /*�������Ҫ��ѯ������: ѡ������*/
    private String optionContent;
    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }
    public String getOptionContent() {
        return this.optionContent;
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

    private int optionId;
    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }
    public int getOptionId() {
        return optionId;
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
    @Resource QuestionInfoDAO questionInfoDAO;
    @Resource SelectOptionDAO selectOptionDAO;

    /*��������SelectOption����*/
    private SelectOption selectOption;
    public void setSelectOption(SelectOption selectOption) {
        this.selectOption = selectOption;
    }
    public SelectOption getSelectOption() {
        return this.selectOption;
    }

    /*��ת�����SelectOption��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�QuestionInfo��Ϣ*/
        List<QuestionInfo> questionInfoList = questionInfoDAO.QueryAllQuestionInfoInfo();
        ctx.put("questionInfoList", questionInfoList);
        return "add_view";
    }

    /*���SelectOption��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddSelectOption() {
        ActionContext ctx = ActionContext.getContext();
        try {
            QuestionInfo questionObj = questionInfoDAO.GetQuestionInfoByTitileId(selectOption.getQuestionObj().getTitileId());
            selectOption.setQuestionObj(questionObj);
            selectOptionDAO.AddSelectOption(selectOption);
            ctx.put("message",  java.net.URLEncoder.encode("SelectOption��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SelectOption���ʧ��!"));
            return "error";
        }
    }

    /*��ѯSelectOption��Ϣ*/
    public String QuerySelectOption() {
        if(currentPage == 0) currentPage = 1;
        if(optionContent == null) optionContent = "";
        List<SelectOption> selectOptionList = selectOptionDAO.QuerySelectOptionInfo(questionObj, optionContent, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        selectOptionDAO.CalculateTotalPageAndRecordNumber(questionObj, optionContent);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = selectOptionDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = selectOptionDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("selectOptionList",  selectOptionList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("questionObj", questionObj);
        List<QuestionInfo> questionInfoList = questionInfoDAO.QueryAllQuestionInfoInfo();
        ctx.put("questionInfoList", questionInfoList);
        ctx.put("optionContent", optionContent);
        return "query_view";
    }

    /*��̨������excel*/
    public String QuerySelectOptionOutputToExcel() { 
        if(optionContent == null) optionContent = "";
        List<SelectOption> selectOptionList = selectOptionDAO.QuerySelectOptionInfo(questionObj,optionContent);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "SelectOption��Ϣ��¼"; 
        String[] headers = { "��¼���","������Ϣ","ѡ������"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<selectOptionList.size();i++) {
        	SelectOption selectOption = selectOptionList.get(i); 
        	dataset.add(new String[]{selectOption.getOptionId() + "",selectOption.getQuestionObj().getTitleValue(),
selectOption.getOptionContent()});
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
			response.setHeader("Content-disposition","attachment; filename="+"SelectOption.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯSelectOption��Ϣ*/
    public String FrontQuerySelectOption() {
        if(currentPage == 0) currentPage = 1;
        if(optionContent == null) optionContent = "";
        List<SelectOption> selectOptionList = selectOptionDAO.QuerySelectOptionInfo(questionObj, optionContent, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        selectOptionDAO.CalculateTotalPageAndRecordNumber(questionObj, optionContent);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = selectOptionDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = selectOptionDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("selectOptionList",  selectOptionList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("questionObj", questionObj);
        List<QuestionInfo> questionInfoList = questionInfoDAO.QueryAllQuestionInfoInfo();
        ctx.put("questionInfoList", questionInfoList);
        ctx.put("optionContent", optionContent);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�SelectOption��Ϣ*/
    public String ModifySelectOptionQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������optionId��ȡSelectOption����*/
        SelectOption selectOption = selectOptionDAO.GetSelectOptionByOptionId(optionId);

        List<QuestionInfo> questionInfoList = questionInfoDAO.QueryAllQuestionInfoInfo();
        ctx.put("questionInfoList", questionInfoList);
        ctx.put("selectOption",  selectOption);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�SelectOption��Ϣ*/
    public String FrontShowSelectOptionQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������optionId��ȡSelectOption����*/
        SelectOption selectOption = selectOptionDAO.GetSelectOptionByOptionId(optionId);

        List<QuestionInfo> questionInfoList = questionInfoDAO.QueryAllQuestionInfoInfo();
        ctx.put("questionInfoList", questionInfoList);
        ctx.put("selectOption",  selectOption);
        return "front_show_view";
    }

    /*�����޸�SelectOption��Ϣ*/
    public String ModifySelectOption() {
        ActionContext ctx = ActionContext.getContext();
        try {
            QuestionInfo questionObj = questionInfoDAO.GetQuestionInfoByTitileId(selectOption.getQuestionObj().getTitileId());
            selectOption.setQuestionObj(questionObj);
            selectOptionDAO.UpdateSelectOption(selectOption);
            ctx.put("message",  java.net.URLEncoder.encode("SelectOption��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SelectOption��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��SelectOption��Ϣ*/
    public String DeleteSelectOption() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            selectOptionDAO.DeleteSelectOption(optionId);
            ctx.put("message",  java.net.URLEncoder.encode("SelectOptionɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SelectOptionɾ��ʧ��!"));
            return "error";
        }
    }

}
