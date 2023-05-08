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

	/*图片或文件字段zhutitupian参数接收*/
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
    /*界面层需要查询的属性: 问卷名称*/
    private String questionPaperName;
    public void setQuestionPaperName(String questionPaperName) {
        this.questionPaperName = questionPaperName;
    }
    public String getQuestionPaperName() {
        return this.questionPaperName;
    }

    /*界面层需要查询的属性: 发起人*/
    private String faqiren;
    public void setFaqiren(String faqiren) {
        this.faqiren = faqiren;
    }
    public String getFaqiren() {
        return this.faqiren;
    }

    /*界面层需要查询的属性: 发起日期*/
    private String startDate;
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getStartDate() {
        return this.startDate;
    }

    /*界面层需要查询的属性: 结束日期*/
    private String endDate;
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getEndDate() {
        return this.endDate;
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

    private int paperId;
    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }
    public int getPaperId() {
        return paperId;
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

    /*待操作的SurveyInfo对象*/
    private SurveyInfo surveyInfo;
    public void setSurveyInfo(SurveyInfo surveyInfo) {
        this.surveyInfo = surveyInfo;
    }
    public SurveyInfo getSurveyInfo() {
        return this.surveyInfo;
    }

    /*跳转到添加SurveyInfo视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*添加SurveyInfo信息*/
    @SuppressWarnings("deprecation")
    public String AddSurveyInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            /*处理主题图片上传*/
            String zhutitupianPath = "upload/noimage.jpg"; 
       	 	if(zhutitupianFile != null)
       	 		zhutitupianPath = photoUpload(zhutitupianFile,zhutitupianFileContentType);
       	 	surveyInfo.setZhutitupian(zhutitupianPath);
            surveyInfoDAO.AddSurveyInfo(surveyInfo);
            ctx.put("message",  java.net.URLEncoder.encode("SurveyInfo添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SurveyInfo添加失败!"));
            return "error";
        }
    }

    /*查询SurveyInfo信息*/
    public String QuerySurveyInfo() {
        if(currentPage == 0) currentPage = 1;
        if(questionPaperName == null) questionPaperName = "";
        if(faqiren == null) faqiren = "";
        if(startDate == null) startDate = "";
        if(endDate == null) endDate = "";
        List<SurveyInfo> surveyInfoList = surveyInfoDAO.QuerySurveyInfoInfo(questionPaperName, faqiren, startDate, endDate, currentPage);
        /*计算总的页数和总的记录数*/
        surveyInfoDAO.CalculateTotalPageAndRecordNumber(questionPaperName, faqiren, startDate, endDate);
        /*获取到总的页码数目*/
        totalPage = surveyInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QuerySurveyInfoOutputToExcel() { 
        if(questionPaperName == null) questionPaperName = "";
        if(faqiren == null) faqiren = "";
        if(startDate == null) startDate = "";
        if(endDate == null) endDate = "";
        List<SurveyInfo> surveyInfoList = surveyInfoDAO.QuerySurveyInfoInfo(questionPaperName,faqiren,startDate,endDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "SurveyInfo信息记录"; 
        String[] headers = { "问卷名称","发起人","发起日期","结束日期","主题图片","审核标志"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"SurveyInfo.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询SurveyInfo信息*/
    public String FrontQuerySurveyInfo() {
        if(currentPage == 0) currentPage = 1;
        if(questionPaperName == null) questionPaperName = "";
        if(faqiren == null) faqiren = "";
        if(startDate == null) startDate = "";
        if(endDate == null) endDate = "";
        List<SurveyInfo> surveyInfoList = surveyInfoDAO.QuerySurveyInfoInfo(questionPaperName, faqiren, startDate, endDate, currentPage);
        /*计算总的页数和总的记录数*/
        surveyInfoDAO.CalculateTotalPageAndRecordNumber(questionPaperName, faqiren, startDate, endDate);
        /*获取到总的页码数目*/
        totalPage = surveyInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的SurveyInfo信息*/
    public String ModifySurveyInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键paperId获取SurveyInfo对象*/
        SurveyInfo surveyInfo = surveyInfoDAO.GetSurveyInfoByPaperId(paperId);

        ctx.put("surveyInfo",  surveyInfo);
        return "modify_view";
    }

    /*查询要修改的SurveyInfo信息*/
    public String FrontShowSurveyInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键paperId获取SurveyInfo对象*/
        SurveyInfo surveyInfo = surveyInfoDAO.GetSurveyInfoByPaperId(paperId);

        ctx.put("surveyInfo",  surveyInfo);
        return "front_show_view";
    }

    /*更新修改SurveyInfo信息*/
    public String ModifySurveyInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            /*处理主题图片上传*/
            if(zhutitupianFile != null) {
            	String zhutitupianPath = photoUpload(zhutitupianFile,zhutitupianFileContentType);
            	surveyInfo.setZhutitupian(zhutitupianPath);
            }
            surveyInfoDAO.UpdateSurveyInfo(surveyInfo);
            ctx.put("message",  java.net.URLEncoder.encode("SurveyInfo信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SurveyInfo信息更新失败!"));
            return "error";
       }
   }

    /*删除SurveyInfo信息*/
    public String DeleteSurveyInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            surveyInfoDAO.DeleteSurveyInfo(paperId);
            ctx.put("message",  java.net.URLEncoder.encode("SurveyInfo删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SurveyInfo删除失败!"));
            return "error";
        }
    }

}
