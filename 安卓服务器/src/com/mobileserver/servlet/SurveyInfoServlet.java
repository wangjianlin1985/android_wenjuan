package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.SurveyInfoDAO;
import com.mobileserver.domain.SurveyInfo;

import org.json.JSONStringer;

public class SurveyInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造问卷信息业务层对象*/
	private SurveyInfoDAO surveyInfoDAO = new SurveyInfoDAO();

	/*默认构造函数*/
	public SurveyInfoServlet() {
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
			/*获取查询问卷信息的参数信息*/
			String questionPaperName = request.getParameter("questionPaperName");
			questionPaperName = questionPaperName == null ? "" : new String(request.getParameter(
					"questionPaperName").getBytes("iso-8859-1"), "UTF-8");
			String faqiren = request.getParameter("faqiren");
			faqiren = faqiren == null ? "" : new String(request.getParameter(
					"faqiren").getBytes("iso-8859-1"), "UTF-8");
			Timestamp startDate = null;
			if (request.getParameter("startDate") != null)
				startDate = Timestamp.valueOf(request.getParameter("startDate"));
			Timestamp endDate = null;
			if (request.getParameter("endDate") != null)
				endDate = Timestamp.valueOf(request.getParameter("endDate"));

			/*调用业务逻辑层执行问卷信息查询*/
			List<SurveyInfo> surveyInfoList = surveyInfoDAO.QuerySurveyInfo(questionPaperName,faqiren,startDate,endDate);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<SurveyInfos>").append("\r\n");
			for (int i = 0; i < surveyInfoList.size(); i++) {
				sb.append("	<SurveyInfo>").append("\r\n")
				.append("		<paperId>")
				.append(surveyInfoList.get(i).getPaperId())
				.append("</paperId>").append("\r\n")
				.append("		<questionPaperName>")
				.append(surveyInfoList.get(i).getQuestionPaperName())
				.append("</questionPaperName>").append("\r\n")
				.append("		<faqiren>")
				.append(surveyInfoList.get(i).getFaqiren())
				.append("</faqiren>").append("\r\n")
				.append("		<description>")
				.append(surveyInfoList.get(i).getDescription())
				.append("</description>").append("\r\n")
				.append("		<startDate>")
				.append(surveyInfoList.get(i).getStartDate())
				.append("</startDate>").append("\r\n")
				.append("		<endDate>")
				.append(surveyInfoList.get(i).getEndDate())
				.append("</endDate>").append("\r\n")
				.append("		<zhutitupian>")
				.append(surveyInfoList.get(i).getZhutitupian())
				.append("</zhutitupian>").append("\r\n")
				.append("		<publishFlag>")
				.append(surveyInfoList.get(i).getPublishFlag())
				.append("</publishFlag>").append("\r\n")
				.append("	</SurveyInfo>").append("\r\n");
			}
			sb.append("</SurveyInfos>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(SurveyInfo surveyInfo: surveyInfoList) {
				  stringer.object();
			  stringer.key("paperId").value(surveyInfo.getPaperId());
			  stringer.key("questionPaperName").value(surveyInfo.getQuestionPaperName());
			  stringer.key("faqiren").value(surveyInfo.getFaqiren());
			  stringer.key("description").value(surveyInfo.getDescription());
			  stringer.key("startDate").value(surveyInfo.getStartDate());
			  stringer.key("endDate").value(surveyInfo.getEndDate());
			  stringer.key("zhutitupian").value(surveyInfo.getZhutitupian());
			  stringer.key("publishFlag").value(surveyInfo.getPublishFlag());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加问卷信息：获取问卷信息参数，参数保存到新建的问卷信息对象 */ 
			SurveyInfo surveyInfo = new SurveyInfo();
			int paperId = Integer.parseInt(request.getParameter("paperId"));
			surveyInfo.setPaperId(paperId);
			String questionPaperName = new String(request.getParameter("questionPaperName").getBytes("iso-8859-1"), "UTF-8");
			surveyInfo.setQuestionPaperName(questionPaperName);
			String faqiren = new String(request.getParameter("faqiren").getBytes("iso-8859-1"), "UTF-8");
			surveyInfo.setFaqiren(faqiren);
			String description = new String(request.getParameter("description").getBytes("iso-8859-1"), "UTF-8");
			surveyInfo.setDescription(description);
			Timestamp startDate = Timestamp.valueOf(request.getParameter("startDate"));
			surveyInfo.setStartDate(startDate);
			Timestamp endDate = Timestamp.valueOf(request.getParameter("endDate"));
			surveyInfo.setEndDate(endDate);
			String zhutitupian = new String(request.getParameter("zhutitupian").getBytes("iso-8859-1"), "UTF-8");
			surveyInfo.setZhutitupian(zhutitupian);
			int publishFlag = Integer.parseInt(request.getParameter("publishFlag"));
			surveyInfo.setPublishFlag(publishFlag);

			/* 调用业务层执行添加操作 */
			String result = surveyInfoDAO.AddSurveyInfo(surveyInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除问卷信息：获取问卷信息的记录编号*/
			int paperId = Integer.parseInt(request.getParameter("paperId"));
			/*调用业务逻辑层执行删除操作*/
			String result = surveyInfoDAO.DeleteSurveyInfo(paperId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新问卷信息之前先根据paperId查询某个问卷信息*/
			int paperId = Integer.parseInt(request.getParameter("paperId"));
			SurveyInfo surveyInfo = surveyInfoDAO.GetSurveyInfo(paperId);

			// 客户端查询的问卷信息对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("paperId").value(surveyInfo.getPaperId());
			  stringer.key("questionPaperName").value(surveyInfo.getQuestionPaperName());
			  stringer.key("faqiren").value(surveyInfo.getFaqiren());
			  stringer.key("description").value(surveyInfo.getDescription());
			  stringer.key("startDate").value(surveyInfo.getStartDate());
			  stringer.key("endDate").value(surveyInfo.getEndDate());
			  stringer.key("zhutitupian").value(surveyInfo.getZhutitupian());
			  stringer.key("publishFlag").value(surveyInfo.getPublishFlag());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新问卷信息：获取问卷信息参数，参数保存到新建的问卷信息对象 */ 
			SurveyInfo surveyInfo = new SurveyInfo();
			int paperId = Integer.parseInt(request.getParameter("paperId"));
			surveyInfo.setPaperId(paperId);
			String questionPaperName = new String(request.getParameter("questionPaperName").getBytes("iso-8859-1"), "UTF-8");
			surveyInfo.setQuestionPaperName(questionPaperName);
			String faqiren = new String(request.getParameter("faqiren").getBytes("iso-8859-1"), "UTF-8");
			surveyInfo.setFaqiren(faqiren);
			String description = new String(request.getParameter("description").getBytes("iso-8859-1"), "UTF-8");
			surveyInfo.setDescription(description);
			Timestamp startDate = Timestamp.valueOf(request.getParameter("startDate"));
			surveyInfo.setStartDate(startDate);
			Timestamp endDate = Timestamp.valueOf(request.getParameter("endDate"));
			surveyInfo.setEndDate(endDate);
			String zhutitupian = new String(request.getParameter("zhutitupian").getBytes("iso-8859-1"), "UTF-8");
			surveyInfo.setZhutitupian(zhutitupian);
			int publishFlag = Integer.parseInt(request.getParameter("publishFlag"));
			surveyInfo.setPublishFlag(publishFlag);

			/* 调用业务层执行更新操作 */
			String result = surveyInfoDAO.UpdateSurveyInfo(surveyInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
