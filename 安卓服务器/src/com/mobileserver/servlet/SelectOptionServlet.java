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

	/*����ѡ����Ϣҵ������*/
	private SelectOptionDAO selectOptionDAO = new SelectOptionDAO();

	/*Ĭ�Ϲ��캯��*/
	public SelectOptionServlet() {
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
			/*��ȡ��ѯѡ����Ϣ�Ĳ�����Ϣ*/
			int questionObj = 0;
			if (request.getParameter("questionObj") != null)
				questionObj = Integer.parseInt(request.getParameter("questionObj"));
			String optionContent = request.getParameter("optionContent");
			optionContent = optionContent == null ? "" : new String(request.getParameter(
					"optionContent").getBytes("iso-8859-1"), "UTF-8");

			/*����ҵ���߼���ִ��ѡ����Ϣ��ѯ*/
			List<SelectOption> selectOptionList = selectOptionDAO.QuerySelectOption(questionObj,optionContent);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ���ѡ����Ϣ����ȡѡ����Ϣ�������������浽�½���ѡ����Ϣ���� */ 
			SelectOption selectOption = new SelectOption();
			int optionId = Integer.parseInt(request.getParameter("optionId"));
			selectOption.setOptionId(optionId);
			int questionObj = Integer.parseInt(request.getParameter("questionObj"));
			selectOption.setQuestionObj(questionObj);
			String optionContent = new String(request.getParameter("optionContent").getBytes("iso-8859-1"), "UTF-8");
			selectOption.setOptionContent(optionContent);

			/* ����ҵ���ִ����Ӳ��� */
			String result = selectOptionDAO.AddSelectOption(selectOption);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ��ѡ����Ϣ����ȡѡ����Ϣ�ļ�¼���*/
			int optionId = Integer.parseInt(request.getParameter("optionId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = selectOptionDAO.DeleteSelectOption(optionId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*����ѡ����Ϣ֮ǰ�ȸ���optionId��ѯĳ��ѡ����Ϣ*/
			int optionId = Integer.parseInt(request.getParameter("optionId"));
			SelectOption selectOption = selectOptionDAO.GetSelectOption(optionId);

			// �ͻ��˲�ѯ��ѡ����Ϣ���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ����ѡ����Ϣ����ȡѡ����Ϣ�������������浽�½���ѡ����Ϣ���� */ 
			SelectOption selectOption = new SelectOption();
			int optionId = Integer.parseInt(request.getParameter("optionId"));
			selectOption.setOptionId(optionId);
			int questionObj = Integer.parseInt(request.getParameter("questionObj"));
			selectOption.setQuestionObj(questionObj);
			String optionContent = new String(request.getParameter("optionContent").getBytes("iso-8859-1"), "UTF-8");
			selectOption.setOptionContent(optionContent);

			/* ����ҵ���ִ�и��²��� */
			String result = selectOptionDAO.UpdateSelectOption(selectOption);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
