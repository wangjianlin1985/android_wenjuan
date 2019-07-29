package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Answer;
public class AnswerListHandler extends DefaultHandler {
	private List<Answer> answerList = null;
	private Answer answer;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (answer != null) { 
            String valueString = new String(ch, start, length); 
            if ("answerId".equals(tempString)) 
            	answer.setAnswerId(new Integer(valueString).intValue());
            else if ("selectOptionObj".equals(tempString)) 
            	answer.setSelectOptionObj(new Integer(valueString).intValue());
            else if ("userObj".equals(tempString)) 
            	answer.setUserObj(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Answer".equals(localName)&&answer!=null){
			answerList.add(answer);
			answer = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		answerList = new ArrayList<Answer>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Answer".equals(localName)) {
            answer = new Answer(); 
        }
        tempString = localName; 
	}

	public List<Answer> getAnswerList() {
		return this.answerList;
	}
}
