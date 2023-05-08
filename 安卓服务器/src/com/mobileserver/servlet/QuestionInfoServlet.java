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

	/*����������Ϣҵ������*/
	private QuestionInfoDAO questionInfoDAO = new QuestionInfoDAO();

	/*Ĭ�Ϲ��캯��*/
	public QuestionInfoServlet() {
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
			/*��ȡ��ѯ������Ϣ�Ĳ�����Ϣ*/
			int questionPaperObj = 0;
			if (request.getParameter("questionPaperObj") != null)
				questionPaperObj = Integer.parseInt(request.getParameter("questionPaperObj"));
			String titleValue = request.getParameter("titleValue");
			titleValue = titleValue == null ? "" : new String(request.getParameter(
					"titleValue").getBytes("iso-8859-1"), "UTF-8");

			/*����ҵ���߼���ִ��������Ϣ��ѯ*/
			List<QuestionInfo> questionInfoList = questionInfoDAO.QueryQuestionInfo(questionPaperObj,titleValue);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ���������Ϣ����ȡ������Ϣ�������������浽�½���������Ϣ���� */ 
			QuestionInfo questionInfo = new QuestionInfo();
			int titileId = Integer.parseInt(request.getParameter("titileId"));
			questionInfo.setTitileId(titileId);
			int questionPaperObj = Integer.parseInt(request.getParameter("questionPaperObj"));
			questionInfo.setQuestionPaperObj(questionPaperObj);
			String titleValue = new String(request.getParameter("titleValue").getBytes("iso-8859-1"), "UTF-8");
			questionInfo.setTitleValue(titleValue);

			/* ����ҵ���ִ����Ӳ��� */
			String result = questionInfoDAO.AddQuestionInfo(questionInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ��������Ϣ����ȡ������Ϣ�ļ�¼���*/
			int titileId = Integer.parseInt(request.getParameter("titileId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = questionInfoDAO.DeleteQuestionInfo(titileId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*����������Ϣ֮ǰ�ȸ���titileId��ѯĳ��������Ϣ*/
			int titileId = Integer.parseInt(request.getParameter("titileId"));
			QuestionInfo questionInfo = questionInfoDAO.GetQuestionInfo(titileId);

			// �ͻ��˲�ѯ��������Ϣ���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ����������Ϣ����ȡ������Ϣ�������������浽�½���������Ϣ���� */ 
			QuestionInfo questionInfo = new QuestionInfo();
			int titileId = Integer.parseInt(request.getParameter("titileId"));
			questionInfo.setTitileId(titileId);
			int questionPaperObj = Integer.parseInt(request.getParameter("questionPaperObj"));
			questionInfo.setQuestionPaperObj(questionPaperObj);
			String titleValue = new String(request.getParameter("titleValue").getBytes("iso-8859-1"), "UTF-8");
			questionInfo.setTitleValue(titleValue);

			/* ����ҵ���ִ�и��²��� */
			String result = questionInfoDAO.UpdateQuestionInfo(questionInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
