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
import com.chengxusheji.dao.QuestionInfoDAO;
import com.chengxusheji.domain.QuestionInfo;
import com.chengxusheji.dao.SurveyInfoDAO;
import com.chengxusheji.domain.SurveyInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class QuestionInfoAction extends BaseAction {

    /*�������Ҫ��ѯ������: �ʾ�����*/
    private SurveyInfo questionPaperObj;
    public void setQuestionPaperObj(SurveyInfo questionPaperObj) {
        this.questionPaperObj = questionPaperObj;
    }
    public SurveyInfo getQuestionPaperObj() {
        return this.questionPaperObj;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private String titleValue;
    public void setTitleValue(String titleValue) {
        this.titleValue = titleValue;
    }
    public String getTitleValue() {
        return this.titleValue;
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

    private int titileId;
    public void setTitileId(int titileId) {
        this.titileId = titileId;
    }
    public int getTitileId() {
        return titileId;
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
    @Resource SurveyInfoDAO surveyInfoDAO;
    @Resource QuestionInfoDAO questionInfoDAO;

    /*��������QuestionInfo����*/
    private QuestionInfo questionInfo;
    public void setQuestionInfo(QuestionInfo questionInfo) {
        this.questionInfo = questionInfo;
    }
    public QuestionInfo getQuestionInfo() {
        return this.questionInfo;
    }

    /*��ת�����QuestionInfo��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�SurveyInfo��Ϣ*/
        List<SurveyInfo> surveyInfoList = surveyInfoDAO.QueryAllSurveyInfoInfo();
        ctx.put("surveyInfoList", surveyInfoList);
        return "add_view";
    }

    /*���QuestionInfo��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddQuestionInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            SurveyInfo questionPaperObj = surveyInfoDAO.GetSurveyInfoByPaperId(questionInfo.getQuestionPaperObj().getPaperId());
            questionInfo.setQuestionPaperObj(questionPaperObj);
            questionInfoDAO.AddQuestionInfo(questionInfo);
            ctx.put("message",  java.net.URLEncoder.encode("QuestionInfo��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("QuestionInfo���ʧ��!"));
            return "error";
        }
    }

    /*��ѯQuestionInfo��Ϣ*/
    public String QueryQuestionInfo() {
        if(currentPage == 0) currentPage = 1;
        if(titleValue == null) titleValue = "";
        List<QuestionInfo> questionInfoList = questionInfoDAO.QueryQuestionInfoInfo(questionPaperObj, titleValue, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        questionInfoDAO.CalculateTotalPageAndRecordNumber(questionPaperObj, titleValue);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = questionInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = questionInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("questionInfoList",  questionInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("questionPaperObj", questionPaperObj);
        List<SurveyInfo> surveyInfoList = surveyInfoDAO.QueryAllSurveyInfoInfo();
        ctx.put("surveyInfoList", surveyInfoList);
        ctx.put("titleValue", titleValue);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryQuestionInfoOutputToExcel() { 
        if(titleValue == null) titleValue = "";
        List<QuestionInfo> questionInfoList = questionInfoDAO.QueryQuestionInfoInfo(questionPaperObj,titleValue);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "QuestionInfo��Ϣ��¼"; 
        String[] headers = { "��¼���","�ʾ�����","��������"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<questionInfoList.size();i++) {
        	QuestionInfo questionInfo = questionInfoList.get(i); 
        	dataset.add(new String[]{questionInfo.getTitileId() + "",questionInfo.getQuestionPaperObj().getQuestionPaperName(),
questionInfo.getTitleValue()});
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
			response.setHeader("Content-disposition","attachment; filename="+"QuestionInfo.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯQuestionInfo��Ϣ*/
    public String FrontQueryQuestionInfo() {
        if(currentPage == 0) currentPage = 1;
        if(titleValue == null) titleValue = "";
        List<QuestionInfo> questionInfoList = questionInfoDAO.QueryQuestionInfoInfo(questionPaperObj, titleValue, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        questionInfoDAO.CalculateTotalPageAndRecordNumber(questionPaperObj, titleValue);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = questionInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = questionInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("questionInfoList",  questionInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("questionPaperObj", questionPaperObj);
        List<SurveyInfo> surveyInfoList = surveyInfoDAO.QueryAllSurveyInfoInfo();
        ctx.put("surveyInfoList", surveyInfoList);
        ctx.put("titleValue", titleValue);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�QuestionInfo��Ϣ*/
    public String ModifyQuestionInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������titileId��ȡQuestionInfo����*/
        QuestionInfo questionInfo = questionInfoDAO.GetQuestionInfoByTitileId(titileId);

        List<SurveyInfo> surveyInfoList = surveyInfoDAO.QueryAllSurveyInfoInfo();
        ctx.put("surveyInfoList", surveyInfoList);
        ctx.put("questionInfo",  questionInfo);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�QuestionInfo��Ϣ*/
    public String FrontShowQuestionInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������titileId��ȡQuestionInfo����*/
        QuestionInfo questionInfo = questionInfoDAO.GetQuestionInfoByTitileId(titileId);

        List<SurveyInfo> surveyInfoList = surveyInfoDAO.QueryAllSurveyInfoInfo();
        ctx.put("surveyInfoList", surveyInfoList);
        ctx.put("questionInfo",  questionInfo);
        return "front_show_view";
    }

    /*�����޸�QuestionInfo��Ϣ*/
    public String ModifyQuestionInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            SurveyInfo questionPaperObj = surveyInfoDAO.GetSurveyInfoByPaperId(questionInfo.getQuestionPaperObj().getPaperId());
            questionInfo.setQuestionPaperObj(questionPaperObj);
            questionInfoDAO.UpdateQuestionInfo(questionInfo);
            ctx.put("message",  java.net.URLEncoder.encode("QuestionInfo��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("QuestionInfo��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��QuestionInfo��Ϣ*/
    public String DeleteQuestionInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            questionInfoDAO.DeleteQuestionInfo(titileId);
            ctx.put("message",  java.net.URLEncoder.encode("QuestionInfoɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("QuestionInfoɾ��ʧ��!"));
            return "error";
        }
    }

}
