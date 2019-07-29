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

    /*界面层需要查询的属性: 选项信息*/
    private SelectOption selectOptionObj;
    public void setSelectOptionObj(SelectOption selectOptionObj) {
        this.selectOptionObj = selectOptionObj;
    }
    public SelectOption getSelectOptionObj() {
        return this.selectOptionObj;
    }

    /*界面层需要查询的属性: 用户*/
    private UserInfo userObj;
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }
    public UserInfo getUserObj() {
        return this.userObj;
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

    private int answerId;
    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }
    public int getAnswerId() {
        return answerId;
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
    @Resource SelectOptionDAO selectOptionDAO;
    @Resource UserInfoDAO userInfoDAO;
    @Resource AnswerDAO answerDAO;

    /*待操作的Answer对象*/
    private Answer answer;
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
    public Answer getAnswer() {
        return this.answer;
    }

    /*跳转到添加Answer视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的SelectOption信息*/
        List<SelectOption> selectOptionList = selectOptionDAO.QueryAllSelectOptionInfo();
        ctx.put("selectOptionList", selectOptionList);
        /*查询所有的UserInfo信息*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*添加Answer信息*/
    @SuppressWarnings("deprecation")
    public String AddAnswer() {
        ActionContext ctx = ActionContext.getContext();
        try {
            SelectOption selectOptionObj = selectOptionDAO.GetSelectOptionByOptionId(answer.getSelectOptionObj().getOptionId());
            answer.setSelectOptionObj(selectOptionObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUserInfoname(answer.getUserObj().getUserInfoname());
            answer.setUserObj(userObj);
            answerDAO.AddAnswer(answer);
            ctx.put("message",  java.net.URLEncoder.encode("Answer添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Answer添加失败!"));
            return "error";
        }
    }

    /*查询Answer信息*/
    public String QueryAnswer() {
        if(currentPage == 0) currentPage = 1;
        List<Answer> answerList = answerDAO.QueryAnswerInfo(selectOptionObj, userObj, currentPage);
        /*计算总的页数和总的记录数*/
        answerDAO.CalculateTotalPageAndRecordNumber(selectOptionObj, userObj);
        /*获取到总的页码数目*/
        totalPage = answerDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QueryAnswerOutputToExcel() { 
        List<Answer> answerList = answerDAO.QueryAnswerInfo(selectOptionObj,userObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Answer信息记录"; 
        String[] headers = { "记录编号","选项信息","用户"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Answer.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询Answer信息*/
    public String FrontQueryAnswer() {
        if(currentPage == 0) currentPage = 1;
        List<Answer> answerList = answerDAO.QueryAnswerInfo(selectOptionObj, userObj, currentPage);
        /*计算总的页数和总的记录数*/
        answerDAO.CalculateTotalPageAndRecordNumber(selectOptionObj, userObj);
        /*获取到总的页码数目*/
        totalPage = answerDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的Answer信息*/
    public String ModifyAnswerQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键answerId获取Answer对象*/
        Answer answer = answerDAO.GetAnswerByAnswerId(answerId);

        List<SelectOption> selectOptionList = selectOptionDAO.QueryAllSelectOptionInfo();
        ctx.put("selectOptionList", selectOptionList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("answer",  answer);
        return "modify_view";
    }

    /*查询要修改的Answer信息*/
    public String FrontShowAnswerQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键answerId获取Answer对象*/
        Answer answer = answerDAO.GetAnswerByAnswerId(answerId);

        List<SelectOption> selectOptionList = selectOptionDAO.QueryAllSelectOptionInfo();
        ctx.put("selectOptionList", selectOptionList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("answer",  answer);
        return "front_show_view";
    }

    /*更新修改Answer信息*/
    public String ModifyAnswer() {
        ActionContext ctx = ActionContext.getContext();
        try {
            SelectOption selectOptionObj = selectOptionDAO.GetSelectOptionByOptionId(answer.getSelectOptionObj().getOptionId());
            answer.setSelectOptionObj(selectOptionObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUserInfoname(answer.getUserObj().getUserInfoname());
            answer.setUserObj(userObj);
            answerDAO.UpdateAnswer(answer);
            ctx.put("message",  java.net.URLEncoder.encode("Answer信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Answer信息更新失败!"));
            return "error";
       }
   }

    /*删除Answer信息*/
    public String DeleteAnswer() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            answerDAO.DeleteAnswer(answerId);
            ctx.put("message",  java.net.URLEncoder.encode("Answer删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Answer删除失败!"));
            return "error";
        }
    }

}
