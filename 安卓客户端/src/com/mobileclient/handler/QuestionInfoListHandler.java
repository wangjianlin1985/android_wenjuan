package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.QuestionInfo;
public class QuestionInfoListHandler extends DefaultHandler {
	private List<QuestionInfo> questionInfoList = null;
	private QuestionInfo questionInfo;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (questionInfo != null) { 
            String valueString = new String(ch, start, length); 
            if ("titileId".equals(tempString)) 
            	questionInfo.setTitileId(new Integer(valueString).intValue());
            else if ("questionPaperObj".equals(tempString)) 
            	questionInfo.setQuestionPaperObj(new Integer(valueString).intValue());
            else if ("titleValue".equals(tempString)) 
            	questionInfo.setTitleValue(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("QuestionInfo".equals(localName)&&questionInfo!=null){
			questionInfoList.add(questionInfo);
			questionInfo = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		questionInfoList = new ArrayList<QuestionInfo>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("QuestionInfo".equals(localName)) {
            questionInfo = new QuestionInfo(); 
        }
        tempString = localName; 
	}

	public List<QuestionInfo> getQuestionInfoList() {
		return this.questionInfoList;
	}
}
