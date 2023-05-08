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

    /*界面层需要查询的属性: 问题信息*/
    private QuestionInfo questionObj;
    public void setQuestionObj(QuestionInfo questionObj) {
        this.questionObj = questionObj;
    }
    public QuestionInfo getQuestionObj() {
        return this.questionObj;
    }

    /*界面层需要查询的属性: 选项内容*/
    private String optionContent;
    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }
    public String getOptionContent() {
        return this.optionContent;
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

    private int optionId;
    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }
    public int getOptionId() {
        return optionId;
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
    @Resource QuestionInfoDAO questionInfoDAO;
    @Resource SelectOptionDAO selectOptionDAO;

    /*待操作的SelectOption对象*/
    private SelectOption selectOption;
    public void setSelectOption(SelectOption selectOption) {
        this.selectOption = selectOption;
    }
    public SelectOption getSelectOption() {
        return this.selectOption;
    }

    /*跳转到添加SelectOption视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的QuestionInfo信息*/
        List<QuestionInfo> questionInfoList = questionInfoDAO.QueryAllQuestionInfoInfo();
        ctx.put("questionInfoList", questionInfoList);
        return "add_view";
    }

    /*添加SelectOption信息*/
    @SuppressWarnings("deprecation")
    public String AddSelectOption() {
        ActionContext ctx = ActionContext.getContext();
        try {
            QuestionInfo questionObj = questionInfoDAO.GetQuestionInfoByTitileId(selectOption.getQuestionObj().getTitileId());
            selectOption.setQuestionObj(questionObj);
            selectOptionDAO.AddSelectOption(selectOption);
            ctx.put("message",  java.net.URLEncoder.encode("SelectOption添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SelectOption添加失败!"));
            return "error";
        }
    }

    /*查询SelectOption信息*/
    public String QuerySelectOption() {
        if(currentPage == 0) currentPage = 1;
        if(optionContent == null) optionContent = "";
        List<SelectOption> selectOptionList = selectOptionDAO.QuerySelectOptionInfo(questionObj, optionContent, currentPage);
        /*计算总的页数和总的记录数*/
        selectOptionDAO.CalculateTotalPageAndRecordNumber(questionObj, optionContent);
        /*获取到总的页码数目*/
        totalPage = selectOptionDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QuerySelectOptionOutputToExcel() { 
        if(optionContent == null) optionContent = "";
        List<SelectOption> selectOptionList = selectOptionDAO.QuerySelectOptionInfo(questionObj,optionContent);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "SelectOption信息记录"; 
        String[] headers = { "记录编号","问题信息","选项内容"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"SelectOption.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询SelectOption信息*/
    public String FrontQuerySelectOption() {
        if(currentPage == 0) currentPage = 1;
        if(optionContent == null) optionContent = "";
        List<SelectOption> selectOptionList = selectOptionDAO.QuerySelectOptionInfo(questionObj, optionContent, currentPage);
        /*计算总的页数和总的记录数*/
        selectOptionDAO.CalculateTotalPageAndRecordNumber(questionObj, optionContent);
        /*获取到总的页码数目*/
        totalPage = selectOptionDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的SelectOption信息*/
    public String ModifySelectOptionQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键optionId获取SelectOption对象*/
        SelectOption selectOption = selectOptionDAO.GetSelectOptionByOptionId(optionId);

        List<QuestionInfo> questionInfoList = questionInfoDAO.QueryAllQuestionInfoInfo();
        ctx.put("questionInfoList", questionInfoList);
        ctx.put("selectOption",  selectOption);
        return "modify_view";
    }

    /*查询要修改的SelectOption信息*/
    public String FrontShowSelectOptionQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键optionId获取SelectOption对象*/
        SelectOption selectOption = selectOptionDAO.GetSelectOptionByOptionId(optionId);

        List<QuestionInfo> questionInfoList = questionInfoDAO.QueryAllQuestionInfoInfo();
        ctx.put("questionInfoList", questionInfoList);
        ctx.put("selectOption",  selectOption);
        return "front_show_view";
    }

    /*更新修改SelectOption信息*/
    public String ModifySelectOption() {
        ActionContext ctx = ActionContext.getContext();
        try {
            QuestionInfo questionObj = questionInfoDAO.GetQuestionInfoByTitileId(selectOption.getQuestionObj().getTitileId());
            selectOption.setQuestionObj(questionObj);
            selectOptionDAO.UpdateSelectOption(selectOption);
            ctx.put("message",  java.net.URLEncoder.encode("SelectOption信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SelectOption信息更新失败!"));
            return "error";
       }
   }

    /*删除SelectOption信息*/
    public String DeleteSelectOption() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            selectOptionDAO.DeleteSelectOption(optionId);
            ctx.put("message",  java.net.URLEncoder.encode("SelectOption删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SelectOption删除失败!"));
            return "error";
        }
    }

}
