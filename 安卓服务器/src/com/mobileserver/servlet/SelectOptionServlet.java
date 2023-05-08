package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.SelectOptionDAO;
import com.mobileserver.domain.SelectOption;

import org.json.JSONStringer;

public class SelectOptionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造选项信息业务层对象*/
	private SelectOptionDAO selectOptionDAO = new SelectOptionDAO();

	/*默认构造函数*/
	public SelectOptionServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*获取action参数，根据action的值执行不同的业务处理*/
		String action = request.getParameter("action");
		if (action.equals("query")) {
			/*获取查询选项信息的参数信息*/
			int questionObj = 0;
			if (request.getParameter("questionObj") != null)
				questionObj = Integer.parseInt(request.getParameter("questionObj"));
			String optionContent = request.getParameter("optionContent");
			optionContent = optionContent == null ? "" : new String(request.getParameter(
					"optionContent").getBytes("iso-8859-1"), "UTF-8");

			/*调用业务逻辑层执行选项信息查询*/
			List<SelectOption> selectOptionList = selectOptionDAO.QuerySelectOption(questionObj,optionContent);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<SelectOptions>").append("\r\n");
			for (int i = 0; i < selectOptionList.size(); i++) {
				sb.append("	<SelectOption>").append("\r\n")
				.append("		<optionId>")
				.append(selectOptionList.get(i).getOptionId())
				.append("</optionId>").append("\r\n")
				.append("		<questionObj>")
				.append(selectOptionList.get(i).getQuestionObj())
				.append("</questionObj>").append("\r\n")
				.append("		<optionContent>")
				.append(selectOptionList.get(i).getOptionContent())
				.append("</optionContent>").append("\r\n")
				.append("	</SelectOption>").append("\r\n");
			}
			sb.append("</SelectOptions>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(SelectOption selectOption: selectOptionList) {
				  stringer.object();
			  stringer.key("optionId").value(selectOption.getOptionId());
			  stringer.key("questionObj").value(selectOption.getQuestionObj());
			  stringer.key("optionContent").value(selectOption.getOptionContent());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加选项信息：获取选项信息参数，参数保存到新建的选项信息对象 */ 
			SelectOption selectOption = new SelectOption();
			int optionId = Integer.parseInt(request.getParameter("optionId"));
			selectOption.setOptionId(optionId);
			int questionObj = Integer.parseInt(request.getParameter("questionObj"));
			selectOption.setQuestionObj(questionObj);
			String optionContent = new String(request.getParameter("optionContent").getBytes("iso-8859-1"), "UTF-8");
			selectOption.setOptionContent(optionContent);

			/* 调用业务层执行添加操作 */
			String result = selectOptionDAO.AddSelectOption(selectOption);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除选项信息：获取选项信息的记录编号*/
			int optionId = Integer.parseInt(request.getParameter("optionId"));
			/*调用业务逻辑层执行删除操作*/
			String result = selectOptionDAO.DeleteSelectOption(optionId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新选项信息之前先根据optionId查询某个选项信息*/
			int optionId = Integer.parseInt(request.getParameter("optionId"));
			SelectOption selectOption = selectOptionDAO.GetSelectOption(optionId);

			// 客户端查询的选项信息对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("optionId").value(selectOption.getOptionId());
			  stringer.key("questionObj").value(selectOption.getQuestionObj());
			  stringer.key("optionContent").value(selectOption.getOptionContent());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新选项信息：获取选项信息参数，参数保存到新建的选项信息对象 */ 
			SelectOption selectOption = new SelectOption();
			int optionId = Integer.parseInt(request.getParameter("optionId"));
			selectOption.setOptionId(optionId);
			int questionObj = Integer.parseInt(request.getParameter("questionObj"));
			selectOption.setQuestionObj(questionObj);
			String optionContent = new String(request.getParameter("optionContent").getBytes("iso-8859-1"), "UTF-8");
			selectOption.setOptionContent(optionContent);

			/* 调用业务层执行更新操作 */
			String result = selectOptionDAO.UpdateSelectOption(selectOption);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
