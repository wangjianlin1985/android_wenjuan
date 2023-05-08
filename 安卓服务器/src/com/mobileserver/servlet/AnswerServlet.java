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

	/*��������Ϣҵ������*/
	private AnswerDAO answerDAO = new AnswerDAO();

	/*Ĭ�Ϲ��캯��*/
	public AnswerServlet() {
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
			/*��ȡ��ѯ�����Ϣ�Ĳ�����Ϣ*/
			int selectOptionObj = 0;
			if (request.getParameter("selectOptionObj") != null)
				selectOptionObj = Integer.parseInt(request.getParameter("selectOptionObj"));
			String userObj = "";
			if (request.getParameter("userObj") != null)
				userObj = request.getParameter("userObj");

			/*����ҵ���߼���ִ�д����Ϣ��ѯ*/
			List<Answer> answerList = answerDAO.QueryAnswer(selectOptionObj,userObj);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ��Ӵ����Ϣ����ȡ�����Ϣ�������������浽�½��Ĵ����Ϣ���� */ 
			Answer answer = new Answer();
			int answerId = Integer.parseInt(request.getParameter("answerId"));
			answer.setAnswerId(answerId);
			int selectOptionObj = Integer.parseInt(request.getParameter("selectOptionObj"));
			answer.setSelectOptionObj(selectOptionObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			answer.setUserObj(userObj);

			/* ����ҵ���ִ����Ӳ��� */
			String result = answerDAO.AddAnswer(answer);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ�������Ϣ����ȡ�����Ϣ�ļ�¼���*/
			int answerId = Integer.parseInt(request.getParameter("answerId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = answerDAO.DeleteAnswer(answerId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*���´����Ϣ֮ǰ�ȸ���answerId��ѯĳ�������Ϣ*/
			int answerId = Integer.parseInt(request.getParameter("answerId"));
			Answer answer = answerDAO.GetAnswer(answerId);

			// �ͻ��˲�ѯ�Ĵ����Ϣ���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ���´����Ϣ����ȡ�����Ϣ�������������浽�½��Ĵ����Ϣ���� */ 
			Answer answer = new Answer();
			int answerId = Integer.parseInt(request.getParameter("answerId"));
			answer.setAnswerId(answerId);
			int selectOptionObj = Integer.parseInt(request.getParameter("selectOptionObj"));
			answer.setSelectOptionObj(selectOptionObj);
			String userObj = new String(request.getParameter("userObj").getBytes("iso-8859-1"), "UTF-8");
			answer.setUserObj(userObj);

			/* ����ҵ���ִ�и��²��� */
			String result = answerDAO.UpdateAnswer(answer);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
