package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.AnswerDAO;
import com.mobileserver.domain.Answer;

import org.json.JSONStringer;

public class AnswerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造答卷信息业务层对象*/
	private AnswerDAO answerDAO = new AnswerDAO();

	/*默认构造函数*/
	public AnswerServlet() {
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
			/*获取查询答卷信息的参数信息*/
			int selectOptionObj = 0;
			if (request.getParameter("selectOptionObj") != null)
				selectOptionObj = Integer.parseInt(request.getParameter("selectOptionObj"));
			String userObj = "";
			if (request.getParameter("userObj") != null)
				userObj = request.getParameter("userObj");

			/*调用业务逻辑层执行答卷信息查询*/
			List<Answer> answerList = answerDAO.QueryAnswer(selectOptionObj,userObj);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<Answers>").append("\r\n");
			for (int i = 0; i < answerList.size(); i++) {
				sb.append("	<Answer>").append("\r\n")
				.append("		<answerId>")
				.append(answerList.get(i).getAnswerId())
				.append("</answerId>").append("\r\n")
				.append("		<selectOptionObj>")
				.append(answerList.get(i).getSelectOptionObj())
				.append("</selectOptionObj>").append("\r\n")
				.append("		<userObj>")
				.append(answerList.get(i).getUserObj())
				.append("</userObj>").append("\r\n")
				.append("	</Answer>").append("\r\n");
			}
			sb.append("</Answers>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(Answer answer: answerList) {
				  stringer.object();
			  stringer.key("answerId").value(answer.getAnswerId());
			  stringer.key("selectOptionObj").value(answer.getSelectOptionObj());
			  stringer.key("userObj").value(answer.getUserObj());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加答卷信息：获取答卷信息参数，参数保存到新建的答卷信息对象 */ 
			Answer answer = new Answer();
			int answerId = Integer.parseInt(request.getParameter("answerId"));
			answer.setAnswerId(answerId);
			int selectOptionObj = Integer.parseInt(request.getParameter("selectOptionObj"));
			answer.setSelectOptionObj(selectOptionObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			answer.setUserObj(userObj);

			/* 调用业务层执行添加操作 */
			String result = answerDAO.AddAnswer(answer);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除答卷信息：获取答卷信息的记录编号*/
			int answerId = Integer.parseInt(request.getParameter("answerId"));
			/*调用业务逻辑层执行删除操作*/
			String result = answerDAO.DeleteAnswer(answerId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新答卷信息之前先根据answerId查询某个答卷信息*/
			int answerId = Integer.parseInt(request.getParameter("answerId"));
			Answer answer = answerDAO.GetAnswer(answerId);

			// 客户端查询的答卷信息对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("answerId").value(answer.getAnswerId());
			  stringer.key("selectOptionObj").value(answer.getSelectOptionObj());
			  stringer.key("userObj").value(answer.getUserObj());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新答卷信息：获取答卷信息参数，参数保存到新建的答卷信息对象 */ 
			Answer answer = new Answer();
			int answerId = Integer.parseInt(request.getParameter("answerId"));
			answer.setAnswerId(answerId);
			int selectOptionObj = Integer.parseInt(request.getParameter("selectOptionObj"));
			answer.setSelectOptionObj(selectOptionObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			answer.setUserObj(userObj);

			/* 调用业务层执行更新操作 */
			String result = answerDAO.UpdateAnswer(answer);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
