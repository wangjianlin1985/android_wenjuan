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

	/*�����ʾ���Ϣҵ������*/
	private SurveyInfoDAO surveyInfoDAO = new SurveyInfoDAO();

	/*Ĭ�Ϲ��캯��*/
	public SurveyInfoServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*��ȡaction����������action��ִֵ�в�ͬ��ҵ����*/
		String action = request.getParameter("action");
		if (action.equals("query")) {
			/*��ȡ��ѯ�ʾ���Ϣ�Ĳ�����Ϣ*/
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

			/*����ҵ���߼���ִ���ʾ���Ϣ��ѯ*/
			List<SurveyInfo> surveyInfoList = surveyInfoDAO.QuerySurveyInfo(questionPaperName,faqiren,startDate,endDate);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ����ʾ���Ϣ����ȡ�ʾ���Ϣ�������������浽�½����ʾ���Ϣ���� */ 
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

			/* ����ҵ���ִ����Ӳ��� */
			String result = surveyInfoDAO.AddSurveyInfo(surveyInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ���ʾ���Ϣ����ȡ�ʾ���Ϣ�ļ�¼���*/
			int paperId = Integer.parseInt(request.getParameter("paperId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = surveyInfoDAO.DeleteSurveyInfo(paperId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*�����ʾ���Ϣ֮ǰ�ȸ���paperId��ѯĳ���ʾ���Ϣ*/
			int paperId = Integer.parseInt(request.getParameter("paperId"));
			SurveyInfo surveyInfo = surveyInfoDAO.GetSurveyInfo(paperId);

			// �ͻ��˲�ѯ���ʾ���Ϣ���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* �����ʾ���Ϣ����ȡ�ʾ���Ϣ�������������浽�½����ʾ���Ϣ���� */ 
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

			/* ����ҵ���ִ�и��²��� */
			String result = surveyInfoDAO.UpdateSurveyInfo(surveyInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
