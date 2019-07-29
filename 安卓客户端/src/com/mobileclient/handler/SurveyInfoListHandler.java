package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.SurveyInfo;
public class SurveyInfoListHandler extends DefaultHandler {
	private List<SurveyInfo> surveyInfoList = null;
	private SurveyInfo surveyInfo;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (surveyInfo != null) { 
            String valueString = new String(ch, start, length); 
            if ("paperId".equals(tempString)) 
            	surveyInfo.setPaperId(new Integer(valueString).intValue());
            else if ("questionPaperName".equals(tempString)) 
            	surveyInfo.setQuestionPaperName(valueString); 
            else if ("faqiren".equals(tempString)) 
            	surveyInfo.setFaqiren(valueString); 
            else if ("description".equals(tempString)) 
            	surveyInfo.setDescription(valueString); 
            else if ("startDate".equals(tempString)) 
            	surveyInfo.setStartDate(Timestamp.valueOf(valueString));
            else if ("endDate".equals(tempString)) 
            	surveyInfo.setEndDate(Timestamp.valueOf(valueString));
            else if ("zhutitupian".equals(tempString)) 
            	surveyInfo.setZhutitupian(valueString); 
            else if ("publishFlag".equals(tempString)) 
            	surveyInfo.setPublishFlag(new Integer(valueString).intValue());
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("SurveyInfo".equals(localName)&&surveyInfo!=null){
			surveyInfoList.add(surveyInfo);
			surveyInfo = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		surveyInfoList = new ArrayList<SurveyInfo>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("SurveyInfo".equals(localName)) {
            surveyInfo = new SurveyInfo(); 
        }
        tempString = localName; 
	}

	public List<SurveyInfo> getSurveyInfoList() {
		return this.surveyInfoList;
	}
}
