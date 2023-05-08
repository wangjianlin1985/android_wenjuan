package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.QuestionInfoDAO;
import com.mobileserver.domain.QuestionInfo;

import org.json.JSONStringer;

public class QuestionInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造问题信息业务层对象*/
	private QuestionInfoDAO questionInfoDAO = new QuestionInfoDAO();

	/*默认构造函数*/
	public QuestionInfoServlet() {
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
			/*获取查询问题信息的参数信息*/
			int questionPaperObj = 0;
			if (request.getParameter("questionPaperObj") != null)
				questionPaperObj = Integer.parseInt(request.getParameter("questionPaperObj"));
			String titleValue = request.getParameter("titleValue");
			titleValue = titleValue == null ? "" : new String(request.getParameter(
					"titleValue").getBytes("iso-8859-1"), "UTF-8");

			/*调用业务逻辑层执行问题信息查询*/
			List<QuestionInfo> questionInfoList = questionInfoDAO.QueryQuestionInfo(questionPaperObj,titleValue);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<QuestionInfos>").append("\r\n");
			for (int i = 0; i < questionInfoList.size(); i++) {
				sb.append("	<QuestionInfo>").append("\r\n")
				.append("		<titileId>")
				.append(questionInfoList.get(i).getTitileId())
				.append("</titileId>").append("\r\n")
				.append("		<questionPaperObj>")
				.append(questionInfoList.get(i).getQuestionPaperObj())
				.append("</questionPaperObj>").append("\r\n")
				.append("		<titleValue>")
				.append(questionInfoList.get(i).getTitleValue())
				.append("</titleValue>").append("\r\n")
				.append("	</QuestionInfo>").append("\r\n");
			}
			sb.append("</QuestionInfos>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(QuestionInfo questionInfo: questionInfoList) {
				  stringer.object();
			  stringer.key("titileId").value(questionInfo.getTitileId());
			  stringer.key("questionPaperObj").value(questionInfo.getQuestionPaperObj());
			  stringer.key("titleValue").value(questionInfo.getTitleValue());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加问题信息：获取问题信息参数，参数保存到新建的问题信息对象 */ 
			QuestionInfo questionInfo = new QuestionInfo();
			int titileId = Integer.parseInt(request.getParameter("titileId"));
			questionInfo.setTitileId(titileId);
			int questionPaperObj = Integer.parseInt(request.getParameter("questionPaperObj"));
			questionInfo.setQuestionPaperObj(questionPaperObj);
			String titleValue = new String(request.getParameter("titleValue").getBytes("iso-8859-1"), "UTF-8");
			questionInfo.setTitleValue(titleValue);

			/* 调用业务层执行添加操作 */
			String result = questionInfoDAO.AddQuestionInfo(questionInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除问题信息：获取问题信息的记录编号*/
			int titileId = Integer.parseInt(request.getParameter("titileId"));
			/*调用业务逻辑层执行删除操作*/
			String result = questionInfoDAO.DeleteQuestionInfo(titileId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新问题信息之前先根据titileId查询某个问题信息*/
			int titileId = Integer.parseInt(request.getParameter("titileId"));
			QuestionInfo questionInfo = questionInfoDAO.GetQuestionInfo(titileId);

			// 客户端查询的问题信息对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("titileId").value(questionInfo.getTitileId());
			  stringer.key("questionPaperObj").value(questionInfo.getQuestionPaperObj());
			  stringer.key("titleValue").value(questionInfo.getTitleValue());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新问题信息：获取问题信息参数，参数保存到新建的问题信息对象 */ 
			QuestionInfo questionInfo = new QuestionInfo();
			int titileId = Integer.parseInt(request.getParameter("titileId"));
			questionInfo.setTitileId(titileId);
			int questionPaperObj = Integer.parseInt(request.getParameter("questionPaperObj"));
			questionInfo.setQuestionPaperObj(questionPaperObj);
			String titleValue = new String(request.getParameter("titleValue").getBytes("iso-8859-1"), "UTF-8");
			questionInfo.setTitleValue(titleValue);

			/* 调用业务层执行更新操作 */
			String result = questionInfoDAO.UpdateQuestionInfo(questionInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
