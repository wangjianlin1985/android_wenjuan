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
import com.chengxusheji.dao.SurveyInfoDAO;
import com.chengxusheji.domain.SurveyInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class SurveyInfoAction extends BaseAction {

	/*ͼƬ���ļ��ֶ�zhutitupian��������*/
	private File zhutitupianFile;
	private String zhutitupianFileFileName;
	private String zhutitupianFileContentType;
	public File getZhutitupianFile() {
		return zhutitupianFile;
	}
	public void setZhutitupianFile(File zhutitupianFile) {
		this.zhutitupianFile = zhutitupianFile;
	}
	public String getZhutitupianFileFileName() {
		return zhutitupianFileFileName;
	}
	public void setZhutitupianFileFileName(String zhutitupianFileFileName) {
		this.zhutitupianFileFileName = zhutitupianFileFileName;
	}
	public String getZhutitupianFileContentType() {
		return zhutitupianFileContentType;
	}
	public void setZhutitupianFileContentType(String zhutitupianFileContentType) {
		this.zhutitupianFileContentType = zhutitupianFileContentType;
	}
    /*�������Ҫ��ѯ������: �ʾ�����*/
    private String questionPaperName;
    public void setQuestionPaperName(String questionPaperName) {
        this.questionPaperName = questionPaperName;
    }
    public String getQuestionPaperName() {
        return this.questionPaperName;
    }

    /*�������Ҫ��ѯ������: ������*/
    private String faqiren;
    public void setFaqiren(String faqiren) {
        this.faqiren = faqiren;
    }
    public String getFaqiren() {
        return this.faqiren;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private String startDate;
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getStartDate() {
        return this.startDate;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private String endDate;
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getEndDate() {
        return this.endDate;
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

    private int paperId;
    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }
    public int getPaperId() {
        return paperId;
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

    /*��������SurveyInfo����*/
    private SurveyInfo surveyInfo;
    public void setSurveyInfo(SurveyInfo surveyInfo) {
        this.surveyInfo = surveyInfo;
    }
    public SurveyInfo getSurveyInfo() {
        return this.surveyInfo;
    }

    /*��ת�����SurveyInfo��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*���SurveyInfo��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddSurveyInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            /*��������ͼƬ�ϴ�*/
            String zhutitupianPath = "upload/noimage.jpg"; 
       	 	if(zhutitupianFile != null)
       	 		zhutitupianPath = photoUpload(zhutitupianFile,zhutitupianFileContentType);
       	 	surveyInfo.setZhutitupian(zhutitupianPath);
            surveyInfoDAO.AddSurveyInfo(surveyInfo);
            ctx.put("message",  java.net.URLEncoder.encode("SurveyInfo��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SurveyInfo���ʧ��!"));
            return "error";
        }
    }

    /*��ѯSurveyInfo��Ϣ*/
    public String QuerySurveyInfo() {
        if(currentPage == 0) currentPage = 1;
        if(questionPaperName == null) questionPaperName = "";
        if(faqiren == null) faqiren = "";
        if(startDate == null) startDate = "";
        if(endDate == null) endDate = "";
        List<SurveyInfo> surveyInfoList = surveyInfoDAO.QuerySurveyInfoInfo(questionPaperName, faqiren, startDate, endDate, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        surveyInfoDAO.CalculateTotalPageAndRecordNumber(questionPaperName, faqiren, startDate, endDate);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = surveyInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = surveyInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("surveyInfoList",  surveyInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("questionPaperName", questionPaperName);
        ctx.put("faqiren", faqiren);
        ctx.put("startDate", startDate);
        ctx.put("endDate", endDate);
        return "query_view";
    }

    /*��̨������excel*/
    public String QuerySurveyInfoOutputToExcel() { 
        if(questionPaperName == null) questionPaperName = "";
        if(faqiren == null) faqiren = "";
        if(startDate == null) startDate = "";
        if(endDate == null) endDate = "";
        List<SurveyInfo> surveyInfoList = surveyInfoDAO.QuerySurveyInfoInfo(questionPaperName,faqiren,startDate,endDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "SurveyInfo��Ϣ��¼"; 
        String[] headers = { "�ʾ�����","������","��������","��������","����ͼƬ","��˱�־"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<surveyInfoList.size();i++) {
        	SurveyInfo surveyInfo = surveyInfoList.get(i); 
        	dataset.add(new String[]{surveyInfo.getQuestionPaperName(),surveyInfo.getFaqiren(),new SimpleDateFormat("yyyy-MM-dd").format(surveyInfo.getStartDate()),new SimpleDateFormat("yyyy-MM-dd").format(surveyInfo.getEndDate()),surveyInfo.getZhutitupian(),surveyInfo.getPublishFlag() + ""});
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
			response.setHeader("Content-disposition","attachment; filename="+"SurveyInfo.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯSurveyInfo��Ϣ*/
    public String FrontQuerySurveyInfo() {
        if(currentPage == 0) currentPage = 1;
        if(questionPaperName == null) questionPaperName = "";
        if(faqiren == null) faqiren = "";
        if(startDate == null) startDate = "";
        if(endDate == null) endDate = "";
        List<SurveyInfo> surveyInfoList = surveyInfoDAO.QuerySurveyInfoInfo(questionPaperName, faqiren, startDate, endDate, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        surveyInfoDAO.CalculateTotalPageAndRecordNumber(questionPaperName, faqiren, startDate, endDate);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = surveyInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = surveyInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("surveyInfoList",  surveyInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("questionPaperName", questionPaperName);
        ctx.put("faqiren", faqiren);
        ctx.put("startDate", startDate);
        ctx.put("endDate", endDate);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�SurveyInfo��Ϣ*/
    public String ModifySurveyInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������paperId��ȡSurveyInfo����*/
        SurveyInfo surveyInfo = surveyInfoDAO.GetSurveyInfoByPaperId(paperId);

        ctx.put("surveyInfo",  surveyInfo);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�SurveyInfo��Ϣ*/
    public String FrontShowSurveyInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������paperId��ȡSurveyInfo����*/
        SurveyInfo surveyInfo = surveyInfoDAO.GetSurveyInfoByPaperId(paperId);

        ctx.put("surveyInfo",  surveyInfo);
        return "front_show_view";
    }

    /*�����޸�SurveyInfo��Ϣ*/
    public String ModifySurveyInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            /*��������ͼƬ�ϴ�*/
            if(zhutitupianFile != null) {
            	String zhutitupianPath = photoUpload(zhutitupianFile,zhutitupianFileContentType);
            	surveyInfo.setZhutitupian(zhutitupianPath);
            }
            surveyInfoDAO.UpdateSurveyInfo(surveyInfo);
            ctx.put("message",  java.net.URLEncoder.encode("SurveyInfo��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SurveyInfo��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��SurveyInfo��Ϣ*/
    public String DeleteSurveyInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            surveyInfoDAO.DeleteSurveyInfo(paperId);
            ctx.put("message",  java.net.URLEncoder.encode("SurveyInfoɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SurveyInfoɾ��ʧ��!"));
            return "error";
        }
    }

}
