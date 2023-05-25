package gov.nih.nci.evs.bean;

import java.io.*;
import java.util.*;
import java.net.*;

import com.google.gson.*;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.XStream;

public class Concept
{

// Variable declaration
	private String code;
	private String label;
	private String codingScheme;
	private String version;
	private String nameSpace;
	private String semanticType;
	private String url;

// Default constructor
	public Concept() {
	}

// Constructor
	public Concept(
		String code,
		String label,
		String codingScheme,
		String version,
		String nameSpace,
		String semanticType,
		String url) {

		this.code = code;
		this.label = label;
		this.codingScheme = codingScheme;
		this.version = version;
		this.nameSpace = nameSpace;
		this.semanticType = semanticType;
		this.url = url;
	}

// Set methods
	public void setCode(String code) { 
		this.code = code;
	}

	public void setLabel(String label) { 
		this.label = label;
	}

	public void setCodingScheme(String codingScheme) { 
		this.codingScheme = codingScheme;
	}

	public void setVersion(String version) { 
		this.version = version;
	}

	public void setNameSpace(String nameSpace) { 
		this.nameSpace = nameSpace;
	}

	public void setSemanticType(String semanticType) { 
		this.semanticType = semanticType;
	}

	public void setUrl(String url) { 
		this.url = url;
	}


// Get methods
	public String getCode() { 
		return this.code;
	}

	public String getLabel() { 
		return this.label;
	}

	public String getCodingScheme() { 
		return this.codingScheme;
	}

	public String getVersion() { 
		return this.version;
	}

	public String getNameSpace() { 
		return this.nameSpace;
	}

	public String getSemanticType() { 
		return this.semanticType;
	}

	public String getUrl() { 
		return this.url;
	}

	public String toXML() {
		XStream xstream_xml = new XStream(new DomDriver());
		String xml = xstream_xml.toXML(this);
		xml = escapeDoubleQuotes(xml);
		StringBuffer buf = new StringBuffer();
		String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		buf.append(XML_DECLARATION).append("\n").append(xml);
		xml = buf.toString();
		return xml;
	}

	public String toJson() {
		JsonParser parser = new JsonParser();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
	}

	public String escapeDoubleQuotes(String inputStr) {
		char doubleQ = '"';
		StringBuffer buf = new StringBuffer();
		for (int i=0;  i<inputStr.length(); i++) {
			char c = inputStr.charAt(i);
			if (c == doubleQ) {
				buf.append(doubleQ).append(doubleQ);
			}
			buf.append(c);
		}
		return buf.toString();
	}
}
