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

    /*界面层需要查询的属性: 问卷名称*/
    private SurveyInfo questionPaperObj;
    public void setQuestionPaperObj(SurveyInfo questionPaperObj) {
        this.questionPaperObj = questionPaperObj;
    }
    public SurveyInfo getQuestionPaperObj() {
        return this.questionPaperObj;
    }

    /*界面层需要查询的属性: 问题内容*/
    private String titleValue;
    public void setTitleValue(String titleValue) {
        this.titleValue = titleValue;
    }
    public String getTitleValue() {
        return this.titleValue;
    }

    /*当前第几页*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*一共多少页*/
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

    /*当前查询的总记录数目*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*业务层对象*/
    @Resource SurveyInfoDAO surveyInfoDAO;
    @Resource QuestionInfoDAO questionInfoDAO;

    /*待操作的QuestionInfo对象*/
    private QuestionInfo questionInfo;
    public void setQuestionInfo(QuestionInfo questionInfo) {
        this.questionInfo = questionInfo;
    }
    public QuestionInfo getQuestionInfo() {
        return this.questionInfo;
    }

    /*跳转到添加QuestionInfo视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的SurveyInfo信息*/
        List<SurveyInfo> surveyInfoList = surveyInfoDAO.QueryAllSurveyInfoInfo();
        ctx.put("surveyInfoList", surveyInfoList);
        return "add_view";
    }

    /*添加QuestionInfo信息*/
    @SuppressWarnings("deprecation")
    public String AddQuestionInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            SurveyInfo questionPaperObj = surveyInfoDAO.GetSurveyInfoByPaperId(questionInfo.getQuestionPaperObj().getPaperId());
            questionInfo.setQuestionPaperObj(questionPaperObj);
            questionInfoDAO.AddQuestionInfo(questionInfo);
            ctx.put("message",  java.net.URLEncoder.encode("QuestionInfo添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("QuestionInfo添加失败!"));
            return "error";
        }
    }

    /*查询QuestionInfo信息*/
    public String QueryQuestionInfo() {
        if(currentPage == 0) currentPage = 1;
        if(titleValue == null) titleValue = "";
        List<QuestionInfo> questionInfoList = questionInfoDAO.QueryQuestionInfoInfo(questionPaperObj, titleValue, currentPage);
        /*计算总的页数和总的记录数*/
        questionInfoDAO.CalculateTotalPageAndRecordNumber(questionPaperObj, titleValue);
        /*获取到总的页码数目*/
        totalPage = questionInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QueryQuestionInfoOutputToExcel() { 
        if(titleValue == null) titleValue = "";
        List<QuestionInfo> questionInfoList = questionInfoDAO.QueryQuestionInfoInfo(questionPaperObj,titleValue);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "QuestionInfo信息记录"; 
        String[] headers = { "记录编号","问卷名称","问题内容"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"QuestionInfo.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
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
    /*前台查询QuestionInfo信息*/
    public String FrontQueryQuestionInfo() {
        if(currentPage == 0) currentPage = 1;
        if(titleValue == null) titleValue = "";
        List<QuestionInfo> questionInfoList = questionInfoDAO.QueryQuestionInfoInfo(questionPaperObj, titleValue, currentPage);
        /*计算总的页数和总的记录数*/
        questionInfoDAO.CalculateTotalPageAndRecordNumber(questionPaperObj, titleValue);
        /*获取到总的页码数目*/
        totalPage = questionInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的QuestionInfo信息*/
    public String ModifyQuestionInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键titileId获取QuestionInfo对象*/
        QuestionInfo questionInfo = questionInfoDAO.GetQuestionInfoByTitileId(titileId);

        List<SurveyInfo> surveyInfoList = surveyInfoDAO.QueryAllSurveyInfoInfo();
        ctx.put("surveyInfoList", surveyInfoList);
        ctx.put("questionInfo",  questionInfo);
        return "modify_view";
    }

    /*查询要修改的QuestionInfo信息*/
    public String FrontShowQuestionInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键titileId获取QuestionInfo对象*/
        QuestionInfo questionInfo = questionInfoDAO.GetQuestionInfoByTitileId(titileId);

        List<SurveyInfo> surveyInfoList = surveyInfoDAO.QueryAllSurveyInfoInfo();
        ctx.put("surveyInfoList", surveyInfoList);
        ctx.put("questionInfo",  questionInfo);
        return "front_show_view";
    }

    /*更新修改QuestionInfo信息*/
    public String ModifyQuestionInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            SurveyInfo questionPaperObj = surveyInfoDAO.GetSurveyInfoByPaperId(questionInfo.getQuestionPaperObj().getPaperId());
            questionInfo.setQuestionPaperObj(questionPaperObj);
            questionInfoDAO.UpdateQuestionInfo(questionInfo);
            ctx.put("message",  java.net.URLEncoder.encode("QuestionInfo信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("QuestionInfo信息更新失败!"));
            return "error";
       }
   }

    /*删除QuestionInfo信息*/
    public String DeleteQuestionInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            questionInfoDAO.DeleteQuestionInfo(titileId);
            ctx.put("message",  java.net.URLEncoder.encode("QuestionInfo删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("QuestionInfo删除失败!"));
            return "error";
        }
    }

}
