package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.SelectOption;
public class SelectOptionListHandler extends DefaultHandler {
	private List<SelectOption> selectOptionList = null;
	private SelectOption selectOption;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (selectOption != null) { 
            String valueString = new String(ch, start, length); 
            if ("optionId".equals(tempString)) 
            	selectOption.setOptionId(new Integer(valueString).intValue());
            else if ("questionObj".equals(tempString)) 
            	selectOption.setQuestionObj(new Integer(valueString).intValue());
            else if ("optionContent".equals(tempString)) 
            	selectOption.setOptionContent(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("SelectOption".equals(localName)&&selectOption!=null){
			selectOptionList.add(selectOption);
			selectOption = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		selectOptionList = new ArrayList<SelectOption>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("SelectOption".equals(localName)) {
            selectOption = new SelectOption(); 
        }
        tempString = localName; 
	}

	public List<SelectOption> getSelectOptionList() {
		return this.selectOptionList;
	}
}
