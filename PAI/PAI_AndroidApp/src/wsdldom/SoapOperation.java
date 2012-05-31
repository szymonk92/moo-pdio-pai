package wsdldom;

import org.kxml2.kdom.Element;

public class SoapOperation {
	
	Element requestBody, responseBody;
	Element requestHeader, responseHeader;
	String sopaAction;
	
	
	public SoapOperation(Element requestBody, Element responseBody,
			Element requestHeader, Element responseHeader, String sopaAction) {
		super();
		this.requestBody = requestBody;
		this.responseBody = responseBody;
		this.requestHeader = requestHeader;
		this.responseHeader = responseHeader;
		this.sopaAction = sopaAction;
	}
	public Element getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(Element requestBody) {
		this.requestBody = requestBody;
	}
	public Element getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(Element responseBody) {
		this.responseBody = responseBody;
	}
	public Element getRequestHeader() {
		return requestHeader;
	}
	public void setRequestHeader(Element requestHeader) {
		this.requestHeader = requestHeader;
	}
	public Element getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(Element responseHeader) {
		this.responseHeader = responseHeader;
	}
	public String getSopaAction() {
		return sopaAction;
	}
	public void setSopaAction(String sopaAction) {
		this.sopaAction = sopaAction;
	}
	


}
